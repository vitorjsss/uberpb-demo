package com.uberpb.repository;

import com.uberpb.model.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository {
    Veiculo save(Veiculo veiculo);
    Optional<Veiculo> findById(int id);
    List<Veiculo> findAll();
    Veiculo update(Veiculo veiculo);
    boolean deleteById(int id);
    Optional<Veiculo> findByPlaca(String placa);
    List<Veiculo> findByTipo(String tipo);
    List<Veiculo> findByCategoria(String categoria);
    List<Veiculo> findByMarca(String marca);
}
