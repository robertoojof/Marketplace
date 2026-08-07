package com.mps.users.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.shared.facade.FacadeSingletonController;
import com.mps.shared.facade.FachadasDeTeste;
import com.mps.shared.facade.UserFacade;
import com.mps.shared.security.SessaoUsuario;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class UserViewTest {

    private static final String SENHA_VALIDA = "Senha@2024!";

    private FacadeSingletonController facade;
    private UserFacade userFacade;
    private PrintStream saidaOriginal;

    @BeforeEach
    void preparar() {
        FachadasDeTeste.reiniciar();
        facade = FacadeSingletonController.getInstance(false);
        userFacade = UserFacade.getInstance();
        saidaOriginal = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void limpar() {
        System.setOut(saidaOriginal);
        SessaoUsuario.getInstance().encerrar();
        FachadasDeTeste.reiniciar();
    }

    private UserView viewCom(String entrada) {
        return new UserView(new Scanner(entrada), userFacade, facade);
    }

    private User usuarioCadastrado() {
        User user = new User(UUID.randomUUID(), "vendedorum", "111.111.111-11", "Vendedor Um",
                "v@email.com", SENHA_VALIDA, Role.USER, true);
        userFacade.adicionarUsuario(user);
        return user;
    }

    @Test
    void atualizacao_rejeitada_nao_deve_alterar_o_usuario_armazenado() {
        User original = usuarioCadastrado();

        // 4 = atualizar; login "joao123" viola a regra de não conter números;
        // os demais campos ficam em branco para manter o valor atual; 9 = voltar.
        viewCom(String.join("\n", "4", original.getId().toString(),
                "joao123", "", "", "", "", "9") + "\n").userMenu();

        User armazenado = userFacade.buscarUsuarioPorId(original.getId()).orElseThrow();
        assertEquals("vendedorum", armazenado.getLogin());
    }

    @Test
    void atualizacao_valida_deve_alterar_apenas_os_campos_informados() {
        User original = usuarioCadastrado();

        viewCom(String.join("\n", "4", original.getId().toString(),
                "", "Nome Alterado", "", "", "", "9") + "\n").userMenu();

        User armazenado = userFacade.buscarUsuarioPorId(original.getId()).orElseThrow();
        assertEquals("Nome Alterado", armazenado.getName());
        assertEquals("vendedorum", armazenado.getLogin());
        assertEquals("v@email.com", armazenado.getEmail());
        assertEquals(SENHA_VALIDA, armazenado.getPassword());
    }
}
