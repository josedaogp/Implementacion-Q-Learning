package PaqueteVideo2;

import java.util.ArrayList;

import core.game.StateObservation;
import ontology.Types;
import serialization.Vector2d;

public class Estado {

	/*Atributos*/
	public int arriba, abajo, izquierda, derecha,arribaDerecha,arribaIzquierda; //Información del mapa
	public String indice; //Indice del estado
	public int gasolina;//Nos dice en que direccion esta la gasolina 1:arriba 2:abajo 3:izquierda 4:derecha -1: no ha aparecido
	
	/*Constructor*/
	//1100 estados
	public Estado(int arr, int ab, int iz, int der, int arrDer, int arrIz, int DirGasolina) {
		this.arriba = arr;
		this.abajo = ab;
		this.izquierda = iz;
		this.derecha = der;
		this.arribaDerecha = arrDer;
		this.arribaIzquierda = arrIz;
		this.gasolina=DirGasolina;
		indice = funcionHash(this);
		
	}
	
	public Estado(StateObservation Ob) {
		//Si muere, le ponemos un estado propio
		if (Ob.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
			
			this.arriba = -1;
			this.abajo = -1;
			this.izquierda = -1;
			this.derecha =  -1;
			this.arribaDerecha = -1;
			this.arribaIzquierda =  -1;
			this.gasolina=-1;
			indice = funcionHash(this);
		
		}else{
			
		Entorno m=new Entorno(Ob);
		
		int[] A= m.getPosAvatar();
		int x= A[0];
		int y=A[1];
        
        //sacamos las celdas que necesitamos del avatar
        this.arriba = m.getCelda(x, y-1);
		this.abajo = m.getCelda(x, y+1);///////////////////////
		this.izquierda = m.getCelda(x-1, y);
		this.derecha =  m.getCelda(x+1, y);
		this.arribaDerecha = m.getCelda(x+1, y-1);
		this.arribaIzquierda =  m.getCelda(x-1, y-1);
		this.gasolina = m.direccionGasolina(A);
		indice = funcionHash(this);
		}
		
	}
	
	//Calcular indice único del estado
	private String funcionHash(Estado estado) {
		return "" + estado.arriba + estado.abajo + estado.izquierda + estado.derecha + estado.arribaDerecha + estado.arribaIzquierda+estado.gasolina;
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

	public int getAbajo() {
		return abajo;
	}

	public void setAbajo(int abajo) {
		this.abajo = abajo;
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

	public int getArribaDerecha() {
		return arribaDerecha;
	}

	public void setArribaDerecha(int arribaDerecha) {
		this.arribaDerecha = arribaDerecha;
	}

	public int getArribaIzquierda() {
		return arribaIzquierda;
	}

	public void setArribaIzquierda(int arribaIzquierda) {
		this.arribaIzquierda = arribaIzquierda;
	}
	public ArrayList<Integer> valoresBloqueado() {
		ArrayList<Integer> bloq=new ArrayList<Integer>();
		if(this.arriba==-1) bloq.add(3);
		if(this.abajo==-1) bloq.add(2);
		return bloq;
	}
	public int getGasolina() {
		return this.gasolina;
	}
	
	
}
