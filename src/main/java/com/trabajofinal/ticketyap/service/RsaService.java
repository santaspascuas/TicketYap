package com.trabajofinal.ticketyap.service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.modelos.DTO.ConfirmacionPdfData;

/**
 * Servicio RSA para cadena de custodia de verificaciones.
 *
 * Como intermediario no generamos tickets, por lo que el RSA NO valida
 * firmas de terceros. En cambio, firmamos nosotros el resultado de nuestra
 * propia verificación: hash del PDF + códigos extraídos + timestamp.
 *
 * Esto permite a cualquier parte verificar que ESTA plataforma procesó
 * y validó ese ticket en un momento determinado, y que los datos no
 * han sido alterados desde entonces.
 */
@Service
public class RsaService {

    private static final Logger log = LoggerFactory.getLogger(RsaService.class);
    private static final String ALGORITMO = "SHA256withRSA";

    @Value("${rsa.private-key:}")
    private String privateKeyB64;

    @Value("${rsa.public-key:}")
    private String publicKeyB64;

    private PrivateKey privateKey;
    private PublicKey  publicKey;

    @PostConstruct
    public void init() {
        if (privateKeyB64.isBlank() || publicKeyB64.isBlank()) {
            log.warn("[RSA] Claves no configuradas. El token de verificación se omitirá. " +
                     "Ejecuta RsaService.generarClavesParaConfig() una vez y añade las claves " +
                     "a application-dev.properties.");
            return;
        }
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyB64)));
            publicKey  = kf.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyB64)));
            log.info("[RSA] Claves cargadas correctamente.");
        } catch (Exception e) {
            log.error("[RSA] Error al cargar claves: {}", e.getMessage());
        }
    }

    public boolean clavesCargadas() {
        return privateKey != null && publicKey != null;
    }

    // ── API pública ────────────────────────────────────────────────────────────

    /**
     * Firma el resultado de la verificación de un ticket.
     * Payload: hash|codigos|evento|nombre|timestamp
     *
     * @return token Base64 firmado, o null si las claves no están configuradas.
     */
    public String firmarVerificacion(List<String> codigos,
                                     ConfirmacionPdfData datos,
                                     String hash) {
        if (!clavesCargadas()) {
            log.warn("[RSA] Token de verificación no generado: claves no configuradas.");
            return null;
        }
        try {
            String payload = buildPayload(codigos, datos, hash, Instant.now().getEpochSecond());
            String firma   = firmar(payload);
            // El token devuelto es payload::firma para que el receptor pueda separar ambas partes
            String token = Base64.getEncoder().encodeToString(payload.getBytes("UTF-8"))
                         + "::" + firma;
            log.info("[RSA] Token de verificación generado para hash: {}", hash);
            return token;
        } catch (Exception e) {
            log.error("[RSA] Error al firmar verificación: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Verifica un token de verificación generado por firmarVerificacion().
     * Separa el payload de la firma y comprueba la integridad.
     */
    public boolean verificarToken(String token) {
        if (!clavesCargadas() || token == null) return false;
        try {
            String[] partes = token.split("::");
            if (partes.length != 2) return false;
            String payload = new String(Base64.getDecoder().decode(partes[0]), "UTF-8");
            return verificar(payload, partes[1]);
        } catch (Exception e) {
            log.error("[RSA] Error al verificar token: {}", e.getMessage());
            return false;
        }
    }

    // ── Primitivas criptográficas ──────────────────────────────────────────────

    private String firmar(String payload) throws Exception {
        Signature signer = Signature.getInstance(ALGORITMO);
        signer.initSign(privateKey);
        signer.update(payload.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(signer.sign());
    }

    private boolean verificar(String payload, String firmaB64) {
        try {
            Signature verifier = Signature.getInstance(ALGORITMO);
            verifier.initVerify(publicKey);
            verifier.update(payload.getBytes("UTF-8"));
            return verifier.verify(Base64.getDecoder().decode(firmaB64));
        } catch (Exception e) {
            log.error("[RSA] Error en verificación: {}", e.getMessage());
            return false;
        }
    }

    // ── Construcción del payload ───────────────────────────────────────────────

    /**
     * Formato canónico del payload firmado:
     * hash:{hash}|codigos:{c1,c2,...}|evento:{e}|nombre:{n}|ts:{epoch}
     */
    private String buildPayload(List<String> codigos, ConfirmacionPdfData datos,
                                String hash, long epochSeconds) {
        StringJoiner codigosJoined = new StringJoiner(",");
        if (codigos != null) codigos.forEach(codigosJoined::add);

        return "hash:"    + (hash != null ? hash : "")
             + "|codigos:" + codigosJoined
             + "|evento:"  + (datos.getEvento()  != null ? datos.getEvento()  : "")
             + "|nombre:"  + (datos.getNombre()  != null ? datos.getNombre()  : "")
             + "|ts:"      + epochSeconds;
    }

    // ── Utilidad de generación de claves ──────────────────────────────────────

    /**
     * Genera un par de claves RSA 2048-bit listas para pegar en properties.
     * Ejecutar UNA sola vez al configurar el entorno.
     */
    public static void generarClavesParaConfig() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048, new SecureRandom());
        KeyPair par = gen.generateKeyPair();

        String privB64 = Base64.getEncoder().encodeToString(par.getPrivate().getEncoded());
        String pubB64  = Base64.getEncoder().encodeToString(par.getPublic().getEncoded());

        System.out.println("# ── Añadir a application-dev.properties ──────────────");
        System.out.println("rsa.private-key=" + privB64);
        System.out.println("rsa.public-key="  + pubB64);
        System.out.println("# ─────────────────────────────────────────────────────");
    }
}
