package step.learning.dall.dao;

import step.learning.dall.dto.CartItem;

import java.util.UUID;

public class CartDao {
    public CartItem[] getCart() {
        return new CartItem[] {
                new CartItem(UUID.randomUUID(), UUID.randomUUID(), 1),
                new CartItem(UUID.randomUUID(), UUID.randomUUID(), 2),
                new CartItem(UUID.randomUUID(), UUID.randomUUID(), 3),
        };
    }
}
