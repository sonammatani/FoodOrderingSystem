package com.project.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderData extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deliverable")
    private boolean isDeliverable;
}
