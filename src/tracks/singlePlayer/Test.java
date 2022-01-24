package tracks.singlePlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import PaqueteVideo2b.Estadisticas;
import PaqueteVideo2b.TablaQ;
import core.competition.CompetitionParameters;
import core.game.Game;
import core.game.GameDescription;
import core.logging.Logger;
import core.vgdl.VGDLFactory;
import core.vgdl.VGDLParser;
import core.vgdl.VGDLRegistry;
import tools.ElapsedCpuTimer;
import tools.Utils;
import tracks.ArcadeMachine;
import tracks.levelGeneration.LevelGenMachine;
import tracks.levelGeneration.TestLevelGeneration;
import tracks.levelGeneration.constructiveLevelGenerator.LevelGenerator;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 04/10/13 Time: 16:29 This is a
 * Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Test {

    public static void main(String[] args) {

		// Available tracks:
		String sampleRandomController = "tracks.singlePlayer.simple.sampleRandom.Agent";
		String doNothingController = "tracks.singlePlayer.simple.doNothing.Agent";
		String sampleOneStepController = "tracks.singlePlayer.simple.sampleonesteplookahead.Agent";
		String sampleFlatMCTSController = "tracks.singlePlayer.simple.greedyTreeSearch.Agent";

		String sampleMCTSController = "tracks.singlePlayer.advanced.sampleMCTS.Agent";
        String sampleRSController = "tracks.singlePlayer.advanced.sampleRS.Agent";
        String sampleRHEAController = "tracks.singlePlayer.advanced.sampleRHEA.Agent";
		String sampleOLETSController = "tracks.singlePlayer.advanced.olets.Agent";

		//Load available games
		String spGamesCollection =  "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		//Game settings
		boolean visuals = true;
		int seed = new Random().nextInt();

		// Game and level to play
		int gameIdx = 80;//roadfighter
		int levelIdx =2; // level names from 0 to 4 (game_lvlN.txt).
		String gameName = games[gameIdx][1];
		String game = games[gameIdx][0];
		String level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);
		
		//System.out.println( game);

		String recordActionsFile = null;

		// 2. This plays a game in a level by the controller.
			try {
				TablaQ.leerFichero();
			} catch (IOException ex) {
			// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			ArcadeMachine.runOneGame(game, level1, visuals, "PaqueteVideo2b.AgentTestEstado", recordActionsFile, seed, 0);
		
								


		// 4. This plays a single game, in N levels, M times :
		/*String level5 = new String(game).replace(gameName, gameName + "_lvl" + 18);
		String level6 = new String(game).replace(gameName, gameName + "_lvl" + 19);
		String level7 = new String(game).replace(gameName, gameName + "_lvl" + 22);
		String level8 = new String(game).replace(gameName, gameName + "_lvl" + 23);
		String level9 = new String(game).replace(gameName, gameName + "_lvl" + 9);
		String level10 = new String(game).replace(gameName, gameName + "_lvl" + 10);
		String level11 = new String(game).replace(gameName, gameName + "_lvl" + 11);
		String level12 = new String(game).replace(gameName, gameName + "_lvl" + 12);
		String level13 = new String(game).replace(gameName, gameName + "_lvl" + 13);
		String level14 = new String(game).replace(gameName, gameName + "_lvl" + 14);
		String level15 = new String(game).replace(gameName, gameName + "_lvl" + 15);
		String level16 = new String(game).replace(gameName, gameName + "_lvl" + 16);
		String level17 = new String(game).replace(gameName, gameName + "_lvl" + 17);
		String level111 = new String(game).replace(gameName, gameName + "_lvl" + 20);
		String level3 = new String(game).replace(gameName, gameName + "_lvl" + 21);*/
		
		
		
		/*String level5 = new String(game).replace(gameName, gameName + "_lvl" + 18);
		String level6 = new String(game).replace(gameName, gameName + "_lvl" + 19);
		String level7 = new String(game).replace(gameName, gameName + "_lvl" + 20);
		String level8 = new String(game).replace(gameName, gameName + "_lvl" + 21);
		String level9 = new String(game).replace(gameName, gameName + "_lvl" + 22);
		String level10 = new String(game).replace(gameName, gameName + "_lvl" + 23);
		String level11 = new String(game).replace(gameName, gameName + "_lvl" + 10);
		String level12 = new String(game).replace(gameName, gameName + "_lvl" + 11);
		String level13 = new String(game).replace(gameName, gameName + "_lvl" + 13);
		String level14 = new String(game).replace(gameName, gameName + "_lvl" + 18);
		String level15 = new String(game).replace(gameName, gameName + "_lvl" + 19);
		String level16 = new String(game).replace(gameName, gameName + "_lvl" + 20);
		String level17 = new String(game).replace(gameName, gameName + "_lvl" + 21);
		String level111 = new String(game).replace(gameName, gameName + "_lvl" + 22);
		String level3 = new String(game).replace(gameName, gameName + "_lvl" + 23);*/


	/*	int M = 300;


		ArcadeMachine.runGames(game, new String[]{level5,level6,level7,level8,level9,level10,level11,level12,level13,level14,level15,level16,level17,level3,level111,level17,level16,level7,level15,level9,level111,level3,level7,level8,level9,level10,level11,level12,level13,level14,level15,level16,level17,level5,level6,level17,level16,level7,level15,level9,level5,level6,level7,level8,level9,level10,level11,level12,level13,level14,level15,level16,level17,level3,level111,level17,level16,level7,level15,level9,level111,level3,level7,level8,level9,level10,level11,level12,level13,level14,level15,level16,level17,level5,level6,level17,level16,level7,level15,level9,level5,level6,level7,level8,level9,level10,level11,level12,level13,level14,level15,level16,level17,level3,level111,level17,level16,level7,level15,level9,level111,level3,level7,level8,level9,level10,level11,level12,level13,level14,level15,level16,level17,level5,level6,level17,level16,level7,level15,level9}, M, "PaqueteVideo2b.AgentEstado", null);//120
		TablaQ.guardarTabla();
		Estadisticas.graficar();
		Estadisticas.graficarMedia(1000);*/
		

		
		

	}

    }

