package org.jala.university.domain.entity;

import lombok.Getter;

@Getter
public final class ServiceCatalog {

    private final String id;
    private final String nombre;
    private final String tipo;
    private final String proveedor;
    private final String categoria;

    public ServiceCatalog(String id, String nombre, String tipo,
                          String proveedor, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.proveedor = proveedor;
        this.categoria = categoria;
    }
}
