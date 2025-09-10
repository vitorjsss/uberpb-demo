package com.uberpb.repository;

import com.uberpb.model.Passageiro;

import java.util.List;
import java.util.Optional;

public interface PassageiroRepository {
    Passageiro save(Passageiro passageiro);
    Optional<Passageiro> findById(int id);
    List<Passageiro> findAll();
    Passageiro update(Passageiro passageiro);
    boolean deleteById(int id);
    List<Passageiro> findByLocalizacao(String localizacao);
    List<Passageiro> findEmCorrida();
}
