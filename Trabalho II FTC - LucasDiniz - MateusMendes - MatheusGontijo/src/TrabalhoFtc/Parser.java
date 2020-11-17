package TrabalhoFtc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Parser {
	public boolean encontrouLambda = false;
	private String matrizSentenca[][];

	Parser(ArrayList<Gram> gramatica, String sentenca) throws IOException {
		FileWriter arq = new FileWriter("arquivo-saida.txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		VerificacaoDaGramatica(gramatica);
		boolean aceitaSentenca = validaSentenca(sentenca, gramatica);
	    gravarArq.print(aceitaSentenca);
		arq.close();
		
		System.out.println("Entrada: " + sentenca);
		System.out.println("Resultado: " + (aceitaSentenca ? "Verdadeiro" : "Falso"));
	}

	public void VerificacaoDaGramatica(ArrayList<Gram> gramatica) {
		for (int i = 0; i < gramatica.size(); i++){
			for (int j = 0; j < gramatica.get(i).listaRegras.size(); j++){
				while (gramatica.get(i).listaRegras.get(j).length() > 2) {
					boolean flag = false;
					for (int k = 0; k < gramatica.size(); k++) {
						if (gramatica.get(k).listaRegras.size() == 1 && (gramatica.get(k).listaRegras.get(0).length() == 2)) {
							if (gramatica.get(k).listaRegras.get(0).charAt(0) == gramatica.get(i).listaRegras.get(j).charAt(gramatica.get(i).listaRegras.get(j).length() - 2) & gramatica.get(k).listaRegras.get(0).charAt(1) == gramatica.get(i).listaRegras.get(j).charAt(gramatica.get(i).listaRegras.get(j).length() - 1)) {
								flag = true;
								String setNewVar = gramatica.get(i).listaRegras.get(j).substring(0, gramatica.get(i).listaRegras.get(j).length() - 2);
								Character valorVariavel = gramatica.get(k).letra;
								gramatica.get(i).listaRegras.set(j, setNewVar + valorVariavel);
								break;
							}
						}
					}
					
					if (!flag) {
						gramatica.add(new Gram(geraNovaRegra(gramatica).toString()));
						String aumatrizSentenca = new StringBuilder().append(gramatica.get(i).listaRegras.get(j).charAt(gramatica.get(i).listaRegras.get(j).length() - 2)).append(gramatica.get(i).listaRegras.get(j).charAt(gramatica.get(i).listaRegras.get(j).length() - 1)).toString();
						gramatica.get(gramatica.size() - 1).listaRegras.add(aumatrizSentenca);
						gramatica.get(i).listaRegras.set(j, (gramatica.get(i).listaRegras.get(j).substring(0, gramatica.get(i).listaRegras.get(j).length() - 2)) + gramatica.get(gramatica.size() - 1).letra);
					}
				}
			}
		}
		
		removeVazio(gramatica);
		removeRegra(gramatica);
	}

	private void removeRegra(ArrayList<Gram> gramatica) {
		for (int i = 0; i < gramatica.size(); i++){
			for (int j = 0; j < gramatica.get(i).listaRegras.size(); j++) {
				if (gramatica.get(i).listaRegras.get(j).equals("")) {
					gramatica.get(i).listaRegras.remove(j);
					j--;
				}
				
				if (gramatica.get(i).listaRegras.get(j).equals(gramatica.get(i).letra)) {
					gramatica.get(i).listaRegras.remove(j);
					j--;
				}else{
					for (int k = j + 1; k < gramatica.get(i).listaRegras.size(); k++){
						if (gramatica.get(i).listaRegras.get(j).equals(gramatica.get(i).listaRegras.get(k))) {
							gramatica.get(i).listaRegras.remove(k);
							k--;
						}
					}
				}
			}
		}
	}

	public boolean validaSentenca(String sentenca, ArrayList<Gram> gramatica) {
		boolean sentAceita = false;
		int tamanho = sentenca.length();

		if (tamanho == 0 && gramatica.get(0).possuiLambdaEmRegra()){
			return true;
		}

		matrizSentenca = new String[tamanho + 1][tamanho];
		
		for (int i = 0; i < tamanho + 1; i++){
			for (int j = 0; j < tamanho; j++){
				matrizSentenca[i][j] = "";
			}
		}
		
		for (int i = 0; i < tamanho; i++){
			matrizSentenca[0][i] = sentenca.substring(i, i + 1);
		}

		for (int i = 0; i < tamanho; i++) {
			matrizSentenca[i + 1][i] = retornaTermo(matrizSentenca[0][i], gramatica);
			if (matrizSentenca[i + 1][i] == "/"){
				return sentAceita;
			}
		}
		
		for (int j = 1; j < tamanho; j++){
			for (int i = j; i > 0; i--){
				for (int k = i; k < j + 1; k++) {
					matrizSentenca[i][j] += retornaVariavel(defineRegras(matrizSentenca[i][k - 1], matrizSentenca[k + 1][j]), gramatica);

					if (matrizSentenca[i][j].contentEquals("/")){
						matrizSentenca[i][j] = "";
					}
				}
			}
		}

		if (matrizSentenca[1][tamanho - 1].contains(gramatica.get(0).letra.toString())){
			return true;
		}
		
		return sentAceita;
	}
	
	private void removeVazio(ArrayList<Gram> gramatica) {
		String[][] vetorAnulaveis = new String[2][gramatica.size()];

		for (int i = 0; i < gramatica.size(); i++) {
			vetorAnulaveis[0][i] = gramatica.get(i).letra.toString();
			vetorAnulaveis[1][i] = "n";
		}

		boolean continuarPercorrendo = false;
		while (continuarPercorrendo == false) {
			continuarPercorrendo = true;
			for (int i = 0; i < gramatica.size(); i++) {
				if (vetorAnulaveis[1][i] == "n") {
					for (int j = 0; j < gramatica.get(i).listaRegras.size(); j++) {
						int tamanhoRegra = gramatica.get(i).listaRegras.get(j).length();
						if (tamanhoRegra == 1)
							if (gramatica.get(i).listaRegras.get(j).equals("&")) {
								vetorAnulaveis[1][i] = "y";
								continuarPercorrendo = false;
								break;
							}

						boolean flagParada = false;

						for (int k = 0; k < gramatica.get(i).listaRegras.get(j).length(); k++)
							if (Character.isUpperCase(gramatica.get(i).listaRegras.get(j).charAt(k))) {
								boolean segundaFlag = true;
								for (int l = 0; l < vetorAnulaveis[0].length; l++) {
									Character letraRegra = gramatica.get(i).listaRegras.get(j).charAt(k);
									if (vetorAnulaveis[0][l].charAt(0) == letraRegra) {
										if (vetorAnulaveis[1][l] == "n") {
											segundaFlag = false;
											break;
										}
									}
								}

								if (segundaFlag == true) {
									flagParada = true;
									break;
								}
							} else
								flagParada = false;

						if (flagParada == true) {
							vetorAnulaveis[1][i] = "y";
							continuarPercorrendo = false;
							break;
						}
					}
				}
			}
		}

		if (vetorAnulaveis[1][0] == "y") {
			this.encontrouLambda = true;
		}

		for (int i = 0; i < gramatica.size(); i++)
			for (int j = 0; j < gramatica.get(i).listaRegras.size(); j++)
				if (gramatica.get(i).listaRegras.get(j).length() > 0)
					for (int k = 0; k < vetorAnulaveis[0].length; k++) {

						if (vetorAnulaveis[0][k].charAt(0) == gramatica.get(i).listaRegras.get(j).charAt(0)
								& vetorAnulaveis[1][k] == "y")
							if (gramatica.get(i).listaRegras.get(j).length() > 1)
								gramatica.get(i).listaRegras.add(gramatica.get(i).listaRegras.get(j).charAt(1) + "");

						if (gramatica.get(i).listaRegras.get(j).length() > 1)
							if (vetorAnulaveis[0][k].charAt(0) == gramatica.get(i).listaRegras.get(j).charAt(1)
									& vetorAnulaveis[1][k] == "y")
								gramatica.get(i).listaRegras.add(gramatica.get(i).listaRegras.get(j).charAt(0) + "");
					}

		for (int i = 0; i < gramatica.size(); i++){
			for (int j = 0; j < gramatica.get(i).listaRegras.size(); j++){
				if (gramatica.get(i).listaRegras.get(j).equals("&")) {
					gramatica.get(i).listaRegras.remove(j);
					j--;
				}
			}
		}
	}

	public String retornaVariavel(String variavel, ArrayList<Gram> g) {
		String incremento = "/";
		for (int i = 0; i < g.size(); i++) {
			String arrayVariaveis[] = variavel.split("/");

			for (int j = 0; j < variavel.length() - 1; j += 3){
				for (int k = 0; k < arrayVariaveis.length; k++){
					if (g.get(i).listaRegras.contains(arrayVariaveis[k])){
						incremento += g.get(i).letra.toString() + '/';
					}
				}
			}
		}

		return incremento;
	}

	public String retornaTermo(String terminal,ArrayList<Gram> g) {
		String incremento = "/";
		
		for(int i = 0; i < g.size(); i++)
			for (int j = 0; j < g.get(i).listaRegras.size(); j++)
				if (g.get(i).listaRegras.get(j).contains(terminal))
					incremento += g.get(i).letra.toString() + '/';
		
		return incremento;
	}
	
	public String defineRegras(String a, String b) {
		String incremento = "/";

		for (int i = 1; i < a.length(); i += 2) {
			for (int j = 1; j < b.length(); j += 2) {
				incremento += a.substring(i, i + 1) + b.substring(j, j + 1) + '/';
			}
		}

		return incremento;
	}
	
	public Character geraNovaRegra(ArrayList<Gram> gramatica) {
		int contador = 0;
		int quantidadeOpcoes = 26;

		Character[] opcoesLetra = new Character[quantidadeOpcoes];
		for (int i = 0; i < quantidadeOpcoes; i++) {
			opcoesLetra[i] = (Character) (char) (65 + i);
		}

		for (int i = 0; i < gramatica.size(); i++) {
			for (int j = 0; j < opcoesLetra.length; j++) {
				if (opcoesLetra[j].equals(gramatica.get(i).letra)) {
					contador++;
				}
			}
		}
		return opcoesLetra[contador];
	}
}