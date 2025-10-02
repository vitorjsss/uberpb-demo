package com.uberpb.session;

import com.uberpb.model.User;
import com.uberpb.repository.DatabaseManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionManager {
    private static final String SESSION_FILE = "database/session/current_session.json";
    private static User currentUser = null;

    public static void saveSession(User user) {
        try {
            // Criar diretório se não existir
            Path sessionDir = Paths.get("database/session");
            Files.createDirectories(sessionDir);

            // Salvar dados da sessão
            SessionData sessionData = new SessionData();
            sessionData.setUserId(user.getId());
            sessionData.setEmail(user.getEmail());
            sessionData.setLoginTime(LocalDateTime.now());
            sessionData.setLastActivity(LocalDateTime.now());

            // Converter para JSON manualmente
            String json = sessionDataToJson(sessionData);
            Files.write(Paths.get(SESSION_FILE), json.getBytes());

            currentUser = user;
            // System.out.println("Sessão salva em: " + SESSION_FILE);
        } catch (IOException e) {
            System.err.println("Erro ao salvar sessão: " + e.getMessage());
        }
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            loadSession();
        }
        return currentUser;
    }

    private static void loadSession() {
        try {
            if (Files.exists(Paths.get(SESSION_FILE))) {
                String json = Files.readString(Paths.get(SESSION_FILE));
                SessionData sessionData = parseSessionData(json);

                // Verificar se a sessão ainda é válida (menos de 24h)
                if (sessionData.getLastActivity().isAfter(LocalDateTime.now().minusHours(24))) {
                    // Recarregar usuário do banco
                    DatabaseManager db = new DatabaseManager();
                    var userOpt = db.findUserByEmail(sessionData.getEmail());
                    if (userOpt.isPresent()) {
                        currentUser = userOpt.get();

                        // Atualizar última atividade
                        updateLastActivity();
                    } else {
                        // Usuário não existe mais, limpar sessão
                        clearSession();
                    }
                } else {
                    // Sessão expirada
                    System.out.println("Sessão anterior expirada.");
                    clearSession();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar sessão: " + e.getMessage());
            clearSession();
        }
    }

    public static void updateLastActivity() {
        if (currentUser != null) {
            try {
                if (Files.exists(Paths.get(SESSION_FILE))) {
                    String json = Files.readString(Paths.get(SESSION_FILE));
                    SessionData sessionData = parseSessionData(json);
                    sessionData.setLastActivity(LocalDateTime.now());

                    String updatedJson = sessionDataToJson(sessionData);
                    Files.write(Paths.get(SESSION_FILE), updatedJson.getBytes());
                }
            } catch (IOException e) {
                System.err.println("Erro ao atualizar atividade da sessão: " + e.getMessage());
            }
        }
    }

    public static void logout() {
        clearSession();
        System.out.println("Logout realizado com sucesso!");
    }

    private static void clearSession() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
            currentUser = null;
        } catch (IOException e) {
            System.err.println("Erro ao limpar sessão: " + e.getMessage());
        }
    }

    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public static void trackActivity(String action) {
        if (currentUser != null) {
            System.out.println("[Sessão] " + currentUser.getNome() + " - " + action);
            updateLastActivity();
        }
    }

    // Métodos auxiliares para serialização manual
    private static String sessionDataToJson(SessionData sessionData) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"userId\": ").append(sessionData.getUserId()).append(",\n");
        json.append("  \"email\": \"").append(sessionData.getEmail()).append("\",\n");
        json.append("  \"loginTime\": \"")
                .append(sessionData.getLoginTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\",\n");
        json.append("  \"lastActivity\": \"")
                .append(sessionData.getLastActivity().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\"\n");
        json.append("}");
        return json.toString();
    }

    private static SessionData parseSessionData(String json) {
        SessionData sessionData = new SessionData();

        // Parse manual básico
        String[] lines = json.replace("{", "").replace("}", "").split(",");
        for (String line : lines) {
            line = line.trim();
            if (line.contains("userId")) {
                String value = line.split(":")[1].trim();
                sessionData.setUserId(Integer.parseInt(value));
            } else if (line.contains("email")) {
                String value = line.split(":")[1].trim().replace("\"", "");
                sessionData.setEmail(value);
            } else if (line.contains("loginTime")) {
                String value = line.split(":", 2)[1].trim().replace("\"", "");
                sessionData.setLoginTime(LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else if (line.contains("lastActivity")) {
                String value = line.split(":", 2)[1].trim().replace("\"", "");
                sessionData.setLastActivity(LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        }

        return sessionData;
    }
}