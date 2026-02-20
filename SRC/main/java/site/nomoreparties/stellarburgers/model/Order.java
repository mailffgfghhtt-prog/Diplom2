package site.nomoreparties.stellarburgers.model;

import java.util.List;
import lombok.Getter;

@Getter
public class Order {
    private List<String> ingredients;
    private String orderId; // Добавляем поле


    public Order setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Order setOrderId(String orderId) { // Добавляем сеттер
        this.orderId = orderId;
        return this;
    }
}
