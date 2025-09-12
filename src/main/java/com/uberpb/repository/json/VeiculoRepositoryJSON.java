package com.uberpb.repository.json;

import com.uberpb.model.Veiculo;
import com.uberpb.repository.BaseRepository;
import com.uberpb.repository.VeiculoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VeiculoRepositoryJSON extends BaseRepository<Veiculo> implements VeiculoRepository {

    public VeiculoRepositoryJSON() {
        super("veiculos");
    }

    @Override
    public Veiculo save(Veiculo veiculo) {
        lock.writeLock().lock();
        try {
            // Verificar se placa já existe
            if (veiculo.getPlaca() != null && findByPlaca(veiculo.getPlaca()).isPresent()) {
                throw new IllegalArgumentException("Veículo com placa " + veiculo.getPlaca() + " já existe");
            }

            // Gerar ID único se não tiver
            if (veiculo.getId() == 0) {
                veiculo.setId(generateNextId());
            }

            // Carregar veículos existentes
            List<Veiculo> veiculos = loadAll();

            // Adicionar novo veículo
            veiculos.add(veiculo);

            // Salvar lista atualizada
            saveAll(veiculos);

            return veiculo;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Veiculo> findById(int id) {
        lock.readLock().lock();
        try {
            List<Veiculo> veiculos = loadAll();
            return veiculos.stream()
                    .filter(veiculo -> veiculo.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Veiculo> findAll() {
        return loadAll();
    }

    @Override
    public Veiculo update(Veiculo veiculo) {
        lock.writeLock().lock();
        try {
            List<Veiculo> veiculos = loadAll();

            // Encontrar veículo existente
            for (int i = 0; i < veiculos.size(); i++) {
                if (veiculos.get(i).getId() == veiculo.getId()) {
                    veiculos.set(i, veiculo);
                    saveAll(veiculos);
                    return veiculo;
                }
            }

            throw new IllegalArgumentException("Veículo com ID " + veiculo.getId() + " não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteById(int id) {
        lock.writeLock().lock();
        try {
            List<Veiculo> veiculos = loadAll();

            // Remover veículo se existir
            boolean removed = veiculos.removeIf(veiculo -> veiculo.getId() == id);

            if (removed) {
                saveAll(veiculos);
            }

            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        lock.readLock().lock();
        try {
            List<Veiculo> veiculos = loadAll();
            return veiculos.stream()
                    .filter(veiculo -> placa.equals(veiculo.getPlaca()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Veiculo> findByCategoria(String categoria) {
        lock.readLock().lock();
        try {
            List<Veiculo> veiculos = loadAll();
            return veiculos.stream()
                    .filter(veiculo -> categoria.equals(veiculo.getCategoria()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Veiculo> findByMarca(String marca) {
        lock.readLock().lock();
        try {
            List<Veiculo> veiculos = loadAll();
            return veiculos.stream()
                    .filter(veiculo -> marca.equals(veiculo.getMarca()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    protected int getId(Veiculo entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Veiculo entity, int id) {
        entity.setId(id);
    }
}
