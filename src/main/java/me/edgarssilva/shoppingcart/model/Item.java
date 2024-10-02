package me.edgarssilva.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    public Long id;
    public String name;
    public int quantity;
    public double price;

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
