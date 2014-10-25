package bmge.zombiegame;

import java.util.List;

import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Image;
import bmge.framework.Input.TouchEvent;
import bmge.framework.Screen;

public class GameSelectionScreen extends Screen {

	private enum SelectionState {
		FadeIn, WaitingGameSelection, ShowSelected, FadeOut
	}

	private SelectionState state = SelectionState.FadeIn;
	int alphaValue = 255;
	int alphaIncrement = 5;

	final int gameIconSkullThrowCenterX = 190;
	final int gameIconShuffleBoardCenterX = 440;
	final int gameIconPatternGymnasticCenterX = 740;
	final int gameIconBallBalancingCenterX = 1090;

	double iconScale = 0.0;
	double scaleIncrement = 0.01;

	public GameSelectionScreen(Game game) {
		super(game);
		selectedGame = UNINITIALIZED;
		// Play main theme if stopped
		if ( Assets.music_mainTheme.isStopped() ) {
			Assets.music_mainTheme.play();
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == SelectionState.FadeIn)
			updateFadeIn(touchEvents);
		else if (state == SelectionState.WaitingGameSelection)
			updateWaitingGameSelection(touchEvents);
		else if (state == SelectionState.ShowSelected)
			updateShowSelected(touchEvents);
		else if (state == SelectionState.FadeOut)
			updateFadeOut(touchEvents);

		if (iconScale > 0.15) {
			scaleIncrement = -0.005 * deltaTime;
		} else if (iconScale < 0.0) {
			scaleIncrement = 0.005 * deltaTime;
		}

		iconScale += scaleIncrement;
	}

	private void updateFadeIn(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue > 0) {
			alphaValue -= alphaIncrement;
		} else {
			alphaValue = 0;
			state = SelectionState.WaitingGameSelection;
			
			if (thisPlayer.isAdmin) {
				Assets.voice_zombieChoose.play(0.5f);
			}
		}
	}

	private void updateWaitingGameSelection(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);

			if (event.type == TouchEvent.TOUCH_UP) {

				if (thisPlayer.isAdmin && selectedGame == UNINITIALIZED) {

					Image gameIcon = Assets.gameIconSkullThrowImg;
					int skullThrowTopLeftX = gameIconSkullThrowCenterX
							- gameIcon.getWidth() / 2;
					int skullThrowTopLeftY = screenCenterY
							- gameIcon.getHeight() / 2;

					if (inBounds(event, skullThrowTopLeftX, skullThrowTopLeftY,
							Assets.gameIconSkullThrowImg.getWidth(),
							Assets.gameIconSkullThrowImg.getHeight())) {
						
							selectedGame = GAME_SKULL_THROW;
							Assets.sfx_plipPlop.play(0.8f);
					}
					
				}
			}
		}

		if (selectedGame != UNINITIALIZED) {
			state = SelectionState.ShowSelected;
		}
	}

	private void updateShowSelected(List<TouchEvent> touchEvents) {
		touchEvents.clear();
	}

	private void updateFadeOut(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue < 255) {
			alphaValue += alphaIncrement;
		} else {
			alphaValue = 255;
			thisPlayer.placement = UNINITIALIZED;

			if (selectedGame == GAME_SKULL_THROW) {
				game.setScreen(new GameplaySkullThrowScreen(game));
				// Stop music
				if ( Assets.music_mainTheme.isPlaying() ) {
					Assets.music_mainTheme.stop();
				}
				
			}
			// else if (selectedGame == GAME_SHUFFLE_BOARD) {
			// ...
			// }
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.menuScreenBg, 0, 0);

		Image gameIcon = null;
		int x = 0;
		int y = 0;

		if (state == SelectionState.FadeIn
				|| state == SelectionState.WaitingGameSelection) {

			if (thisPlayer.isAdmin) {
				Image textImg = Assets.chooseGameTextImg;
				g.drawImage(textImg, screenCenterX - textImg.getWidth() / 2,
						Assets.gameAreaHeight - 2 * textImg.getHeight());

				gameIcon = Assets.gameIconSkullThrowImg;
				int widthChange = (int) ((double) (gameIcon.getWidth() / 2) * iconScale);
				int heightChange = (int) ((double) (gameIcon.getHeight() / 2) * iconScale);

				int topLeftX = gameIconSkullThrowCenterX - gameIcon.getWidth()
						/ 2 - widthChange;
				int topLeftY = screenCenterY - gameIcon.getHeight() / 2
						- heightChange;

				g.drawScaledImage(gameIcon, topLeftX, topLeftY,
						gameIcon.getWidth() + 2 * widthChange,
						gameIcon.getHeight() + 2 * heightChange, 0, 0,
						gameIcon.getWidth(), gameIcon.getHeight());

				gameIcon = Assets.gameIconShuffleBoardMutedImg;
				x = gameIconShuffleBoardCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);

				gameIcon = Assets.gameIconPatternGymnasticMutedImg;
				x = gameIconPatternGymnasticCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);

				gameIcon = Assets.gameIconBallBalancingMutedImg;
				x = gameIconBallBalancingCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);

			} else {
				gameIcon = Assets.gameIconSkullThrowMutedImg;
				x = gameIconSkullThrowCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);

				gameIcon = Assets.gameIconShuffleBoardMutedImg;
				x = gameIconShuffleBoardCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);

				gameIcon = Assets.gameIconPatternGymnasticMutedImg;
				x = gameIconPatternGymnasticCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);

				gameIcon = Assets.gameIconBallBalancingMutedImg;
				x = gameIconBallBalancingCenterX - gameIcon.getWidth() / 2;
				y = screenCenterY - gameIcon.getHeight() / 2;

				g.drawImage(gameIcon, x, y);
			}

		} else if (state == SelectionState.ShowSelected
				|| state == SelectionState.FadeOut) {
			if (selectedGame == GAME_SKULL_THROW) {
				gameIcon = Assets.gameIconSkullThrowImg;
				x = screenCenterX - gameIcon.getWidth();
				y = screenCenterY - gameIcon.getHeight();

				g.drawScaledImage(gameIcon, x, y, gameIcon.getWidth() * 2,
						gameIcon.getHeight() * 2, 0, 0, gameIcon.getWidth(),
						gameIcon.getHeight());

			}
			// else if (selectedGame == GAME_SHUFFLE_BOARD) {
			// ...
			// }
		}

		if (state == SelectionState.FadeIn) {
			g.drawARGB(alphaValue, 0, 0, 0);
		} else if (state == SelectionState.FadeOut) {
			g.drawARGB(alphaValue, 255, 255, 255);
		}
	}

	@Override
	public void moveToGameplayScreen() {
		state = SelectionState.FadeOut;
	}
	
	@Override
	public void onResume() {
		if (Assets.music_mainTheme != null && Assets.music_mainTheme.isStopped() ) {
			Assets.music_mainTheme.play();
		}
	}
	
	@Override
	public void onPause() {
		if (Assets.music_mainTheme != null && Assets.music_mainTheme.isPlaying() ) {
			Assets.music_mainTheme.pause();
		}
	}
}
