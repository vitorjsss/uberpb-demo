package com.uberpb.repository;

import com.uberpb.model.Corrida;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CorridaRepository {

    private static final String CORRIDAS_FILE = "database/corridas/corridas.json";
    private static final String ID_COUNTER_FILE = "database/id_counter.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Corrida> corridas;

    public CorridaRepository() {
        corridas = loadCorridas();
    }

    private List<Corrida> loadCorridas() {
        try {
            File file = new File(CORRIDAS_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                mapper.writeValue(file, new ArrayList<>());
            }
            return mapper.readValue(file, new TypeReference<List<Corrida>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Corrida> getAll() {
        return corridas;
    }

    public void saveAll() {
        try {
            mapper.writeValue(new File(CORRIDAS_FILE), corridas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        try {
            File file = new File(ID_COUNTER_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                mapper.writeValue(file, mapper.createObjectNode().put("corrida", 0));
            }
            var node = mapper.readTree(file);
            int id = node.get("corrida").asInt() + 1;
            ((com.fasterxml.jackson.databind.node.ObjectNode) node).put("corrida", id);
            mapper.writeValue(file, node);
            return id;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void add(Corrida corrida) {
        corridas.add(corrida);
        saveAll();
    }

    public void update(Corrida corrida) {
        for (int i = 0; i < corridas.size(); i++) {
            if (corridas.get(i).getId() == corrida.getId()) {
                corridas.set(i, corrida);
                break;
            }
        }
        saveAll();
    }
}
