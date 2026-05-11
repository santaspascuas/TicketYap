package com.trabajofinal.ticketyap.dao;

import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.Carrito;

public interface CarritoDao {
    Optional<Carrito>obtenerCarritoActivoByUser(Integer id_usuario);
    Integer eliminarCarrito(Integer id_carrito);
    Integer crearCarrito(Carrito carrito);
    Integer actualizarCarrito(Carrito carrito);
    Optional<Carrito> obtenerCarritoById(Integer id_carrito);
    Integer actualizarEstadoCarrito(Integer id_carrito, String estado);
    Integer actualizarFechaModificacion(Integer id_carrito);
}
