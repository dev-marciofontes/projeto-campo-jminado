package br.com.marciofontes;

import br.com.marciofontes.modelo.Tabuleiro;
import br.com.marciofontes.visao.TabuleiroTerminal;

public class Aplicacao {

	public static void main(String[] args) {

		Tabuleiro tabuleiro = new Tabuleiro(6, 6);

		new TabuleiroTerminal(tabuleiro);

	}

}
