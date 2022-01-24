package PaqueteVideo2DistanciaYDireccion;

import java.util.ArrayList;

import core.game.StateObservation;
import ontology.Types;
import serialization.Vector2d;

public class Estado {

	final static int MUYCERCA = 0;
	final static int CERCA = 1;
	final static int MEDIO = 2;
	final static int LEJOS = 3;
	
	/*Atributos*/
	public int arriba, izquierda, derecha; //Información del mapa
	public String indice; //Indice del estado
	public int gasolina;//Nos dice en que direccion esta la gasolina 1:arriba 2:izquierda 3:derecha -1: no ha aparecido y abajo
	public int libre;//Nos dice la direccion de la celda mas cercana 0: izquierda 1:Derecha 2:mismaColumna
	public int distanciaGasolina;
	public int distanciaObstaculo;
	
	/*Constructor*/

	
	
	public Estado(StateObservation Ob) {
		//Si muere, le ponemos un estado propio
		if (Ob.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
			
			this.arriba = -1;
			this.izquierda = -1;
			this.derecha =  -1;
			this.gasolina=-1;
			this.libre=-1;
			this.distanciaGasolina=-1;
			this.distanciaObstaculo=-1;
			indice = funcionHash(this);
		
		}else{
			
		Entorno m=new Entorno(Ob);
		
		int[] A= m.getPosAvatar();
		int x= A[0];
		int y=A[1];
        
        //sacamos las celdas que necesitamos del avatar
        this.arriba = m.getCelda(x, y-1);
		this.izquierda = m.getCelda(x-1, y);
		this.derecha =  m.getCelda(x+1, y);
		this.gasolina = m.direccionGasolina(A);
		this.libre=m.DireccionLibre(A);
		this.distanciaGasolina=m.distanciaGasolina(A);
		this.distanciaObstaculo=m.distanciaObstaculo(A);
		indice = funcionHash(this);
		}
		
	}
	
	public boolean gasolinaMasCerca(Estado Futuro) {
		int distFuturo = Futuro.getDistanciaGasolina();
		
		if (distanciaGasolina>distFuturo) {
			return true;
		}else return false;
	}
	
	/*public boolean obstaculoCerca(Estado Futuro) {
		int distFuturo = Futuro.getDistanciaObstaculo()();
		
		if (distanciaObstaculo<distFuturo) {
			return true;
		}else return false;
	}*/
	
	public int getDistanciaObstaculo() {
		return distanciaObstaculo;
	}

	public void setDistanciaObstaculo(int distanciaObstaculo) {
		this.distanciaObstaculo = distanciaObstaculo;
	}

	public int getDistanciaGasolina() {
		return distanciaGasolina;
	}

	public void setDistanciaGasolina(int distanciaGasolina) {
		this.distanciaGasolina = distanciaGasolina;
	}

	public int getLibre() {
		return libre;
	}

	public void setLibre(int libre) {
		this.libre = libre;
	}

	/*public void setGasolina(int gasolina) {
		this.gasolina = gasolina;
	}*/

	//Calcular indice único del estado
	private String funcionHash(Estado estado) {
		return "" + estado.arriba + estado.izquierda + estado.derecha+estado.gasolina+estado.libre;
	}

	/*Ver si el estado dado es igual a this*/
	public boolean compararEstados(Estado a) {
		return(a.indice == this.indice);
	}
	

	/*Setters y getters*/
	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}
	
	public int getArriba() {
		return arriba;
	}

	public void setArriba(int arriba) {
		this.arriba = arriba;
	}

	public int getIzquierda() {
		return izquierda;
	}

	public void setIzquierda(int izquierda) {
		this.izquierda = izquierda;
	}

	public int getDerecha() {
		return derecha;
	}

	public void setDerecha(int derecha) {
		this.derecha = derecha;
	}


	public int getGasolina() {
		return this.gasolina;
	}
	
	
	
}
