package com.trabajofinal.ticketyap.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.BarcodeRegistroDao;
import com.trabajofinal.ticketyap.exceptions.BarcodeDuplicadoException;
import com.trabajofinal.ticketyap.modelos.BarcodeRegistro;

@Service
public class BarcodeRegistroService {

    private static final Logger log = LoggerFactory.getLogger(BarcodeRegistroService.class);
    private static final int MAX_CONTENIDO = 500;

    private final BarcodeRegistroDao dao;

    public BarcodeRegistroService(BarcodeRegistroDao dao) {
        this.dao = dao;
    }

    /**
     * Verifica que ningún código de la lista esté ya registrado.
     * Si encuentra un duplicado lanza BarcodeDuplicadoException con la fecha
     * del registro original, lo que permite detectar intentos de venta múltiple.
     */
    public void verificarNoDuplicados(List<String> codigos) {
        for (String codigo : codigos) {
            String hash = sha256(codigo);
            Optional<BarcodeRegistro> existente = dao.buscarPorHash(hash);
            if (existente.isPresent()) {
                log.warn("[BarcodeRegistro] Duplicado detectado. Hash: {}, registrado: {}",
                         hash, existente.get().getFechaRegistro());
                //Si esta duplicado vamos a ver si se queda en la database porque en minio si.
                throw new BarcodeDuplicadoException(hash, existente.get().getFechaRegistro());
            }
        }
        log.info("[BarcodeRegistro] Sin duplicados. Códigos analizados: {}", codigos.size());
    }

    /**
     * Registra todos los códigos una vez superada la verificación.
     * Se llama solo cuando el PDF es válido y no contiene duplicados.
     */
    public void registrarBarcodes(List<String> codigos, Integer idEntradaTmp, Integer idUsuario) {
        for (String codigo : codigos) {
            String hash      = sha256(codigo);
            String contenido = codigo.length() > MAX_CONTENIDO
                               ? codigo.substring(0, MAX_CONTENIDO) : codigo;

            BarcodeRegistro registro = new BarcodeRegistro(hash, contenido, idEntradaTmp, idUsuario);
            dao.registrar(registro);
            log.debug("[BarcodeRegistro] Registrado hash: {}", hash);
        }
        log.info("[BarcodeRegistro] {} código(s) registrados para entrada {}", codigos.size(), idEntradaTmp);
    }

    // ── Utilidad ────────────────────────────────────────────────────────────────

    public String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
