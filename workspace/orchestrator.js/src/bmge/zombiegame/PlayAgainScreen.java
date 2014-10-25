package bmge.zombiegame;

import java.util.ArrayList;
import java.util.List;

import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Image;
import bmge.framework.Screen;
import bmge.framework.Input.TouchEvent;

public class PlayAgainScreen extends Screen {

	private enum SelectionState {
		FadeIn, ShowChoose, ShowSelections, FadeOutContinue, FadeOutExit
	}

	private SelectionState state = SelectionState.FadeIn;
	int alphaValue = 255;
	int alphaIncrement = 5;

	final int gamePlaceholderCenterX = Assets.gameAreaWidth / 2;
	final int gamePlaceholderCenterY = 140;

	int yesButtonTopLeftX = 510;
	int yesButtonTopLeftY = 420;
	int noButtonTopLeftX = 650;
	int noButtonTopLeftY = 420;

	int player0TopLeftX = 100;
	int player0TopLeftY = 50;
	int player1TopLeftX = 880;
	int player1TopLeftY = 50;
	int player2TopLeftX = 100;
	int player2TopLeftY = 450;
	int player3TopLeftX = 880;
	int player3TopLeftY = 450;

	int counterTopLeftX = 580;
	int counterTopLeftY = 610;

	long startTime = UNINITIALIZED;
	int chooseTime = 10;
	int secondsLeft = 10;

	double iconScale = 0.0;
	double scaleIncrement = 0.03;

	public PlayAgainScreen(Game game) {
		super(game);
		// These are already set to false in WinnerScreen
		thisPlayer.hasDecided = false;
		thisPlayer.wantsToContinue = false;
		otherPlayers = null;
		
		Assets.music_tickTack.setLooping(true);
		Assets.music_tickTack.setVolume(0.3f);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == SelectionState.FadeIn)
			updateFadeIn(touchEvents);
		else if (state == SelectionState.ShowChoose)
			updateShowChoose(touchEvents);
		else if (state == SelectionState.ShowSelections)
			updateShowSelections(touchEvents);
		else if (state == SelectionState.FadeOutContinue)
			updateFadeOutContinue(touchEvents);
		else if (state == SelectionState.FadeOutExit)
			updateFadeOutExit(touchEvents);

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
			state = SelectionState.ShowChoose;
			startTime = System.nanoTime();
		}
	}

	private void updateShowChoose(List<TouchEvent> touchEvents) {
		int secondsPassed = (int) ((System.nanoTime() - startTime) / 1000000000.000f);
		secondsLeft = chooseTime - secondsPassed;

		if (!Assets.music_tickTack.isPlaying()) {
			Assets.music_tickTack.play();
		}
		
		if (!thisPlayer.hasDecided) {

			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);

				if (event.type == TouchEvent.TOUCH_UP) {
					if (inBounds(event, yesButtonTopLeftX, yesButtonTopLeftY,
							Assets.buttonYesImg.getWidth(),
							Assets.buttonYesImg.getHeight())) {
						thisPlayer.hasDecided = true;
						thisPlayer.wantsToContinue = true;
						state = SelectionState.ShowSelections;
						Assets.sfx_plipPlop.play(0.8f);
						Assets.music_tickTack.stop();
					} else if (inBounds(event, noButtonTopLeftX,
							noButtonTopLeftY, Assets.buttonNoImg.getWidth(),
							Assets.buttonNoImg.getHeight())) {
						thisPlayer.hasDecided = true;
						thisPlayer.wantsToContinue = false;
						state = SelectionState.FadeOutExit;
						Assets.sfx_plip.play(0.8f);
						Assets.music_tickTack.stop();
					}
				}
			}

			if ((secondsLeft < 0)) {
				thisPlayer.hasDecided = true;
				thisPlayer.wantsToContinue = false;
				state = SelectionState.FadeOutExit;
				Assets.music_tickTack.stop();
			}
		}

	}

	private void updateShowSelections(List<TouchEvent> touchEvents) {

	}

	private void updateFadeOutContinue(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue < 255) {
			alphaValue += alphaIncrement;
		} else {
			alphaValue = 255;
			thisPlayer.placement = UNINITIALIZED;

			if (selectedGame == GAME_SKULL_THROW) {
				game.setScreen(new GameplaySkullThrowScreen(game));
			}
			// else if (selectedGame == GAME_SHUFFLE_BOARD) {
			// ...
			// }
		}
	}

	private void updateFadeOutExit(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue < 255) {
			alphaValue += alphaIncrement;
		} else {
			alphaValue = 255;
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawImage(Assets.playAgainScreenBg, 0, 0);

		Image gameIcon = null;

		if (selectedGame == GAME_SKULL_THROW) {
			gameIcon = Assets.gameIconSkullThrowSmallImg;
		} // else if (selectedGame == GAME_SHUFFLE_BOARD)...

		int widthChange = (int) ((double) (gameIcon.getWidth() / 2) * iconScale);
		int heightChange = (int) ((double) (gameIcon.getHeight() / 2) * iconScale);

		int topLeftIconX = gamePlaceholderCenterX - gameIcon.getWidth() / 2
				- widthChange;
		int topLeftIconY = gamePlaceholderCenterY - gameIcon.getHeight() / 2
				- heightChange;

		g.drawScaledImage(gameIcon, topLeftIconX, topLeftIconY,
				gameIcon.getWidth() + 2 * widthChange, gameIcon.getHeight() + 2
						* heightChange, 0, 0, gameIcon.getWidth(),
				gameIcon.getHeight());
		

		if (state == SelectionState.ShowChoose
				|| state == SelectionState.FadeIn) {
			if (!thisPlayer.hasDecided) {
				g.drawImage(Assets.buttonYesImg, yesButtonTopLeftX,
						yesButtonTopLeftY);
				g.drawImage(Assets.buttonNoImg, noButtonTopLeftX,
						noButtonTopLeftY);

				if (state == SelectionState.FadeIn) {
					g.drawImage(Assets.number10Img, counterTopLeftX,
							counterTopLeftY);
				}
			}

			if (state == SelectionState.ShowChoose) {
				if (secondsLeft == 10) {
					g.drawImage(Assets.number10Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 9) {
					g.drawImage(Assets.number9Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 8) {
					g.drawImage(Assets.number8Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 7) {
					g.drawImage(Assets.number7Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 6) {
					g.drawImage(Assets.number6Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 5) {
					g.drawImage(Assets.number5Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 4) {
					g.drawImage(Assets.number4Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 3) {
					g.drawImage(Assets.number3Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 2) {
					g.drawImage(Assets.number2Img, counterTopLeftX,
							counterTopLeftY);
				} else if (secondsLeft == 1) {
					g.drawImage(Assets.number1Img, counterTopLeftX,
							counterTopLeftY);
				}
			}
		} else if (state == SelectionState.ShowSelections
				|| state == SelectionState.FadeOutContinue
				|| state == SelectionState.FadeOutExit) {
			List<Player> allPlayers = new ArrayList<Player>();

			allPlayers.add(thisPlayer);
			if (otherPlayers != null) {
				for (int i = 0; i < otherPlayers.size(); ++i) {
					allPlayers.add(otherPlayers.get(i));
				}
			}

			for (int i = 0; i < allPlayers.size(); ++i) {
				Player player = allPlayers.get(i);

				if (player.wantsToContinue) {
					int playerFlagCornerX = 0;
					int playerFlagCornerY = 0;

					if (player.number == 0) {
						playerFlagCornerX = player0TopLeftX;
						playerFlagCornerY = player0TopLeftY;
					} else if (player.number == 1) {
						playerFlagCornerX = player1TopLeftX;
						playerFlagCornerY = player1TopLeftY;
					} else if (player.number == 2) {
						playerFlagCornerX = player2TopLeftX;
						playerFlagCornerY = player2TopLeftY;
					} else if (player.number == 3) {
						playerFlagCornerX = player3TopLeftX;
						playerFlagCornerY = player3TopLeftY;
					}

					if (player.country == COUNTRY_FIN) {
						g.drawImage(Assets.countryFlagWithHeadFinlandImg,
								playerFlagCornerX, playerFlagCornerY);
					} else if (player.country == COUNTRY_GER) {
						g.drawImage(Assets.countryFlagWithHeadGermanyImg,
								playerFlagCornerX, playerFlagCornerY);
					} else if (player.country == COUNTRY_JAP) {
						g.drawImage(Assets.countryFlagWithHeadJapanImg,
								playerFlagCornerX, playerFlagCornerY);
					} else if (player.country == COUNTRY_USA) {
						g.drawImage(Assets.countryFlagWithHeadUsaImg,
								playerFlagCornerX, playerFlagCornerY);
					} else if (player.country == COUNTRY_SWE) {
						g.drawImage(Assets.countryFlagWithHeadSwedenImg,
								playerFlagCornerX, playerFlagCornerY);
					}
				}
			}
		}

		if (state == SelectionState.FadeIn
				|| state == SelectionState.FadeOutExit) {
			g.drawARGB(alphaValue, 0, 0, 0);
		} else if (state == SelectionState.FadeOutContinue) {
			g.drawARGB(alphaValue, 255, 255, 255);
		}

	}

	@Override
	public void moveToGameplayScreen() {
		state = SelectionState.FadeOutContinue;
	}

	@Override
	public void moveToMainMenuScreen() {
		state = SelectionState.FadeOutExit;
	}
	
	@Override
	public void onResume() {
	}
	
	@Override
	public void onPause() {
	}
}
