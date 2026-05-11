package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.BarcodeRegistroDao;
import com.trabajofinal.ticketyap.modelos.BarcodeRegistro;
import com.trabajofinal.ticketyap.utilitys.BarcodeRegistroRowMapper;

@Repository
public class BarcodeRegistroImplements implements BarcodeRegistroDao {

    private final JdbcTemplate jdbcTemplate;

    public BarcodeRegistroImplements(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existeHash(String hashBarcode) {
        String sql = "SELECT COUNT(*) FROM barcode_registro WHERE hash_barcode = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, hashBarcode);
        return count != null && count > 0;
    }

    @Override
    public Optional<BarcodeRegistro> buscarPorHash(String hashBarcode) {
        String sql = "SELECT * FROM barcode_registro WHERE hash_barcode = ?";
        try {
            BarcodeRegistro registro = jdbcTemplate.queryForObject(
                sql, new BarcodeRegistroRowMapper(), hashBarcode);
            return Optional.ofNullable(registro);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void registrar(BarcodeRegistro registro) {
        String sql = """
            INSERT INTO barcode_registro
                (hash_barcode, contenido, id_entrada_tmp, id_usuario, fecha_registro)
            VALUES (?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
            registro.getHashBarcode(),
            registro.getContenido(),
            registro.getIdEntradaTmp(),
            registro.getIdUsuario(),
            registro.getFechaRegistro()
        );
    }
}
