package org.jala.university.domain.entity;

import lombok.Getter;

@Getter
public final class Servicio {

    private String id;
    private String nombre;
    private String tipo;
    private String proveedor;
    private String categoria;

    public Servicio(String id, String nombre, String tipo,
                    String proveedor, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.proveedor = proveedor;
        this.categoria = categoria;
    }
}
