package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.modelos.AuthModel;
import com.trabajofinal.ticketyap.modelos.User;
import com.trabajofinal.ticketyap.utilitys.UserRowMapper;

@Repository
public class UserImplements implements UserDao{

    //Aqui se va a usar el jdbc
    private final JdbcTemplate jdbctemplate;

    //Inyectamos la dependencia

    public UserImplements(JdbcTemplate jdbctemplate){
        this.jdbctemplate=jdbctemplate;
    }

    @Override
    public List<User> selectUsers() {
       String sql = "SELECT * FROM  usuario";
       return jdbctemplate.query(sql, new UserRowMapper());
    }

    @Override
    public Integer deleteUser(Integer id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        return jdbctemplate.update(sql,id);
    }

    @Override
    public Integer insertarUsuario(User usuario) {

        String sql ="""
        INSERT INTO usuario (nombre,email,contrasena,numero_cuenta, role)
            VALUES (?,?,?,?,?)
                    """;

        return jdbctemplate.update(sql,
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getContrasena(),
            usuario.getNumero_cuenta(),
            usuario.getRole()
        );
    }

    @Override
    public Integer updateUser(User usuario) {
        String sql ="""
                UPDATE usuario SET nombre =?, email = ?, contrasena = ?, numero_cuenta = ?
                WHERE id_usuario = ?
                """;
        return jdbctemplate.update(sql,
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getContrasena(),
            usuario.getNumero_cuenta(),
            usuario.getId_usuario()
        );
    }

    @Override
    public Optional<User> selectUserbyemail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        //Que pasa que yo traigo un usuario cuando lo extraigo de la base de datosº
        //El otional es un opcional que puede traer el usuario o no
        List<User>usuarioEncontrado = jdbctemplate.query(sql, new UserRowMapper(),email);//yo aqui tengo un usuario
        return usuarioEncontrado.stream().findFirst();
    }

    @Override
    public Optional<User> selectusuariobyid(Integer id) {

        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";

        try {
            User usuariobuscado = jdbctemplate.queryForObject(sql, new UserRowMapper(),id);
            return Optional.of(usuariobuscado);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer cambiarContrasenaUsuario(AuthModel modelo) {
        String sql = "UPDATE usuario set contrasena = ? WHERE email = ?";

        return jdbctemplate.update(sql,
            modelo.getContrasena(), modelo.getEmail()
        );
    }

    @Override
    public Optional<User> selectentradabyID(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try {
            User usuariobuscado = jdbctemplate.queryForObject(sql, new UserRowMapper(), id);
            return Optional.of(usuariobuscado);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> selectUsuariobyIban(String iban) {

        String sql = "SELECT * FROM usuario WHERE numero_cuenta = ?";

         List<User> usuarioencontrado = jdbctemplate.query(sql, new UserRowMapper(),iban);
         
         return usuarioencontrado.stream().findFirst();
    }

    @Override
    public List<User> Usuarioregistradosrecientes() {
        //Sacamos listado de los ultimos usuarios.
        String sql = """
                select * from usuario order by fecha_registro desc limit 5
                """;
        return jdbctemplate.query(sql, new UserRowMapper());
    }

    @Override
    public Integer insertarUsuarioByAdmin(User usuarioadmin) {
         String sql ="""
        INSERT INTO usuario (nombre,email,contrasena,numero_cuenta,role)
            VALUES (?,?,?,?,?)
                    """;

         return jdbctemplate.update(sql,
            usuarioadmin.getNombre(),
            usuarioadmin.getEmail(),
            usuarioadmin.getContrasena(),
            usuarioadmin.getNumero_cuenta(),
            usuarioadmin.getRole()
        );
    }


}
