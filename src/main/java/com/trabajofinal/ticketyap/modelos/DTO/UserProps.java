package com.trabajofinal.ticketyap.modelos.DTO;

public class UserProps {
    private Integer id_usuario;
    private String name;
    private String email;
    private String numero_cuenta;

    public UserProps(String email, Integer id_usuario, String name, String numero_cuenta) {
        this.email = email;
        this.id_usuario = id_usuario;
        this.name = name;
        this.numero_cuenta = numero_cuenta;
    }
    

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }


    













}



