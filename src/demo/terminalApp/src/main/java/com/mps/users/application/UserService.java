package com.mps.users.application;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;
import com.mps.users.domain.exception.ValidacaoUsuarioException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserService {

    private final IUserRepository userRepository;
    private final Validator validator;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void adicionarUsuario(User user) {
        validar(user);
        userRepository.salvar(user);
    }

    public List<User> listarUsuarios() {
        return userRepository.buscarTodos();
    }

    public Optional<User> buscarUsuarioPorId(UUID id) {
        return userRepository.buscarPorId(id);
    }

    public User atualizarUsuario(User user) {
        validar(user);
        return userRepository.atualizar(user);
    }

    public void removerUsuario(UUID id) {
        userRepository.deletar(id);
    }

    private void validar(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            List<String> erros = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.toList());
            throw new ValidacaoUsuarioException(erros);
        }
    }
}
