package PaqueteVideo2;

import java.util.ArrayList;

import core.game.Observation;
import core.game.StateObservation;

public class Entorno2 { //todos los obst?culos tienen el mismo n?mero

	private int mapa [][];
	private StateObservation Ob;
	private int xGasolina=-1;
	private int yGasolina=-1;
	/*
	 * -16 aparicion arboles
	 * -13 aparicion coche azul 
	 * -6 coche azul
	 * -14 aparicion coches verdes
	 *  -7 coche verde
	 * -15 aparicion gasolina
	 *  -9 gasolina
	 * -10 arbol
	 * -1 avatar
	 */
	public Entorno2(StateObservation Ob) {
		this.Ob=Ob;
		ArrayList<Observation>[][] M = Ob.getObservationGrid();
		mapa = new int[M.length][M[1].length];
		
		for (int i = 0; i < mapa.length; i++) {
			for (int j = 0; j < mapa[0].length; j++) {
				 if (M[i][j].size() == 0) {
					 if(j>0 && mapa[i][j-1]==10) mapa[i][j] = 10;//Para que los espacios entre arboles salga que es un arbol
					 else mapa[i][j] = 0;//si no hay nada, se coloca un 0
				}else {
					//A los respawn le damos el valor del objeto que aparece ahi.
					if(M[i][j].get(0).itype == 13) mapa[i][j] =6;
					else if(M[i][j].get(0).itype == 14) mapa[i][j] =6;
					else if(M[i][j].get(0).itype == 15) mapa[i][j] =9;
					else if(M[i][j].get(0).itype == 2) mapa[i][j] =0;//en el futuro los 0 salen como 2
					else if(M[i][j].get(0).itype == 16) mapa[i][j] =6;
					else if(M[i][j].get(0).itype == 3) mapa[i][j] =10;//sale entre arbol en el futuro, por lo que lo pongo a 0
					else if(M[i][j].get(0).itype == 7) mapa[i][j] =6;
					else if(M[i][j].get(0).itype == 9) {mapa[i][j] =9; xGasolina=i; yGasolina=j;}
					else mapa[i][j] =M[i][j].get(0).itype;//guardamos el tipo
					
				}
			}

		}
	}
	
	
	public void verMapa() {

		for (int j= 0; j < mapa[1].length; j++) {
			System.out.print("\n");
			for (int i = 0; i < mapa.length; i++) {
				System.out.print(mapa[i][j]+"  ");
			}

		}
			System.out.print("\n Fin \n\n");
	}
	
	public int getCelda(int i,int j) {
		if (i<0 || j<0 || i>=mapa.length || j>=mapa[0].length) return -1;//-1 es el indice que le vamos a poner a los limites del mapa
		return mapa[i][j];
	}
	
	public int [][] getTablaMapa(){
		return mapa;
	}
	
	public int[] getPosAvatar() {
		
		//Calculamos la posicion del avatar
		int tB=Ob.getBlockSize();//cuantos pixeles tiene cada bloque
        tools.Vector2d v=Ob.getAvatarPosition();
        int[] A= {(int)v.x/tB,(int)v.y/tB};//Posicion del avatar
		
		return A;
	}
	
	public static int[] getPosAvatar(StateObservation Ob) {
		
		//Calculamos la posicion del avatar
		int tB=Ob.getBlockSize();//cuantos pixeles tiene cada bloque
        tools.Vector2d v=Ob.getAvatarPosition();
        int[] A= {(int)v.x/tB,(int)v.y/tB};//Posicion del avatar
		
		return A;
	}

	static public boolean celdaConGasolina(int[] celda,StateObservation Ob) {///sin comprobar
		
		ArrayList<Observation>[][] M = Ob.getObservationGrid();
		int numElementosCelda=M[celda[0]][celda[1]].size();//conseguimos la lista de elementos de la celda
		
		for (int i = 0; i < numElementosCelda; i++) {
			if(M[celda[0]][celda[1]].get(i).itype==9) return true;//si encontramos gasolina devolvemos true
		}
		return false;
		
	}
	
	public int direccionGasolina(int[] Avatar) {
		//Nos dice en que direccion esta la gasolina 1:arriba 2:abajo 3:izquierda 4:derecha -1: no ha aparecido
		if(xGasolina==-1) return -1;
		if(Avatar[0]==xGasolina) {
			if(yGasolina<=Avatar[1]) return 1;
			else return 2;
		}else {
			if(xGasolina<Avatar[0]) return 3;
			else return 4;
		}
	}
	
	



}