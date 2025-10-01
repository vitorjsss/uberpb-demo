package com.uberpb;

import com.uberpb.cli.menus.MenuInicialCLI;
import com.uberpb.model.User;
import com.uberpb.session.SessionManager;

public class App {
    public static void main(String[] args) {
        while (true) {
            // Verificar se já existe uma sessão ativa
            if (SessionManager.isLoggedIn()) {
                User user = SessionManager.getCurrentUser();
                System.out.println("Bem-vindo de volta, " + user.getNome() + "!");
                MenuInicialCLI.menuPrincipal(user);
                // Após sair do menu principal (logout), continua o loop para menu inicial
            } else {
                MenuInicialCLI.menuInicial();
            }
        }
    }
}