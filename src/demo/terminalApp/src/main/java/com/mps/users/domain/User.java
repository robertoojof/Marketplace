package com.mps.users.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    private UUID id;

    @NotBlank(message = "Login não pode ser vazio")
    @Size(max = 12, message = "Login deve ter no máximo {max} caracteres")
    @Pattern(regexp = "[^0-9]*", message = "Login não pode conter números")
    @Column(name = "login", unique = true, nullable = false, length = 12)
    private String login;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, max = 128, message = "Senha deve ter entre {min} e {max} caracteres")
    @Pattern(regexp = ".*[A-Z].*", message = "Senha deve conter ao menos uma letra maiúscula")
    @Pattern(regexp = ".*[a-z].*", message = "Senha deve conter ao menos uma letra minúscula")
    @Pattern(regexp = ".*[0-9].*", message = "Senha deve conter ao menos um número")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{}|'].*", message = "Senha deve conter ao menos um caractere especial")
    @Column(name = "senha", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;
}
