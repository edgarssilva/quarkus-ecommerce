package me.edgarssilva.shoppingcart.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ItemService {
    private final Map<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong itemIds = new AtomicLong();

    public void addItem(Item item) {
        if (item.getPrice() <= 0)
            throw new WebApplicationException("Item price must be greater than 0!", Response.Status.BAD_REQUEST);

        Long id = itemIds.incrementAndGet();
        item.setId(id);
        items.put(id, item);
    }

    public List<Item> getAllItems() {
        return List.copyOf(items.values());
    }

    public Item getItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId)).orElseThrow(() -> new WebApplicationException("Item ID " + itemId + " not found!", Response.Status.NOT_FOUND));
    }

    public boolean itemExists(Long itemId) {
        return items.containsKey(itemId);
    }

    public void updateItem(Item item) {
        if (!items.containsKey(item.getId()))
            throw new WebApplicationException("Item ID " + item.getId() + " not found!", Response.Status.NOT_FOUND);

        items.put(item.getId(), item);
    }

    public void removeItem(Long itemId) {
        if (items.remove(itemId) == null)
            throw new WebApplicationException("Item ID " + itemId + " not found!", Response.Status.NOT_FOUND);
    }

}
