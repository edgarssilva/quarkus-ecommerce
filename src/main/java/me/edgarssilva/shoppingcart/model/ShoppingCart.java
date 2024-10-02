package me.edgarssilva.shoppingcart.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {
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

    public void clear() {
        entries.clear();
    }
}