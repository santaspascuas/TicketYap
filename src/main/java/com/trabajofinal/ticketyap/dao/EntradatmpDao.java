package com.trabajofinal.ticketyap.dao;

import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.Entradatmp;

public interface EntradatmpDao {
      Integer insertartmpfile(Entradatmp entradatmp);
     Optional<Entradatmp>obtenerEntradatmp(String nombreentrada);
     Integer deleteEntradatmp(Integer nombreentrada);
     boolean buscaEntradatmpbyhash(String hash);
     Optional<Entradatmp> obtenerEntradabyHash(String hash);
}
