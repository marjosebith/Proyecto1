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
        lista.add(new Servicio("004", "Agua", "Hogar", "Aguas Andinas", "Servicios"));
        lista.add(new Servicio("005", "Disney+", "Streaming", "Disney", "Entretenimiento"));
        lista.add(new Servicio("006", "HBO Max", "Streaming", "Warner", "Entretenimiento"));
        lista.add(new Servicio("007", "Gas", "Hogar", "Gas Natural", "Servicios"));
        lista.add(new Servicio("008", "YouTube Premium", "Música", "Google", "Entretenimiento"));
        lista.add(new Servicio("009", "Internet", "Telecom", "Movistar", "Servicios"));
        lista.add(new Servicio("010", "Claro TV", "Telecom", "Claro", "Entretenimiento"));
        lista.add(new Servicio("011", "Agua Hogar", "Hogar", "Aguas Andinas", "Servicios"));
        lista.add(new Servicio("012", "Agua Industrial", "Hogar", "Aguas Andinas", "Servicios"));
        lista.add(new Servicio("013", "Movistar Internet", "Telecom", "Movistar", "Servicios"));
        lista.add(new Servicio("014", "Movistar TV", "Telecom", "Movistar", "Entretenimiento"));

        if (texto == null || texto.isEmpty()) {
            return lista;
        }

        String filtro = texto.toLowerCase();

        return lista.stream().filter(s -> {

            if ("Todos".equalsIgnoreCase(campo)) {
                return s.getNombre().toLowerCase().contains(filtro)
                        || s.getTipo().toLowerCase().contains(filtro)
                        || s.getProveedor().toLowerCase().contains(filtro)
                        || s.getCategoria().toLowerCase().contains(filtro);
            }

            switch (campo.toLowerCase()) {
                case "nombre":
                    return s.getNombre().toLowerCase().contains(filtro);
                case "tipo":
                    return s.getTipo().toLowerCase().contains(filtro);
                case "proveedor":
                    return s.getProveedor().toLowerCase().contains(filtro);
                case "categoria":
                    return s.getCategoria().toLowerCase().contains(filtro);
                default:
                    return true;
            }
        }).collect(Collectors.toList());
    }
}
