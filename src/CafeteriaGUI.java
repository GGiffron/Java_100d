import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class CafeteriaGUI {

    static java.util.List<Produto> cardapio = new ArrayList<>();
    static java.util.List<Venda> vendas = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CafeteriaGUI::criarInterface);
    }

    static void criarInterface() {
        JFrame frame = new JFrame("DamascenoHouse: JazzBean Café");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new GridLayout(6, 1, 10, 10));

        JLabel titulo = new JLabel("Sistema de Gerenciamento", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titulo);

        JButton btnCadastrar = new JButton("Cadastrar Produto");
        JButton btnRegistrar = new JButton("Registrar Venda");
        JButton btnCardapio = new JButton("Ver Cardápio");
        JButton btnRelatorio = new JButton("Gerar Relatório");
        JButton btnSair = new JButton("Sair");

        frame.add(btnCadastrar);
        frame.add(btnRegistrar);
        frame.add(btnCardapio);
        frame.add(btnRelatorio);
        frame.add(btnSair);

        btnCadastrar.addActionListener(e -> mostrarCadastroProduto());
        btnRegistrar.addActionListener(e -> mostrarRegistroVenda());
        btnCardapio.addActionListener(e -> mostrarCardapio());
        btnRelatorio.addActionListener(e -> mostrarRelatorio());
        btnSair.addActionListener(e -> System.exit(0));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // -------------------------------------------
    // Classe Produto e Venda (internas)
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

        @Override
        public String toString() {
            return id + " - " + nome + " (R$ " + preco + ")";
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

        double getTotal() {
            return produto.preco * quantidade;
        }
    }

    // -------------------------------------------
    // Telas
    static void mostrarCadastroProduto() {
        JTextField idField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField precoField = new JTextField();
        JCheckBox alcoolicoBox = new JCheckBox("É bebida alcoólica");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Preço (R$):"));
        panel.add(precoField);
        panel.add(alcoolicoBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Cadastrar Produto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String nome = nomeField.getText();
                double preco = Double.parseDouble(precoField.getText());
                boolean ehAlcoolico = alcoolicoBox.isSelected();

                for (Produto p : cardapio) {
                    if (p.id == id) {
                        JOptionPane.showMessageDialog(null, "ID já existente!");
                        return;
                    }
                }

                cardapio.add(new Produto(id, nome, preco, ehAlcoolico));
                JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro nos dados. Verifique os campos.");
            }
        }
    }

    static void mostrarRegistroVenda() {
        if (cardapio.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado ainda.");
            return;
        }

        JComboBox<Produto> combo = new JComboBox<>(cardapio.toArray(new Produto[0]));
        JTextField idadeField = new JTextField();
        JTextField quantidadeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Produto:"));
        panel.add(combo);
        panel.add(new JLabel("Idade do cliente:"));
        panel.add(idadeField);
        panel.add(new JLabel("Quantidade:"));
        panel.add(quantidadeField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Registrar Venda",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Produto p = (Produto) combo.getSelectedItem();
                int idade = Integer.parseInt(idadeField.getText());
                int qtd = Integer.parseInt(quantidadeField.getText());

                if (p.ehAlcoolico && idade < 18) {
                    JOptionPane.showMessageDialog(null, "Venda não autorizada! Cliente menor de idade.");
                    return;
                }

                vendas.add(new Venda(p, qtd, idade));
                JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Dados inválidos.");
            }
        }
    }

    static void mostrarCardapio() {
        StringBuilder sb = new StringBuilder("CARDÁPIO:\n\n");
        for (Produto p : cardapio) {
            sb.append(String.format("ID: %d | %s | R$ %.2f | %s\n",
                    p.id, p.nome, p.preco,
                    p.ehAlcoolico ? "ALCOÓLICO" : "Café/Bebida"));
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Cardápio", JOptionPane.INFORMATION_MESSAGE);
    }

    static void mostrarRelatorio() {
        if (vendas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma venda registrada.");
            return;
        }

        double total = 0, totalAlcool = 0;
        int itensTotal = 0, itensAlcool = 0, idadeAlcoolSoma = 0;

        StringBuilder sb = new StringBuilder("RELATÓRIO DE VENDAS:\n\n");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        for (Venda v : vendas) {
            sb.append(String.format("[%s] %s x%d - R$ %.2f (%s - %d anos)\n",
                    sdf.format(v.horaVenda),
                    v.produto.nome, v.quantidade,
                    v.getTotal(),
                    v.produto.ehAlcoolico ? "Álcool" : "Café",
                    v.idadeCliente));

            total += v.getTotal();
            itensTotal += v.quantidade;

            if (v.produto.ehAlcoolico) {
                totalAlcool += v.getTotal();
                itensAlcool += v.quantidade;
                idadeAlcoolSoma += v.idadeCliente;
            }
        }

        sb.append("\nTotal vendido: R$ ").append(String.format("%.2f", total));
        sb.append("\nItens vendidos: ").append(itensTotal);
        sb.append("\nTicket médio: R$ ").append(String.format("%.2f", total / vendas.size()));

        if (itensAlcool > 0) {
            sb.append("\n\n--- Bebidas Alcoólicas ---");
            sb.append("\nTotal álcool: R$ ").append(String.format("%.2f", totalAlcool));
            sb.append("\nQtd álcool: ").append(itensAlcool);
            sb.append("\nIdade média: ").append(String.format("%.1f", (double) idadeAlcoolSoma / itensAlcool));
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Relatório", JOptionPane.INFORMATION_MESSAGE);
    }
}

