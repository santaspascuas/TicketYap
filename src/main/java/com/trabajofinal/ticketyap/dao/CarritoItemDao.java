package com.trabajofinal.ticketyap.dao;

import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.CarritoItem;
import com.trabajofinal.ticketyap.modelos.DTO.CarritoItemDTO;

public interface CarritoItemDao {
    Integer agregarItem(CarritoItem item);
    Integer eliminarItem(Integer id_item);
    List<CarritoItemDTO> obtenerItemsPorCarrito(Integer id_carrito);
    Integer vaciarCarrito(Integer id_carrito);
    boolean existeEntradaEnCarrito(Integer id_carrito, Integer id_entrada);
    Optional<CarritoItem> obtenerItemById(Integer id_item);
}
