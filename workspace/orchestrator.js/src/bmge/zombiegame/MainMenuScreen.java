package bmge.zombiegame;

import java.util.ArrayList;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;

import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Screen;
import bmge.framework.Input.TouchEvent;
import bmge.framework.Screen.Player;
import bmge.framework.Sound;
import bmge.framework.Audio;

public class MainMenuScreen extends Screen {

	final int iconDrawY = 650;
	final int iconGearCenterX = 180;
	final int iconInfoCenterX = 480;
	final int iconMusicCenterX = 780;
	final int iconSoundCenterX = 1080;

	int gameTypeDrawY = 275;
	int singlePlayerCenterX = 400;
	int multiPlayerCenterX = 880;

	public MainMenuScreen(Game game) {
		super(game);

		otherPlayers = null;
		
		// Play main theme
		Assets.music_mainTheme.setLooping(true);
		Assets.music_mainTheme.setVolume(0.5f);
		Assets.music_mainTheme.play();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);

			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event,
						multiPlayerCenterX - Assets.multiPlayerImg.getWidth()
								/ 2, gameTypeDrawY,
						Assets.multiPlayerImg.getWidth(),
						Assets.multiPlayerImg.getHeight())) {

					Assets.sfx_plip.play(0.8f);
					game.setScreen(new CountrySelectionScreen(game));
				}
			}
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawImage(Assets.menuScreenBg, 0, 0);
		g.drawImage(Assets.titleTextImg,
				screenCenterX - Assets.titleTextImg.getWidth() / 2,
				Assets.titleTextImg.getHeight() / 2);

		g.drawImage(Assets.singlePlayerImg, singlePlayerCenterX
				- Assets.singlePlayerImg.getWidth() / 2, gameTypeDrawY);

		g.drawImage(Assets.multiPlayerImg, multiPlayerCenterX
				- Assets.multiPlayerImg.getWidth() / 2, gameTypeDrawY);

		g.drawImage(Assets.gearIcon,
				iconGearCenterX - Assets.gearIcon.getWidth() / 2, iconDrawY);

		g.drawImage(Assets.infoIcon,
				iconInfoCenterX - Assets.infoIcon.getWidth() / 2, iconDrawY);

		g.drawImage(Assets.musicIcon,
				iconMusicCenterX - Assets.musicIcon.getWidth() / 2, iconDrawY);

		g.drawImage(Assets.soundIcon,
				iconSoundCenterX - Assets.soundIcon.getWidth() / 2, iconDrawY);

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