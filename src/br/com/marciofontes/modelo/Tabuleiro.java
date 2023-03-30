package br.com.marciofontes.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import br.com.marciofontes.excecao.ExplosaoException;

public class Tabuleiro {

	private int qtdLinhas;
	private int qtdColunas;
	private int qtdMinas;

	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int qtdLinhas, int qtdColunas) {
		this.qtdLinhas = qtdLinhas;
		this.qtdColunas = qtdColunas;

		int totalCampos = qtdLinhas * qtdColunas;

		int min = qtdLinhas;
		int max = totalCampos;

		Random random = new Random();
		int numeroAleatorio = random.nextInt(max - min + 1) + min;

		this.qtdMinas = numeroAleatorio;

		// local ideal para inicializar o objeto
		gerarCampos();
		associarOsVizinhos();
		sortearCamposComMinas();

	}

	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
					.ifPresent(c -> c.abrir());
			;

		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setcAberto(true));
			throw e;
		}
	}

	public void alterarMarcacao(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alternarMarcacao());
		;
	}

	private void gerarCampos() {
		for (int linha = 0; linha < qtdLinhas; linha++) {
			for (int coluna = 0; coluna < qtdColunas; coluna++) {
				campos.add(new Campo(linha, coluna));
			}
		}

	}

	private void associarOsVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearCamposComMinas() {
		long minasDistribuidas = 0;
		Predicate<Campo> minado = c -> c.isMinado();

		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasDistribuidas = campos.stream().filter(minado).count();
		} while (minasDistribuidas < qtdMinas);
	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciaJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearCamposComMinas();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("  ");
		for (int coluna = 0; coluna < qtdColunas; coluna++) {
			sb.append(" ");
			sb.append(coluna);
			sb.append(" ");
		}

		sb.append("\n");

		int i = 0;
		for (int linha = 0; linha < qtdLinhas; linha++) {
			sb.append(linha);
			sb.append(" ");
			for (int coluna = 0; coluna < qtdColunas; coluna++) {
				sb.append(" ");
				sb.append(campos.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public int getQtdLinhas() {
		return this.qtdLinhas;
	}

	public int getQtdColunas() {
		return this.qtdColunas;
	}

}
