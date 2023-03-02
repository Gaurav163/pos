package com.increff.pos.dao;

import com.increff.pos.pojo.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao<Product> {
    private static final String SELECT_BY_BARCODE_LIST = "select b from Product b where barcode in :barcode_list";
    private static final String SELECT_BY_ID_LIST = "select b from Product b where id in :id_list";

    public ProductDao() {
        super(Product.class);
    }

    public List<Product> getListByBarcodeList(List<String> barcodes) {
        TypedQuery<Product> query = getQuery(SELECT_BY_BARCODE_LIST);
        query.setParameter("barcode_list", barcodes);
        return query.getResultList();
    }

    public List<Product> getListByIdList(List<Long> idList) {
        TypedQuery<Product> query = getQuery(SELECT_BY_ID_LIST);
        query.setParameter("id_list", idList);
        return query.getResultList();
    }
}
