package bmge.zombiegame;

import java.util.List;

import android.graphics.Color;

import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Image;
import bmge.framework.Screen;
import bmge.framework.Input.TouchEvent;

public class WinnerScreen extends Screen {

	private enum SelectionState {
		FadeIn, ShowPodium, FadeOut
	}

	private SelectionState state = SelectionState.FadeIn;
	int alphaValue = 255;
	int alphaIncrement = 5;

	final int gameIconsCenterY = 80;
	final int gameIconsWidth = 100;

	final int gameIconSkullThrowSmallX = 140;
	final int gameIconShuffleBoardSmallX = 440;
	final int gameIconPatternGymnasticSmallX = 740;
	final int gameIconBallBalancingSmallX = 1040;

	final int gameIconSkullThrowCenterX = gameIconSkullThrowSmallX
			+ gameIconsWidth / 2;
	final int gameIconShuffleBoardCenterX = gameIconShuffleBoardSmallX
			+ gameIconsWidth / 2;
	final int gameIconPatternGymnasticCenterX = gameIconPatternGymnasticSmallX
			+ gameIconsWidth / 2;
	final int gameIconBallBalancingCenterX = gameIconBallBalancingSmallX
			+ gameIconsWidth / 2;

	final int podium4BottomCenterX = 250;
	final int podium4BottomCenterY = 690;
	final int podium2BottomCenterX = 520;
	final int podium2BottomCenterY = 600;
	final int podium1BottomCenterX = 800;
	final int podium1BottomCenterY = 560;
	final int podium3BottomCenterX = 1070;
	final int podium3BottomCenterY = 670;

	double iconScale = 0.0;
	double scaleIncrement = 0.03;

	public WinnerScreen(Game game) {
		super(game);
		// Stop music
		if (Assets.music_backBeat.isPlaying()) {
			Assets.music_backBeat.stop();
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == SelectionState.FadeIn)
			updateFadeIn(touchEvents);
		else if (state == SelectionState.ShowPodium)
			updateShowPodium(touchEvents);
		else if (state == SelectionState.FadeOut)
			updateFadeOut(touchEvents);

		if (iconScale > 0.3) {
			scaleIncrement = -0.01 * deltaTime;
		} else if (iconScale < 0.0) {
			scaleIncrement = 0.01 * deltaTime;
		}

		iconScale += scaleIncrement;
	}

	private void updateFadeIn(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue > 0) {
			alphaValue -= alphaIncrement;
		} else {
			alphaValue = 0;
			state = SelectionState.ShowPodium;
			
			if (thisPlayer.placement == 1) {
				Assets.voice_zombieGood.play(0.5f);
				//Assets.voice_zombieGoodZombie.play(0.5f);
			} else {
				Assets.voice_zombieBad.play(0.5f);
				//Assets.voice_zombieBadZombie.play(0.5f);
			}
		}
	}

	private void updateShowPodium(List<TouchEvent> touchEvents) {
		alphaValue = 0;

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);

			if (event.type == TouchEvent.TOUCH_UP) {

				if (thisPlayer.isAdmin && selectedGame == UNINITIALIZED) {
					if (inBounds(event, gameIconSkullThrowSmallX, 0,
							Assets.gameIconSkullThrowSmallImg.getWidth(),
							Assets.gameIconSkullThrowSmallImg.getHeight())) {
						selectedGame = GAME_SKULL_THROW;
						Assets.sfx_plipPlop.play(0.8f);
					}
					// else if (inBounds of shuffleboard)
				}
			}
		}
	}

	private void updateFadeOut(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue < 255) {
			alphaValue += alphaIncrement;
		} else {
			alphaValue = 255;
			game.setScreen(new PlayAgainScreen(game));
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.winnerScreenBg, 0, 0);

		List<Player> allPlayers = getAllPlayers();

		for (int place = allPlayers.size(); place >= 1; --place) {
			for (int j = 0; j < allPlayers.size(); ++j) {
				Player player = allPlayers.get(j);
				int drawPosX = 0;
				int drawPosY = 0;

				if (player.placement == 1 && place == 1) {
					drawPosX = podium1BottomCenterX;
					drawPosY = podium1BottomCenterY;
				} else if (player.placement == 2 && place == 2) {
					drawPosX = podium2BottomCenterX;
					drawPosY = podium2BottomCenterY;
				} else if (player.placement == 3 && place == 3) {
					drawPosX = podium3BottomCenterX;
					drawPosY = podium3BottomCenterY;
				} else if (player.placement == 4 && place == 4) {
					drawPosX = podium4BottomCenterX;
					drawPosY = podium4BottomCenterY;
				}

				if (player.country == COUNTRY_FIN) {
					g.drawImage(Assets.nationFinlandImg, drawPosX
							- Assets.nationFinlandImg.getWidth() / 2, drawPosY
							- Assets.nationFinlandImg.getHeight());
				} else if (player.country == COUNTRY_GER) {
					g.drawImage(Assets.nationGermanyImg, drawPosX
							- Assets.nationGermanyImg.getWidth() / 2, drawPosY
							- Assets.nationGermanyImg.getHeight());
				} else if (player.country == COUNTRY_JAP) {
					g.drawImage(Assets.nationJapanImg, drawPosX
							- Assets.nationJapanImg.getWidth() / 2, drawPosY
							- Assets.nationJapanImg.getHeight());
				} else if (player.country == COUNTRY_USA) {
					g.drawImage(Assets.nationUsaImg, drawPosX
							- Assets.nationUsaImg.getWidth() / 2, drawPosY
							- Assets.nationUsaImg.getHeight());
				} else if (player.country == COUNTRY_SWE) {
					g.drawImage(Assets.nationSwedenImg, drawPosX
							- Assets.nationSwedenImg.getWidth() / 2, drawPosY
							- Assets.nationSwedenImg.getHeight());
				}
			}
		}

		g.drawRect(0, 0, Assets.gameAreaWidth + 1, 163, Color.BLACK);

		Image greyCircle = Assets.greyCircleSmall;
		g.drawImage(greyCircle, gameIconSkullThrowCenterX
				- greyCircle.getWidth() / 2, 0);
		g.drawImage(greyCircle, gameIconShuffleBoardCenterX
				- greyCircle.getWidth() / 2, 0);
		g.drawImage(greyCircle, gameIconPatternGymnasticCenterX
				- greyCircle.getWidth() / 2, 0);
		g.drawImage(greyCircle, gameIconBallBalancingCenterX
				- greyCircle.getWidth() / 2, 0);
		
		if (thisPlayer.isAdmin) {
			if (selectedGame == UNINITIALIZED) {
				Image gameIcon = Assets.gameIconSkullThrowSmallImg;
				int widthChange = (int) ((double) (gameIcon.getWidth() / 2) * iconScale);
				int heightChange = (int) ((double) (gameIcon.getHeight() / 2) * iconScale);

				int topLeftSkullThrowX = gameIconSkullThrowCenterX
						- gameIcon.getWidth() / 2 - widthChange;
				int topLeftSkullThrowY = gameIconsCenterY
						- gameIcon.getHeight() / 2 - heightChange;

				g.drawScaledImage(gameIcon, topLeftSkullThrowX,
						topLeftSkullThrowY, gameIcon.getWidth() + 2
								* widthChange, gameIcon.getHeight() + 2
								* heightChange, 0, 0, gameIcon.getWidth(),
						gameIcon.getHeight());
				
				gameIcon = Assets.gameIconShuffleBoardMutedSmallImg;
				g.drawImage(gameIcon, gameIconShuffleBoardSmallX,
						gameIconsCenterY - gameIcon.getHeight() / 2);

				gameIcon = Assets.gameIconPatternGymnasticMutedSmallImg;
				g.drawImage(gameIcon, gameIconPatternGymnasticSmallX,
						gameIconsCenterY - gameIcon.getHeight() / 2);

				gameIcon = Assets.gameIconBallBalancingMutedSmallImg;
				g.drawImage(gameIcon, gameIconBallBalancingSmallX,
						gameIconsCenterY - gameIcon.getHeight() / 2);
			} else {
				int topLeftX = gameIconSkullThrowCenterX;
				int topLeftY = gameIconsCenterY;

				if (selectedGame == GAME_SKULL_THROW) {
					topLeftX -= Assets.gameIconSkullThrowSmallImg.getWidth() / 2;
					topLeftY -= Assets.gameIconSkullThrowSmallImg.getHeight() / 2;

					g.drawImage(greyCircle, gameIconSkullThrowCenterX
							- greyCircle.getWidth() / 2, 0);
					g.drawImage(Assets.gameIconSkullThrowSmallImg, topLeftX,
							topLeftY);
				}
				// TODO change UNINITIALIZED to correct GAME_... when
				// they get ready
				/*
				 * if (selectedGame != UNINITIALIZED) {
				 * g.drawImage(Assets.gameIconShuffleBoardMutedSmallImg,
				 * gameIconShuffleBoardSmallX, 0); } if (selectedGame !=
				 * UNINITIALIZED) {
				 * g.drawImage(Assets.gameIconPatternGymnasticMutedSmallImg,
				 * gameIconPatternGymnasticSmallX, 0); } if (selectedGame !=
				 * UNINITIALIZED) {
				 * g.drawImage(Assets.gameIconBallBalancingMutedSmallImg,
				 * gameIconBallBalancingSmallX, 0); }
				 */
			}

		} else {
			Image gameIcon = Assets.gameIconSkullThrowMutedSmallImg;
			g.drawImage(gameIcon, gameIconSkullThrowSmallX, gameIconsCenterY
					- gameIcon.getHeight() / 2);

			gameIcon = Assets.gameIconShuffleBoardMutedSmallImg;
			g.drawImage(gameIcon, gameIconShuffleBoardSmallX, gameIconsCenterY
					- gameIcon.getHeight() / 2);

			gameIcon = Assets.gameIconPatternGymnasticMutedSmallImg;
			g.drawImage(gameIcon, gameIconPatternGymnasticSmallX,
					gameIconsCenterY - gameIcon.getHeight() / 2);

			gameIcon = Assets.gameIconBallBalancingMutedSmallImg;
			g.drawImage(gameIcon, gameIconBallBalancingSmallX, gameIconsCenterY
					- gameIcon.getHeight() / 2);

		}

		g.drawARGB(alphaValue, 0, 0, 0);
	}

	@Override
	public void moveToPlayAgainScreen() {
		// Set these already here, so that the server doesn't
		// get old true values too quickly when it polls them
		thisPlayer.hasDecided = false;
		thisPlayer.wantsToContinue = false;

		state = SelectionState.FadeOut;
	}
	
	@Override
	public void onResume() {
	}
	
	@Override
	public void onPause() {
	}
}
