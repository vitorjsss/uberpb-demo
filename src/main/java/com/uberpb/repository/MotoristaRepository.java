package com.uberpb.repository;

import com.uberpb.model.Motorista;

import java.util.List;
import java.util.Optional;

public interface MotoristaRepository {
    Motorista save(Motorista motorista);
    Optional<Motorista> findById(int id);
    List<Motorista> findAll();
    Motorista update(Motorista motorista);
    boolean deleteById(int id);
    List<Motorista> findByLocalizacao(String localizacao);
    List<Motorista> findDisponiveis();
    List<Motorista> findAtivos();
    Optional<Motorista> findByCnh(String cnh);
}
