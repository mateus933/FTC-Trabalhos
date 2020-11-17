package TrabalhoFtc;

import java.util.ArrayList;

public class Gram {
    ArrayList<String> listaRegras = new ArrayList<>();
    Character letra;

    public boolean possuiLambdaEmRegra() {
        return !!listaRegras.contains("&");
    }

    Gram(String linha) {
        String[] itemsDaLinha = linha.split("->|/");
        letra = itemsDaLinha[0].charAt(0);
        for (int i = 1; i < itemsDaLinha.length; i++){
            listaRegras.add(itemsDaLinha[i]);
        }
    }
}
