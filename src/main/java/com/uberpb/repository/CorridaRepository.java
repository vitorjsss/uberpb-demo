package com.uberpb.repository;

import com.uberpb.model.Corrida;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.enums.Categoria;

import java.util.List;
import java.util.Optional;

public interface CorridaRepository {
    Corrida save(Corrida corrida);

    Optional<Corrida> findById(int id);

    List<Corrida> findAll();

    Corrida update(Corrida corrida);

    boolean deleteById(int id);

    // Métodos específicos para Corrida
    List<Corrida> findByPassageiroId(int passageiroId);

    List<Corrida> findByMotoristaId(int motoristaId);

    List<Corrida> findByStatus(CorridaStatus status);

    List<Corrida> findByCategoria(Categoria categoria);

    List<Corrida> findByVeiculoId(int veiculoId);

    List<Corrida> findByOrigem(String origem);

    List<Corrida> findByDestino(String destino);

    List<Corrida> findByOrigemAndDestino(String origem, String destino);

    // Métodos para estatísticas
    long countByStatus(CorridaStatus status);

    double getTotalReceitaByMotorista(int motoristaId);
}
