package TrabalhoFtc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import TrabalhoFtc.Parser;

public class Principal {
    public static void main(String[] args) {
        try {
            String nomeArquivoGram = "gram.txt";
            String nomeArquivoSentenca = "arquivo-entrada.txt";

            FileReader arquivoGramatica = new FileReader(nomeArquivoGram);
            FileReader arquivoSentenca = new FileReader(nomeArquivoSentenca);
            BufferedReader leituraGramatica = new BufferedReader(arquivoGramatica);
            BufferedReader leituraSentenca = new BufferedReader(arquivoSentenca);
            String linhaGramatica = leituraGramatica.readLine();
            String linhaS = leituraSentenca.readLine();
            ArrayList<Gram> listaGramatica = new ArrayList<>();

            String flagInicioArquivo = "Gram";

            if (linhaGramatica.startsWith(flagInicioArquivo))
                while (linhaGramatica != null) {
                    linhaGramatica = leituraGramatica.readLine();
                    if (linhaGramatica.startsWith("/")){
                        break;
                    }
                    listaGramatica.add(new Gram(linhaGramatica));
                }

            Parser resultadoParser = new Parser(listaGramatica, linhaS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}