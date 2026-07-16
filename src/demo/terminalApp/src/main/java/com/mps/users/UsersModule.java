package com.mps.users;

import java.util.Scanner;

import com.mps.users.application.UserService;
import com.mps.users.domain.IUserRepository;
import com.mps.users.infrastructure.HibernateUserRepository;
import com.mps.users.infrastructure.InMemoryUserRepository;
import com.mps.users.presentation.controller.UserController;
import com.mps.users.view.UserView;

public class UsersModule {

    public record Bundle(IUserRepository repository, UserController controller, UserView view) {
    }

    public static Bundle create(Scanner scanner, boolean usarBancoDeDados) {
        IUserRepository repository = usarBancoDeDados
                ? new HibernateUserRepository()
                : new InMemoryUserRepository();
        UserService service = new UserService(repository);
        UserController controller = new UserController(service);
        UserView view = new UserView(scanner, controller);
        return new Bundle(repository, controller, view);
    }
}
