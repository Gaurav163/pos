package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.Helper;
import com.increff.pos.pojo.Inventory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class InventoryDaoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private Helper helper;

    @Test
    public void testCreate() {
        Inventory inventory = helper.getInventory(1L, 10L);
        inventoryDao.create(inventory);
        Inventory savedInv = inventoryDao.getById(1L);
        assertNotNull(savedInv);
        assertEquals(inventory.getQuantity(), savedInv.getQuantity());
    }

    @Test
    public void testGetAll() {
        helper.createInventory(1L, 10L);
        helper.createInventory(2L, 20L);
        helper.createInventory(3L, 30L);
        helper.createInventory(4L, 40L);
        List<Inventory> inventories = inventoryDao.getAll();
        assertEquals(4, inventories.size());
    }

    @Test
    public void testGetById() {
        Inventory inventory1 = helper.createInventory(2L, 20L);
        Inventory savedInv = inventoryDao.getById(2L);
        Inventory dummuyInv = inventoryDao.getById(4L);
        assertNotNull(savedInv);
        assertEquals(Long.valueOf(20L), savedInv.getQuantity());
        assertNull(dummuyInv);
    }
}
