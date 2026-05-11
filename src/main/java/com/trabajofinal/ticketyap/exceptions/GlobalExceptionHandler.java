package com.trabajofinal.ticketyap.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

//Controlador global de excepciones.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
        public ApiResponse<Object>handleDatabaseError(Exception ex, HttpServletRequest request){
        return ResponseUtil.error(ex.getMessage(), "Error de base de datos", HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
    }
    @ExceptionHandler(IllegalStateException.class)
        public ApiResponse<Object>handleDatabaseValor(Exception ex, HttpServletRequest request){
            return ResponseUtil.error(ex.getMessage(),"error datos repetidos en la base", HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>>handleErrorSubidaArchivo(Exception ex, HttpServletRequest request){
        
        ApiResponse<Object> body = ResponseUtil.error(
        ex.getMessage(),
        "Error en la carga del fichero pdf en la ruta",
        HttpStatus.BAD_REQUEST.value(),
        request.getRequestURI()
    );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);
    }
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiResponse<Object>>handleRefreshTokenException(RefreshTokenException ex, HttpServletRequest request){
        
        ApiResponse<Object> body = ResponseUtil.error(
        ex.getMessage(),
        "Error con el refresh token",
        HttpStatus.UNAUTHORIZED.value(),
        request.getRequestURI()
    );
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(body);
    }
    @ExceptionHandler(GetNumberException.class)
    public ResponseEntity<ApiResponse<Object>>handleGetNumberException(GetNumberException ex, HttpServletRequest request){
        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error al obtener el numero de paginas del pdf",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);

    }
    @ExceptionHandler(HashException.class)
    public ResponseEntity<ApiResponse<Object>>handleHashException(HashException ex, HttpServletRequest request){
        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error al calcular el hash del pdf",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body);

    }

    @ExceptionHandler(DuplicadoBucketException.class)
    public ResponseEntity<ApiResponse<Object>>handleDuplicadoBucketException(DuplicadoBucketException ex, HttpServletRequest request){
        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error al crear el bucket, ya existe un bucket con ese nombre",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);
    }
    
    @ExceptionHandler(IdentificatioGoogleException.class)
    public ResponseEntity<ApiResponse<Object>>handleIdentificatioGoogleException(IdentificatioGoogleException ex, HttpServletRequest request){
        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error al identificar con Google",
            HttpStatus.UNAUTHORIZED.value(),
            request.getRequestURI()
        );
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(body);
    }

    @ExceptionHandler(GoogleUsuarioException.class)
    public ResponseEntity<ApiResponse<Object>>handleGoogleServiceToken(GoogleUsuarioException ex, HttpServletRequest request){

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(), 
            "Error en el servicio para token de Google", 
            HttpStatus.BAD_REQUEST.value(), 
            request.getRequestURI());

        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>handleNotFound(NotFoundException ex, HttpServletRequest request){

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error con el usuario en la database",
            HttpStatus.UNAUTHORIZED.value(), 
            request.getRequestURI());

        return  ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);  
    }
    
    @ExceptionHandler(MinioException.class)
    public ResponseEntity<ApiResponse<Object>> handleMinioException(
            MinioException ex, HttpServletRequest request) {

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error interno al procesar archivo en MinIO",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
}

    @ExceptionHandler(EventoImagenException.class)
    public ResponseEntity<ApiResponse<Object>> handleEventoImagenException(
        EventoImagenException ex, HttpServletRequest request) {

            ApiResponse<Object> body = ResponseUtil.error(
                ex.getMessage(),
                "Error  interno en la  base de datos de ImagenEventos", 
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                request.getRequestURI());
                
        return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);            
        }

    @ExceptionHandler(ListaEventoException.class)
    public ResponseEntity<ApiResponse<Object>> handleListaEventoException(
        ListaEventoException ex, HttpServletRequest request){

            ApiResponse<Object> body = ResponseUtil.error(
                ex.getMessage(),
                "Error interno en la base de datos ListaEvento",
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                request.getRequestURI());

        return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);      
    }

    @ExceptionHandler(EntradaTmpException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntradatmpException(
        EntradaTmpException ex, HttpServletRequest request) {

            ApiResponse<Object> body = ResponseUtil.error(
                ex.getMessage(),
                "Error en el servicio de Entradatmp",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI());

            return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);

     }
    
    @ExceptionHandler(BarcodeDuplicadoException.class)
    public ResponseEntity<ApiResponse<Object>> handleBarcodeDuplicado(
            BarcodeDuplicadoException ex, HttpServletRequest request) {

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Ticket duplicado: posible intento de venta múltiple del mismo ticket",
            HttpStatus.CONFLICT.value(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(PdfValidacionException.class)
    public ResponseEntity<ApiResponse<Object>> handlePdfValidacionException(
            PdfValidacionException ex, HttpServletRequest request) {

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error en la validación del PDF",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(OfertaException.class)
    public ResponseEntity<ApiResponse<Object>> handleOfertaException(
            OfertaException ex, HttpServletRequest request) {

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error en el servicio de ofertas",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConversacionException.class)
    public ResponseEntity<ApiResponse<Object>> handleConversacionException(
            ConversacionException ex, HttpServletRequest request) {

        ApiResponse<Object> body = ResponseUtil.error(
            ex.getMessage(),
            "Error en el servicio de conversaciones",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }













}



