package com.trabajofinal.ticketyap.modelos.DTO;

public class UserDto {
    private Integer id_usuario;
    private String nombre;
    private String email;
    private String numero_cuenta;
    private String role;



    public UserDto(Integer id_usuario, String nombre, String email, String numero_cuenta, String role) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.email = email;
        this.numero_cuenta = numero_cuenta;
        this.role = role;
    }


    
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
    public String getNumero_cuenta() {
        return numero_cuenta;
    }
    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
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
    


}
