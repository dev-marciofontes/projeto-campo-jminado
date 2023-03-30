package br.com.marciofontes.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.marciofontes.excecao.ExplosaoException;

public class CampoTeste {

	private Campo campo;

	@BeforeEach
	void iniciarCampo() {
		campo = new Campo(3, 3);
	}

	@Test
	void testeVizinhoRealDistancia1Esquerda() {
		Campo vizinho = new Campo(3, 2);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertTrue(resultado);
	}

	@Test
	void testeVizinhoRealDistancia1Direita() {
		Campo vizinho = new Campo(3, 4);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertTrue(resultado);
	}

	@Test
	void testeVizinhoRealDistancia1Cima() {
		Campo vizinho = new Campo(2, 3);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertTrue(resultado);
	}

	@Test
	void testeVizinhoRealDistancia1Baixo() {
		Campo vizinho = new Campo(4, 3);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertTrue(resultado);
	}

	@Test
	void testeVizinhoRealDistancia2Diagonal() {
		Campo vizinho = new Campo(2, 2);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertTrue(resultado);
	}

	@Test
	void testeNaoVizinhoDistanciaReta() {
		Campo vizinho = new Campo(1, 3);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertFalse(resultado);
	}

	@Test
	void testeNaoVizinhoDistanciaDiagonal() {
		Campo vizinho = new Campo(1, 2);
		boolean resultado = campo.adicionarVizinho(vizinho);
		assertFalse(resultado);
	}

	@Test
	void testeValorPadraoAtributoMarcado() {
		assertFalse(campo.isMarcado());
	}

	@Test
	void testeAlternarMarcacao() {
		campo.alternarMarcacao();
		assertTrue(campo.isMarcado());
	}

	@Test
	void testeAlternarMarcacaoDuasChamadas() {
		campo.alternarMarcacao();
		campo.alternarMarcacao();
		assertFalse(campo.isMarcado());
	}

	@Test
	void testeAbrirNaoMinadoNaoMArcado() {
		assertTrue(campo.abrir());
	}

	@Test
	void testeAbrirNaoMinadoMArcado() {
		campo.alternarMarcacao();
		assertFalse(campo.abrir());
	}

	@Test
	void testeAbrirMinadoMArcado() {
		campo.alternarMarcacao();
		campo.minar();
		assertFalse(campo.abrir());
	}

	@Test
	void testeAbrirMinadoNaoMArcado() {
		campo.minar();

		assertThrows(ExplosaoException.class, () -> {
			campo.abrir();
		});

	}

	@Test
	void testeAbrirComVizinhos1() {
		Campo campo11 = new Campo(1, 1);

		Campo campo22 = new Campo(2, 2);
		campo22.adicionarVizinho(campo11);

		campo.adicionarVizinho(campo22);
		campo.abrir();

		assertTrue(campo22.isAberto() && campo11.isAberto());

	}

	@Test
	void testeAbrirComVizinhos2() {
		Campo campo11 = new Campo(1, 1);
		Campo campo12 = new Campo(1, 2);
		campo12.minar();

		Campo campo22 = new Campo(2, 2);
		campo22.adicionarVizinho(campo11);
		campo22.adicionarVizinho(campo12);

		campo.adicionarVizinho(campo22);
		campo.abrir();

		assertTrue(campo22.isAberto() && campo11.isFechado());

	}

	@Test
	void testeGetLinha() {
		assertEquals(3, campo.getLinha());
	}

	@Test
	void testeGetColuna() {
		assertEquals(3, campo.getColuna());
	}

	@Test
	void testeObjetivoAlcancado() {
		Campo campo1 = new Campo(0, 0);
		campo1.minar();
		campo1.alternarMarcacao();
		assertTrue(campo1.objetivoAlcancado());

		Campo campo2 = new Campo(0, 1);
		campo2.abrir();
		assertTrue(campo2.objetivoAlcancado());

		Campo campo3 = new Campo(1, 0);
		campo3.minar();
		assertThrows(ExplosaoException.class, () -> {
			campo3.abrir();
		});
		assertFalse(campo3.objetivoAlcancado());

	}
	
	@Test 
	void testeReiniciar(){
	    campo.minar();
	    assertThrows(ExplosaoException.class, () -> {
			campo.abrir();
		});
	    campo.alternarMarcacao();

	    campo.reiniciar();

	    assertFalse(campo.isAberto());
	    assertFalse(campo.isMinado());
	    assertFalse(campo.isMarcado());
	}
	
	 @Test
	    void testMinasNaVizinhanca() {
	        // criação dos campos e minagem dos campos 1 e 4
	        Campo campo1 = new Campo(0, 0);
	        Campo campo2 = new Campo(0, 1);
	        Campo campo3 = new Campo(1, 0);
	        Campo campo4 = new Campo(1, 1);
	        campo1.minar();
	        campo4.minar();

	        // adição de vizinhos
	        campo1.adicionarVizinho(campo2);
	        campo1.adicionarVizinho(campo3);	        
	        campo1.adicionarVizinho(campo4);
	        
	        campo2.adicionarVizinho(campo1);
	        campo2.adicionarVizinho(campo3);	        
	        campo2.adicionarVizinho(campo4);
	        
	        campo3.adicionarVizinho(campo1);
	        campo3.adicionarVizinho(campo2);
	        campo3.adicionarVizinho(campo4);
	        
	        
	        campo4.adicionarVizinho(campo1);
	        campo4.adicionarVizinho(campo2);	        
	        campo4.adicionarVizinho(campo3);

	        // teste de contagem de minas na vizinhança
	        assertEquals(2, campo2.minasNaVizinhanca());
	        assertEquals(2, campo3.minasNaVizinhanca());
	        assertEquals(1, campo1.minasNaVizinhanca());
	    }

	    @Test
	    void testMinasNaVizinhancaSemVizinhosMinados() {
	        // criação dos campos e minagem do campo 1
	        Campo campo1 = new Campo(3, 3);
	        Campo campo2 = new Campo(0, 1);
	        Campo campo3 = new Campo(1, 0);
	        Campo campo4 = new Campo(1, 1);
	        campo1.minar();

	        // adição de vizinhos
	        campo2.adicionarVizinho(campo3);
	        campo2.adicionarVizinho(campo4);
	        
	        campo3.adicionarVizinho(campo4);
	        campo3.adicionarVizinho(campo2);
	        
	        campo4.adicionarVizinho(campo1);
	        campo4.adicionarVizinho(campo3);

	        // teste de contagem de minas na vizinhança
	        assertEquals(0, campo2.minasNaVizinhanca());
	        assertEquals(0, campo3.minasNaVizinhanca());
	        assertEquals(0, campo4.minasNaVizinhanca());
	    }
	
	

}
