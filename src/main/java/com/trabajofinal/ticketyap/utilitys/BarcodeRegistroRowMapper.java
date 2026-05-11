package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.BarcodeRegistro;

public class BarcodeRegistroRowMapper implements RowMapper<BarcodeRegistro> {

    @Override
    public BarcodeRegistro mapRow(ResultSet rs, int rowNum) throws SQLException {
        BarcodeRegistro b = new BarcodeRegistro();
        b.setId(rs.getInt("id"));
        b.setHashBarcode(rs.getString("hash_barcode"));
        b.setContenido(rs.getString("contenido"));
        b.setIdEntradaTmp(rs.getInt("id_entrada_tmp"));
        b.setIdUsuario(rs.getInt("id_usuario"));

        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) b.setFechaRegistro(ts.toLocalDateTime());

        return b;
    }
}
