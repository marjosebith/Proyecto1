package org.jala.university.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.jala.university.commons.domain.entity.BaseEntity;

@Entity
@Data
@Builder
public final class ServiceEntity implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "user_id")
    private UUID userId;

    @Override
    public Integer getId() {
        return id;
    }
}

