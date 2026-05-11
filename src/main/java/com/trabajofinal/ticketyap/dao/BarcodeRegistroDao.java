package com.trabajofinal.ticketyap.dao;

import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.BarcodeRegistro;

public interface BarcodeRegistroDao {

    /** Devuelve true si el hash ya existe en la tabla. */
    boolean existeHash(String hashBarcode);

    /** Devuelve el registro completo (con fecha) para informar del duplicado. */
    Optional<BarcodeRegistro> buscarPorHash(String hashBarcode);

    /** Inserta un nuevo registro de barcode verificado. */
    void registrar(BarcodeRegistro registro);
}
