package org.jala.university.application.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Validador para servicios registrados.
 * Realiza validaciones de datos antes de guardar en BD.
 */
public class UserServiceValidator {

    /**
     * Valida los datos de un servicio registrado.
     * @return Lista de errores encontrados (vacía si es válido)
     */
    public static List<String> validate(String alias, String accountNumber, String notes) {
        List<String> errors = new ArrayList<>();

        if (alias == null || alias.trim().isEmpty()) {
            errors.add("El alias del servicio es requerido");
        } else if (alias.length() > 100) {
            errors.add("El alias no puede exceder 100 caracteres");
        }

        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            if (accountNumber.length() > 50) {
                errors.add("El número de cuenta no puede exceder 50 caracteres");
            }
            // Validar que sea alfanumérico
            if (!accountNumber.matches("^[a-zA-Z0-9-_.]*$")) {
                errors.add("El número de cuenta contiene caracteres inválidos");
            }
        }

        if (notes != null && notes.length() > 500) {
            errors.add("Las notas no pueden exceder 500 caracteres");
        }

        return errors;
    }

    /**
     * Valida si los datos son válidos.
     */
    public static boolean isValid(String alias, String accountNumber, String notes) {
        return validate(alias, accountNumber, notes).isEmpty();
    }
}

