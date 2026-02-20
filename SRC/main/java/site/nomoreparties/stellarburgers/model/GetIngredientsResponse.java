package site.nomoreparties.stellarburgers.model;
import java.util.List;
import lombok.*;
@Getter
public class GetIngredientsResponse {
    private boolean success;
    private List<Ingredient> data;
}