package com.trabajofinal.ticketyap.modelos;

import java.io.Serializable;
import java.time.LocalDate;


public class User  implements  Serializable{

    private Integer id_usuario;
    private String nombre;
    private String email;
    private String contrasena;
    private String numero_cuenta;
    private LocalDate fecha_registro;
    private String role;

   
    public User(Integer id_usuario, String nombre, String email, String contrasena, String numero_cuenta,
            LocalDate fecha_registro, String role) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.numero_cuenta = numero_cuenta;
        this.fecha_registro = fecha_registro;
        this.role = role;
    }

    public User(String nombre, String email, String contrasena, String numero_cuenta){
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.numero_cuenta = numero_cuenta;
    }
    //Contructor sin id para administrador
    public User(String nombre, String email, String contrasena, String numero_cuenta, String role){
        this.nombre = nombre;
        this.email = email;
        this.fecha_registro = LocalDate.now();
        this.contrasena = contrasena;
        this.numero_cuenta = numero_cuenta;
        this.role = role;
    }


    public User(){}
    
    public Integer getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getNumero_cuenta() {
        return numero_cuenta;
    }
    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }
    public LocalDate getFecha_registro() {
        return fecha_registro;
    }
    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("id_usuario=").append(id_usuario);
        sb.append(", nombre=").append(nombre);
        sb.append(", email=").append(email);
        sb.append(", contrasena=").append(contrasena);
        sb.append(", numero_cuenta=").append(numero_cuenta);
        sb.append(", fecha_registro=").append(fecha_registro);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }




















    
}

