package me.edgarssilva.shoppingcart.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.edgarssilva.shoppingcart.model.CartEntry;
import me.edgarssilva.shoppingcart.model.KpiMetrics;
import me.edgarssilva.shoppingcart.model.ShoppingCart;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class AnalyticsService {

    @Inject
    ShoppingCartService cartService;

    /**
     * Retrieves key performance indicators (KPIs) for shopping carts.
     *
     * @return an instance of KpiMetrics containing the KPIs
     */
    public KpiMetrics getKPIs() {
        List<ShoppingCart> carts = cartService.getAllCarts();

        long cartsWithItems = carts.stream()
                .filter(cart -> !cart.isEmpty())
                .count();

        IntSummaryStatistics itemsInCartStats = carts.stream()
                .mapToInt(cart -> cart.getItems().size())
                .summaryStatistics();

        List<KpiMetrics.TopItem> topItems = carts.stream()
                .flatMap(cart -> cart.getItems().stream())
                .collect(Collectors.groupingBy(CartEntry::getItemId, Collectors.summingInt(CartEntry::getQuantity)))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new KpiMetrics.TopItem(entry.getKey(), entry.getValue()))
                .toList();
    
        return new KpiMetrics(
                cartsWithItems,
                itemsInCartStats.getMax(),
                itemsInCartStats.getAverage(),
                itemsInCartStats.getMin(),
                topItems
        );
    }
}