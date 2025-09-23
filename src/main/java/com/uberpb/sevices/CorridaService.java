package com.uberpb.service;

import com.uberpb.model.Corrida;
import com.uberpb.model.CorridaStatus;
import com.uberpb.repository.CorridaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CorridaService {

    private final CorridaRepository repository;

    public CorridaService() {
        repository = new CorridaRepository();
    }

    public Corrida criarCorrida(String origem, String destino, int passageiroId, int motoristaId, double distancia) {
        int id = repository.getNextId();
        Corrida corrida = new Corrida(id, origem, destino, null, passageiroId, motoristaId, 0, distancia);
        repository.add(corrida);
        return corrida;
    }

    public void iniciarCorrida(int corridaId) {
        Corrida corrida = getCorridaById(corridaId);
        if (corrida != null) {
            corrida.iniciarCorrida();
            repository.update(corrida);
        }
    }

    public void finalizarCorrida(int corridaId) {
        Corrida corrida = getCorridaById(corridaId);
        if (corrida != null) {
            corrida.finalizarCorrida();
            repository.update(corrida);
        }
    }

    public void cancelarCorrida(int corridaId) {
        Corrida corrida = getCorridaById(corridaId);
        if (corrida != null) {
            corrida.cancelarCorrida();
            repository.update(corrida);
        }
    }

    public List<Corrida> listarCorridas() {
        return repository.getAll();
    }

    public List<Corrida> listarCorridasPorPassageiro(int passageiroId) {
        return repository.getAll().stream()
                .filter(c -> c.getPassageiroId() == passageiroId)
                .collect(Collectors.toList());
    }

    public List<Corrida> listarCorridasPorMotorista(int motoristaId) {
        return repository.getAll().stream()
                .filter(c -> c.getMotoristaId() == motoristaId)
                .collect(Collectors.toList());
    }

    public Corrida getCorridaById(int id) {
        return repository.getAll().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
