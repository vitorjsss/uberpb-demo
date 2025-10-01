package com.uberpb.repository.json;

import com.uberpb.model.Corrida;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.enums.Categoria;
import com.uberpb.repository.BaseRepository;
import com.uberpb.repository.CorridaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CorridaRepositoryJSON extends BaseRepository<Corrida> implements CorridaRepository {

    public CorridaRepositoryJSON() {
        super("corridas");
    }

    public Corrida save(Corrida corrida) {
        lock.writeLock().lock();
        try {
            // Gerar ID único se não tiver
            if (corrida.getId() == 0) {
                corrida.setId(generateNextId());
            }

            // Carregar corridas existentes
            List<Corrida> corridas = loadAll();

            // Adicionar nova corrida
            corridas.add(corrida);

            // Salvar lista atualizada
            saveAll(corridas);

            return corrida;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Corrida> findById(int id) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findAll() {
        return loadAll();
    }

    public Corrida update(Corrida corrida) {
        lock.writeLock().lock();
        try {
            List<Corrida> corridas = loadAll();

            // Encontrar corrida existente
            for (int i = 0; i < corridas.size(); i++) {
                if (corridas.get(i).getId() == corrida.getId()) {
                    corridas.set(i, corrida);
                    saveAll(corridas);
                    return corrida;
                }
            }

            throw new IllegalArgumentException("Corrida com ID " + corrida.getId() + " não encontrada");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean deleteById(int id) {
        lock.writeLock().lock();
        try {
            List<Corrida> corridas = loadAll();

            // Remover corrida se existir
            boolean removed = corridas.removeIf(corrida -> corrida.getId() == id);

            if (removed) {
                saveAll(corridas);
            }

            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Métodos específicos para Corrida
    public List<Corrida> findByPassageiroId(int passageiroId) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getPassageiroId() == passageiroId)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByMotoristaId(int motoristaId) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getMotoristaId() == motoristaId)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByStatus(CorridaStatus status) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getStatus() == status)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByCategoria(Categoria categoria) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getCategoria() == categoria)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByVeiculoId(int veiculoId) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getVeiculoId() == veiculoId)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByOrigem(String origem) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> origem.equalsIgnoreCase(corrida.getOrigem()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByDestino(String destino) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> destino.equalsIgnoreCase(corrida.getDestino()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Corrida> findByOrigemAndDestino(String origem, String destino) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> origem.equalsIgnoreCase(corrida.getOrigem())
                            && destino.equalsIgnoreCase(corrida.getDestino()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    // Métodos para estatísticas
    public long countByStatus(CorridaStatus status) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getStatus() == status)
                    .count();
        } finally {
            lock.readLock().unlock();
        }
    }

    public double getTotalReceitaByMotorista(int motoristaId) {
        lock.readLock().lock();
        try {
            List<Corrida> corridas = loadAll();
            return corridas.stream()
                    .filter(corrida -> corrida.getMotoristaId() == motoristaId
                            && corrida.getStatus() == CorridaStatus.FINALIZADA)
                    .mapToDouble(Corrida::getPrecoEstimado)
                    .sum();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    protected int getId(Corrida entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Corrida entity, int id) {
        entity.setId(id);
    }
}