package com.uberpb.repository;

import com.uberpb.model.Motorista;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MotoristaRepositoryJSON extends BaseRepository<Motorista> implements MotoristaRepository {
    
    public MotoristaRepositoryJSON() {
        super("motoristas");
    }
    
    @Override
    public Motorista save(Motorista motorista) {
        lock.writeLock().lock();
        try {
            // Verificar se CNH já existe
            if (motorista.getCnh() != null && findByCnh(motorista.getCnh()).isPresent()) {
                throw new IllegalArgumentException("Motorista com CNH " + motorista.getCnh() + " já existe");
            }
            
            // Gerar ID único se não tiver
            if (motorista.getId() == 0) {
                motorista.setId(generateNextId());
            }
            
            // Carregar motoristas existentes
            List<Motorista> motoristas = loadAll();
            
            // Adicionar novo motorista
            motoristas.add(motorista);
            
            // Salvar lista atualizada
            saveAll(motoristas);
            
            return motorista;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<Motorista> findById(int id) {
        lock.readLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            return motoristas.stream()
                    .filter(motorista -> motorista.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Motorista> findAll() {
        return loadAll();
    }
    
    @Override
    public Motorista update(Motorista motorista) {
        lock.writeLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            
            // Encontrar motorista existente
            for (int i = 0; i < motoristas.size(); i++) {
                if (motoristas.get(i).getId() == motorista.getId()) {
                    motoristas.set(i, motorista);
                    saveAll(motoristas);
                    return motorista;
                }
            }
            
            throw new IllegalArgumentException("Motorista com ID " + motorista.getId() + " não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        lock.writeLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            
            // Remover motorista se existir
            boolean removed = motoristas.removeIf(motorista -> motorista.getId() == id);
            
            if (removed) {
                saveAll(motoristas);
            }
            
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public List<Motorista> findByLocalizacao(String localizacao) {
        lock.readLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            return motoristas.stream()
                    .filter(motorista -> localizacao.equals(motorista.getLocalizacaoAtual()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Motorista> findDisponiveis() {
        lock.readLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            return motoristas.stream()
                    .filter(Motorista::isDisponivel)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Motorista> findAtivos() {
        lock.readLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            return motoristas.stream()
                    .filter(Motorista::isAtivo)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Optional<Motorista> findByCnh(String cnh) {
        lock.readLock().lock();
        try {
            List<Motorista> motoristas = loadAll();
            return motoristas.stream()
                    .filter(motorista -> cnh.equals(motorista.getCnh()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    protected int getId(Motorista entity) {
        return entity.getId();
    }
    
    @Override
    protected void setId(Motorista entity, int id) {
        entity.setId(id);
    }
}
