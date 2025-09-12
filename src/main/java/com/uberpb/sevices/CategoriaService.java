package com.uberpb.sevices;

import com.uberpb.model.Categoria;
import com.uberpb.model.Veiculo;
import com.uberpb.repository.VeiculoRepository;

public class CategoriaService {

    private final VeiculoRepository veiculoRepository;

    // Construtor
    public CategoriaService(VeiculoRepository veiculoRepository) {
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
}