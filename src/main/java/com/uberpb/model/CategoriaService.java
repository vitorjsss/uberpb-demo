package com.uberpb.model;

import com.uberpb.model.Categoria;
import com.uberpb.model.Veiculo;
import com.uberpb.repository.SimpleJSONRepository;

import java.util.List;

public class CategoriaService {

    private final SimpleJSONRepository veiculoRepository;

    // Construtor
    public CategoriaService(SimpleJSONRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    /**
     * Classifica um veículo em uma categoria de acordo com suas características.
     */
    public Categoria classificarVeiculo(Veiculo veiculo) {
        // Regras de classificação
        if (veiculo.getNumeroPassageiros() >= 6) {
            return Categoria.XL;
        }
        if (veiculo.getCapacidadePortaMalas() > 450) {
            return Categoria.BAG;
        }
        if (veiculo.getMarca() != null && veiculo.getMarca().equalsIgnoreCase("BMW")
                || veiculo.getMarca().equalsIgnoreCase("Mercedes")) {
            if (Categoria.BLACK.veiculoAtende(veiculo.getNumeroPassageiros(), veiculo.getAno())) {
                return Categoria.BLACK;
            }
        }
        if (Categoria.COMFORT.veiculoAtende(veiculo.getNumeroPassageiros(), veiculo.getAno())) {
            return Categoria.COMFORT;
        }
        return Categoria.UBER_X;
    }

    /**
     * Atualiza a categoria do veículo e salva no repositório.
     */
    public Veiculo atualizarCategoria(Veiculo veiculo) {
        Categoria categoria = classificarVeiculo(veiculo);
        veiculo.setCategoria(categoria.getNome());
        return veiculoRepository.save(veiculo);
    }

    /**
     * Classifica e atualiza todos os veículos do repositório.
     */
    public void classificarTodos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        for (Veiculo v : veiculos) {
            atualizarCategoria(v);
        }
    }

    /**
     * Busca veículos por categoria.
     */
    public List<Veiculo> buscarPorCategoria(Categoria categoria) {
        return veiculoRepository.findAll()
                .stream()
                .filter(v -> v.getCategoria() != null && v.getCategoria().equalsIgnoreCase(categoria.getNome()))
                .toList();
    }
}