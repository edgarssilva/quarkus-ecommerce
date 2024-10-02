package me.edgarssilva.shoppingcart.service;

import jakarta.enterprise.context.ApplicationScoped;
import me.edgarssilva.shoppingcart.model.Item;
import me.edgarssilva.shoppingcart.model.ShoppingCart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ShoppingCartService {
    private final Map<Long, ShoppingCart> carts = new ConcurrentHashMap<>();
    private final AtomicLong cartIds = new AtomicLong();

    public List<ShoppingCart> getAllCarts() {
        return new ArrayList<>(carts.values());
    }

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart(cartIds.incrementAndGet());
        carts.put(cart.getId(), cart);
        return cart;
    }

    public Optional<ShoppingCart> getCart(Long cartId) {
        return Optional.ofNullable(carts.get(cartId));
    }

    public boolean addItem(Long cartId, Long itemId, int quantity) {
        return getCart(cartId).flatMap(cart -> cart.getItem(itemId).map(item -> {
            item.setQuantity(quantity);
            cart.addItem(item);
            return true;
        })).orElse(false);
    }

    public boolean updateItem(Long cartId, Item item) {
        return getCart(cartId).flatMap(cart -> cart.updateItem(item.getId(), item)).isPresent();
    }

    public void clearCart(Long cartId) {
        getCart(cartId).ifPresent(ShoppingCart::clear);
    }

    public boolean isCartEmpty(Long cartId) {
        return getCart(cartId).map(cart -> cart.getItems().isEmpty()).orElse(true);
    }

    public boolean removeItem(Long cartId, Long itemId) {
        return getCart(cartId).map(cart -> cart.deleteItem(itemId)).orElse(false);
    }

    public boolean deleteCart(Long cartId) {
        return carts.remove(cartId) != null;
    }
}