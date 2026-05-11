package com.trabajofinal.ticketyap.response;

import java.util.List;

public class ResponseUtil {
    //El metodo es de tipo generico que acepta cualquier tipo de variable.
    //Tanto en la aceptación de los datos como en la devolución de ellos.



    public static <T> ApiResponse<T> sucess(T data, String message, String path){

        ApiResponse<T> response = new ApiResponse<>(); // Estas creando la respuesta que devolvera un tipo generico
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setErrors(null);
        response.setErrorCode(0);
        response.setTimestamp(System.currentTimeMillis());
        response.setPath(path);
        return response;

    }

    //Creamos una respuesta personalizada para los errores. En este caso este seria para errore acumulados
    //como una especie de validador de formularios.
    public static <T>ApiResponse<T> errors(List<String>errors, String message, int errorCode,String path ){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setErrors(errors);
        response.setErrorCode(errorCode);
        response.setTimestamp(System.currentTimeMillis());
        response.setPath(path);
        return response;
    }

    //Funcion hecha para contener errores simples con la misma nomenclatura del errors.
    public static <T>ApiResponse<T> error(String error, String message, int errorCode,String path){
        return errors(List.of(error), message, errorCode, path);
    }

}
