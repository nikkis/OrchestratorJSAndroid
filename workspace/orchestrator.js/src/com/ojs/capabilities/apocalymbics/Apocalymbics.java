package com.ojs.capabilities.apocalymbics;



import java.util.ArrayList;

import org.json.JSONArray;

import bmge.framework.Game;
import bmge.framework.Screen;
import bmge.zombiegame.SampleGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Apocalymbics {

	private static final String TAG = Apocalymbics.class.getSimpleName();
	/*************************************************************************
	 * 	SDP stuff                                                            *
	 *************************************************************************/
	private static Context applicationContext;	
	public void initCapability( Context applicationContext ) {
		Apocalymbics.applicationContext = applicationContext;		
	}
	/*************************************************************************/

	/**
	 * First method to be called from orchestrator.js
	 */
	static Game game;	
	public void initGame() {
		Log.d(TAG, "initGame");
		if(game != null) {
			Log.d(TAG, "game was NOT null -> finish()");
			((Activity) game).finish();
			Log.d(TAG, "finish() called!");
		}
		game = null;
		Intent testIntent = new Intent(Apocalymbics.applicationContext, SampleGame.class);
		testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		applicationContext.startActivity(testIntent);

	}
	
	/** THESE METHODS ARE CALLED FROM THE SERVER **/

	/**
	 * The second method that is called from the server. Because of this method
	 * call, it starts starts SDPInterfaceBlackActivity, which executes its
	 * OnCreate() method, which in turn starts AndroidGame activity and starts
	 * the game on client
	 * 
	 * @param numOfDevices
	 *            Number of devices (players) in the game
	 * @param playerNumber
	 *            Number of this player
	 * @param isAdmin
	 *            Is this player also the admin (who gets to choose the game)
	 */
	public int launchApplication(Integer playerNumber) {
		try {

			game.getCurrentScreen().setInitialInfo(playerNumber);


		} catch (Exception e) {
			Log.d("RemoteMethodListener/launchApplication", e.toString());
		}

		return 0;
	}

	/**
	 * Makes the player on this client admin
	 */
	public int makeAdminPlayer() {
		game.getCurrentScreen().makeAdminPlayer();
		return 0;
	}

	/**
	 * Makes the player on this client normal player
	 */
	public int makeNormalPlayer() {
		game.getCurrentScreen().makeNormalPlayer();
		return 0;
	}

	/**
	 * Returns the selected country of the client. If no country is selected or
	 * player is not in country selection screen, value UNINITIALIZED is
	 * returned
	 * 
	 * @return Number of the chosen game
	 */
	public Integer getCountrySelection() {
		Log.d("RemoteMethodListener", "getCountrySelection() entry");

		// In case player is not in country selection screen, return so that
		// no turn for country selection is given to the player
		if (!game.getCurrentScreen().isInCountrySelectionScreen()) {
			return Integer.valueOf(Screen.UNINITIALIZED);
		}

		// Give player his selection turn. If the player already has the turn,
		// this call doesn't have an effect
		game.getCurrentScreen().giveCountrySelectionTurn();

		int countryNumber = game.getCurrentScreen().getCountrySelection();
		//return countryNumber; 
		return Integer.valueOf(countryNumber);
	}

	/**
	 * Updates the currently selected countries by other players
	 * 
	 * @param countrySelections
	 *            An array which contains currently selected countries
	 */
	public int updatePlayerInfo(JSONArray countrySelections, JSONArray playerNumbers) {
		try {
			/*
			JSONArray countrySelections = JSONmethodcallParameters
					.getJSONArray("countrySelections");
			JSONArray playerNumbers = JSONmethodcallParameters
					.getJSONArray("playerNumbers");
*/
			ArrayList<Screen.Player> inGamePlayers = new ArrayList<Screen.Player>();

			int amountOfSelections = countrySelections.length();
			for (int player = 0; player < amountOfSelections; ++player) {
				Object countrySelectionObject = countrySelections.get(player);
				Object playerNumberObject = playerNumbers.get(player);
				Integer countryNumber = (Integer) countrySelectionObject;
				Integer playerNumber = (Integer) playerNumberObject;

				Screen.Player newPlayer = new Screen.Player();
				newPlayer.country = countryNumber;
				newPlayer.number = playerNumber;
				newPlayer.allThrows = new ArrayList<Integer>();
				newPlayer.wantsToContinue = true;
				inGamePlayers.add(newPlayer);
			}

			game.getCurrentScreen().updateInGamePlayers(inGamePlayers);

		} catch (Exception e) {
			Log.d("RemoteMethodListener/updatePlayerInfo", e.toString());
		}

		return 0;
	}

	/**
	 * Changes the screen to game selection
	 */
	public int moveToGameSelectionScreen() {
		game.getCurrentScreen().moveToGameSelectionScreen();
		return 0;
	}

	/**
	 * Inquires the selected game. This call is done only for the admin
	 * 
	 * @return Number of the selected game
	 */
	public Integer getGameSelectionFromAdmin() {
		int gameNumber = game.getCurrentScreen().getGameSelectionFromAdmin();
		//return gameNumber; 
		return Integer.valueOf(gameNumber);
	}

	/**
	 * Informs client of the game that admin selected
	 */
	public int updateGameSelection(Integer selectedGame) {

		try {
			//int selectedGame = JSONmethodcallParameters.getInt("selectedGame");
			game.getCurrentScreen().updateGameSelection(selectedGame);
		} catch (Exception e) {
			Log.d("RemoteMethodListener/updateGameSelection", e.toString());
		}

		return 0;
	}

	/**
	 * Changes the screen to selected mini game
	 */
	public int moveToGameplayScreen() {
		game.getCurrentScreen().moveToGameplayScreen();
		return 0;
	}

	/**
	 * Changes the screen to winner screen
	 */
	public int moveToWinnerScreen() {
		game.getCurrentScreen().moveToWinnerScreen();
		return 0;
	}

	/**
	 * Returns final placement of the player. Server uses it to determine which
	 * player gets the admin status next
	 */
	public Integer getPlayerPlacement() {
		int playerPlacement = game.getCurrentScreen().getThisPlayerPlacement();
		//return playerPlacement; 
		return Integer.valueOf(playerPlacement);
	}

	/**
	 * Moves the player to play again screen
	 */
	public int moveToPlayAgainScreen() {
		game.getCurrentScreen().moveToPlayAgainScreen();
		return 0;
	}

	/**
	 * Has the player decided to continue or exit in play again screen
	 * 
	 * @return Has the player decided to continue or exit game
	 */
	public Boolean hasDecided() {
		boolean hasDecided = game.getCurrentScreen().hasDecided();
		return Boolean.valueOf(hasDecided);
	}

	/**
	 * Has the player decided to continue playing
	 * 
	 * @return Has the player decided to continue playing
	 */
	public Boolean wantsToContinue(Boolean wantsToContinue) {
		//boolean wantsToContinue = game.getCurrentScreen().wantsToContinue();
		return Boolean.valueOf(wantsToContinue);
	} 

	/**
	 * Moves player to main menu screen (no other players anymore who want to
	 * continue playing)
	 */
	public int moveToMainMenuScreen() {
		game.getCurrentScreen().moveToMainMenuScreen();
		return 0;
	}

	/***************************************/
	/**
	 * SKULL THROW GAME SPECIFIC METHODS /
	 ***************************************/

	public int startRound(Integer roundNumber) {

		try {
			//int roundNumber = JSONmethodcallParameters.getInt("roundNumber");
			game.getCurrentScreen().startRound(roundNumber);
		} catch (Exception e) {
			Log.d("RemoteMethodListener/startRound", e.toString());
		}

		return 0;
	}

	public int updateGameplayTurn(Integer playerNumber) {

		try {
			//int playerNumber = JSONmethodcallParameters.getInt("playerNumber");
			game.getCurrentScreen().updateGameplayTurn(playerNumber);
		} catch (Exception e) {
			Log.d("RemoteMethodListener/updateGameplayTurn", e.toString());
		}

		return 0;
	}

	public Integer getPlayerThrowAngle() {
		int throwAngle = game.getCurrentScreen().getPlayerThrowAngle();
		//return throwAngle; 
		return Integer.valueOf(throwAngle);
	}

	public int showPlayerThrow(Integer playerNumber, Integer throwAngle) {

		try {
			//int playerNumber = JSONmethodcallParameters.getInt("playerNumber");
			//int throwAngle = JSONmethodcallParameters.getInt("throwAngle");
			game.getCurrentScreen().showPlayerThrow(playerNumber, throwAngle);
		} catch (Exception e) {
			Log.d("RemoteMethodListener/showPlayerThrow", e.toString());
		}

		return 0;
	}

	public Boolean isReady() {
		boolean isReady = game.getCurrentScreen().isReady();
		return Boolean.valueOf(isReady);
	}

	public static void setGame(Game androidGame) {
		game = androidGame;
	}

	
	
	
	
	
	
	
	
	
}
