package com.trabajofinal.ticketyap.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private   String FROM;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoRegistro(String destinatario) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(FROM);
        helper.setTo(destinatario);
        helper.setSubject("Registro en TicketYap — ¡Bienvenido!");
        helper.setText(plantillaRegistro(destinatario), true);
        mailSender.send(message);
        log.info("Correo de registro enviado a {}", destinatario);
    }

    public void enviarCorreoRecuperarPassword(String destinatario) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(FROM);
        helper.setTo(destinatario);
        helper.setSubject("Solicitud de cambio de contraseña");
        helper.setText(plantillaRecuperarPassword(destinatario), true);
        mailSender.send(message);
        log.info("Correo de recuperación enviado a {}", destinatario);
    }

    public void enviarCorreoCompra(String destinatario) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(FROM);
        helper.setTo(destinatario);
        helper.setSubject("Confirmación de compra — TicketYap");
        helper.setText(plantillaNotificacionCompra(destinatario), true);
        mailSender.send(message);
        log.info("Correo de compra enviado a {}", destinatario);
    }

    public void enviarCorreoCarritoAbandonado(String destinatario, String nombre, int numItems) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(FROM);
        helper.setTo(destinatario);
        helper.setSubject("¡Tienes entradas esperándote en TicketYap!");
        helper.setText(plantillaCarritoAbandonado(nombre, numItems), true);
        mailSender.send(message);
        log.info("Correo de recordatorio de carrito enviado a {}", destinatario);
    }

    public void enviarCorreoNuevaOferta(String destinatario, String nombreComprador,
            BigDecimal precioOferta, Integer idEntrada) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(FROM);
        helper.setTo(destinatario);
        helper.setSubject("Nueva oferta recibida — TicketYap");
        helper.setText(plantillaNuevaOferta(nombreComprador, precioOferta, idEntrada), true);
        mailSender.send(message);
        log.info("Correo de nueva oferta enviado a {}", destinatario);
    }

    private String generarFecha() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("'el' dd 'de' MMMM 'de' yyyy', a las' HH:mm"));
    }

    

    private String plantillaRecuperarPassword(String email) {
        String empresa = "TicketYap";
        String fecha = generarFecha();
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Recuperar contraseña</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f6f9fc; font-family: Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f6f9fc; padding:20px 0;">
                    <tr><td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="max-width:600px; background-color:white; border-radius:12px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.1);">
                            <tr>
                                <td align="center" style="padding:30px 20px; background-color:white;">
                                    <h1 style="color:#2c3e50; font-size:24px; margin:0;">Bienvenido a <strong>%s</strong></h1>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0 40px;">
                                    <h2 style="color:#2c3e50; text-align:center; font-size:20px;">¡Actualiza tu contraseña!</h2>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0 40px 30px; font-size:16px; color:#34495e; line-height:1.6;">
                                    <p style="text-align:center; margin:0 0 25px;">
                                        Hola,<br><br>
                                        %s se ha solicitado cambiar la contraseña de<br>
                                        <span style="color:#3498db; font-weight:bold;">%s</span>
                                    </p>
                                    <p style="text-align:center; margin:0 0 25px;">
                                        Pulsa el enlace para cambiar tu contraseña:<br><br>
                                        <a href="http://localhost:5173/reset_contra_nueva" style="color:#3498db;">http://localhost:5173/reset_contra_nueva</a>
                                    </p>
                                    <p style="text-align:center; color:#95a5a6; font-size:14px; margin-top:40px;">
                                        Si no solicitaste este cambio, contáctanos inmediatamente.
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="background:#2c3e50; color:#bdc3c7; padding:25px; text-align:center; font-size:13px;">
                                    <p style="margin:5px 0;">© 2025 Ticketyap • Todos los derechos reservados</p>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
                </table>
            </body>
            </html>
            """.formatted(empresa, fecha, email);
    }

    private String plantillaRegistro(String email) {
        String empresa = "TicketYap";
        String fecha = generarFecha();
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Registro exitoso</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f6f9fc; font-family: Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f6f9fc; padding:20px 0;">
                    <tr><td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="max-width:600px; background-color:white; border-radius:12px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.1);">
                            <tr>
                                <td align="center" style="padding:30px 20px; background-color:white;">
                                    <h1 style="color:#2c3e50; font-size:24px; margin:0;">Bienvenido a <strong>%s</strong></h1>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0 40px;">
                                    <h2 style="color:#2c3e50; text-align:center; font-size:20px;">¡Te has registrado correctamente!</h2>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0 40px 30px; font-size:16px; color:#34495e; line-height:1.6;">
                                    <p style="text-align:center; margin:0 0 25px;">
                                        Hola,<br><br>
                                        %s te has registrado en la aplicación con el correo:<br>
                                        <span style="color:#3498db; font-weight:bold;">%s</span>
                                    </p>
                                    <p style="text-align:center; color:#95a5a6; font-size:14px; margin-top:40px;">
                                        ¡Bienvenido a bordo!
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="background:#2c3e50; color:#bdc3c7; padding:25px; text-align:center; font-size:13px;">
                                    <p style="margin:5px 0;">© 2025 Ticketyap • Todos los derechos reservados</p>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
                </table>
            </body>
            </html>
            """.formatted(empresa, fecha, email);
    }

    private String plantillaNuevaOferta(String nombreComprador, BigDecimal precioOferta, Integer idEntrada) {
        String empresa = "TicketYap";
        String fecha = generarFecha();
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"><title>Nueva oferta</title></head>
            <body style="margin:0;padding:0;background-color:#f6f9fc;font-family:Arial,sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f6f9fc;padding:20px 0;">
                    <tr><td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="max-width:600px;background-color:white;border-radius:12px;overflow:hidden;box-shadow:0 10px 30px rgba(0,0,0,0.1);">
                            <tr><td align="center" style="padding:30px 20px;">
                                <h1 style="color:#2c3e50;font-size:24px;margin:0;">%s</h1>
                            </td></tr>
                            <tr><td style="padding:0 40px 30px;font-size:16px;color:#34495e;line-height:1.6;">
                                <h2 style="color:#2c3e50;text-align:center;">¡Tienes una nueva oferta!</h2>
                                <p style="text-align:center;margin:0 0 20px;">
                                    %s — <strong>%s</strong> ofrece
                                    <span style="color:#1d8f2c;font-weight:bold;font-size:20px;">%s €</span>
                                    por tu entrada <strong>#%d</strong>.
                                </p>
                                <p style="text-align:center;">
                                    <a href="http://localhost:5173/user/entradas"
                                       style="background-color:#1d8f2c;color:white;padding:12px 28px;border-radius:8px;text-decoration:none;font-weight:bold;">
                                        Ver mis entradas
                                    </a>
                                </p>
                            </td></tr>
                            <tr><td style="background:#2c3e50;color:#bdc3c7;padding:20px;text-align:center;font-size:13px;">
                                <p style="margin:0;">© 2025 TicketYap • Todos los derechos reservados</p>
                            </td></tr>
                        </table>
                    </td></tr>
                </table>
            </body>
            </html>
            """.formatted(empresa, fecha, nombreComprador, precioOferta.toPlainString(), idEntrada);
    }

    private String plantillaCarritoAbandonado(String nombre, int numItems) {
        String empresa = "TicketYap";
        String itemsTexto = numItems == 1 ? "1 entrada" : numItems + " entradas";
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Entradas en tu carrito</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f6f9fc; font-family: Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f6f9fc; padding:20px 0;">
                    <tr><td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="max-width:600px; background-color:white; border-radius:12px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.1);">
                            <tr>
                                <td align="center" style="padding:30px 20px; background-color:#1d8f2c;">
                                    <h1 style="color:white; font-size:24px; margin:0;">%s</h1>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:30px 40px 10px;">
                                    <h2 style="color:#2c3e50; text-align:center; font-size:20px; margin:0;">
                                        ¡Hola, %s! 🎟️
                                    </h2>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:10px 40px 30px; font-size:16px; color:#34495e; line-height:1.7;">
                                    <p style="text-align:center; margin:0 0 20px;">
                                        Tienes <strong style="color:#1d8f2c;">%s</strong> guardadas en tu carrito.<br>
                                        ¡No dejes que alguien más se lleve tus entradas!
                                    </p>
                                    <p style="text-align:center; margin:0 0 30px;">
                                        <a href="http://localhost:5173/carrito"
                                           style="background-color:#1d8f2c; color:white; padding:14px 32px; border-radius:8px; text-decoration:none; font-weight:bold; font-size:16px;">
                                            Completar compra
                                        </a>
                                    </p>
                                    <p style="text-align:center; color:#95a5a6; font-size:13px; margin:0;">
                                        Si ya realizaste la compra, ignora este mensaje.
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="background:#2c3e50; color:#bdc3c7; padding:25px; text-align:center; font-size:13px;">
                                    <p style="margin:5px 0;">© 2025 TicketYap • Todos los derechos reservados</p>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
                </table>
            </body>
            </html>
            """.formatted(empresa, nombre, itemsTexto);
    }

    private String plantillaNotificacionCompra(String email) {
        String empresa = "TicketYap";
        String fecha = generarFecha();
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Confirmación de compra</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f6f9fc; font-family: Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f6f9fc; padding:20px 0;">
                    <tr><td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="max-width:600px; background-color:white; border-radius:12px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.1);">
                            <tr>
                                <td align="center" style="padding:30px 20px; background-color:white;">
                                    <h1 style="color:#2c3e50; font-size:24px; margin:0;">Bienvenido a <strong>%s</strong></h1>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0 40px;">
                                    <h2 style="color:#2c3e50; text-align:center; font-size:20px;">¡Gracias por tu compra!</h2>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0 40px 30px; font-size:16px; color:#34495e; line-height:1.6;">
                                    <p style="text-align:center; margin:0 0 25px;">
                                        Hola,<br><br>
                                        %s has completado una compra de entrada en %s.<br>
                                        <span style="color:#3498db; font-weight:bold;">%s</span>
                                    </p>
                                    <p style="text-align:center; color:#95a5a6; font-size:14px; margin-top:40px;">
                                        ¡Te esperamos en la próxima compra!
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="background:#2c3e50; color:#bdc3c7; padding:25px; text-align:center; font-size:13px;">
                                    <p style="margin:5px 0;">© 2025 Ticketyap • Todos los derechos reservados</p>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
                </table>
            </body>
            </html>
            """.formatted(empresa, fecha, empresa, email);
    }
}
