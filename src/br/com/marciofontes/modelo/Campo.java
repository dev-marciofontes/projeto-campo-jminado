package br.com.marciofontes.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.marciofontes.excecao.ExplosaoException;

public class Campo {

	private final int linha;
	private final int coluna;

	private boolean cAberto = false;
	private boolean cMinado = false;
	private boolean cMarcado = false;

	// Declara a lista de vizinhos cVizinhos, inicializada como uma lista vazia.
	private List<Campo> cVizinhos = new ArrayList<>();

	// Define o construtor para a classe Campo.
	Campo(int linha, int coluna) {
		// Define as variáveis linha e coluna a partir dos argumentos de entrada.
		this.linha = linha;
		this.coluna = coluna;
	}

	// Define o método adicionarVizinho para adicionar vizinhos a um campo.
	boolean adicionarVizinho(Campo vizinho) {
		// Verifica se o vizinho está na mesma linha ou coluna, ou em uma diagonal.
		boolean linhaDiferente = this.linha != vizinho.linha;
		boolean colunaDiferente = this.coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;

		// Calcula a distância em linhas e colunas entre o campo e o vizinho.
		int deltaLinha = Math.abs(this.linha - vizinho.linha);
		int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;

		// Verifica se o vizinho é um vizinho válido.
		if (deltaGeral == 1 && !diagonal) {
			cVizinhos.add(vizinho); // Adiciona o vizinho à lista de vizinhos.
			return true; // Retorna true indicando que o vizinho foi adicionado.
		} else if (deltaGeral == 2 && diagonal) {
			cVizinhos.add(vizinho); // Adiciona o vizinho à lista de vizinhos.
			return true; // Retorna true indicando que o vizinho foi adicionado.
		} else {
			return false; // Retorna false indicando que o vizinho não foi adicionado.
		}

	}

	// Define o método alternarMarcacao para alternar a marcação de um campo.
	void alternarMarcacao() {
		// Verifica se o campo não está aberto.
		if (!cAberto) {
			cMarcado = !cMarcado; // Inverte o valor da marcação.
		}
	}

	// Define o método abrir para abrir um campo.
	boolean abrir() {
		// Verifica se o campo não está aberto e não está marcado.
		if (!cAberto && !cMarcado) {
			cAberto = true; // Define o campo como aberto.

			// Verifica se o campo está minado.
			if (cMinado) {
				throw new ExplosaoException(); // Lança uma exceção de explosão.
			}

			// Verifica se a vizinhança do campo é segura.
			if (vizinhancaSegura()) {
				cVizinhos.forEach(v -> v.abrir()); // Abre todos os vizinhos seguros.
			}

			return true; // Retorna true indicando que o campo foi aberto.
		} else {
			return false; // Retorna false indicando que o campo não foi aberto.
		}
	}

	// Define o método vizinhancaSegura para verificar se a vizinhança de um campo é
	// segura.
	boolean vizinhancaSegura() {
		return cVizinhos.stream().noneMatch(v -> v.cMinado); // Retorna true se nenhum vizinho estiver minado.
	}

	// Define o método minar para marcar um campo como minado.
	void minar() {
		cMinado = true; // Define o campo como minado.
	}

	// Define o método isMarcado para verificar se um campo está marcado.
	public boolean isMarcado() {
		return cMarcado; // Retorna o valor da marcação do campo.
	}

	// Define o método isAberto para verificar se um campo está aberto.
	public boolean isAberto() {
		return cAberto; // Retorna o valor da abertura do campo.
	}

	void setcAberto(boolean cAberto) {
		this.cAberto = cAberto;
	}

	// Define o método isFechado para verificar se um campo está fechado.
	public boolean isFechado() {
		return !cAberto; // Retorna true se o campo não estiver aberto.
	}

	public boolean isMinado() {
		return cMinado;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	// Define o método objetivoAlcancado para verificar se o objetivo do jogo foi
	// alcançado.
	boolean objetivoAlcancado() {
		// Verifica se o campo foi desvendado (não minado e aberto) ou protegido (minado
		// e marcado).
		boolean desvendado = !cMinado && cAberto;
		boolean protegido = cMinado && cMarcado;

		return desvendado || protegido; // Retorna true se o campo foi desvendado ou protegido.
	}

	// Define o método minasNaVizinhanca para contar quantas minas há na vizinhança
	// de um campo.
	long minasNaVizinhanca() {
		return cVizinhos.stream().filter(v -> v.cMinado).count(); // Retorna a contagem de vizinhos minados.
	}

	// Define o método reiniciar para reiniciar os valores de um campo.
	void reiniciar() {
		this.cAberto = false; // Define o campo como não aberto.
		this.cMinado = false; // Define o campo como não minado.
		this.cMarcado = false; // Define o campo como não marcado.
	}

	public String toString() {
		if (cMarcado) {
			String corAzul = "\u001B[34m";
			String marca = "x";
			String colorir = corAzul + marca +  "\u001B[0m";
			return colorir;
		} else if (cAberto && cMinado) {
			String corVermelha = "\u001B[31m";
			String marca = "*";
			String colorir = corVermelha + marca +  "\u001B[0m";
			return colorir;
		} else if (cAberto && minasNaVizinhanca() > 0) {
			String qtdMinasNaVizinhanca = Long.toString(minasNaVizinhanca());
			String colorirAmarelo = "\u001B[32m";
			String qtdMinas = colorirAmarelo + qtdMinasNaVizinhanca +  "\u001B[0m";
			return qtdMinas;
		} else if (cAberto) {
			return " ";
		} else {
			String corMagenta = "\u001B[34m";
			String marcador = "?";
			String colorir = corMagenta + marcador +  "\u001B[0m";
			return colorir;
		}
	}

}
