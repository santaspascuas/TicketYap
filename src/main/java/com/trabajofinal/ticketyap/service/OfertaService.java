package com.trabajofinal.ticketyap.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.dao.OfertaDao;
import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.events.NuevaOfertaEvent;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.exceptions.OfertaException;
import com.trabajofinal.ticketyap.modelos.Entrada;
import com.trabajofinal.ticketyap.modelos.Oferta;
import com.trabajofinal.ticketyap.modelos.User;
import com.trabajofinal.ticketyap.modelos.DTO.OfertaDTO;
import com.trabajofinal.ticketyap.modelos.DTO.OfertaRequest;

@Service
public class OfertaService {

    private static final Logger log = LoggerFactory.getLogger(OfertaService.class);

    private final OfertaDao ofertaDao;
    private final EntradaDao entradaDao;
    private final UserDao userDao;
    private final ApplicationEventPublisher eventPublisher;

    public OfertaService(OfertaDao ofertaDao, EntradaDao entradaDao,
                         UserDao userDao, ApplicationEventPublisher eventPublisher) {
        this.ofertaDao      = ofertaDao;
        this.entradaDao     = entradaDao;
        this.userDao        = userDao;
        this.eventPublisher = eventPublisher;
    }

    public OfertaDTO crearOferta(OfertaRequest request) {
        Integer idComprador = idUsuarioActual();
        Entrada entrada = obtenerEntrada(request.getId_entrada());

        validarNoPropiaEntrada(entrada, idComprador);
        validarSinOfertaPendiente(request.getId_entrada(), idComprador);

        Oferta nuevaOferta = new Oferta(
            request.getId_entrada(),
            idComprador,
            request.getPrecio_oferta(),
            request.getMensaje()
        );
        nuevaOferta.setFecha_expiracion(request.getFecha_expiracion());
        ofertaDao.insertar(nuevaOferta);

        notificarVendedor(entrada, idComprador, request);
        log.info("Oferta creada por usuario {} sobre entrada {}", idComprador, request.getId_entrada());

        return ofertaDao.selectPorEntrada(request.getId_entrada())
            .stream()
            .filter(o -> o.getId_comprador().equals(idComprador))
            .findFirst()
            .orElseThrow(() -> new OfertaException("Error al recuperar la oferta recién creada"));
    }

    public List<OfertaDTO> obtenerOfertasPorEntrada(Integer idEntrada) {
        Integer idUsuario = idUsuarioActual();
        Entrada entrada = obtenerEntrada(idEntrada);

        if (!entrada.getId_vendedor().equals(idUsuario)) {
            throw new OfertaException("Solo el vendedor puede consultar las ofertas de su entrada");
        }

        return ofertaDao.selectPorEntrada(idEntrada);
    }

    public List<OfertaDTO> obtenerMisOfertas() {
        return ofertaDao.selectPorComprador(idUsuarioActual());
    }

    public void aceptarOferta(Integer idOferta) {
        Integer idUsuario = idUsuarioActual();
        OfertaDTO oferta = obtenerOfertaDTO(idOferta);
        Entrada entrada = obtenerEntrada(oferta.getId_entrada());

        validarEsVendedor(entrada, idUsuario);
        validarEstadoPendiente(oferta);

        ofertaDao.actualizarEstado(idOferta, Oferta.EstadoOferta.ACEPTADA);
        ofertaDao.rechazarPendientesPorEntrada(oferta.getId_entrada());
        log.info("Oferta {} aceptada por vendedor {}", idOferta, idUsuario);
    }

    public void rechazarOferta(Integer idOferta) {
        Integer idUsuario = idUsuarioActual();
        OfertaDTO oferta = obtenerOfertaDTO(idOferta);
        Entrada entrada = obtenerEntrada(oferta.getId_entrada());

        validarEsVendedor(entrada, idUsuario);
        validarEstadoPendiente(oferta);

        ofertaDao.actualizarEstado(idOferta, Oferta.EstadoOferta.RECHAZADA);
        log.info("Oferta {} rechazada por vendedor {}", idOferta, idUsuario);
    }

    // --- helpers privados ---

    private Integer idUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.selectUserbyemail(email)
            .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado"))
            .getId_usuario();
    }

    private Entrada obtenerEntrada(Integer idEntrada) {
        return entradaDao.selectEntradaById(idEntrada)
            .orElseThrow(() -> new OfertaException("Entrada no encontrada: " + idEntrada));
    }

    private OfertaDTO obtenerOfertaDTO(Integer idOferta) {
        return ofertaDao.selectById(idOferta)
            .orElseThrow(() -> new OfertaException("Oferta no encontrada: " + idOferta));
    }

    private void validarNoPropiaEntrada(Entrada entrada, Integer idComprador) {
        if (entrada.getId_vendedor().equals(idComprador)) {
            throw new OfertaException("No puedes hacer una oferta sobre tu propia entrada");
        }
    }

    private void validarSinOfertaPendiente(Integer idEntrada, Integer idComprador) {
        if (ofertaDao.existeOfertaPendiente(idEntrada, idComprador)) {
            throw new OfertaException("Ya tienes una oferta pendiente para esta entrada");
        }
    }

    private void validarEsVendedor(Entrada entrada, Integer idUsuario) {
        if (!entrada.getId_vendedor().equals(idUsuario)) {
            throw new OfertaException("Solo el vendedor puede gestionar las ofertas de esta entrada");
        }
    }

    private void validarEstadoPendiente(OfertaDTO oferta) {
        if (!Oferta.EstadoOferta.PENDIENTE.equals(oferta.getEstado_oferta())) {
            throw new OfertaException("Solo se pueden gestionar ofertas en estado PENDIENTE");
        }
    }

    private void notificarVendedor(Entrada entrada, Integer idComprador, OfertaRequest request) {
        User vendedor = userDao.selectusuariobyid(entrada.getId_vendedor())
            .orElseThrow(() -> new OfertaException("Vendedor no encontrado"));
        User comprador = userDao.selectusuariobyid(idComprador)
            .orElseThrow(() -> new OfertaException("Comprador no encontrado"));

        eventPublisher.publishEvent(new NuevaOfertaEvent(
            vendedor.getEmail(),
            comprador.getNombre(),
            request.getPrecio_oferta(),
            request.getId_entrada()
        ));
    }
}
