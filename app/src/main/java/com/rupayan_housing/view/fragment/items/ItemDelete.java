package com.rupayan_housing.view.fragment.items;

public interface ItemDelete {
    void delete(String vendorId,String storeId,String productId);
    void addTag(Integer position,String categoryId,String productId,String packetId,String productTitl);
    void deleteTag(Integer position,String productId,String packetId);
}
