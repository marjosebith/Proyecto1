package org.jala.university.domain.entity;

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
}
