package com.increff.pos.dao;

import com.increff.pos.pojo.Inventory;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryDao extends AbstractDao<Inventory> {
    public InventoryDao() {
        super(Inventory.class);
    }
}
