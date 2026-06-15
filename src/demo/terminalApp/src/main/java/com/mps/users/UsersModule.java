package com.mps.users;

import java.util.Scanner;

import com.mps.users.application.UserService;
import com.mps.users.infrastructure.InMemoryUserRepository;
import com.mps.users.presentation.controller.UserController;
import com.mps.users.view.UserView;

public class UsersModule {

    public static UserView create(Scanner scanner) {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        UserService service = new UserService(repository);
        UserController controller = new UserController(service);
        return new UserView(scanner, controller);
    }
}
