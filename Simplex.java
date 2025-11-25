import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Simplex {

    public static void main(String[] args) throws IOException {

        String caminhoArquivo = "entrada.txt";
        float[][] matriz = lerArquivoEConstruirMatriz(caminhoArquivo);

        imprimirMatriz(matriz);

        gaussJordan(matriz);
    }

    public static float[][] lerArquivoEConstruirMatriz(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));

        float tipo = Float.parseFloat(br.readLine().trim());
        int x = Integer.parseInt(br.readLine().trim());     
        int y = Integer.parseInt(br.readLine().trim());     

        String[] linhaFO = br.readLine().trim().split("\\s+");
        float[] funcObjetivo = new float[x];

        for (int i = 0; i < x; i++) {
            funcObjetivo[i] = Float.parseFloat(linhaFO[i]) * tipo;
        }

        int colunas = x + y + 1;
        int linhas = y + 1;
        float[][] matriz = new float[linhas][colunas];

        for (int i = 0; i < x; i++) {
            matriz[0][i] = -funcObjetivo[i];
        }

        for (int i = 1; i <= y; i++) {
            String[] restr = br.readLine().trim().split("\\s+");

            for (int j = 0; j < x; j++) {
                matriz[i][j] = Float.parseFloat(restr[j]);
            }

            matriz[i][x + (i - 1)] = 1f;
            matriz[i][colunas - 1] = Float.parseFloat(restr[x]);
        }

        br.close();
        return matriz;
    }

    public static void gaussJordan(float matriz[][]) {
        while (true) {
            imprimirMatriz(matriz);

            int colunaPivo = obterColunaPivo(matriz);
            if (matriz[0][colunaPivo] >= 0) break;

            int linhaPivo = obterLinhaPivo(matriz, colunaPivo);
            if (linhaPivo == -1) {
                System.out.println("Solução não encontrada.");
                return;
            }

            float pivo = matriz[linhaPivo][colunaPivo];

            for (int j = 0; j < matriz[linhaPivo].length; j++) {
                matriz[linhaPivo][j] /= pivo;
            }

            for (int i = 0; i < matriz.length; i++) {
                if (i != linhaPivo) {
                    float fator = matriz[i][colunaPivo];
                    for (int j = 0; j < matriz[i].length; j++) {
                        matriz[i][j] -= fator * matriz[linhaPivo][j];
                    }
                }
            }
        }

        System.out.println("Solução ótima encontrada:");
        imprimirMatriz(matriz);

        imprimirSolucao(matriz);
    }

    public static void imprimirSolucao(float[][] matriz) {
        int linhas = matriz.length;
        int colunas = matriz[0].length;

        int numVariaveis = colunas - linhas;
        int numFolgas = linhas - 1;

        System.out.println("Valores das variáveis:");

        for (int j = 0; j < numVariaveis; j++) {
            int linhaBase = -1;

            for (int i = 1; i < linhas; i++) {
                if (matriz[i][j] == 1f) {
                    boolean ehBase = true;
                    for (int k = 1; k < linhas; k++) {
                        if (k != i && matriz[k][j] != 0f) {
                            ehBase = false;
                            break;
                        }
                    }
                    if (ehBase) linhaBase = i;
                }
            }

            if (linhaBase == -1) {
                System.out.println("x" + (j + 1) + " = 0");
            } else {
                System.out.println("x" + (j + 1) + " = " + matriz[linhaBase][colunas - 1]);
            }
        }

        for (int j = numVariaveis; j < numVariaveis + numFolgas; j++) {
            int linhaBase = -1;

            for (int i = 1; i < linhas; i++) {
                if (matriz[i][j] == 1f) {
                    boolean ehBase = true;
                    for (int k = 1; k < linhas; k++) {
                        if (k != i && matriz[k][j] != 0f) {
                            ehBase = false;
                            break;
                        }
                    }
                    if (ehBase) linhaBase = i;
                }
            }

            int slackID = j - numVariaveis + 1;

            if (linhaBase == -1) {
                System.out.println("s" + slackID + " = 0");
            } else {
                System.out.println("s" + slackID + " = " + matriz[linhaBase][colunas - 1]);
            }
        }

        System.out.println("\nValor ótimo = " + matriz[0][colunas - 1]);
    }

    public static void imprimirMatriz(float matriz[][]) {
        for (float[] linha : matriz) {
            for (float v : linha) {
                System.out.print(v + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int obterColunaPivo(float matriz[][]) {
        int colunaPivo = 0;
        float valorMinimo = matriz[0][0];

        for (int j = 1; j < matriz[0].length - 1; j++) {
            if (matriz[0][j] < valorMinimo) {
                valorMinimo = matriz[0][j];
                colunaPivo = j;
            }
        }
        return colunaPivo;
    }

    public static int obterLinhaPivo(float matriz[][], int colunaPivo) {
        int linhaPivo = -1;
        float razaoMinima = Float.MAX_VALUE;

        for (int i = 1; i < matriz.length; i++) {
            if (matriz[i][colunaPivo] > 0) {
                float razao = matriz[i][matriz[i].length - 1] / matriz[i][colunaPivo];
                if (razao < razaoMinima) {
                    razaoMinima = razao;
                    linhaPivo = i;
                }
            }
        }
        return linhaPivo;
    }
}
