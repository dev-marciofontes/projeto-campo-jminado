package br.com.marciofontes.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.marciofontes.excecao.ExplosaoException;
import br.com.marciofontes.excecao.SairException;
import br.com.marciofontes.modelo.Tabuleiro;

public class TabuleiroTerminal {

	private Tabuleiro tabuleiro;
	private Scanner entrada = new Scanner(System.in);

	public TabuleiroTerminal(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;

		executarJogo();

	}

	private void executarJogo() {
		try {
			boolean continua = true;

			while (continua) {

				cicloDoJogo();

				System.out.println("Outra partida? (S/n) ");
				String resposta = entrada.nextLine();

				if ("n".equalsIgnoreCase(resposta)) {
					continua = false;
				} else {
					tabuleiro.reiniciaJogo();
				}
			}

		} catch (SairException e) {
			System.out.println("Tchau!! Até logo!");
		} finally {
			entrada.close();
		}

	}

	private void cicloDoJogo() {
		try {
			while (!tabuleiro.objetivoAlcancado()) {
				System.out.println(tabuleiro);

				String digitado = capturaValorDigitado("Digite (Linha, Coluna): ");

				String[] digitadoSplit = digitado.trim().split(",");
				if (digitadoSplit.length != 2) {
					System.out.println("Digite dois números separados por vírgula!");
					continue;
				}

				int linha, coluna;

				try {
					linha = Integer.parseInt(digitadoSplit[0].trim());
					coluna = Integer.parseInt(digitadoSplit[1].trim());

					if (linha > tabuleiro.getQtdLinhas() && coluna > tabuleiro.getQtdColunas()) {
						System.out.println("\nALERTA::::Você digitou algum número maior "
								+ "do que o número de campos no tabuleiro (Máximo de Linhas: "
								+ tabuleiro.getQtdLinhas() + " ," + "Máximo de Colunas: " + tabuleiro.getQtdColunas()
								+ " )" + "!!");
						continue;
					}
				} catch (NumberFormatException e) {
					System.out.println("Digite dois números inteiros válidos!");
					continue;
				}

				Iterator<Integer> xy = Arrays.asList(linha, coluna).iterator();

				digitado = capturaValorDigitado("1 - Abrir ou 2 - (Des)Marcar: ");

				if ("1".equals(digitado)) {
					tabuleiro.abrir(xy.next(), xy.next());
				} else if ("2".equals(digitado)) {
					tabuleiro.alterarMarcacao(xy.next(), xy.next());
				} else if (digitado != "1" || digitado != "2") {
					System.out.println("\nEssa opção não existe!!");
					System.out.println("PRESTE MAIS ATENÇÃO!! - Por sua culpa o jogo será reiniciado...");
					tabuleiro.reiniciaJogo();
				}

			}

			System.out.println("Você ganhou!!");
		} catch (ExplosaoException e) {
			System.out.println(tabuleiro);
			System.out.println("Você perdeu!!");
		}
	}

	private String capturaValorDigitado(String texto) {
		System.out.println(texto);
		String digitado = entrada.nextLine();

		if ("sair".equalsIgnoreCase(digitado)) {
			throw new SairException();
		}

		return digitado;
	}

}
