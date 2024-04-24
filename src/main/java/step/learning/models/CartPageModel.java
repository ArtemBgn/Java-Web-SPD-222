package step.learning.models;

import step.learning.dall.dto.CartItem;
import step.learning.dall.dto.Product;

import java.util.List;

public class CartPageModel {
    private List<Product> products;
    private List<CartItem> cartItems;

    public CartPageModel(List<Product> products, List<CartItem> cartItem) {
        this.products = products;
        this.cartItems = cartItem;
    }
    public List<Product> getProducts() { return products; }
    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
