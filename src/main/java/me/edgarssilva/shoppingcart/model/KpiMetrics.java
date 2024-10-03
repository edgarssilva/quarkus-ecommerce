package me.edgarssilva.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KpiMetrics {
    private long cartsWithItems;
    private int maxItemsInCart;
    private double avgItemsInCart;
    private int minItemsInCart;
    private List<TopItem> topItems;

    public record TopItem(long itemId, int quantity) {
    }
}