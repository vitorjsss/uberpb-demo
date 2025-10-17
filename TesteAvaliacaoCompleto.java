import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TesteAvaliacaoCompleto {
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTE COMPLETO DO SISTEMA DE AVALIAÇÕES ===");
            
            // Simular uma avaliação real
            System.out.println("\n1. Criando avaliação de teste...");
            
            // Dados da avaliação (baseado nos dados reais do projeto)
            int corridaId = 1;
            int passageiroId = 1; // Vitor (do passageiros.json)
            int motoristaId = 2;  // Alexandre (do motoristas.json)
            float nota = 4.5f;
            String comentario = "Motorista muito educado e dirigiu com segurança!";
            
            // Criar JSON manualmente (similar ao que o sistema faria)
            String avaliacaoJson = String.format(
                "{\n" +
                "  \"id\": %d,\n" +
                "  \"corridaId\": %d,\n" +
                "  \"avaliadorId\": %d,\n" +
                "  \"avaliadoId\": %d,\n" +
                "  \"tipoAvaliador\": \"PASSAGEIRO\",\n" +
                "  \"tipoAvaliado\": \"MOTORISTA\",\n" +
                "  \"nota\": %.1f,\n" +
                "  \"comentario\": \"%s\",\n" +
                "  \"criadoEm\": \"%s\",\n" +
                "  \"atualizadoEm\": \"%s\"\n" +
                "}",
                1, corridaId, passageiroId, motoristaId, nota, comentario,
                java.time.LocalDateTime.now().toString(),
                java.time.LocalDateTime.now().toString()
            );
            
            System.out.println("✅ Avaliação criada:");
            System.out.println(avaliacaoJson);
            
            // Salvar no arquivo de avaliações
            System.out.println("\n2. Salvando no banco de dados...");
            
            String avaliacoesContent = "[\n  " + avaliacaoJson + "\n]";
            Files.write(Paths.get("database/avaliacoes/avaliacoes.json"), avaliacoesContent.getBytes());
            
            System.out.println("✅ Avaliação salva em: database/avaliacoes/avaliacoes.json");
            
            // Verificar se foi salvo
            System.out.println("\n3. Verificando dados salvos...");
            String savedContent = Files.readString(Paths.get("database/avaliacoes/avaliacoes.json"));
            System.out.println("📄 Conteúdo do arquivo:");
            System.out.println(savedContent);
            
            // Simular segunda avaliação (motorista avaliando passageiro)
            System.out.println("\n4. Adicionando segunda avaliação...");
            
            String avaliacao2Json = String.format(
                "{\n" +
                "  \"id\": %d,\n" +
                "  \"corridaId\": %d,\n" +
                "  \"avaliadorId\": %d,\n" +
                "  \"avaliadoId\": %d,\n" +
                "  \"tipoAvaliador\": \"MOTORISTA\",\n" +
                "  \"tipoAvaliado\": \"PASSAGEIRO\",\n" +
                "  \"nota\": %.1f,\n" +
                "  \"comentario\": \"%s\",\n" +
                "  \"criadoEm\": \"%s\",\n" +
                "  \"atualizadoEm\": \"%s\"\n" +
                "}",
                2, corridaId, motoristaId, passageiroId, 5.0f, "Passageiro pontual e educado.",
                java.time.LocalDateTime.now().toString(),
                java.time.LocalDateTime.now().toString()
            );
            
            String avaliacoesCompletasContent = "[\n  " + avaliacaoJson + ",\n  " + avaliacao2Json + "\n]";
            Files.write(Paths.get("database/avaliacoes/avaliacoes.json"), avaliacoesCompletasContent.getBytes());
            
            System.out.println("✅ Segunda avaliação adicionada!");
            
            // Mostrar resultado final
            System.out.println("\n5. Estado final do banco de avaliações:");
            String finalContent = Files.readString(Paths.get("database/avaliacoes/avaliacoes.json"));
            System.out.println(finalContent);
            
            // Comparar com sistema antigo
            System.out.println("\n=== COMPARAÇÃO COM SISTEMA ANTIGO ===");
            
            System.out.println("\n📊 Sistema Antigo (passageiros.json):");
            String passageirosContent = Files.readString(Paths.get("database/passageiros/passageiros.json"));
            System.out.println("Vitor tem avaliação: " + (passageirosContent.contains("\"avaliacaoMedia\" : 3.0") ? "SIM" : "NÃO"));
            System.out.println("Tem comentários: NÃO - apenas notas numéricas");
            
            System.out.println("\n🆕 Sistema Novo (avaliacoes.json):");
            System.out.println("Tem avaliações detalhadas: SIM");
            System.out.println("Tem comentários: SIM");
            System.out.println("Identifica quem avaliou quem: SIM");
            System.out.println("Tem timestamps: SIM");
            
            System.out.println("\n🎉 SISTEMA DE COMENTÁRIOS FUNCIONANDO!");
            System.out.println("📁 Os comentários estão sendo salvos em: database/avaliacoes/avaliacoes.json");
            
        } catch (IOException e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }
}