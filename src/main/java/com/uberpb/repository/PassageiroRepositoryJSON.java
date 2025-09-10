package com.uberpb.repository;

import com.uberpb.model.Passageiro;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PassageiroRepositoryJSON extends BaseRepository<Passageiro> implements PassageiroRepository {
    
    public PassageiroRepositoryJSON() {
        super("passageiros");
    }
    
    @Override
    public Passageiro save(Passageiro passageiro) {
        lock.writeLock().lock();
        try {
            // Gerar ID único se não tiver
            if (passageiro.getId() == 0) {
                passageiro.setId(generateNextId());
            }
            
            // Carregar passageiros existentes
            List<Passageiro> passageiros = loadAll();
            
            // Adicionar novo passageiro
            passageiros.add(passageiro);
            
            // Salvar lista atualizada
            saveAll(passageiros);
            
            return passageiro;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<Passageiro> findById(int id) {
        lock.readLock().lock();
        try {
            List<Passageiro> passageiros = loadAll();
            return passageiros.stream()
                    .filter(passageiro -> passageiro.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Passageiro> findAll() {
        return loadAll();
    }
    
    @Override
    public Passageiro update(Passageiro passageiro) {
        lock.writeLock().lock();
        try {
            List<Passageiro> passageiros = loadAll();
            
            // Encontrar passageiro existente
            for (int i = 0; i < passageiros.size(); i++) {
                if (passageiros.get(i).getId() == passageiro.getId()) {
                    passageiros.set(i, passageiro);
                    saveAll(passageiros);
                    return passageiro;
                }
            }
            
            throw new IllegalArgumentException("Passageiro com ID " + passageiro.getId() + " não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        lock.writeLock().lock();
        try {
            List<Passageiro> passageiros = loadAll();
            
            // Remover passageiro se existir
            boolean removed = passageiros.removeIf(passageiro -> passageiro.getId() == id);
            
            if (removed) {
                saveAll(passageiros);
            }
            
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public List<Passageiro> findByLocalizacao(String localizacao) {
        lock.readLock().lock();
        try {
            List<Passageiro> passageiros = loadAll();
            return passageiros.stream()
                    .filter(passageiro -> localizacao.equals(passageiro.getLocalizacaoAtual()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Passageiro> findEmCorrida() {
        lock.readLock().lock();
        try {
            List<Passageiro> passageiros = loadAll();
            return passageiros.stream()
                    .filter(Passageiro::isEmCorrida)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    protected int getId(Passageiro entity) {
        return entity.getId();
    }
    
    @Override
    protected void setId(Passageiro entity, int id) {
        entity.setId(id);
    }
}
