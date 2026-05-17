package org.jala.university.domain.entity;

public final class Servicio {

    private String id;
    private String nombre;
    private String tipo;
    private String proveedor;
    private String categoria;
    private String description;

    public Servicio(String id, String nombre, String tipo,
                    String proveedor, String categoria, String description) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return nombre + " - " + description;
    }
}
