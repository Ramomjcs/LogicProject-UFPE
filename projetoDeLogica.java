package logic;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static int findOperator(String exp) {

        int numParenthesis = 0;
        for (int i = 0; i < exp.length(); i++) {

            if (exp.charAt(i) == '(')
                numParenthesis++;
            else if (exp.charAt(i) == ')')
                numParenthesis--;

            if ((exp.charAt(i) == '>' || exp.charAt(i) == '&' || exp.charAt(i) == 'v' || exp.charAt(i) == '~')
                    && numParenthesis == 1)
                return i;
        }

        return -1;
    }

    static boolean isLegit(String exp) {

        if (exp.length() == 1 && exp.charAt(0) >= 'A' && exp.charAt(0) <= 'Z')
            return true;

        int s = 0;
        int f = exp.length() - 1;

        if (exp.charAt(s) == '(' && exp.charAt(f) == ')' && exp.charAt(s + 1) == '~' && s + 2 < f)
            return isLegit(exp.substring(s + 2, f));

        else if (exp.charAt(s) == '(' && exp.charAt(f) == ')') {
            int operatorIndex = findOperator(exp);
            if (operatorIndex != -1)
                return isLegit(exp.substring(s + 1, operatorIndex - 1)) && isLegit(exp.substring(operatorIndex + 2, f));
        }

        return false;

    }

    static boolean satisfies(Map<Character, Boolean> valoresverdade, String exp) {

        if (exp.length() == 1)
            return valoresverdade.get(exp.charAt(0));

        int s = 0;
        int f = exp.length() - 1;
        int operatorIndex = findOperator(exp);
        switch (exp.charAt(operatorIndex)) {

        case '~':
            return !satisfies(valoresverdade, exp.substring(operatorIndex + 1, f));
        case '&':
            return satisfies(valoresverdade, exp.substring(s + 1, operatorIndex - 1))
                    && satisfies(valoresverdade, exp.substring(operatorIndex + 2, f));
        case 'v':
            return satisfies(valoresverdade, exp.substring(s + 1, operatorIndex - 1))
                    || satisfies(valoresverdade, exp.substring(operatorIndex + 2, f));
        case '>':
            return !satisfies(valoresverdade, exp.substring(s + 1, operatorIndex - 1))
                    || satisfies(valoresverdade, exp.substring(operatorIndex + 2, f));
        default:
            return true;
        }

    }

    static int tamanho(String n) {
        String aux = n;
        int tamanho = aux.length();
        return tamanho;
    }

    static int[] PegarNumeros(int tamanho, String numeros) {
        int var[] = new int[tamanho];
        for (int j = 0; j < tamanho; j++) {
            var[j] = Character.getNumericValue(numeros.charAt(j)); // pego os numeros
        }
        return var;
    }

    public static void main(String[] args) {

        try {
            Scanner input = new Scanner(System.in);
            input = new Scanner(new FileReader("Arquivo"));
            FileWriter out = new FileWriter("Saida");

            int n = input.nextInt();
            input.nextLine();

            for (int i = 0; i < n; i++) {

                String exp = input.nextLine();
                Map<Character, Boolean> valoresverdade = new HashMap<>();

                String letras = exp; // expressao sem numeros
                String numeros = ""; // string de numeros
                int tam = tamanho(letras);
                boolean s = false;

                for (int k = 0; k < tam; k++) {
                    if (s == false) {
                        if (letras.charAt(k) == '0' || letras.charAt(k) == '1') {
                            numeros = letras.substring(k); // tira as letras - continua String
                            letras = letras.substring(0, k - 1); // tira os numeros
                            s = true; // stop
                        }
                    }
                }
                numeros = numeros.replaceAll("\\s", ""); // junta tudo - sem espaco

                tam = tamanho(numeros);

                int arrayNumerosValores[] = PegarNumeros(tam, numeros); // vetor de numeros - transformado de String
                                                                        // para int

                boolean valores[] = new boolean[tam];

                for (int k = 0; k < tam; k++) { // coloco os valores verdades no array de acordo com os numeros
                    if (arrayNumerosValores[k] == 0)
                        valores[k] = false;
                    else if (arrayNumerosValores[k] == 1) {
                        valores[k] = true;
                    }
                }

                int k1 = 0;
                String novaLetras = letras; // expressao sem numeros
                tam = tamanho(letras);
                for (int k = 0; k < tam; k++) {

                    if (letras.charAt(k) >= 'A' && letras.charAt(k) <= 'Z') { // So pega as letras de A a Z para
                                                                                // valora-las

                        valoresverdade.put(letras.charAt(k), valores[k1]); // coloca o valor verdade em cada letra no
                                                                            // mapa
                                                                            // hash
                        k1++;
                        letras = letras.replace(letras.charAt(k), '-');

                    }
                }
                letras = novaLetras;

                // etapa 4
                if (letras.charAt(0) != '{') { // se não for um conjunto...
                    if (isLegit(letras) && satisfies(valoresverdade, letras)) {

                        out.write("Problema #" + (i + 1) + "\n"); // numero do problema
                        out.write("A valoracao-verdade satisfaz a proposicao.\n");
                        out.write("\n");
                    } else if (isLegit(letras) && !(satisfies(valoresverdade, letras))) {
                        out.write("Problema #" + (i + 1) + "\n");
                        out.write("A valoracao-verdade nao satisfaz a proposicao.\n");
                        out.write("\n");
                    }

                    else if (!(isLegit(letras))) { // aqui
                        out.write("Problema #" + (i + 1) + "\n");
                        out.write("A palavra nao e legitima.\n");
                        out.write("\n");
                    }
                }

                else { // se for

                    String[] vetorSentencas = letras.split(","); // armazena cada sentença em um index
                    vetorSentencas[0] = vetorSentencas[0].substring(1); // tira a { chave e ja armazena a sentenca
                    vetorSentencas[vetorSentencas.length - 1] = vetorSentencas[vetorSentencas.length - 1].substring(0,
                            vetorSentencas[vetorSentencas.length - 1].length() - 1); // tira a ultima chave } e ja
                                                                                        // armazena na sentenca
                    for (int k = 1; k < vetorSentencas.length; k++) {
                        vetorSentencas[k] = vetorSentencas[k].substring(1); // garanto que o resto fique no seu index
                                                                            // certo, já que o [inicial] e o [ultimo]
                                                                            // estao certos pelo de cima
                    }

                    boolean tudoVerdade = true;
                    boolean alltrueaux = false;
                    int auxiliar = 0;
                    
                    while (auxiliar < vetorSentencas.length && tudoVerdade == true) {
                        if ((tudoVerdade) == true && isLegit(vetorSentencas[auxiliar]) == true
                                && satisfies(valoresverdade, vetorSentencas[auxiliar]) == false) {

                            out.write("Problema #" + (i + 1) + "\n");
                            out.write("A valoracao-verdade nao satisfaz o conjunto.\n");
                            out.write("\n");
                            tudoVerdade = false;
                            auxiliar = -1;
                        }

                        else if ((tudoVerdade == true) && (isLegit(vetorSentencas[auxiliar])) == false) {
                            out.write("Problema #" + (i + 1) + "\n"); // se nao for legitimo
                            out.write("Ha uma palavra nao legitima no conjunto.\n");
                            out.write("\n");
                            tudoVerdade = false;
                            auxiliar = -1;
                        }

                        if (tudoVerdade == true) {
                            auxiliar++;
                        }

                        if (auxiliar >= vetorSentencas.length) {
                            tudoVerdade = false;
                            alltrueaux = true;
                        }
                    }
                    if (alltrueaux) {
                        out.write("Problema #" + (i + 1) + "\n");
                        out.write("A valoracao-verdade satisfaz o conjunto.\n");
                        out.write("\n");
                    }
                }

                input.close();
                out.close();
            }

        } catch (IOException e) {
        }

    }

}