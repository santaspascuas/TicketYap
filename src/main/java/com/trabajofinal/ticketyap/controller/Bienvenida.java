package com.trabajofinal.ticketyap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.service.ServiceSaludo;


@RestController
@RequestMapping("home") 
public class Bienvenida {

    //Inyecto el servicio
    private final ServiceSaludo saludo;

    //Contructor

    public Bienvenida(ServiceSaludo saludo){
        this.saludo = saludo;
    }


    @GetMapping("")
    public String MensajeBienvenida() {
        return saludo.MensajeSaludo();
    }
    

}
