package com.choco.shop.dto;

import com.choco.shop.entity.Product;
import java.math.BigDecimal;

public class CartItemDTO {
    private Product product;
    private int quantity;
    private BigDecimal subtotal;

    public CartItemDTO(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
