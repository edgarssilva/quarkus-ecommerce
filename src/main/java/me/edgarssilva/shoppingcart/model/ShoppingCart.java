package me.edgarssilva.shoppingcart.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@NoArgsConstructor
public class ShoppingCart {
    @Getter
    private Long id;
    private Map<Long, CartEntry> entries = new ConcurrentHashMap<>();

    public ShoppingCart(Long id) {
        this.id = id;
    }

    public void addItem(CartEntry entry) {
        entries.merge(entry.getItemId(), entry, (o, n) -> {
            o.setQuantity(o.getQuantity() + n.getQuantity());
            return o;
        });
    }

    public List<CartEntry> getItems() {
        return List.copyOf(entries.values());
    }

    public Optional<CartEntry> getItem(Long itemId) {
        return Optional.ofNullable(entries.get(itemId));
    }

    public boolean removeItem(Long itemId) {
        return entries.remove(itemId) != null;
    }

    public boolean updateItem(CartEntry item) {
        if (!entries.containsKey(item.getItemId())) return false;
        entries.put(item.getItemId(), item);
        return true;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void clear() {
        entries.clear();
    }
}