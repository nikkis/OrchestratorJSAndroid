package bmge.framework;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import bmge.framework.Input.TouchEvent;
import bmge.framework.implementation.AndroidGame;
import bmge.zombiegame.*;

import android.app.Activity;
import android.util.Log;

public abstract class Screen {

	protected final Game game;

	public Screen(Game game) {
		this.game = game;
	}

	public abstract void update(float deltaTime);

	public abstract void paint(float deltaTime);
	
	
	/********************************************************/
	/** INFORMATION FOR APOCALYMPICS MINI GAMES IN GENERAL **/
	/********************************************************/
	
	public final static int UNINITIALIZED = -1;
	public final static int ERROR = -2;
	
	public final static int COUNTRY_FIN = 0;
	public final static int COUNTRY_GER = 1;
	public final static int COUNTRY_JAP = 2;
	public final static int COUNTRY_USA = 3;
	public final static int COUNTRY_SWE = 4;
	
	public final static int GAME_SKULL_THROW = 0;
	
	protected final int screenCenterX = Assets.gameAreaWidth / 2;
	protected final int screenCenterY = Assets.gameAreaHeight / 2;
	
	protected static boolean isInCountrySelectionScreen = false;
	protected static int selectedGame = UNINITIALIZED;
	
	public static class Player {
		public int number;
		public int country;
		public boolean isAdmin;
		public int placement;
		public int placementTargetHeadX;
		public int placementCurrentHeadX;
		public boolean wantsToContinue;
		public boolean hasDecided;
		
		public int longestThrow;
		public List<Integer> allThrows;
	}
	
	protected static Player thisPlayer = null;
	protected static List<Player> otherPlayers = null;
	
	
	protected boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}
	
	protected boolean inBoundsCircle(TouchEvent event, int x, int y, int radius) {
		int distanceX = x - event.x;
		int distanceY = y - event.y;
		int distanceFromFlagCenter = (int)Math.sqrt(distanceX*distanceX + distanceY*distanceY);
		
		if (distanceFromFlagCenter < radius)
			return true;
		else
			return false;
	}
	
	protected List<Player> getAllPlayers() {
		List<Player> allPlayers = new ArrayList<Player>();
		allPlayers.add(thisPlayer);
		if (otherPlayers != null) {
			for (int i = 0; i < otherPlayers.size(); ++i) {
				allPlayers.add(otherPlayers.get(i));
			}
		}
		
		return allPlayers;
	}
	
	public abstract void onResume();
	public abstract void onPause();
	
	/***********************************************************/
	/** REMOTE METHODS TO BE CALLED FROM RemoteMethodListener **/
	/** These methods are general methods
	/***********************************************************/
	
	private static RemoteMethodListener listener;
	public void setListener(RemoteMethodListener listenerArg) {
		listener = listenerArg;
	}
	
	public static RemoteMethodListener getListenerInstance() {
		return listener;
	}
	
	public void setInitialInfo(int playerNumber) {
		thisPlayer = new Player();
		thisPlayer.number = playerNumber;
		thisPlayer.allThrows = new ArrayList<Integer>();
		thisPlayer.country = UNINITIALIZED;
		thisPlayer.wantsToContinue = false;
		selectedGame = UNINITIALIZED;

	}
	
	public void makeAdminPlayer() {
		thisPlayer.isAdmin = true;
	}
	
	public void makeNormalPlayer() {
		thisPlayer.isAdmin = false;
	}
	
	public boolean isInCountrySelectionScreen() {
		return isInCountrySelectionScreen;
	}
	
	// Implemented in CountrySelectionScreen
	public void giveCountrySelectionTurn() {
	}
	
	public int getCountrySelection() {
		return thisPlayer.country;
	}
	
	public void updateInGamePlayers(List<Player> inGamePlayers) {
		List<Player> otherPlayersTemp = new ArrayList<Player>();
		for (int i = 0; i < inGamePlayers.size(); ++i) {
			Player player = inGamePlayers.get(i);
			
			if (player.number != thisPlayer.number) {
				otherPlayersTemp.add(player);
			}
		}
		otherPlayers = otherPlayersTemp;
	}
	
	// Implemented in CountrySelectionScreen
	public void moveToGameSelectionScreen() {
		game.setScreen(new GameSelectionScreen(game));
	}
	
	public int getGameSelectionFromAdmin() {
		if (thisPlayer.isAdmin) {
			return selectedGame;
		}
		return ERROR;
	}
	
	public void updateGameSelection(int selectedGameArg) {
		selectedGame = selectedGameArg;
	}
	
	// Implemented in GameSelectionScreen
	public void moveToGameplayScreen() {
		thisPlayer.placement = UNINITIALIZED;
		
		if (selectedGame == GAME_SKULL_THROW) {
			game.setScreen(new GameplaySkullThrowScreen(game));
		}
		//else if (selectedGame == GAME_SHUFFLE_BOARD) {
		//	 ...
		//}
	} 
	
	// Implemented in every game play screen
	public void moveToWinnerScreen() {
		game.setScreen(new WinnerScreen(game));
	} 
	
	public int getThisPlayerPlacement() {
		return thisPlayer.placement;
	}
	
	// Implemented in WinnerScreen
	public void moveToPlayAgainScreen() {
		game.setScreen(new PlayAgainScreen(game));
	}
	
	public boolean hasDecided() {
		return thisPlayer.hasDecided;
	}
	
	public boolean wantsToContinue() {
		return thisPlayer.wantsToContinue;
	}
	
	// Implemented in PlayAgainScreen
	public void moveToMainMenuScreen() {
		game.setScreen(new MainMenuScreen(game));
	}
	
	/*********************************************/
	/** SKULL THROW GAME SPECIFIC METHODS,
	/** IMPLEMENTED IN GameplaySkullThrowScreen
	/*********************************************/

	public void startRound (int roundNumber) {
	}
	
	public void updateGameplayTurn(int playerNumber) {
	}
	
	public int getPlayerThrowAngle() {
		return 0;
	}
	
	public void showPlayerThrow(int playerNumber, int throwAngleInt) {
	}
	
	public boolean isReady() {
		return false;
	}
	
	
}
