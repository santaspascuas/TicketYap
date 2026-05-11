package com.trabajofinal.ticketyap.dao;

import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.AuthModel;
import com.trabajofinal.ticketyap.modelos.User;

public interface UserDao {
    List<User> selectUsers();
    Optional<User>selectUserbyemail(String email);
    Integer deleteUser(Integer id);
    Integer insertarUsuario(User usuario);
    Integer updateUser(User usuario);
    Optional<User>selectusuariobyid(Integer id);
    Integer cambiarContrasenaUsuario(AuthModel modelo);
    Optional<User>selectentradabyID(Integer id);
    Optional<User>selectUsuariobyIban(String iban);
    List<User>Usuarioregistradosrecientes();
    Integer insertarUsuarioByAdmin(User usuario);
}
