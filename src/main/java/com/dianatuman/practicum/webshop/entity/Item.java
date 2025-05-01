package com.dianatuman.practicum.webshop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private String description;

    private Double price;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
}
