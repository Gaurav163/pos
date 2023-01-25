package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inventories")
public class InventoryPojo {
    @Id
    private Long id;
    @Column(nullable = false)
    private Long quantity;
}
