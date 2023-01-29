package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public InventoryPojo getById(Long id) {
        return inventoryDao.getById(id);
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.getAll();
    }


    public InventoryPojo addQuantity(Long id, Long quantity) throws ApiException {
        InventoryPojo pojo = inventoryDao.getById(id);
        if (pojo == null) {
            InventoryPojo newInventory = new InventoryPojo();
            newInventory.setId(id);
            newInventory.setQuantity(quantity);
            return inventoryDao.create(newInventory);
        } else {
            pojo.setQuantity(pojo.getQuantity() + quantity);
            return pojo;
        }

    }

    public InventoryPojo getQuantity(Long id) {
        InventoryPojo pojo = inventoryDao.getById(id);
        InventoryPojo result = new InventoryPojo();
        result.setId(id);
        if (pojo == null) {
            result.setQuantity(0L);
        } else {
            result.setQuantity(pojo.getQuantity());
        }
        return result;
    }

    public InventoryPojo removeQuantity(Long id, Long quantity) throws ApiException {
        InventoryPojo pojo = inventoryDao.getById(id);
        if (pojo.getQuantity() < quantity) {
            throw new ApiException("Quantity of product not satisfied by inventory");
        }
        pojo.setQuantity(pojo.getQuantity() - quantity);
        return pojo;
    }
}
