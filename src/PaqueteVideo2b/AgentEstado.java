package PaqueteVideo2b;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

/**
 * Created with IntelliJ IDEA. User: ssamot Date: 14/11/13 Time: 21:45 This is a
 * Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class AgentEstado extends AbstractPlayer {
	/**
	 * Random generator for the agent.
	 */
	protected Random randomGenerator;
	/**
	 * List of available actions for the agent
	 */
	protected ArrayList<Types.ACTIONS> actions;

	public TablaQ tablaQ;

	public static int n = 0;

	public static double alpha = 0.2;
	public static final float gamma = 0.3f;
	public static final double k =3000;

	private static Random rand= new Random(100);

	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so           state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 */
	public AgentEstado(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();
		actions = so.getAvailableActions();
		actions.add(Types.ACTIONS.ACTION_NIL);
		actions.remove(Types.ACTIONS.ACTION_DOWN);
		actions.remove(Types.ACTIONS.ACTION_UP);

	}

	/**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
	 * @throws InterruptedException 
     */
    
  
    
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer)  {
    	int accion;
    	
    	Estado s = new Estado(stateObs); //Se crea e inicializa el estado s
    	String ident_s = s.getIndice(); //identificador de s
    	
    	//do {
		if (!TablaQ.comprobarEstado(ident_s)) { //Si no est� el estado, se mete.
			TablaQ.meterRandom(ident_s,rand); //Se mete con valores random 
		}
		
		//ESCOGER LA ACCI�N SEG�N LA POL�TICA DEL AGENTE
		double epsilon = k/(k+n); //n es el tiempo o iteraciones 
		double aleatorio = rand.nextDouble();

		if (aleatorio < epsilon) { //si el numero aleatorio es menor que epsilon, se ejecuta una accion aleatoria
			System.out.println("ALEATORIO");
			boolean repetir=false;

				repetir = false;
				accion = (int) Math.floor(Math.random()*3);
				
			
			//Devolver accion aleatoria. Se puede generar un entero entre 0 y 2 y al final del metodo act devolverlo
		}
		else { //Se elige la acci�n de mayor valor Q en la tablaQ
			System.out.println("Tabla Q");
			accion = TablaQ.sacarMejor(ident_s);
		}
    		
		//VER ESTADO SIGUIENTE Y RECOMPENSA
		StateObservation Futuro = stateObs.copy();
		Futuro.advance(actions.get(accion));
		Estado sPrima = new Estado(Futuro);
		String ident_sPrima = sPrima.getIndice();
		
		//PARA LAS ESTADISTICAS, SI LA PARTIDA VA A ACABAR, MIRA LOS TIMESTEPS QUE LLEVA Y LOS METE EN LAS ESTADISTICAS
		if (Futuro.getGameWinner() == Types.WINNER.PLAYER_LOSES || Futuro.getGameWinner() == Types.WINNER.PLAYER_WINS ) {
			Estadisticas.meterDato(stateObs.getGameTick());
			Estadisticas.numPartidas++;
			System.out.println("NUM PARTIDAS: " + Estadisticas.numPartidas);
		}
		
		
		//Si muere se va a un estado raro el cual no queremos controlar 
		
		if (!TablaQ.comprobarEstado(ident_sPrima)) { //Si no est� el estado, se mete.
			if (Futuro.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
				TablaQ.meter(ident_sPrima);
			
			}
			else
			TablaQ.meterRandom(ident_sPrima,rand); //Se mete con valores random 
		}
		float recompensa = 0;
		if (Futuro.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
			recompensa -=2;
		}
		else if (Entorno.celdaConGasolina(Entorno.getPosAvatar(Futuro), Futuro)) {
			
			recompensa +=1;
		}
		/*else if(sPrima.getLibre()== 2) { //Recompensa si esta en la columna de la celda libre
			recompensa += 0.001;
		}
		else if(sPrima.getGasolina()==1) { //Recompensa si esta en la columna de la celda de gasolina
			recompensa += 0.01;
		}*/
		
		else recompensa += 0.005;
		
		//CALCULAR VALORES Q
		double Qactual = TablaQ.recogerQvalor(ident_s, accion);
		double Qfuturo = TablaQ.recogerQvalor(ident_sPrima, TablaQ.sacarMejor(ident_sPrima));
		double v =  Qactual + alpha*(recompensa + gamma*Qfuturo - Qactual);
		
		//System.out.println("El valor de Q es: "+v);
		
		/*Entorno e = new Entorno(stateObs);
		System.out.println("libre: "+e.DireccionLibre(e.getPosAvatar()));
		e.verMapa();
		Entorno f = new Entorno(Futuro);
		System.out.println("libre futuro: "+f.DireccionLibre(f.getPosAvatar()));
		
		/*System.out.println();
		System.out.println("El estado actual es: "+ident_s);
		System.out.println();
		System.out.println("La acci�n tomada es: "+ actions.get(accion).toString());
		System.out.println();
		System.out.println("El estado futuro es: "+ident_sPrima);
		System.out.println();
		System.out.println("--------------------------------------------------------------\n\n");*/
		
		TablaQ.actualizar(ident_s, accion, v);
        return actions.get(accion); //ACCION NO ALEATORIA
    }

}
