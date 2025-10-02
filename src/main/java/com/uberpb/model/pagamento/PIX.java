package com.uberpb.model.pagamento;

import com.uberpb.enums.TipoChavePix;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.regex.Pattern;

/**
 * Classe simples para PIX com validadores de chave.
 */
public class PIX {
    private int id;
    private int usuarioId;
    private String chave;
    private TipoChavePix tipoChave;
    private String descricao; // Apelido da chave
    private boolean ativo;

    // Padrões de validação
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern TELEFONE_PATTERN = Pattern
            .compile("^\\+?55?\\d{10,11}$|^\\(\\d{2}\\)\\s?\\d{4,5}-?\\d{4}$");
    private static final Pattern CHAVE_ALEATORIA_PATTERN = Pattern
            .compile("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$");

    // Construtor vazio
    public PIX() {
        this.ativo = true;
    }

    // Construtor completo
    public PIX(int usuarioId, String chave, TipoChavePix tipoChave, String descricao) {
        this();
        this.usuarioId = usuarioId;
        this.chave = chave;
        this.tipoChave = tipoChave;
        this.descricao = descricao;
    }

    // Validação da chave PIX
    @JsonIgnore
    public boolean isChaveValida() {
        if (chave == null || chave.trim().isEmpty() || tipoChave == null) {
            return false;
        }

        String chaveNormalizada = chave.trim();

        switch (tipoChave) {
            case CPF:
                return validarCPF(chaveNormalizada);
            case CNPJ:
                return validarCNPJ(chaveNormalizada);
            case EMAIL:
                return EMAIL_PATTERN.matcher(chaveNormalizada).matches();
            case TELEFONE:
                return validarTelefone(chaveNormalizada);
            case CHAVE_ALEATORIA:
                return CHAVE_ALEATORIA_PATTERN.matcher(chaveNormalizada).matches();
            default:
                return false;
        }
    }

    // Validação completa do PIX
    @JsonIgnore
    public boolean isValido() {
        return isChaveValida() &&
                descricao != null && !descricao.trim().isEmpty() &&
                ativo;
    }

    // Validadores específicos
    private boolean validarCPF(String cpf) {
        return CPF_PATTERN.matcher(cpf).matches();
    }

    private boolean validarCNPJ(String cnpj) {
        return CNPJ_PATTERN.matcher(cnpj).matches();
    }

    private boolean validarTelefone(String telefone) {
        return TELEFONE_PATTERN.matcher(telefone).matches();
    }

    // Normalizar chave para exibição
    @JsonIgnore
    public String getChaveFormatada() {
        if (chave == null)
            return "Chave inválida";

        switch (tipoChave) {
            case CPF:
                return formatarCPF(chave);
            case CNPJ:
                return formatarCNPJ(chave);
            case TELEFONE:
                return formatarTelefone(chave);
            default:
                return chave;
        }
    }

    // Formatadores
    private String formatarCPF(String cpf) {
        String numerosApenas = cpf.replaceAll("\\D", "");
        if (numerosApenas.length() == 11) {
            return numerosApenas.substring(0, 3) + "." +
                    numerosApenas.substring(3, 6) + "." +
                    numerosApenas.substring(6, 9) + "-" +
                    numerosApenas.substring(9);
        }
        return cpf;
    }

    private String formatarCNPJ(String cnpj) {
        String numerosApenas = cnpj.replaceAll("\\D", "");
        if (numerosApenas.length() == 14) {
            return numerosApenas.substring(0, 2) + "." +
                    numerosApenas.substring(2, 5) + "." +
                    numerosApenas.substring(5, 8) + "/" +
                    numerosApenas.substring(8, 12) + "-" +
                    numerosApenas.substring(12);
        }
        return cnpj;
    }

    private String formatarTelefone(String telefone) {
        String numerosApenas = telefone.replaceAll("\\D", "");
        if (numerosApenas.length() == 11) {
            return "(" + numerosApenas.substring(0, 2) + ") " +
                    numerosApenas.substring(2, 7) + "-" +
                    numerosApenas.substring(7);
        } else if (numerosApenas.length() == 10) {
            return "(" + numerosApenas.substring(0, 2) + ") " +
                    numerosApenas.substring(2, 6) + "-" +
                    numerosApenas.substring(6);
        }
        return telefone;
    }

    // Máscara da chave para exibição segura
    @JsonIgnore
    public String getChaveMascarada() {
        if (chave == null)
            return "Chave inválida";

        switch (tipoChave) {
            case CPF:
                return "***.***.***-" + chave.replaceAll("\\D", "").substring(9);
            case CNPJ:
                return "**.***.***/****-" + chave.replaceAll("\\D", "").substring(12);
            case EMAIL:
                String[] partes = chave.split("@");
                if (partes.length == 2) {
                    String usuario = partes[0];
                    String dominio = partes[1];
                    return usuario.charAt(0) + "***@" + dominio;
                }
                return chave;
            case TELEFONE:
                String numerosApenas = chave.replaceAll("\\D", "");
                if (numerosApenas.length() >= 4) {
                    return "(**) ****-" + numerosApenas.substring(numerosApenas.length() - 4);
                }
                return chave;
            case CHAVE_ALEATORIA:
                return chave.substring(0, 8) + "-****-****-****-************";
            default:
                return chave;
        }
    }

    // toString para exibição
    @Override
    public String toString() {
        return String.format("PIX %s\n%s: %s\nDescrição: %s",
                tipoChave.getDescricao(),
                tipoChave.getDescricao(),
                getChaveMascarada(),
                descricao != null ? descricao : "Sem descrição");
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public TipoChavePix getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(TipoChavePix tipoChave) {
        this.tipoChave = tipoChave;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}