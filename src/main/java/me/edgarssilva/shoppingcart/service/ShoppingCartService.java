package me.edgarssilva.shoppingcart.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.CartEntry;
import me.edgarssilva.shoppingcart.model.CheckoutDetails;
import me.edgarssilva.shoppingcart.model.Item;
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

    private void validateCartItem(CartEntry entry) {
        if (!itemService.itemExists(entry.getItemId()))
            throw new WebApplicationException("ID " + entry.getItemId() + " is not a valid item!", Response.Status.BAD_REQUEST);

        if (entry.getQuantity() <= 0)
            throw new WebApplicationException("Item quantity must be greater than 0!", Response.Status.BAD_REQUEST);
    }

    public void addCartItem(Long cartId, CartEntry entry) {
        validateCartItem(entry);

        getCart(cartId).addItem(entry);
    }

    public List<CartEntry> getCartItems(Long cartId) {
        return getCart(cartId).getItems();
    }

    public CartEntry getCartItem(Long cartId, Long itemId) {
        return getCart(cartId).getItem(itemId)
                .orElseThrow(() -> new WebApplicationException("Item ID " + itemId + " not found in cart!" + cartId, Response.Status.NOT_FOUND));
    }

    public void updateCartItem(Long cartId, CartEntry entry) {
        validateCartItem(entry);

        if (!getCart(cartId).updateItem(entry))
            throw new WebApplicationException("Item ID " + entry.getItemId() + " not found in cart!" + cartId, Response.Status.NOT_FOUND);
    }


    public void removeCartItem(Long cartId, Long itemId) {
        if (!getCart(cartId).removeItem(itemId))
            throw new WebApplicationException("Item ID " + itemId + " not found in cart!" + cartId, Response.Status.NOT_FOUND);
    }

    public CheckoutDetails checkout(Long cartId) {
        ShoppingCart cart = getCart(cartId);
        if (cart.isEmpty())
            throw new WebApplicationException("Cart is empty!", Response.Status.BAD_REQUEST);

        List<CheckoutDetails.ItemCost> costs = new ArrayList<>();
        double total = 0;

        for (CartEntry entry : cart.getItems()) {
            Item item = itemService.getItem(entry.getItemId());
            double itemTotal = item.getPrice() * entry.getQuantity();
            costs.add(new CheckoutDetails.ItemCost(item.getName(), item.getPrice(), entry.getQuantity(), itemTotal));
            total += itemTotal;
        }

        return new CheckoutDetails(costs, total);
    }

    public void clearCartItems(Long cartId) {
        getCart(cartId).clear();
    }

    public void deleteCart(Long cartId) {
        if (carts.remove(cartId) == null)
            throw new WebApplicationException("Cart ID " + cartId + " not found!", Response.Status.NOT_FOUND);
    }
}