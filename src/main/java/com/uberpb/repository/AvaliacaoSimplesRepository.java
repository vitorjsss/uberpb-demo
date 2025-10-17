package com.uberpb.repository;

import com.uberpb.model.AvaliacaoSimples;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository simplificado para avaliações sem dependências Jackson
 * Usado para testar e demonstrar a funcionalidade básica
 */
public class AvaliacaoSimplesRepository {

    private static final String DATA_DIR = "database";
    private static final String AVALIACOES_FILE = "avaliacoes/avaliacoes.json";
    private final Path dataPath;

    public AvaliacaoSimplesRepository() {
        this.dataPath = Paths.get(DATA_DIR, AVALIACOES_FILE);
        initializeDataFile();
    }

    private void initializeDataFile() {
        try {
            Files.createDirectories(dataPath.getParent());
            if (!Files.exists(dataPath)) {
                Files.write(dataPath, "[]".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar arquivo de avaliações", e);
        }
    }

    public AvaliacaoSimples save(AvaliacaoSimples avaliacao) {
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação não pode ser nula");
        }

        try {
            // Gerar ID simples baseado no timestamp
            if (avaliacao.getId() == 0) {
                avaliacao.setId((int) System.currentTimeMillis() % 100000);
            }

            // Ler conteúdo atual
            String currentContent = Files.readString(dataPath);
            
            // Se arquivo está vazio ou contém apenas []
            if (currentContent.trim().equals("[]")) {
                String newContent = "[\n  " + avaliacao.toJson() + "\n]";
                Files.write(dataPath, newContent.getBytes());
            } else {
                // Adicionar nova avaliação ao final
                String contentWithoutClosing = currentContent.trim();
                contentWithoutClosing = contentWithoutClosing.substring(0, contentWithoutClosing.length() - 1); // remove ]
                
                String newContent;
                if (contentWithoutClosing.trim().equals("[")) {
                    newContent = contentWithoutClosing + "\n  " + avaliacao.toJson() + "\n]";
                } else {
                    newContent = contentWithoutClosing + ",\n  " + avaliacao.toJson() + "\n]";
                }
                
                Files.write(dataPath, newContent.getBytes());
            }

            return avaliacao;
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar avaliação", e);
        }
    }

    public List<AvaliacaoSimples> findAll() {
        try {
            String content = Files.readString(dataPath);
            List<AvaliacaoSimples> avaliacoes = new ArrayList<>();
            
            // Parse simples do JSON (apenas para demonstração)
            // Em produção, seria melhor usar uma biblioteca JSON adequada
            
            return avaliacoes;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar avaliações", e);
        }
    }

    public int countAvaliacoes() {
        try {
            String content = Files.readString(dataPath);
            // Contar número de objetos JSON de forma simples
            int count = 0;
            for (int i = 0; i < content.length() - 1; i++) {
                if (content.charAt(i) == '{') {
                    count++;
                }
            }
            return count;
        } catch (IOException e) {
            return 0;
        }
    }

    public String getJsonContent() {
        try {
            return Files.readString(dataPath);
        } catch (IOException e) {
            return "[]";
        }
    }
}