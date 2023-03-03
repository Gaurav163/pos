package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public List<Inventory> getAll() {
        return inventoryDao.getAll();
    }


    public Inventory increaseInventory(Long id, Long quantity) {
        Inventory inventory = inventoryDao.getById(id);
        if (inventory == null) {
            Inventory newInventory = new Inventory();
            newInventory.setId(id);
            newInventory.setQuantity(quantity);
            return inventoryDao.create(newInventory);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            return inventory;
        }

    }

    public Inventory getById(Long id) {
        Inventory inventory = inventoryDao.getById(id);
        Inventory result = new Inventory();
        result.setId(id);
        if (inventory == null) {
            result.setQuantity(0L);
        } else {
            result.setQuantity(inventory.getQuantity());
        }
        return result;
    }

    public void reduceInventory(Long id, Long quantity) throws ApiException {
        Inventory inventory = inventoryDao.getById(id);
        if (inventory == null) {
            throw new ApiException("Zero quantity available");
        }
        if (inventory.getQuantity() < quantity) {
            throw new ApiException("Only " + inventory.getQuantity() + " quantity available");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
    }

    public Inventory updateInventory(Long id, Long quantity) {
        Inventory inventory = inventoryDao.getById(id);
        if (inventory == null) {
            Inventory newInventory = new Inventory();
            newInventory.setId(id);
            newInventory.setQuantity(quantity);
            return inventoryDao.create(newInventory);
        } else {
            inventory.setQuantity(quantity);
            return inventory;
        }

    }
}
