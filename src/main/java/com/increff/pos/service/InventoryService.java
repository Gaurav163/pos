package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class InventoryService extends AbstractService<InventoryPojo> {
    private final InventoryDao inventoryDao;

    @Autowired
    public InventoryService(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
        this.dao = inventoryDao;
    }

    @Transactional
    public InventoryPojo create(Long id, Long quantity) throws ApiException {
        if (inventoryDao.check(id)) {
            throw new ApiException("Inventory Already exist.");
        }
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(id);
        pojo.setQuantity(quantity);
        return inventoryDao.create(pojo);
    }

    @Transactional
    public void addQuantity(Long id, Long quantity) throws ApiException {
        InventoryPojo pojo = inventoryDao.findById(id);
        if (pojo == null) {
            throw new ApiException("Inventory With given Id not exist");
        }
        pojo.setQuantity(pojo.getQuantity() + quantity);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo getQuantity(Long id, Long quantity) throws ApiException {
        InventoryPojo pojo = inventoryDao.findById(id);
        if (pojo.getQuantity() < quantity) {
            throw new ApiException("Quantity of Product not satisfied by Inventory.");
        }
        pojo.setQuantity(pojo.getQuantity() - quantity);
        return pojo;
    }
}
