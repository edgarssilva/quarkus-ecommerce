package me.edgarssilva.shoppingcart.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.CartEntry;
import me.edgarssilva.shoppingcart.model.ShoppingCart;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ShoppingCartService {
    private final Map<Long, ShoppingCart> carts = new ConcurrentHashMap<>();
    private final AtomicLong cartIds = new AtomicLong();

    @Inject
    ItemService itemService;

    public List<ShoppingCart> getAllCarts() {
        return List.copyOf(carts.values());
    }

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart(cartIds.incrementAndGet());
        carts.put(cart.getId(), cart);
        return cart;
    }

    public ShoppingCart getCart(Long cartId) {
        return Optional.ofNullable(carts.get(cartId))
                .orElseThrow(() -> new WebApplicationException("Cart ID " + cartId + " not found!", Response.Status.NOT_FOUND));
    }

    public void addCartItem(Long cartId, CartEntry entry) {
        if (!itemService.itemExists(entry.getItemId()))
            throw new WebApplicationException("ID " + entry.getItemId() + "is not a valid item! ", Response.Status.BAD_REQUEST);

        getCart(cartId).addItem(entry);
    }

    public Collection<CartEntry> getCartItems(Long cartId) {
        return getCart(cartId).getEntries().values();
    }

    public CartEntry getCartItem(Long cartId, Long itemId) {
        return getCart(cartId).getItem(itemId)
                .orElseThrow(() -> new WebApplicationException("Item ID " + itemId + " not found in cart!" + cartId, Response.Status.NOT_FOUND));
    }

    public void updateCartItem(Long cartId, CartEntry entry) {
        if (!itemService.itemExists(entry.getItemId()))
            throw new WebApplicationException("ID " + entry.getItemId() + "is not a valid item!", Response.Status.BAD_REQUEST);

        if (!getCart(cartId).updateItem(entry))
            throw new WebApplicationException("Item ID " + entry.getItemId() + " not found in cart!" + cartId, Response.Status.NOT_FOUND);
    }


    public boolean isCartEmpty(Long cartId) {
        return getCart(cartId).getEntries().isEmpty();
    }

    public void removeCartItem(Long cartId, Long itemId) {
        if (!getCart(cartId).removeItem(itemId))
            throw new WebApplicationException("Item ID " + itemId + " not found in cart!" + cartId, Response.Status.NOT_FOUND);
    }

    public void clearCartItems(Long cartId) {
        getCart(cartId).clear();
    }

    public void deleteCart(Long cartId) {
        if (carts.remove(cartId) == null)
            throw new WebApplicationException("Cart ID " + cartId + " not found!", Response.Status.NOT_FOUND);
    }
}