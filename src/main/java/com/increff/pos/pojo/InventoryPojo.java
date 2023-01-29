package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.increff.pos.pojo.PojoConstants.INVENTORY_TABLE_NAME;

@Data
@NoArgsConstructor
@Entity
@Table(name = INVENTORY_TABLE_NAME)
public class InventoryPojo {
    @Id
    private Long id;
    @Column(nullable = false)
    private Long quantity;
}
