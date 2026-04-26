package org.jala.university.application.service;

import org.jala.university.domain.entity.Servicio;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ExternalPaymentServiceImpl implements ExternalPaymentService {

    @Override
    public List<Servicio> buscarServicios(String texto, String campo) {

        List<Servicio> lista = new ArrayList<>();

        lista.add(new Servicio("001", "Netflix", "Streaming", "Netflix Inc", "Entretenimiento"));
        lista.add(new Servicio("002", "Spotify", "Música", "Spotify Ltd", "Entretenimiento"));
        lista.add(new Servicio("003", "Luz", "Hogar", "Enel", "Servicios"));

        if (texto == null || texto.isEmpty()) {
            return lista;
        }

        String filtro = texto.toLowerCase();

        return lista.stream().filter(s -> {
            if ("nombre".equalsIgnoreCase(campo)) {
                return s.getNombre().toLowerCase().contains(filtro);
            } else if ("tipo".equalsIgnoreCase(campo)) {
                return s.getTipo().toLowerCase().contains(filtro);
            } else if ("proveedor".equalsIgnoreCase(campo)) {
                return s.getProveedor().toLowerCase().contains(filtro);
            } else if ("categoria".equalsIgnoreCase(campo)) {
                return s.getCategoria().toLowerCase().contains(filtro);
            }
            return true;
        }).collect(Collectors.toList());
    }
}
