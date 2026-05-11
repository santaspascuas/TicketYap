package com.trabajofinal.ticketyap.dao;


import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.Entrada;

public interface EntradaDao {
    List<Entrada>selectEntradas();
    Optional<Entrada>selectEntradabyuser(Integer iduser);
    Optional<Entrada>selectEntradaById(Integer idEntrada);
    Integer insertarEntrada(Entrada entrada);
    Integer deleteEntrada(Integer user, Integer entrada);
    Integer updateEntrada(Entrada update);
    Integer updateEstadoCompra(Integer idEntrada,Integer iduser,String estado);
    Optional<Entrada> selectEntradabyqr(String qr);
    List<Entrada> selectEntradadisponiblebyid(Integer idevento);
    List<Entrada> selectEntradasPropias(Integer userId);
    List<Entrada> selectEntradasCompradas(Integer userId);

}
