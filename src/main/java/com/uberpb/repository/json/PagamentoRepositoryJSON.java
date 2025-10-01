package com.uberpb.repository.json;

import com.uberpb.model.Pagamento;
import com.uberpb.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public class PagamentoRepositoryJSON extends BaseRepository<Pagamento> {

    public PagamentoRepositoryJSON() {
        super("pagamentos"); // cria/usa pagamentos.json
    }

    // Salvar novo pagamento
    public Pagamento salvar(Pagamento p) {
        lock.writeLock().lock();
        try {
            if (p.getId() == 0) {
                p.setId(generateNextId());
            }
            List<Pagamento> lista = loadAll();
            lista.add(p);
            saveAll(lista);
            return p;
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Buscar pagamento por ID
    public Optional<Pagamento> buscarPorId(int id) {
        lock.readLock().lock();
        try {
            return loadAll().stream()
                    .filter(x -> x.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    // Listar todos os pagamentos
    public List<Pagamento> listarTodos() {
        lock.readLock().lock();
        try {
            return loadAll();
        } finally {
            lock.readLock().unlock();
        }
    }

    // Atualizar pagamento existente
    public void atualizar(Pagamento p) {
        lock.writeLock().lock();
        try {
            List<Pagamento> lista = loadAll();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() == p.getId()) {
                    lista.set(i, p);
                    saveAll(lista);
                    return;
                }
            }
            throw new IllegalArgumentException("Pagamento não encontrado para atualização");
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected int getId(Pagamento entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Pagamento entity, int id) {
        entity.setId(id);
    }
}
