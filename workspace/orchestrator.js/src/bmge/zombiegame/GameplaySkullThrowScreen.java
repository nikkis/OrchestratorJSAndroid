package bmge.zombiegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Image;
import bmge.framework.Screen;
import bmge.framework.Input.TouchEvent;

public class GameplaySkullThrowScreen extends Screen {
	private enum GameState {
		FadeIn, StartRound, Waiting, Playing, ShowThrow, FadeOut
	}

	GameState state = GameState.FadeIn; // Current game play state
	int alphaValue = 255;
	int alphaIncrement = 5;
	boolean isReady = true; // True when not showing a throw
	Player playerInTurn = null;
	final int placementHeadOffset = Assets.gameAreaWidth / 10;

	int roundNumber = 1;
	int lastThrowLength = UNINITIALIZED;
	float lastThrowAngle = UNINITIALIZED;
	float currentHandAngle = UNINITIALIZED;
	final float angleOffset = (float) 30.0;

	float skullX = UNINITIALIZED;
	float skullY = UNINITIALIZED;
	float skullSpeedX = UNINITIALIZED;
	float skullSpeedY = UNINITIALIZED;
	final float skullStartX = Assets.gameAreaWidth / 6;
	final float skullStartY = (Assets.gameAreaHeight * 3) / 4;
	final int skullStopHeight = (Assets.gameAreaHeight * 7) / 8;

	final float skullSpeedAddition = (float) 0.025;
	final float skullDropSpeed = (float) 0.4;
	float skullFlyRotationAngle = UNINITIALIZED;
	float skullFlyAngle = UNINITIALIZED;

	float emptyHandX = UNINITIALIZED;
	float emptyHandY = UNINITIALIZED;
	float emptyHandSpeedX = UNINITIALIZED;
	float emptyHandSpeedY = UNINITIALIZED;

	final float baseRotationSpeed = (float) 3.0;
	final float angleSpeedAddition = (float) 0.5;

	final int cornerCharacterBaseBottomLeftX = 200;
	final int cornerCharacterBaseBottomLeftY = skullStopHeight;
	final int fieldCharacterBaseBottomLeftX = cornerCharacterBaseBottomLeftX / 2;
	final int fieldCharacterBaseBottomLeftY = cornerCharacterBaseBottomLeftY
			+ (Assets.gameAreaHeight / 16);

	final int cornerBottomLeftBaseOffsetFinlandX = 140;
	final int cornerBottomLeftBaseOffsetFinlandY = 175;
	final int fieldBottomLeftBaseOffsetFinlandX = cornerBottomLeftBaseOffsetFinlandX / 2;
	final int fieldBottomLeftBaseOffsetFinlandY = cornerBottomLeftBaseOffsetFinlandY / 2;

	final int cornerBottomLeftBaseOffsetGermanyX = 50;
	final int cornerBottomLeftBaseOffsetGermanyY = 220;
	final int fieldBottomLeftBaseOffsetGermanyX = cornerBottomLeftBaseOffsetGermanyX / 2;
	final int fieldBottomLeftBaseOffsetGermanyY = cornerBottomLeftBaseOffsetGermanyY / 2;

	final int cornerBottomLeftBaseOffsetJapanX = 45;
	final int cornerBottomLeftBaseOffsetJapanY = 200;
	final int fieldBottomLeftBaseOffsetJapanX = cornerBottomLeftBaseOffsetJapanX / 2;
	final int fieldBottomLeftBaseOffsetJapanY = cornerBottomLeftBaseOffsetJapanY / 2;

	final int cornerBottomLeftBaseOffsetUsaX = 45;
	final int cornerBottomLeftBaseOffsetUsaY = 200;
	final int fieldBottomLeftBaseOffsetUsaX = cornerBottomLeftBaseOffsetUsaX / 2;
	final int fieldBottomLeftBaseOffsetUsaY = cornerBottomLeftBaseOffsetUsaY / 2;

	final int cornerBottomLeftBaseOffsetSwedenX = 125;
	final int cornerBottomLeftBaseOffsetSwedenY = 235;
	final int fieldBottomLeftBaseOffsetSwedenX = cornerBottomLeftBaseOffsetSwedenX / 2;
	final int fieldBottomLeftBaseOffsetSwedenY = cornerBottomLeftBaseOffsetSwedenY / 2;

	final int cornerBaseFinlandTopLeftX = cornerCharacterBaseBottomLeftX;
	final int cornerBaseFinlandTopLeftY = cornerCharacterBaseBottomLeftY
			- Assets.zombieBaseCornerFinlandImg.getHeight();
	int cornerSkullHandFinlandTopLeftX = cornerCharacterBaseBottomLeftX
			+ cornerBottomLeftBaseOffsetFinlandX
			- Assets.zombieSkullHandCornerFinlandImg.getWidth() / 2;
	int cornerSkullHandFinlandTopLeftY = cornerCharacterBaseBottomLeftY
			- cornerBottomLeftBaseOffsetFinlandY
			- Assets.zombieBaseCornerFinlandImg.getHeight() / 2;
	final int fieldBaseFinlandTopLeftX = fieldCharacterBaseBottomLeftX;
	final int fieldBaseFinlandTopLeftY = fieldCharacterBaseBottomLeftY
			- Assets.zombieBaseFieldFinlandImg.getHeight();
	int fieldSkullHandFinlandTopLeftX = fieldCharacterBaseBottomLeftX
			+ fieldBottomLeftBaseOffsetFinlandX
			- Assets.zombieSkullHandFieldFinlandImg.getWidth() / 2;
	int fieldSkullHandFinlandTopLeftY = fieldCharacterBaseBottomLeftY
			- fieldBottomLeftBaseOffsetFinlandY
			- Assets.zombieBaseFieldFinlandImg.getHeight() / 2;

	final int cornerBaseGermanyTopLeftX = cornerCharacterBaseBottomLeftX;
	final int cornerBaseGermanyTopLeftY = cornerCharacterBaseBottomLeftY
			- Assets.zombieBaseCornerGermanyImg.getHeight();
	int cornerSkullHandGermanyTopLeftX = cornerCharacterBaseBottomLeftX
			+ cornerBottomLeftBaseOffsetGermanyX
			- Assets.zombieSkullHandCornerGermanyImg.getWidth() / 2;
	int cornerSkullHandGermanyTopLeftY = cornerCharacterBaseBottomLeftY
			- cornerBottomLeftBaseOffsetGermanyY
			- Assets.zombieBaseCornerGermanyImg.getHeight() / 2;
	final int fieldBaseGermanyTopLeftX = fieldCharacterBaseBottomLeftX;
	final int fieldBaseGermanyTopLeftY = fieldCharacterBaseBottomLeftY
			- Assets.zombieBaseFieldGermanyImg.getHeight();
	int fieldSkullHandGermanyTopLeftX = fieldCharacterBaseBottomLeftX
			+ fieldBottomLeftBaseOffsetGermanyX
			- Assets.zombieSkullHandFieldGermanyImg.getWidth() / 2;
	int fieldSkullHandGermanyTopLeftY = fieldCharacterBaseBottomLeftY
			- fieldBottomLeftBaseOffsetGermanyY
			- Assets.zombieBaseFieldGermanyImg.getHeight() / 2;

	final int cornerBaseJapanTopLeftX = cornerCharacterBaseBottomLeftX;
	final int cornerBaseJapanTopLeftY = cornerCharacterBaseBottomLeftY
			- Assets.zombieBaseCornerJapanImg.getHeight();
	int cornerSkullHandJapanTopLeftX = cornerCharacterBaseBottomLeftX
			+ cornerBottomLeftBaseOffsetJapanX
			- Assets.zombieSkullHandCornerJapanImg.getWidth() / 2;
	int cornerSkullHandJapanTopLeftY = cornerCharacterBaseBottomLeftY
			- cornerBottomLeftBaseOffsetJapanY
			- Assets.zombieBaseCornerJapanImg.getHeight() / 2;
	final int fieldBaseJapanTopLeftX = fieldCharacterBaseBottomLeftX;
	final int fieldBaseJapanTopLeftY = fieldCharacterBaseBottomLeftY
			- Assets.zombieBaseFieldJapanImg.getHeight();
	int fieldSkullHandJapanTopLeftX = fieldCharacterBaseBottomLeftX
			+ fieldBottomLeftBaseOffsetJapanX
			- Assets.zombieSkullHandFieldJapanImg.getWidth() / 2;
	int fieldSkullHandJapanTopLeftY = fieldCharacterBaseBottomLeftY
			- fieldBottomLeftBaseOffsetJapanY
			- Assets.zombieBaseFieldJapanImg.getHeight() / 2;

	final int cornerBaseUsaTopLeftX = cornerCharacterBaseBottomLeftX;
	final int cornerBaseUsaTopLeftY = cornerCharacterBaseBottomLeftY
			- Assets.zombieBaseCornerUsaImg.getHeight();
	int cornerSkullHandUsaTopLeftX = cornerCharacterBaseBottomLeftX
			+ cornerBottomLeftBaseOffsetUsaX
			- Assets.zombieSkullHandCornerUsaImg.getWidth() / 2;
	int cornerSkullHandUsaTopLeftY = cornerCharacterBaseBottomLeftY
			- cornerBottomLeftBaseOffsetUsaY
			- Assets.zombieBaseCornerUsaImg.getHeight() / 2;
	final int fieldBaseUsaTopLeftX = fieldCharacterBaseBottomLeftX;
	final int fieldBaseUsaTopLeftY = fieldCharacterBaseBottomLeftY
			- Assets.zombieBaseFieldUsaImg.getHeight();
	int fieldSkullHandUsaTopLeftX = fieldCharacterBaseBottomLeftX
			+ fieldBottomLeftBaseOffsetUsaX
			- Assets.zombieSkullHandFieldUsaImg.getWidth() / 2;
	int fieldSkullHandUsaTopLeftY = fieldCharacterBaseBottomLeftY
			- fieldBottomLeftBaseOffsetUsaY
			- Assets.zombieBaseFieldUsaImg.getHeight() / 2;

	final int cornerBaseSwedenTopLeftX = cornerCharacterBaseBottomLeftX;
	final int cornerBaseSwedenTopLeftY = cornerCharacterBaseBottomLeftY
			- Assets.zombieBaseCornerSwedenImg.getHeight();
	int cornerSkullHandSwedenTopLeftX = cornerCharacterBaseBottomLeftX
			+ cornerBottomLeftBaseOffsetSwedenX
			- Assets.zombieSkullHandCornerSwedenImg.getWidth() / 2;
	int cornerSkullHandSwedenTopLeftY = cornerCharacterBaseBottomLeftY
			- cornerBottomLeftBaseOffsetSwedenY
			- Assets.zombieBaseCornerSwedenImg.getHeight() / 2;
	final int fieldBaseSwedenTopLeftX = fieldCharacterBaseBottomLeftX;
	final int fieldBaseSwedenTopLeftY = fieldCharacterBaseBottomLeftY
			- Assets.zombieBaseFieldSwedenImg.getHeight();
	int fieldSkullHandSwedenTopLeftX = fieldCharacterBaseBottomLeftX
			+ fieldBottomLeftBaseOffsetSwedenX
			- Assets.zombieSkullHandFieldSwedenImg.getWidth() / 2;
	int fieldSkullHandSwedenTopLeftY = fieldCharacterBaseBottomLeftY
			- fieldBottomLeftBaseOffsetSwedenY
			- Assets.zombieBaseFieldSwedenImg.getHeight() / 2;

	int bestThrow = 0;
	boolean firstThrowIsDone = false;
	boolean showGoodText = false;
	boolean showBadText = false;
	
	float volume_voices = 0.8f; 

	public GameplaySkullThrowScreen(Game game) {
		super(game);

		// Uninitialize every player's game related data
		thisPlayer.placementTargetHeadX = UNINITIALIZED;
		thisPlayer.placementCurrentHeadX = UNINITIALIZED;
		thisPlayer.placement = UNINITIALIZED;
		thisPlayer.longestThrow = UNINITIALIZED;
		thisPlayer.allThrows.clear();
		for (int i = 0; i < otherPlayers.size(); ++i) {
			otherPlayers.get(i).placementTargetHeadX = UNINITIALIZED;
			otherPlayers.get(i).placementCurrentHeadX = UNINITIALIZED;
			otherPlayers.get(i).placement = UNINITIALIZED;
			otherPlayers.get(i).longestThrow = UNINITIALIZED;
			otherPlayers.get(i).allThrows.clear();
		}

		// Stop main theme
		if (Assets.music_mainTheme.isPlaying()) {
			Assets.music_mainTheme.stop();
		}

		// Initialize music
		Assets.music_backBeat.setLooping(true);
		Assets.music_backBeat.setVolume(0.5f);
		Assets.sfx_rotatingHandLoop.setLooping(true);
		Assets.sfx_rotatingHandLoop.setVolume(0.2f);
		Assets.sfx_flyingBrainWhistle.setVolume(0.2f);

		if (!Assets.music_backBeat.isPlaying()) {
			Assets.music_backBeat.play();
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.FadeIn)
			updateFadeIn(touchEvents);
		else if (state == GameState.StartRound)
			updateStartRound(touchEvents);
		else if (state == GameState.Waiting)
			updateWaiting(touchEvents, deltaTime);
		else if (state == GameState.Playing)
			updatePlaying(touchEvents, deltaTime);
		else if (state == GameState.ShowThrow)
			updateShowThrow(touchEvents);
		else if (state == GameState.FadeOut)
			updateFadeOut(touchEvents);

		List<Player> allPlayers = new ArrayList<Player>();
		allPlayers.add(thisPlayer);
		for (int i = 0; i < otherPlayers.size(); ++i) {
			allPlayers.add(otherPlayers.get(i));
		}

		for (int i = 0; i < allPlayers.size(); ++i) {
			Player player = allPlayers.get(i);

			if (player.placementCurrentHeadX != UNINITIALIZED
					&& player.placementTargetHeadX != UNINITIALIZED) {
				if (player.placementCurrentHeadX < player.placementTargetHeadX) {
					player.placementCurrentHeadX += 2;
				} else if (player.placementCurrentHeadX > player.placementTargetHeadX) {
					player.placementCurrentHeadX -= 2;
				}
			}
		}
	}

	private void updateFadeIn(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue > 0) {
			alphaValue -= alphaIncrement;
		} else {
			alphaValue = 0;
			state = GameState.Waiting;
		}
	}

	private void updateStartRound(List<TouchEvent> touchEvents) {
		alphaValue = 0;
		touchEvents.clear();
	}

	private void updateWaiting(List<TouchEvent> touchEvents, float deltaTime) {
		alphaValue = 0;
		touchEvents.clear();
		updatePlayerPlacements(); // Always update current player placements
		isReady = true;

		float rotationSpeed = baseRotationSpeed + angleSpeedAddition
				* (float) roundNumber;

		currentHandAngle += rotationSpeed * deltaTime;
		currentHandAngle = currentHandAngle % (float) 360.0;

		if (playerInTurn != null) {
			// Start hand rotating sound
			if (!Assets.sfx_rotatingHandLoop.isPlaying()) {
				Assets.sfx_rotatingHandLoop.play();
			}
		}
	}

	private void updatePlaying(List<TouchEvent> touchEvents, float deltaTime) {
		// Start hand rotating sound
		if (!Assets.sfx_rotatingHandLoop.isPlaying()) {
			Assets.sfx_rotatingHandLoop.play();
		}

		float rotationSpeed = baseRotationSpeed + angleSpeedAddition
				* (float) roundNumber;

		currentHandAngle += rotationSpeed * deltaTime;
		currentHandAngle = currentHandAngle % (float) 360.0;

		// 1. All touch input is handled here:
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);

			if (event.type == TouchEvent.TOUCH_DOWN) {

				int throwAngleInt = (int) (currentHandAngle * 1000);
				lastThrowAngle = (float) (throwAngleInt / 1000.000);
				initThrow();

				// Stop hand rotating sound
				Assets.sfx_rotatingHandLoop.stop();
				
				// Start whistle sound
				Assets.sfx_flyingBrainWhistleSound.play(0.3f);

				// Show the player his throw immediately
				state = GameState.ShowThrow;
			}
		}
	}

	private void updateShowThrow(List<TouchEvent> touchEvents) {
		touchEvents.clear();
		isReady = false;

		if (emptyHandY < skullStopHeight) {
			emptyHandSpeedY += skullDropSpeed;
			emptyHandX += emptyHandSpeedX;
			emptyHandY += emptyHandSpeedY;
		}

		// Calculate throw projection
		if (skullY < skullStopHeight) {
			skullSpeedY += skullDropSpeed;
			skullX += skullSpeedX;
			skullY += skullSpeedY;
			skullFlyAngle += skullFlyRotationAngle;

			// Increase accuracy with 3 decimal numbers
			lastThrowLength = (int) (skullX * (float) 1000.000);
		}
		// Update throw in the data structure if the throw was
		// longer than previous one of the throwing player
		else {
			if (playerInTurn.number == thisPlayer.number) {
				if (lastThrowLength > bestThrow && firstThrowIsDone) {
					showGoodText = true;
					Assets.voice_zombieGood.play(volume_voices);
				}

				thisPlayer.allThrows.add(lastThrowLength);

				if (thisPlayer.longestThrow < lastThrowLength) {
					thisPlayer.longestThrow = lastThrowLength;
				}

			} else {
				for (int i = 0; i < otherPlayers.size(); ++i) {
					Player otherPlayer = otherPlayers.get(i);

					if (playerInTurn.number == otherPlayer.number) {
						otherPlayer.allThrows.add(lastThrowLength);
						if (otherPlayer.longestThrow < lastThrowLength) {
							otherPlayer.longestThrow = lastThrowLength;
						}
					}
				}
			}

			firstThrowIsDone = true;
			playerInTurn = null;
			state = GameState.Waiting;

			// Stop whistle sound
			Assets.sfx_flyingBrainWhistleSound.stop();

			// And play brain splash crackle sound
			Assets.sfx_wetTowel.play(0.5f);
		}
	}
 
	private void updateFadeOut(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue < 255) {
			alphaValue += alphaIncrement;
		} else {
			alphaValue = 255;
			game.setScreen(new WinnerScreen(game));
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		if (state == GameState.StartRound)
			drawRoundStart();
		else if (state == GameState.Waiting || state == GameState.FadeIn
				|| state == GameState.FadeOut)
			drawWaiting();
		else if (state == GameState.Playing)
			drawPlaying();
		else if (state == GameState.ShowThrow) {
			drawShowThrow();
		}

		drawPlayerPlacements(g, getAllPlayers());

		if (alphaValue > 0) {
			if (state == GameState.FadeIn) {
				g.drawARGB(alphaValue, 255, 255, 255);
			} else if (state == GameState.FadeOut) {
				g.drawARGB(alphaValue, 0, 0, 0);
			}
		}
	}

	private void drawRoundStart() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.skullThrowGameBg, 0, 0);

		int textX = screenCenterX - Assets.textRound1Img.getWidth() / 2;
		int textY = screenCenterY - Assets.gameAreaHeight / 8;

		if (roundNumber == 1) {
			g.drawImage(Assets.textRound1Img, textX, textY);
		} else if (roundNumber == 2) {
			g.drawImage(Assets.textRound2Img, textX, textY);
		} else if (roundNumber == 3) {
			g.drawImage(Assets.textRound3Img, textX, textY);
		}

		drawFieldSkulls(g, getAllPlayers());
	}

	private void drawWaiting() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.skullThrowGameBg, 0, 0);

		drawFieldSkulls(g, getAllPlayers());

		int textX = screenCenterX;
		int textY = screenCenterY / 2 - Assets.textThrowImg.getHeight() / 2;
		if (showGoodText) {
			g.drawImage(Assets.textGoodImg, textX, textY);
		} else if (showBadText) {
			g.drawImage(Assets.textBadImg, textX, textY);
		}

		if (playerInTurn == null)
			return;

		Image imageBase = null;
		Image imageSkullHand = null;
		int baseX = 0;
		int baseY = 0;
		int skullHandX = 0;
		int skullHandY = 0;

		if (playerInTurn.country == COUNTRY_FIN) {
			imageBase = Assets.zombieBaseFieldFinlandImg;
			imageSkullHand = Assets.zombieSkullHandFieldFinlandImg;
			baseX = fieldBaseFinlandTopLeftX;
			baseY = fieldBaseFinlandTopLeftY;
			skullHandX = fieldSkullHandFinlandTopLeftX;
			skullHandY = fieldSkullHandFinlandTopLeftY;
		} else if (playerInTurn.country == COUNTRY_GER) {
			imageBase = Assets.zombieBaseFieldGermanyImg;
			imageSkullHand = Assets.zombieSkullHandFieldGermanyImg;
			baseX = fieldBaseGermanyTopLeftX;
			baseY = fieldBaseGermanyTopLeftY;
			skullHandX = fieldSkullHandGermanyTopLeftX;
			skullHandY = fieldSkullHandGermanyTopLeftY;
		} else if (playerInTurn.country == COUNTRY_JAP) {
			imageBase = Assets.zombieBaseFieldJapanImg;
			imageSkullHand = Assets.zombieSkullHandFieldJapanImg;
			baseX = fieldBaseJapanTopLeftX;
			baseY = fieldBaseJapanTopLeftY;
			skullHandX = fieldSkullHandJapanTopLeftX;
			skullHandY = fieldSkullHandJapanTopLeftY;
		} else if (playerInTurn.country == COUNTRY_USA) {
			imageBase = Assets.zombieBaseFieldUsaImg;
			imageSkullHand = Assets.zombieSkullHandFieldUsaImg;
			baseX = fieldBaseUsaTopLeftX;
			baseY = fieldBaseUsaTopLeftY;
			skullHandX = fieldSkullHandUsaTopLeftX;
			skullHandY = fieldSkullHandUsaTopLeftY;
		} else if (playerInTurn.country == COUNTRY_SWE) {
			imageBase = Assets.zombieBaseFieldSwedenImg;
			imageSkullHand = Assets.zombieSkullHandFieldSwedenImg;
			baseX = fieldBaseSwedenTopLeftX;
			baseY = fieldBaseSwedenTopLeftY;
			skullHandX = fieldSkullHandSwedenTopLeftX;
			skullHandY = fieldSkullHandSwedenTopLeftY;
		}

		g.drawImage(imageBase, baseX, baseY);
		g.drawRotatingImage(imageSkullHand, skullHandX, skullHandY,
				currentHandAngle);

	}

	private void drawPlaying() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.skullThrowGameBgCorner, 0, 0);

		int textX = screenCenterX;
		int textY = screenCenterY - Assets.textThrowImg.getHeight() / 2;
		g.drawImage(Assets.textThrowImg, textX, textY);

		Image imageBase = null;
		Image imageSkullHand = null;
		int baseX = 0;
		int baseY = 0;
		int skullHandX = 0;
		int skullHandY = 0;

		if (playerInTurn.country == COUNTRY_FIN) {
			imageBase = Assets.zombieBaseCornerFinlandImg;
			imageSkullHand = Assets.zombieSkullHandCornerFinlandImg;
			baseX = cornerBaseFinlandTopLeftX;
			baseY = cornerBaseFinlandTopLeftY;
			skullHandX = cornerSkullHandFinlandTopLeftX;
			skullHandY = cornerSkullHandFinlandTopLeftY;
		} else if (playerInTurn.country == COUNTRY_GER) {
			imageBase = Assets.zombieBaseCornerGermanyImg;
			imageSkullHand = Assets.zombieSkullHandCornerGermanyImg;
			baseX = cornerBaseGermanyTopLeftX;
			baseY = cornerBaseGermanyTopLeftY;
			skullHandX = cornerSkullHandGermanyTopLeftX;
			skullHandY = cornerSkullHandGermanyTopLeftY;
		} else if (playerInTurn.country == COUNTRY_JAP) {
			imageBase = Assets.zombieBaseCornerJapanImg;
			imageSkullHand = Assets.zombieSkullHandCornerJapanImg;
			baseX = cornerBaseJapanTopLeftX;
			baseY = cornerBaseJapanTopLeftY;
			skullHandX = cornerSkullHandJapanTopLeftX;
			skullHandY = cornerSkullHandJapanTopLeftY;
		} else if (playerInTurn.country == COUNTRY_USA) {
			imageBase = Assets.zombieBaseCornerUsaImg;
			imageSkullHand = Assets.zombieSkullHandCornerUsaImg;
			baseX = cornerBaseUsaTopLeftX;
			baseY = cornerBaseUsaTopLeftY;
			skullHandX = cornerSkullHandUsaTopLeftX;
			skullHandY = cornerSkullHandUsaTopLeftY;
		} else if (playerInTurn.country == COUNTRY_SWE) {
			imageBase = Assets.zombieBaseCornerSwedenImg;
			imageSkullHand = Assets.zombieSkullHandCornerSwedenImg;
			baseX = cornerBaseSwedenTopLeftX;
			baseY = cornerBaseSwedenTopLeftY;
			skullHandX = cornerSkullHandSwedenTopLeftX;
			skullHandY = cornerSkullHandSwedenTopLeftY;
		}

		g.drawImage(imageBase, baseX, baseY);
		g.drawRotatingImage(imageSkullHand, skullHandX, skullHandY,
				currentHandAngle);

	}

	private void drawShowThrow() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.skullThrowGameBg, 0, 0);

		Image imageBase = null;
		Image imageEmptyHand = null;
		Image imageSkull = null;
		int baseX = 0;
		int baseY = 0;

		if (playerInTurn.country == COUNTRY_FIN) {
			imageBase = Assets.zombieBaseFieldFinlandImg;
			imageEmptyHand = Assets.zombieEmptyHandFieldFinlandImg;
			imageSkull = Assets.skullFinlandImg;
			baseX = fieldBaseFinlandTopLeftX;
			baseY = fieldBaseFinlandTopLeftY;
		} else if (playerInTurn.country == COUNTRY_GER) {
			imageBase = Assets.zombieBaseFieldGermanyImg;
			imageEmptyHand = Assets.zombieEmptyHandFieldGermanyImg;
			imageSkull = Assets.skullGermanyImg;
			baseX = fieldBaseGermanyTopLeftX;
			baseY = fieldBaseGermanyTopLeftY;
		} else if (playerInTurn.country == COUNTRY_JAP) {
			imageBase = Assets.zombieBaseFieldJapanImg;
			imageEmptyHand = Assets.zombieEmptyHandFieldJapanImg;
			imageSkull = Assets.skullJapanImg;
			baseX = fieldBaseJapanTopLeftX;
			baseY = fieldBaseJapanTopLeftY;
		} else if (playerInTurn.country == COUNTRY_USA) {
			imageBase = Assets.zombieBaseFieldUsaImg;
			imageEmptyHand = Assets.zombieEmptyHandFieldUsaImg;
			imageSkull = Assets.skullUsaImg;
			baseX = fieldBaseUsaTopLeftX;
			baseY = fieldBaseUsaTopLeftY;
		} else if (playerInTurn.country == COUNTRY_SWE) {
			imageBase = Assets.zombieBaseFieldSwedenImg;
			imageEmptyHand = Assets.zombieEmptyHandFieldSwedenImg;
			imageSkull = Assets.skullSwedenImg;
			baseX = fieldBaseSwedenTopLeftX;
			baseY = fieldBaseSwedenTopLeftY;
		}

		g.drawImage(imageBase, baseX, baseY);
		g.drawRotatingImage(imageSkull, (int) skullX, (int) skullY,
				skullFlyAngle);
		g.drawImage(imageEmptyHand, (int) emptyHandX, (int) emptyHandY);

		drawFieldSkulls(g, getAllPlayers());
	}

	private void updatePlayerPlacements() {
		List<Integer> throwLengths = new ArrayList<Integer>();
		throwLengths.add(thisPlayer.longestThrow);
		for (int i = 0; i < otherPlayers.size(); ++i) {
			throwLengths.add(otherPlayers.get(i).longestThrow);
		}

		// Sort so that the longest throw is first
		Collections.sort(throwLengths, Collections.reverseOrder());
		for (int i = 0; i < throwLengths.size(); ++i) {
			for (int j = 0; j < otherPlayers.size(); ++j) {
				int throwLength = throwLengths.get(i);
				Player otherPlayer = otherPlayers.get(j);

				if (throwLength > bestThrow) {
					bestThrow = throwLength;
				}

				// Give placement (1st, 2nd, 3rd or 4th)
				if (throwLength == otherPlayer.longestThrow) {
					otherPlayer.placement = i + 1;
					otherPlayer.placementTargetHeadX = Assets.gameAreaWidth
							- placementHeadOffset
							- ((otherPlayer.placement - 1) * placementHeadOffset);

					if (otherPlayer.placementCurrentHeadX == UNINITIALIZED) {
						otherPlayer.placementCurrentHeadX = Assets.gameAreaWidth
								- placementHeadOffset;
					}
				}
				if (throwLength == thisPlayer.longestThrow) {
					thisPlayer.placement = i + 1;
					thisPlayer.placementTargetHeadX = Assets.gameAreaWidth
							- placementHeadOffset
							- ((thisPlayer.placement - 1) * placementHeadOffset);

					if (thisPlayer.placementCurrentHeadX == UNINITIALIZED) {
						thisPlayer.placementCurrentHeadX = Assets.gameAreaWidth
								- placementHeadOffset;
					}
				}

			}
		}
	}

	private void initThrow() {

		skullFlyAngle = (float) 0.0;

		skullX = skullStartX;
		skullY = skullStartY;
		emptyHandX = skullStartX;
		emptyHandY = skullStartY;

		float throwAngle = (lastThrowAngle - angleOffset) % (float) 360.0;

		skullSpeedX = (float) Math.sin(Math.toRadians(throwAngle));
		skullSpeedY = -(float) Math.cos(Math.toRadians(throwAngle));

		if (skullSpeedX < (float) 0.0) {
			skullSpeedX = (float) 4.0
					+ ((float) playerInTurn.country / (float) 10.0);
			skullSpeedY = (float) -3.0;
			skullFlyRotationAngle = (float) 10.0;

			emptyHandSpeedX = (float) 0.0;
			emptyHandSpeedY = (float) -3.0;

			if (playerInTurn.number == thisPlayer.number) {
				showBadText = true;
				Assets.voice_zombieBad.play(volume_voices);
			}
		} else {
			float bonusSpeed = (float) 1.0 + skullSpeedAddition
					* (float) roundNumber;
			skullSpeedX = skullSpeedX * (float) 15.0 * bonusSpeed;
			skullSpeedY = skullSpeedY * (float) 20.0 * bonusSpeed;
			skullFlyRotationAngle = skullSpeedX;

			emptyHandSpeedX = skullSpeedX / (float) 2.0;
			emptyHandSpeedY = skullSpeedY / (float) 3.0;
		}
	}

	void drawPlayerPlacements(Graphics g, List<Player> allPlayers) {
		// Draw all player placements on top of the screen
		for (int i = 0; i < allPlayers.size(); ++i) {
			Player player = allPlayers.get(i);

			// Draw player's position on the top of the screen
			if (player.allThrows.size() > 0) {
				if (player.country == COUNTRY_FIN) {
					g.drawImage(Assets.zombieHeadSmallFinlandImg,
							player.placementCurrentHeadX, 0);
				} else if (player.country == COUNTRY_GER) {
					g.drawImage(Assets.zombieHeadSmallGermanyImg,
							player.placementCurrentHeadX, 0);
				} else if (player.country == COUNTRY_JAP) {
					g.drawImage(Assets.zombieHeadSmallJapanImg,
							player.placementCurrentHeadX, 0);
				} else if (player.country == COUNTRY_USA) {
					g.drawImage(Assets.zombieHeadSmallUsaImg,
							player.placementCurrentHeadX, 0);
				} else if (player.country == COUNTRY_SWE) {
					g.drawImage(Assets.zombieHeadSmallSwedenImg,
							player.placementCurrentHeadX, 0);
				}
			}
		}
	}

	void drawFieldSkulls(Graphics g, List<Player> allPlayers) {
		for (int countryNumber = COUNTRY_SWE; countryNumber >= 0; --countryNumber) {
			for (int i = 0; i < allPlayers.size(); ++i) {
				Player player = allPlayers.get(i);

				if (player.country == countryNumber) {
					for (int t = 0; t < player.allThrows.size(); ++t) {
						int throwX = player.allThrows.get(t) / 1000;
						int throwY = skullStopHeight - player.country
								* Assets.gameAreaHeight / 64 - 43;

						if (player.country == COUNTRY_FIN) {
							g.drawImage(Assets.flagSkullFinlandImg, throwX,
									throwY);
						} else if (player.country == COUNTRY_GER) {
							g.drawImage(Assets.flagSkullGermanyImg, throwX,
									throwY);
						} else if (player.country == COUNTRY_JAP) {
							g.drawImage(Assets.flagSkullJapanImg, throwX,
									throwY);
						} else if (player.country == COUNTRY_USA) {
							g.drawImage(Assets.flagSkullUsaImg, throwX, throwY);
						} else if (player.country == COUNTRY_SWE) {
							g.drawImage(Assets.flagSkullSwedenImg, throwX,
									throwY);
						}
					}
				}
			}
		}
	}

	@Override
	public void startRound(int roundNumber) {
		this.roundNumber = roundNumber;
		state = GameState.StartRound;
		
		if (roundNumber == 1) {
			Assets.voice_zombieRound1.play(volume_voices);
		} else if (roundNumber == 2) {
			Assets.voice_zombieRound2.play(volume_voices);
		} else if (roundNumber == 3) {
			Assets.voice_zombieRound3.play(volume_voices);
		}
		
	}

	@Override
	public void updateGameplayTurn(int playerNumber) {
		lastThrowLength = UNINITIALIZED;
		lastThrowAngle = UNINITIALIZED;
		showGoodText = false;
		showBadText = false;
		
		if (thisPlayer.number == playerNumber) {
			state = GameState.Playing;
			playerInTurn = thisPlayer;
			currentHandAngle = 0;
			
			Assets.voice_zombieThrow.play(volume_voices);
			
		} else {
			state = GameState.Waiting;

			for (int i = 0; i < otherPlayers.size(); ++i) {
				Player otherPlayer = otherPlayers.get(i);
				if (otherPlayer.number == playerNumber) {
					playerInTurn = otherPlayer;
				}
			}
		}
	}

	@Override
	public int getPlayerThrowAngle() {
		if (lastThrowAngle < 0)
			return UNINITIALIZED;
		else
			return (int) (lastThrowAngle * 1000);
	}

	// This updates other clients information of the last throw.
	// Ignore this call if this client did the last throw: it has
	// already been shown or it is currently showing the throw
	@Override
	public void showPlayerThrow(int playerNumber, int throwAngleInt) {
		if (thisPlayer.number != playerNumber) {
			lastThrowAngle = (float) (throwAngleInt / 1000.000);
			initThrow();
			
			// Stop hand rotating sound
			Assets.sfx_rotatingHandLoop.stop();
			
			// Start whistle sound
			Assets.sfx_flyingBrainWhistleSound.play(0.5f);
			
			state = GameState.ShowThrow;
		}
	}

	@Override
	public boolean isReady() {
		return isReady;
	}

	@Override
	public void moveToWinnerScreen() {
		// Uninitialize selectedGame here because server might ask it
		// instantly after the this method call and we might still
		// be in this screen when that asking comes
		selectedGame = UNINITIALIZED;
		state = GameState.FadeOut;
		
		if (Assets.music_backBeat.isPlaying()) {
			Assets.music_backBeat.stop();
		}
		if (Assets.sfx_rotatingHandLoop.isPlaying()) {
			Assets.sfx_rotatingHandLoop.stop();
		}
	}
	
	@Override
	public void onResume() {
		if (Assets.music_backBeat != null && Assets.music_backBeat.isStopped()) {
			Assets.music_backBeat.play();
		}
		if (Assets.sfx_rotatingHandLoop != null && Assets.sfx_rotatingHandLoop.isStopped()) {
			Assets.sfx_rotatingHandLoop.play();
		}
	}
	
	@Override
	public void onPause() {
		if (Assets.music_backBeat != null && Assets.music_backBeat.isPlaying()) {
			Assets.music_backBeat.pause();
		}
		if (Assets.sfx_rotatingHandLoop != null && Assets.sfx_rotatingHandLoop.isPlaying()) {
			Assets.sfx_rotatingHandLoop.pause();
		}
	}

}