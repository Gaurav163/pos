package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Inventory {
    @Id
    private Long id;
    @Column(nullable = false)
    private Long quantity;
}
