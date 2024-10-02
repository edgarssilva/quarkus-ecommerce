package me.edgarssilva.shoppingcart.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {
    private Long id;
    private Map<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong itemIds = new AtomicLong(0);

    public ShoppingCart(Long id) {
        this.id = id;
    }

    public void addItem(Item item) {
        items.merge(item.getId(), item, (existingItem, newItem) -> {
            existingItem.addQuantity(existingItem.getQuantity() + newItem.getQuantity());
            return existingItem;
        });
    }

    public void removeItem(Long id) {
        items.remove(id);
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.values().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

    public Item createItem(Item item) {
        item.setId(itemIds.incrementAndGet());
        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> getItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public Optional<Item> updateItem(Long itemId, Item item) {
        if (!items.containsKey(itemId)) return Optional.empty();

        item.setId(itemId);
        items.put(itemId, item);
        return Optional.of(item);
    }

    public boolean deleteItem(Long itemId) {
        return items.remove(itemId) != null;
    }
}