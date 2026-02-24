package site.nomoreparties.stellarburgers.generators;
import com.google.gson.Gson;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.clients.ApiClientUser;
import site.nomoreparties.stellarburgers.model.GetIngredientsResponse;
import site.nomoreparties.stellarburgers.model.Ingredient;
import java.util.List;
public class IngredientGenerator {
    private static ApiClientUser apiClient = new ApiClientUser();
    private static Gson gson = new Gson();
    static Response responseGetIngredientList = apiClient.getIngredientList();
    static GetIngredientsResponse ingredientListResponse = gson.fromJson(responseGetIngredientList.asString(), GetIngredientsResponse.class);
    static List<Ingredient> ingredientList = ingredientListResponse.getData();
    public static String randomIngredientIdByType(String type) {
        Ingredient ingredient = ingredientList.stream()
                .filter(i -> type.equals(i.getType()))
                .findFirst()
                .orElse(null);
        return ingredient.get_id();
    }
}