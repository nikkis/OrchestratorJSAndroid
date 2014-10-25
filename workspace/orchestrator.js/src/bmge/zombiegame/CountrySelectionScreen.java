package bmge.zombiegame;

import java.util.List;

import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Image;
import bmge.framework.Screen;
import bmge.framework.Input.TouchEvent;

public class CountrySelectionScreen extends Screen {

	private enum SelectionState {
		FadeIn, WaitingTurn, Turn, WaitingFinish, FadeOut
	}

	private SelectionState state = SelectionState.FadeIn;
	int alphaValue = 255;
	int alphaIncrement = 5;

	final int countryFlagRadius = 150;
	final int countryFinCenterX = 290;
	final int countryFinCenterY = 205;
	final int countryGerCenterX = 660;
	final int countryGerCenterY = 205;
	final int countryJapCenterX = 1030;
	final int countryJapCenterY = 205;
	final int countryUsaCenterX = 470;
	final int countryUsaCenterY = 460;
	final int countrySweCenterX = 840;
	final int countrySweCenterY = 460;

	boolean finFree = true;
	boolean gerFree = true;
	boolean japFree = true;
	boolean usaFree = true;
	boolean sweFree = true;

	int selectedCountryX = 0;
	int selectedCountryY = 0;

	public CountrySelectionScreen(Game game) {
		super(game);
		isInCountrySelectionScreen = true;
		thisPlayer.country = UNINITIALIZED;
		
		// Play main theme if stopped
		if ( Assets.music_mainTheme.isStopped() ) {
			Assets.music_mainTheme.play();
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (otherPlayers != null && otherPlayers.size() != 0) {
			for (int player = 0; player < otherPlayers.size(); ++player) {
				int countryNumber = otherPlayers.get(player).country;
				if (countryNumber == COUNTRY_FIN) {
					finFree = false;
				} else if (countryNumber == COUNTRY_GER) {
					gerFree = false;
				} else if (countryNumber == COUNTRY_JAP) {
					japFree = false;
				} else if (countryNumber == COUNTRY_USA) {
					usaFree = false;
				} else if (countryNumber == COUNTRY_SWE) {
					sweFree = false;
				}
			}
		}

		if (state == SelectionState.FadeIn)
			updateFadeIn(touchEvents); 
		else if (state == SelectionState.WaitingTurn)
			updateWaitingTurn(touchEvents);
		else if (state == SelectionState.Turn)
			updateSelectCountry(touchEvents, deltaTime);
		else if (state == SelectionState.WaitingFinish)
			updateWaitingFinish(touchEvents);
		else if (state == SelectionState.FadeOut)
			updateFadeOut(touchEvents);

	}

	private void updateFadeIn(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue > 0) {
			alphaValue -= alphaIncrement;
		} else {
			alphaValue = 0;
			state = SelectionState.WaitingTurn;
		}
	}

	private void updateWaitingTurn(List<TouchEvent> touchEvents) {
		alphaValue = 0;
		touchEvents.clear();
	}

	private void updateSelectCountry(List<TouchEvent> touchEvents,
			float deltaTime) {
		alphaValue = 0;

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);

			if (event.type == TouchEvent.TOUCH_UP) {

				if (finFree
						&& inBoundsCircle(event, countryFinCenterX,
								countryFinCenterY, countryFlagRadius)) {
					thisPlayer.country = COUNTRY_FIN;
					Assets.sfx_plip.play(0.8f);
					state = SelectionState.WaitingFinish;

				} else if (gerFree
						&& inBoundsCircle(event, countryGerCenterX,
								countryGerCenterY, countryFlagRadius)) {
					thisPlayer.country = COUNTRY_GER;
					Assets.sfx_plip.play(0.8f);
					state = SelectionState.WaitingFinish;

				} else if (japFree
						&& inBoundsCircle(event, countryJapCenterX,
								countryJapCenterY, countryFlagRadius)) {
					thisPlayer.country = COUNTRY_JAP;
					Assets.sfx_plip.play(0.8f);
					state = SelectionState.WaitingFinish;

				} else if (usaFree
						&& inBoundsCircle(event, countryUsaCenterX,
								countryUsaCenterY, countryFlagRadius)) {
					thisPlayer.country = COUNTRY_USA;
					Assets.sfx_plip.play(0.8f);
					state = SelectionState.WaitingFinish;

				} else if (sweFree
						&& inBoundsCircle(event, countrySweCenterX,
								countrySweCenterY, countryFlagRadius)) {
					thisPlayer.country = COUNTRY_SWE;
					Assets.sfx_plip.play(0.8f);
					state = SelectionState.WaitingFinish;

				}

			}
		}

	}

	private void updateWaitingFinish(List<TouchEvent> touchEvents) {
		alphaValue = 0;
		touchEvents.clear();
	}

	private void updateFadeOut(List<TouchEvent> touchEvents) {
		touchEvents.clear();

		if (alphaValue < 255) {
			alphaValue += alphaIncrement;
		} else {
			alphaValue = 255;
			game.setScreen(new GameSelectionScreen(game));
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawImage(Assets.menuScreenBg, 0, 0);

		if (state == SelectionState.FadeIn
				|| state == SelectionState.WaitingTurn) {
			if (finFree) {
				g.drawImage(Assets.countryFlagFinlandMutedImg,
						countryFinCenterX - countryFlagRadius,
						countryFinCenterY - countryFlagRadius);
			}
			if (gerFree) {
				g.drawImage(Assets.countryFlagGermanyMutedImg,
						countryGerCenterX - countryFlagRadius,
						countryGerCenterY - countryFlagRadius);
			}
			if (japFree) {
				g.drawImage(Assets.countryFlagJapanMutedImg, countryJapCenterX
						- countryFlagRadius, countryJapCenterY
						- countryFlagRadius);
			}
			if (usaFree) {
				g.drawImage(Assets.countryFlagUsaMutedImg, countryUsaCenterX
						- countryFlagRadius, countryUsaCenterY
						- countryFlagRadius);
			}
			if (sweFree) {
				g.drawImage(Assets.countryFlagSwedenMutedImg, countrySweCenterX
						- countryFlagRadius, countrySweCenterY
						- countryFlagRadius);
			}
		} else if (state == SelectionState.Turn) {
			Image textImg = Assets.chooseCountryTextImg;
			g.drawImage(textImg, screenCenterX - textImg.getWidth() / 2,
					Assets.gameAreaHeight - 2 * textImg.getHeight());

			if (finFree) {
				g.drawImage(Assets.countryFlagFinlandImg, countryFinCenterX
						- countryFlagRadius, countryFinCenterY
						- countryFlagRadius);
			}
			if (gerFree) {
				g.drawImage(Assets.countryFlagGermanyImg, countryGerCenterX
						- countryFlagRadius, countryGerCenterY
						- countryFlagRadius);
			}
			if (japFree) {
				g.drawImage(Assets.countryFlagJapanImg, countryJapCenterX
						- countryFlagRadius, countryJapCenterY
						- countryFlagRadius);
			}
			if (usaFree) {
				g.drawImage(Assets.countryFlagUsaImg, countryUsaCenterX
						- countryFlagRadius, countryUsaCenterY
						- countryFlagRadius);
			}
			if (sweFree) {
				g.drawImage(Assets.countryFlagSwedenImg, countrySweCenterX
						- countryFlagRadius, countrySweCenterY
						- countryFlagRadius);
			}
		} else if (state == SelectionState.WaitingFinish
				|| state == SelectionState.FadeOut) {

			if (thisPlayer.country == COUNTRY_FIN) {
				g.drawScaledImage(Assets.countryFlagWithHeadFinlandImg,
						Assets.gameAreaWidth / 2 - countryFlagRadius * 2,
						Assets.gameAreaHeight / 2 - countryFlagRadius * 2,
						Assets.countryFlagWithHeadFinlandImg.getWidth() * 2,
						Assets.countryFlagWithHeadFinlandImg.getHeight() * 2,
						0, 0, Assets.countryFlagWithHeadFinlandImg.getWidth(),
						Assets.countryFlagWithHeadFinlandImg.getHeight());
			} else if (thisPlayer.country == COUNTRY_GER) {
				g.drawScaledImage(Assets.countryFlagWithHeadGermanyImg,
						Assets.gameAreaWidth / 2 - countryFlagRadius * 2,
						Assets.gameAreaHeight / 2 - countryFlagRadius * 2,
						Assets.countryFlagWithHeadGermanyImg.getWidth() * 2,
						Assets.countryFlagWithHeadGermanyImg.getHeight() * 2,
						0, 0, Assets.countryFlagWithHeadGermanyImg.getWidth(),
						Assets.countryFlagWithHeadGermanyImg.getHeight());
			} else if (thisPlayer.country == COUNTRY_JAP) {
				g.drawScaledImage(Assets.countryFlagWithHeadJapanImg,
						Assets.gameAreaWidth / 2 - countryFlagRadius * 2,
						Assets.gameAreaHeight / 2 - countryFlagRadius * 2,
						Assets.countryFlagWithHeadJapanImg.getWidth() * 2,
						Assets.countryFlagWithHeadJapanImg.getHeight() * 2, 0,
						0, Assets.countryFlagWithHeadJapanImg.getWidth(),
						Assets.countryFlagWithHeadJapanImg.getHeight());
			} else if (thisPlayer.country == COUNTRY_USA) {
				g.drawScaledImage(Assets.countryFlagWithHeadUsaImg,
						Assets.gameAreaWidth / 2 - countryFlagRadius * 2,
						Assets.gameAreaHeight / 2 - countryFlagRadius * 2,
						Assets.countryFlagWithHeadUsaImg.getWidth() * 2,
						Assets.countryFlagWithHeadUsaImg.getHeight() * 2, 0, 0,
						Assets.countryFlagWithHeadUsaImg.getWidth(),
						Assets.countryFlagWithHeadUsaImg.getHeight());
			} else if (thisPlayer.country == COUNTRY_SWE) {
				g.drawScaledImage(Assets.countryFlagWithHeadSwedenImg,
						Assets.gameAreaWidth / 2 - countryFlagRadius * 2,
						Assets.gameAreaHeight / 2 - countryFlagRadius * 2,
						Assets.countryFlagWithHeadSwedenImg.getWidth() * 2,
						Assets.countryFlagWithHeadSwedenImg.getHeight() * 2, 0,
						0, Assets.countryFlagWithHeadSwedenImg.getWidth(),
						Assets.countryFlagWithHeadSwedenImg.getHeight());
			}
		}

		g.drawARGB(alphaValue, 0, 0, 0);
	}

	@Override
	public void giveCountrySelectionTurn() {
		if (state == SelectionState.WaitingTurn) {
			state = SelectionState.Turn;
			
			Assets.voice_zombieChoose.play(0.5f);
		}
	}

	@Override
	public void moveToGameSelectionScreen() {
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
