import java.util.*;

public class Cafeteria {

    static final int IDADE_MINIMA_ALCOOL = 18;

    static class Produto {
        int id;
        String nome;
        double preco;
        boolean ehAlcoolico;

        Produto(int id, String nome, double preco, boolean ehAlcoolico) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
            this.ehAlcoolico = ehAlcoolico;
        }
    }

    static class Venda {
        Produto produto;
        int quantidade;
        int idadeCliente;
        Date horaVenda;

        Venda(Produto produto, int quantidade, int idadeCliente) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.idadeCliente = idadeCliente;
            this.horaVenda = new Date();
        }

        double getSubtotal() {
            return produto.preco * quantidade;
        }
    }

    static List<Produto> cardapio = new ArrayList<>();
    static List<Venda> vendas = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        saudacao();

        int opcao;
        do {
            menu();
            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> registrarVenda();
                case 3 -> exibirCardapio();
                case 4 -> gerarRelatorio();
                case 0 -> System.out.println("Encerrando o sistema...");
                default -> System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 0);
    }

    static void saudacao() {
        System.out.println("""
                --------------------------------------------
                DamascenoHouse: JazzBean Café - Sistema de Gerenciamento
                --------------------------------------------
                Bem-vindo ao Sistema de Gerenciamento!
                """);
    }

    static void menu() {
        System.out.println("""
                \nMenu Principal:
                1. Cadastrar Produto
                2. Registrar Venda
                3. Exibir Cardápio
                4. Gerar Relatório Diário
                0. Sair
                """);
    }

    static void cadastrarProduto() {
        System.out.println("\n--- Cadastro de Produto ---");

        int id = lerInt("ID: ");
        if (buscarProdutoPorId(id) != null) {
            System.out.println("Erro: ID já em uso!");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        double preco;
        do {
            preco = lerDouble("Preço (R$): ");
            if (preco <= 0) System.out.println("Preço deve ser maior que zero!");
        } while (preco <= 0);

        boolean ehAlcoolico = lerSimNao("É bebida alcoólica? (S/N): ");

        Produto novo = new Produto(id, nome, preco, ehAlcoolico);
        cardapio.add(novo);

        System.out.printf("Produto '%s' cadastrado com sucesso!%n", nome);
    }

    static void registrarVenda() {
        if (cardapio.isEmpty()) {
            System.out.println("Nenhum produto cadastrado. Cadastre primeiro.");
            return;
        }

        System.out.println("\n--- Registrar Venda ---");

        int idade = lerInt("Idade do cliente: ");
        if (idade <= 0) {
            System.out.println("Idade inválida.");
            return;
        }

        int id = lerInt("ID do produto (ou 0 para cancelar): ");
        if (id == 0) return;

        Produto produto = buscarProdutoPorId(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        if (produto.ehAlcoolico && idade < IDADE_MINIMA_ALCOOL) {
            System.out.printf("Venda NÃO AUTORIZADA: cliente tem %d anos.%n", idade);
            return;
        }

        int quantidade;
        do {
            quantidade = lerInt("Quantidade: ");
            if (quantidade <= 0) System.out.println("Digite uma quantidade válida.");
        } while (quantidade <= 0);

        vendas.add(new Venda(produto, quantidade, idade));
        double total = produto.preco * quantidade;
        System.out.printf("Venda registrada. Total: R$ %.2f%n", total);
    }

    static void exibirCardapio() {
        System.out.println("\n--- Cardápio ---");
        System.out.printf("%-4s %-20s %-8s %-20s%n", "ID", "Produto", "Preço", "Tipo");

        for (Produto p : cardapio) {
            String tipo = p.ehAlcoolico ? "Bebida Alcoólica (18+)" : "Café/Bebida";
            System.out.printf("%-4d %-20s R$ %-6.2f %-20s%n", p.id, p.nome, p.preco, tipo);
        }
    }

    static void gerarRelatorio() {
        System.out.println("\n--- Relatório Diário ---");

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }

        double total = 0, totalAlcool = 0;
        int itensVendidos = 0, itensAlcool = 0, idadeAlcoolSoma = 0;

        System.out.printf("%-4s %-20s %-4s %-8s %-18s %-6s%n",
                "ID", "Produto", "Qtd", "Total", "Tipo", "Idade");

        for (Venda v : vendas) {
            double subtotal = v.getSubtotal();
            String tipo = v.produto.ehAlcoolico ? "Bebida Alcoólica" : "Café/Bebida";
            System.out.printf("%-4d %-20s %-4d R$ %-6.2f %-18s %-6d%n",
                    v.produto.id, v.produto.nome, v.quantidade, subtotal, tipo, v.idadeCliente);

            total += subtotal;
            itensVendidos += v.quantidade;

            if (v.produto.ehAlcoolico) {
                totalAlcool += subtotal;
                itensAlcool += v.quantidade;
                idadeAlcoolSoma += v.idadeCliente;
            }
        }

        System.out.println("\n--- Resumo ---");
        System.out.printf("Total vendido: R$ %.2f%n", total);
        System.out.printf("Ticket médio: R$ %.2f%n", total / vendas.size());
        System.out.printf("Itens vendidos: %d (média: %.1f por venda)%n",
                itensVendidos, (double) itensVendidos / vendas.size());

        if (itensAlcool > 0) {
            System.out.println("\n--- Bebidas Alcoólicas ---");
            System.out.printf("Total em bebidas alcoólicas: R$ %.2f%n", totalAlcool);
            System.out.printf("Itens vendidos (álcool): %d%n", itensAlcool);
            System.out.printf("Porcentagem do total: %.1f%%%n", (totalAlcool / total) * 100);
            System.out.printf("Idade média dos clientes: %.1f anos%n", (double) idadeAlcoolSoma / itensAlcool);
        }
    }

    // Utilitários

    static Produto buscarProdutoPorId(int id) {
        return cardapio.stream().filter(p -> p.id == id).findFirst().orElse(null);
    }

    static int lerInt(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número inteiro: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpa quebra de linha
        return valor;
    }

    static double lerDouble(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextDouble()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine(); // limpa quebra de linha
        return valor;
    }

    static boolean lerSimNao(String mensagem) {
        String resposta;
        do {
            System.out.print(mensagem);
            resposta = scanner.nextLine().trim().toUpperCase();
        } while (!resposta.equals("S") && !resposta.equals("N"));
        return resposta.equals("S");
    }
}

