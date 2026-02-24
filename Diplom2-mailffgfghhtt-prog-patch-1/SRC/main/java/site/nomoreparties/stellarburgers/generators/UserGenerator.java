package site.nomoreparties.stellarburgers.generators;
import com.github.javafaker.Faker;
import site.nomoreparties.stellarburgers.model.User;
public class UserGenerator {
    public static Faker faker = new Faker();
    public static User randomUser() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password(9, 10))
                .setName(faker.name().fullName());
    }
    public static User randomUserWithoutPassword() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setName(faker.name().fullName());
    }
    public static User randomUserWithoutName() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password(9, 10));
    }
    public static User randomUserWithoutEmail() {
        return new User()
                .setName(faker.name().fullName())
                .setPassword(faker.internet().password(9, 10));
    }
}