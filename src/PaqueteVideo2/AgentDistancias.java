package PaqueteVideo2;

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
public class AgentDistancias extends AbstractPlayer {
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

	public static final double alpha = 0.1;
	public static final float gamma = 0.7f;
	public static final double k = 300;

	final static int MUYCERCA = 0;
	final static int CERCA = 1;
	final static int MEDIO = 2;
	final static int LEJOS = 3;

	private static Random rand = new Random(100);

	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so           state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 */
	public AgentDistancias(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		randomGenerator = new Random();
		actions = so.getAvailableActions();
		actions.add(Types.ACTIONS.ACTION_NIL);

	}

	/**
	 * Picks an action. This function is called every game step to request an action
	 * from the player.
	 * 
	 * @param stateObs     Observation of the current state.
	 * @param elapsedTimer Timer when the action returned is due.
	 * @return An action for the current state
	 * @throws InterruptedException
	 */

	public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		int accion;

		EstadoDistancias s = new EstadoDistancias(stateObs); // Se crea e inicializa el estado s
		String ident_s = s.getIndice(); // identificador de s

		// do {
		if (!TablaQ.comprobarEstado(ident_s)) { // Si no est� el estado, se mete.
			TablaQ.meterRandom(ident_s, rand, s.valoresBloqueado()); // Se mete con valores random
		}

		// ESCOGER LA ACCI�N SEG�N LA POL�TICA DEL AGENTE
		double epsilon = k / (k + n); // n es el tiempo o iteraciones
		double aleatorio = rand.nextDouble();

		if (aleatorio < epsilon) { // si el numero aleatorio es menor que epsilon, se ejecuta una accion aleatoria
			// System.out.println("aleatorio");
			boolean repetir = false;
			do {
				repetir = false;
				accion = (int) Math.floor(Math.random() * 4);
				// Controlamos las acciones bloqueadas
				if ((s.getAbajo() == -1 && accion == 2) || (s.getArriba() == -1 && accion == 3))
					repetir = true;
			} while (repetir);

			// Devolver accion aleatoria. Se puede generar un entero entre 1 y 4 y al final
			// del metodo act devolverlo
		} else { // Se elige la acci�n de mayor valor Q en la tablaQ
					// System.out.println("Q_Valor");
			accion = TablaQ.sacarMejor(ident_s);
		}

		// VER ESTADO SIGUIENTE Y RECOMPENSA
		StateObservation Futuro = stateObs.copy();
		Futuro.advance(actions.get(accion));
		EstadoDistancias sPrima = new EstadoDistancias(Futuro);
		String ident_sPrima = sPrima.getIndice();

		// PARA LAS ESTADISTICAS, SI LA PARTIDA VA A ACABAR, MIRA LOS TIMESTEPS QUE
		// LLEVA Y LOS METE EN LAS ESTADISTICAS
		if (Futuro.getGameWinner() == Types.WINNER.PLAYER_LOSES || Futuro.getGameWinner() == Types.WINNER.PLAYER_WINS) {
			Estadisticas.meterDato(stateObs.getGameTick());
			Estadisticas.numPartidas++;
			System.out.println("NUM PARTIDAS: " + Estadisticas.numPartidas);
		}

		// Si muere se va a un estado raro el cual no queremos controlar

		if (!TablaQ.comprobarEstado(ident_sPrima)) { // Si no est� el estado, se mete.
			if (Futuro.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
				TablaQ.meter(ident_sPrima);

			} else
				TablaQ.meterRandom(ident_sPrima, rand, sPrima.valoresBloqueado()); // Se mete con valores random
		}

		// SISTEMA DE RECOMPENSAS
		float recompensa = 1;

		if (Futuro.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
			recompensa -= 2;
		} else {
			if (EntornoDistancia.celdaConGasolina(Entorno.getPosAvatar(Futuro), Futuro)) {
				recompensa++;
			}

			switch (sPrima.getGasolina()) {
			case MUYCERCA: {
				recompensa += 0.8;
				break;
			}
			case CERCA: {
				recompensa += 0.6;
				break;
			}
			case MEDIO: {
				recompensa += 0.35;
				break;
			}
			case LEJOS: {
				recompensa += 0.05;
				break;
			}
			default:
				System.out.println("El case de gasolina ha entrado en default. GASOLINA = "+sPrima.getGasolina());

			}

			switch (sPrima.getObstaculo()) {
			case MUYCERCA: {
				recompensa -= 0.8;
				break;
			}
			case CERCA: {
				recompensa -= 0.4;
				break;
			}
			case MEDIO: {
				recompensa += 0.05;
				break;
			}
			case LEJOS: {
				recompensa += 0.15;
				break;
			}
			default:
				System.out.println("El case de obstaculo ha entrado en default. OBSTACULO = "+sPrima.getObstaculo());
			}

		}
		// CALCULAR VALORES Q
		double Qactual = TablaQ.recogerQvalor(ident_s, accion);
		double Qfuturo = TablaQ.recogerQvalor(ident_sPrima, TablaQ.sacarMejor(ident_sPrima));
		double v = Qactual + alpha * (recompensa + gamma * Qfuturo - Qactual);

		// System.out.println("El valor de Q es: "+v);
		/*
		 * Entorno4 e = new Entorno4(Futuro); e.verMapa();
		 */
		/*
		 * System.out.println(); System.out.println("El estado actual es: "+ident_s);
		 * System.out.println(); System.out.println("La acci�n tomada es: "+
		 * actions.get(accion).toString()); System.out.println();
		 * System.out.println("El estado futuro es: "+ident_sPrima);
		 * System.out.println(); System.out.println(
		 * "--------------------------------------------------------------\n\n");
		 */

		TablaQ.actualizar(ident_s, accion, v);
		// System.out.println("El tick es: "+ Futuro.getGameTick());
		return actions.get(accion); // ACCION NO ALEATORIA
	}

}