package hexlet.code.utils;

import hexlet.code.models.Label;
import hexlet.code.models.TaskStatus;
import hexlet.code.models.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.instancio.Select.field;

@Getter
@Component
public class InstansioModelGenerator {
    private Model<Label> labelModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<User> userModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {

        labelModel = Instancio.of(Label.class)
                .ignore(field(Label::getId))
                .ignore(field(Label::getCreatedAt))
                .supply(field(Label::getName), () -> faker.gameOfThrones().dragon())
                .toModel();

        userModel = Instancio.of(User.class)
                .ignore(field(User::getId))
                .ignore(field(User::getCreatedAt))
                .supply(field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(field(User::getFirstName), () -> faker.name().firstName())
                .supply(field(User::getLastName), () -> faker.name().lastName())
                .set(field(User::getPassword), "12345")
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(field(TaskStatus::getId))
                .ignore(field(TaskStatus::getCreatedAt))
                .supply(field(TaskStatus::getName), () -> faker.gameOfThrones().character())
                .toModel();
    }
}
