package com.uberpb.repository.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uberpb.model.HistoricoItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para persistência do histórico de corridas em cache
 * T18.4: Garantir persistência do histórico no banco de dados
 */
public class HistoricoRepository {

    private static final String HISTORICO_FILE = "database/historico/historico.json";
    private final ObjectMapper objectMapper;

    public HistoricoRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Salva o histórico completo em cache
     */
    public void salvarHistorico(List<HistoricoItem> historico) {
        try {
            File file = new File(HISTORICO_FILE);
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, historico);
        } catch (IOException e) {
            System.err.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    /**
     * Carrega o histórico do cache
     */
    public List<HistoricoItem> carregarHistorico() {
        try {
            File file = new File(HISTORICO_FILE);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<HistoricoItem>>() {});
        } catch (IOException e) {
            System.err.println("Erro ao carregar histórico: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Limpa o cache do histórico
     */
    public void limparHistorico() {
        try {
            File file = new File(HISTORICO_FILE);
            if (file.exists()) {
                objectMapper.writeValue(file, new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar histórico: " + e.getMessage());
        }
    }
}
