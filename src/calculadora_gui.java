import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class calculadora_gui extends JFrame {

    private JTextField campoNum1, campoNum2;
    private JComboBox<String> operacoes;
    private JLabel resultadoLabel;

    public calculadora_gui() {
        setTitle("Calculadora");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Centraliza a janela

        // Criando os componentes
        campoNum1 = new JTextField(5);
        campoNum2 = new JTextField(5);
        operacoes = new JComboBox<>(new String[]{"+", "-", "*", "/"});
        JButton calcularBtn = new JButton("Calcular");
        resultadoLabel = new JLabel("Resultado: ");

        // Organizando layout
        setLayout(new GridLayout(5, 2));
        add(new JLabel("Número 1:"));
        add(campoNum1);
        add(new JLabel("Operação:"));
        add(operacoes);
        add(new JLabel("Número 2:"));
        add(campoNum2);
        add(new JLabel(""));
        add(calcularBtn);
        add(resultadoLabel);

        // Ação do botão
        calcularBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularResultado();
            }
        });
    }

    private void calcularResultado() {
        try {
            double num1 = Double.parseDouble(campoNum1.getText());
            double num2 = Double.parseDouble(campoNum2.getText());
            String op = (String) operacoes.getSelectedItem();

            double resultado = switch (op) {
                case "+" -> num1 + num2;
                case "-" -> num1 - num2;
                case "*" -> num1 * num2;
                case "/" -> {
                    if (num2 == 0) throw new ArithmeticException("Divisão por zero");
                    yield num1 / num2;
                }
                default -> throw new IllegalStateException("Operação inválida");
            };

            resultadoLabel.setText("Resultado: " + resultado);
        } catch (NumberFormatException ex) {
            resultadoLabel.setText("Entrada inválida!");
        } catch (ArithmeticException ex) {
            resultadoLabel.setText(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new calculadora_gui().setVisible(true);
        });
    }
}
