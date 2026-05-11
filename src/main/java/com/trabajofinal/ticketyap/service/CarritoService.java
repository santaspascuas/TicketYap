package com.trabajofinal.ticketyap.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabajofinal.ticketyap.dao.CarritoDao;
import com.trabajofinal.ticketyap.dao.CarritoItemDao;
import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.events.CarritoConItemsEvent;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.Carrito;
import com.trabajofinal.ticketyap.modelos.CarritoItem;
import com.trabajofinal.ticketyap.modelos.EstadoCarrito;
import com.trabajofinal.ticketyap.modelos.DTO.CarritoConItemsDTO;
import com.trabajofinal.ticketyap.modelos.DTO.CarritoItemDTO;
import com.trabajofinal.ticketyap.modelos.DTO.UserDto;

@Service
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    private final CarritoDao carritodao;
    private final CarritoItemDao carritoItemDao;
    private final EntradaDao entradaDao;
    private final UserService userservice;
    private final ApplicationEventPublisher eventPublisher;

    public CarritoService(CarritoDao carritodao, CarritoItemDao carritoItemDao,
                          EntradaDao entradaDao, UserService userservice,
                          ApplicationEventPublisher eventPublisher) {
        this.carritodao = carritodao;
        this.carritoItemDao = carritoItemDao;
        this.entradaDao = entradaDao;
        this.userservice = userservice;
        this.eventPublisher = eventPublisher;
    }

    public CarritoConItemsDTO obtenerCarritoUsuario() {
        UserDto usuario = obtenerUsuarioActual();
        Carrito carrito = obtenerOCrearCarritoActivo(usuario.getId_usuario());
        List<CarritoItemDTO> items = carritoItemDao.obtenerItemsPorCarrito(carrito.getId_carrito());
        log.info("Carrito obtenido para usuario {}", usuario.getId_usuario());

        if (!items.isEmpty()) {
            eventPublisher.publishEvent(new CarritoConItemsEvent(
                usuario.getId_usuario(),
                usuario.getEmail(),
                usuario.getNombre(),
                items.size()
            ));
        }

        return new CarritoConItemsDTO(
            carrito.getId_carrito(),
            carrito.getId_usuario(),
            carrito.getEstado().name(),
            carrito.getFecha_modificado(),
            items
        );
    }

    @Transactional
    public CarritoConItemsDTO agregarEntrada(Integer id_entrada) {
        UserDto usuario = obtenerUsuarioActual();

        entradaDao.selectEntradaById(id_entrada)
            .orElseThrow(() -> new NotFoundException("Entrada no encontrada: " + id_entrada));

        Carrito carrito = obtenerOCrearCarritoActivo(usuario.getId_usuario());

        if (carritoItemDao.existeEntradaEnCarrito(carrito.getId_carrito(), id_entrada)) {
            throw new IllegalStateException("La entrada ya se encuentra en el carrito");
        }

        carritoItemDao.agregarItem(new CarritoItem(carrito.getId_carrito(), id_entrada));
        carritodao.actualizarFechaModificacion(carrito.getId_carrito());

        log.info("Entrada {} agregada al carrito {} del usuario {}", id_entrada, carrito.getId_carrito(), usuario.getId_usuario());
        return obtenerCarritoUsuario();
    }

    @Transactional
    public CarritoConItemsDTO eliminarItem(Integer id_item) {
        UserDto usuario = obtenerUsuarioActual();
        Carrito carrito = obtenerCarritoActivoDelUsuario(usuario.getId_usuario());

        CarritoItem item = carritoItemDao.obtenerItemById(id_item)
            .orElseThrow(() -> new NotFoundException("Item no encontrado: " + id_item));

        if (!item.getId_carrito().equals(carrito.getId_carrito())) {
            throw new IllegalStateException("El item no pertenece al carrito del usuario");
        }

        carritoItemDao.eliminarItem(id_item);
        carritodao.actualizarFechaModificacion(carrito.getId_carrito());

        log.info("Item {} eliminado del carrito {} del usuario {}", id_item, carrito.getId_carrito(), usuario.getId_usuario());
        return obtenerCarritoUsuario();
    }

    @Transactional
    public CarritoConItemsDTO vaciarCarrito() {
        UserDto usuario = obtenerUsuarioActual();
        Carrito carrito = obtenerCarritoActivoDelUsuario(usuario.getId_usuario());

        carritoItemDao.vaciarCarrito(carrito.getId_carrito());
        carritodao.actualizarFechaModificacion(carrito.getId_carrito());

        log.info("Carrito {} vaciado por usuario {}", carrito.getId_carrito(), usuario.getId_usuario());
        return obtenerCarritoUsuario();
    }

    @Transactional
    public void abandonarCarrito() {
        UserDto usuario = obtenerUsuarioActual();
        Carrito carrito = obtenerCarritoActivoDelUsuario(usuario.getId_usuario());

        carritoItemDao.vaciarCarrito(carrito.getId_carrito());
        carritodao.actualizarEstadoCarrito(carrito.getId_carrito(), EstadoCarrito.ABANDONADO.name());

        log.info("Carrito {} abandonado por usuario {}", carrito.getId_carrito(), usuario.getId_usuario());
    }

    // --- helpers privados ---

    private Carrito obtenerOCrearCarritoActivo(Integer id_usuario) {
        log.debug("obtenerOCrearCarritoActivo para usuario{}", id_usuario);

        return carritodao.obtenerCarritoActivoByUser(id_usuario).orElseGet(() -> {
            log.info("No existe carrito activo para usuario {}, creando uno nuevo", id_usuario);
            LocalDateTime ahora = LocalDateTime.now();
            log.debug("ahora", ahora);
            log.debug("estado", EstadoCarrito.ACTIVO.name());
            Carrito nuevo = new Carrito(id_usuario, ahora, ahora, EstadoCarrito.ACTIVO);
            carritodao.crearCarrito(nuevo);
            return carritodao.obtenerCarritoActivoByUser(id_usuario)
                .orElseThrow(() -> new IllegalStateException("Error al crear el carrito"));
        });
    }

    private Carrito obtenerCarritoActivoDelUsuario(Integer id_usuario) {
        return carritodao.obtenerCarritoActivoByUser(id_usuario)
            .orElseThrow(() -> new NotFoundException("No existe un carrito activo para el usuario"));
    }

    private UserDto obtenerUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userservice.obtenerUsuarioporEmail(email);
    }
}
