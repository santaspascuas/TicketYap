package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.CombinaLocalizacionEvento;
import com.trabajofinal.ticketyap.modelos.Eventos;
import com.trabajofinal.ticketyap.modelos.Localizacion;

public class CombinacionEventoLocaRowMapper implements  RowMapper<CombinaLocalizacionEvento>{

    //v1--Esto funcionara siempre que traiga todas las variables en el select.
    /*
    Final incializamos los rowmapper y luego los usamos.
    La idea viendo como los he sacado es que puedo sacar el evento y a la vez la localizacion.
    Printeo la lista de ambas para luego en el fron sacar todo.
    Devuelvo tanto el evento como la localizacion.
    Apuntar como funciona en el cuadrerno.
     */

    private final RowMapper<Eventos> eventosRowmapper = new EventosRowMapper();
    private final RowMapper<Localizacion> localizacionRowmapper = new LocalizacionRowMapper();
    
    @Override
    public CombinaLocalizacionEvento mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        Eventos evento = eventosRowmapper.mapRow(rs, rowNum); // Le pasamos la clase rowmapper de eventos ya creada
        Localizacion local = localizacionRowmapper.mapRow(rs, rowNum); // Le pasamos la clase rowmapper de localizacion ya creada.

        return new CombinaLocalizacionEvento(evento, local);

    }

}
