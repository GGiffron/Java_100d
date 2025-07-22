import java.util.Scanner;

public class calculadora_basica {

    // Funcoes para cada operacao
    public static double somar(double a, double b) {
        return a + b;
    }

    public static double subtrair(double a, double b) {
        return a - b;
    }

    public static double multiplicar(double a, double b) {
        return a * b;
    }

    public static double dividir(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Erro: divisão por zero.");
        }
        return a / b;
    }

    // Funcao principal
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o primeiro número: ");
        double num1 = sc.nextDouble();

        System.out.print("Digite a operação (+, -, *, /): ");
        char operacao = sc.next().charAt(0);

        System.out.print("Digite o segundo número: ");
        double num2 = sc.nextDouble();

        double resultado;
        try {
            switch (operacao) {
                case '+':
                    resultado = somar(num1, num2);
                    break;
                case '-':
                    resultado = subtrair(num1, num2);
                    break;
                case '*':
                    resultado = multiplicar(num1, num2);
                    break;
                case '/':
                    resultado = dividir(num1, num2);
                    break;
                default:
                    System.out.println("Operação inválida.");
                    return;
            }
            System.out.println("Resultado: " + resultado);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
        sc.close();
    }
}