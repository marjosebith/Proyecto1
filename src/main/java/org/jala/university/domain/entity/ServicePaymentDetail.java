package org.jala.university.domain.entity;

import lombok.Getter;

@Getter
public class ServicePaymentDetail {

    private String serviceName;

    private String tipo;

    private String proveedor;

    private String categoria;

    private String accountNumber;

    private String usuario;

    private double monto;

    private String estado;

    private String fechaVencimiento;

    public static final  class Builder {

        private final ServicePaymentDetail detail =
                new ServicePaymentDetail();

        public Builder serviceName(String value) {
            detail.serviceName = value;
            return this;
        }

        public Builder tipo(String value) {
            detail.tipo = value;
            return this;
        }

        public Builder proveedor(String value) {
            detail.proveedor = value;
            return this;
        }

        public Builder categoria(String value) {
            detail.categoria = value;
            return this;
        }

        public Builder accountNumber(String value) {
            detail.accountNumber = value;
            return this;
        }

        public Builder usuario(String value) {
            detail.usuario = value;
            return this;
        }

        public Builder monto(double value) {
            detail.monto = value;
            return this;
        }

        public Builder estado(String value) {
            detail.estado = value;
            return this;
        }

        public Builder fechaVencimiento(String value) {
            detail.fechaVencimiento = value;
            return this;
        }

        public ServicePaymentDetail build() {
            return detail;
        }
    }
}
