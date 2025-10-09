package com.uberpb.model.test;

import com.uberpb.model.Corrida;
import com.uberpb.model.Passageiro;
import com.uberpb.model.Motorista;
import com.uberpb.model.Veiculo;
import com.uberpb.model.pagamento.Pagamento;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.enums.MetodoPagamento;
import com.uberpb.services.ReciboService;
import com.uberpb.repository.json.*;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReciboServiceTest {

    private ReciboService reciboService;
    private int corridaIdTeste;
    
    @BeforeAll
    void setup() {
        reciboService = new ReciboService();

        // Criar dados de teste
        Passageiro p = new Passageiro(1, "Teste", false);
        p.setNome("João");
        p.setEmail("joao@teste.com");
        new PassageiroRepositoryJSON().save(p);

        Motorista m = new Motorista(1, true, "12345678901", "30/12/2025", 0.0, 5, true, "UberX");
        m.setNome("Carlos");
        m.setCnh("12345678901");
        new MotoristaRepositoryJSON().save(m);

        Veiculo v = new Veiculo(1, "HB20", "Hyundai", "Preto", "ABC-1234", "UberX");
        new VeiculoRepositoryJSON().save(v);

        Corrida corrida = new Corrida();
        corrida.setId(1001);
        corrida.setPassageiroId(p.getId());
        corrida.setMotoristaId(m.getId());
        corrida.setVeiculoId(v.getId());
        corrida.setOrigem("UEPB");
        corrida.setDestino("Centro");
        corrida.setCategoria("UberX");
        corrida.setDistancia(10.5);
        corrida.setStatus(CorridaStatus.FINALIZADA);
        corrida.setDataHoraSolicitacao(LocalDateTime.now().minusMinutes(20));
        corrida.setDataHoraFim(LocalDateTime.now());
        new CorridaRepositoryJSON().save(corrida);

        Pagamento pagamento = new Pagamento(corrida.getId(), p.getId(), 25.50, MetodoPagamento.PIX, 0);
        pagamento.setStatus(com.uberpb.enums.StatusPagamento.SUCESSO);
        new PagamentoRepositoryJSON().salvar(pagamento);

        corridaIdTeste = corrida.getId();
    }

    @Test
    @DisplayName("Testar geração do recibo")
    void testarGerarRecibo() {
        var recibo = reciboService.gerarRecibo(corridaIdTeste);
        assertNotNull(recibo, "Recibo não deve ser nulo");
        assertEquals("João", recibo.getNomePassageiro());
        assertEquals("Carlos", recibo.getNomeMotorista());
        assertEquals("UberX", recibo.getCategoria());
        assertEquals(25.50, recibo.getPrecoFinal());
        assertEquals("PIX", recibo.getMetodoPagamento());
        assertEquals("SUCESSO", recibo.getStatusPagamento());
    }

    @Test
    @DisplayName("Testar exibição do recibo no console")
    void testarExibirRecibo() {
        boolean exibido = reciboService.exibirRecibo(corridaIdTeste);
        assertTrue(exibido, "Recibo deve ser exibido corretamente");
    }

    @Test
    @DisplayName("Testar salvamento do recibo em arquivo")
    void testarSalvarReciboEmArquivo() throws Exception {
        String nomeArquivo = "teste_recibo.txt";
        boolean salvo = reciboService.salvarReciboEmArquivo(corridaIdTeste, nomeArquivo);
        assertTrue(salvo, "Recibo deve ser salvo em arquivo");

        File arquivo = new File(nomeArquivo);
        assertTrue(arquivo.exists(), "Arquivo do recibo deve existir");
        String conteudo = Files.readString(arquivo.toPath());
        assertTrue(conteudo.contains("João"));
        assertTrue(conteudo.contains("Carlos"));
        assertTrue(conteudo.contains("PIX"));

        // Limpeza do arquivo de teste
        arquivo.delete();
    }
}
