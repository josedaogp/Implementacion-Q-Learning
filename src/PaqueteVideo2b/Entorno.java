package PaqueteVideo2b;

import java.util.ArrayList;

import core.game.Observation;
import core.game.StateObservation;

public class Entorno { // todos los obstáculos tienen el mismo número

	
	private int mapa[][];
	private StateObservation Ob;
	private int xGasolina = -1;
	private int xrespawnGasolina;
	private int yGasolina = -1;
	private ArrayList<int[]> obstaculos = new ArrayList<>();

	/*
	 * -16 aparicion arboles -13 aparicion coche azul -6 coche azul -14 aparicion
	 * coches verdes -7 coche verde -15 aparicion gasolina -9 gasolina -10 arbol -1
	 * avatar
	 */
	public Entorno(StateObservation Ob) {
		this.Ob = Ob;
		ArrayList<Observation>[][] M = Ob.getObservationGrid();
		mapa = new int[M.length][M[1].length];

		for (int i = 0; i < mapa.length; i++) {
			for (int j = 0; j < mapa[0].length; j++) {
				if (M[i][j].size() == 0) {
					if (j > 0 && mapa[i][j - 1] == 10)
						mapa[i][j] = 6;// Para que los espacios entre arboles salga que es un arbol
					else
						mapa[i][j] = 0;// si no hay nada, se coloca un 0
				} else {
					// A los respawn le damos el valor del objeto que aparece ahi.
					if (M[i][j].get(0).itype == 13) {
						mapa[i][j] = 6;
						int[] obst = { i, j };
						obstaculos.add(obst);
					}

					else if (M[i][j].get(0).itype == 14) {
						mapa[i][j] = 6;
						int[] obst = { i, j };
						obstaculos.add(obst);
					} else if (M[i][j].get(0).itype == 15) {
						mapa[i][j] = 0;
						xrespawnGasolina=i;
					}
					else if (M[i][j].get(0).itype == 2)
						mapa[i][j] = 0;// en el futuro los 0 salen como 2
					else if (M[i][j].get(0).itype == 16) {
						mapa[i][j] = 6;
					} else if (M[i][j].get(0).itype == 3)
						mapa[i][j] = 6;// sale entre arbol en el futuro, por lo que lo pongo a 0
					else if (M[i][j].get(0).itype == 7) {
						mapa[i][j] = 6;
						int[] obst = { i, j };
						obstaculos.add(obst);
					}
						else if (M[i][j].get(0).itype == 6) {
							mapa[i][j] = 6;
							int[] obst = { i, j };
							obstaculos.add(obst);
					} else if (M[i][j].get(0).itype == 9) {
						mapa[i][j] = 9;
						xGasolina = i;
						yGasolina = j;
					} else
						mapa[i][j] = M[i][j].get(0).itype;// guardamos el tipo

				}
			}

		}
	}

	public void verMapa() {

		for (int j = 0; j < mapa[1].length; j++) {
			System.out.print("\n");
			for (int i = 0; i < mapa.length; i++) {
				System.out.print(mapa[i][j] + "  ");
			}

		}
		System.out.print("\n Fin \n\n");
	}

	public int getCelda(int i, int j) {
		if (i < 0 || j < 0 || i >= mapa.length || j >= mapa[0].length)
			return -1;// -1 es el indice que le vamos a poner a los limites del mapa
		return mapa[i][j];
	}

	public int[][] getTablaMapa() {
		return mapa;
	}

	public int[] getPosAvatar() {

		// Calculamos la posicion del avatar
		int tB = Ob.getBlockSize();// cuantos pixeles tiene cada bloque
		tools.Vector2d v = Ob.getAvatarPosition();
		int[] A = { (int) v.x / tB, (int) v.y / tB };// Posicion del avatar

		return A;
	}

	public static int[] getPosAvatar(StateObservation Ob) {

		// Calculamos la posicion del avatar
		int tB = Ob.getBlockSize();// cuantos pixeles tiene cada bloque
		tools.Vector2d v = Ob.getAvatarPosition();
		int[] A = { (int) v.x / tB, (int) v.y / tB };// Posicion del avatar

		return A;
	}

	static public boolean celdaConGasolina(int[] celda, StateObservation Ob) {/// sin comprobar

		ArrayList<Observation>[][] M = Ob.getObservationGrid();
		int numElementosCelda = M[celda[0]][celda[1]].size();// conseguimos la lista de elementos de la celda

		for (int i = 0; i < numElementosCelda; i++) {
			if (M[celda[0]][celda[1]].get(i).itype == 9)
				return true;// si encontramos gasolina devolvemos true
		}
		return false;

	}

	public int direccionGasolina(int[] Avatar) {
		// Nos dice en que direccion esta la gasolina 1:arriba 2:izquierda
		// 3:derecha -1: no ha aparecido , :abajo
		if (xGasolina == -1)
			return -1;
		if (Avatar[0] == xGasolina) {
			if (yGasolina <= Avatar[1])
				return 1;
			else
				return -1;
		} else {
			if (xGasolina < Avatar[0])
				return 2;
			else
				return 3;
		}
	}


	public int DireccionLibre(int[] Avatar) {
		// 0 izquierda 1 derecha 2 mismaColumna
		double distanciamin = 9999;
		int fila=Avatar[1];
		int columna=Avatar[0];
		for (int i = 0; i < obstaculos.size(); i++) {
			int[] obsti=obstaculos.get(i);
			int xObstaculo = obsti[0];
			int yObstaculo = obsti[1];
			double diferenciaX = Avatar[0] - xObstaculo;
			double diferenciaY = Avatar[1] - yObstaculo;

			double distancia = Math.sqrt(Math.pow(diferenciaX, 2) + Math.pow(diferenciaY, 2));
			
			if (distanciamin>distancia && Avatar[1]>=yObstaculo) {
				distanciamin=distancia;
				fila=yObstaculo;
				columna=xObstaculo;
			}
			
		}
		//System.out.println("objeto: x="+columna+"  y= "+fila);
		
		ArrayList<Integer> columnaCercana= new ArrayList<Integer>();
		double dist=99;
		for (int i = 0; i < mapa.length; i++) {
			int filaAnterior=fila-1;
			if(fila==0) filaAnterior=0;
			if(mapa[i][fila]==0 && (mapa[i][filaAnterior]==0 || mapa[i][filaAnterior]==9) ) {//si esta libre y la siguiente celda esta libre tambien
				if(Math.abs(Avatar[0]-i)<=dist) {
					if(Math.abs(Avatar[0]-i)==dist) {
						columnaCercana.add(i);
					}else {
						columnaCercana.clear();
						columnaCercana.add(i);
						dist=Math.abs(Avatar[0]-i);
					}
					
				}
			}
		}
			
			int minCol=-1;
			dist =99;
			
			for (int j = 0; j < columnaCercana.size(); j++) {
				int colum=columnaCercana.get(j);
				if(Math.abs(xrespawnGasolina-colum)<dist) {
					dist=Math.abs(xrespawnGasolina-colum);
					minCol=colum;
				}
			}
			
			//System.out.println("mincol:    "+minCol);
			
			if(minCol< Avatar[0]) return 0;
			else if(minCol> Avatar[0]) return 1;
			else return 2;
			
			
		
		
		
		
		

	}

}