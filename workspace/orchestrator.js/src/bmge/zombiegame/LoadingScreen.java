package bmge.zombiegame;

import android.graphics.Color;
import android.graphics.Paint;

import bmge.framework.Audio;
import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Image;
import bmge.framework.Music;
import bmge.framework.Screen;
import bmge.framework.Graphics.ImageFormat;


public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) { 
        super(game);
    }


    @Override
    public void update(float deltaTime) {
    	this.paint(0); 
    	
        Graphics g = game.getGraphics();
        
        Assets.menuScreenBg = g.newImage(Assets.menuScreenBgPath, ImageFormat.RGB565);
        Assets.winnerScreenBg = g.newImage(Assets.winnerScreenBgPath, ImageFormat.RGB565);
        Assets.playAgainScreenBg = g.newImage(Assets.playAgainScreenBgPath, ImageFormat.RGB565);

        Assets.countryFlagFinlandImg = g.newImage(Assets.countryFlagFinlandImgPath, ImageFormat.RGB565);
        Assets.countryFlagGermanyImg = g.newImage(Assets.countryFlagGermanyImgPath, ImageFormat.RGB565);
        Assets.countryFlagJapanImg = g.newImage(Assets.countryFlagJapanImgPath, ImageFormat.RGB565);
        Assets.countryFlagUsaImg = g.newImage(Assets.countryFlagUsaImgPath, ImageFormat.RGB565);
        Assets.countryFlagSwedenImg = g.newImage(Assets.countryFlagSwedenImgPath, ImageFormat.RGB565);
        Assets.countryFlagFinlandMutedImg = g.newImage(Assets.countryFlagFinlandMutedImgPath, ImageFormat.RGB565);
        Assets.countryFlagGermanyMutedImg = g.newImage(Assets.countryFlagGermanyMutedImgPath, ImageFormat.RGB565);
        Assets.countryFlagJapanMutedImg = g.newImage(Assets.countryFlagJapanMutedImgPath, ImageFormat.RGB565);
        Assets.countryFlagUsaMutedImg = g.newImage(Assets.countryFlagUsaMutedImgPath, ImageFormat.RGB565);
        Assets.countryFlagSwedenMutedImg = g.newImage(Assets.countryFlagSwedenMutedImgPath, ImageFormat.RGB565);
        Assets.countryFlagWithHeadFinlandImg = g.newImage(Assets.countryFlagWithHeadFinlandImgPath, ImageFormat.RGB565);
        Assets.countryFlagWithHeadGermanyImg = g.newImage(Assets.countryFlagWithHeadGermanyImgPath, ImageFormat.RGB565);
        Assets.countryFlagWithHeadJapanImg = g.newImage(Assets.countryFlagWithHeadJapanImgPath, ImageFormat.RGB565);
        Assets.countryFlagWithHeadUsaImg = g.newImage(Assets.countryFlagWithHeadUsaImgPath, ImageFormat.RGB565);
        Assets.countryFlagWithHeadSwedenImg = g.newImage(Assets.countryFlagWithHeadSwedenImgPath, ImageFormat.RGB565);
        
        Assets.gameIconSkullThrowImg = g.newImage(Assets.gameIconSkullThrowImgPath, ImageFormat.RGB565);
        Assets.gameIconShuffleBoardImg = g.newImage(Assets.gameIconShuffleBoardImgPath, ImageFormat.RGB565);
        Assets.gameIconPatternGymnasticImg = g.newImage(Assets.gameIconPatternGymnasticImgPath, ImageFormat.RGB565);
        Assets.gameIconBallBalancingImg = g.newImage(Assets.gameIconBallBalancingImgPath, ImageFormat.RGB565);
        Assets.gameIconSkullThrowImg = g.newImage(Assets.gameIconSkullThrowImgPath, ImageFormat.RGB565);
        Assets.gameIconSkullThrowMutedImg = g.newImage(Assets.gameIconSkullThrowMutedImgPath, ImageFormat.RGB565);
        Assets.gameIconShuffleBoardMutedImg = g.newImage(Assets.gameIconShuffleBoardMutedImgPath, ImageFormat.RGB565);
        Assets.gameIconPatternGymnasticMutedImg = g.newImage(Assets.gameIconPatternGymnasticMutedImgPath, ImageFormat.RGB565);
        Assets.gameIconBallBalancingMutedImg = g.newImage(Assets.gameIconBallBalancingMutedImgPath, ImageFormat.RGB565);
        
        Assets.gameIconSkullThrowSmallImg = g.newImage(Assets.gameIconSkullThrowSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconShuffleBoardSmallImg = g.newImage(Assets.gameIconShuffleBoardSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconPatternGymnasticSmallImg = g.newImage(Assets.gameIconPatternGymnasticSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconBallBalancingSmallImg = g.newImage(Assets.gameIconBallBalancingSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconSkullThrowMutedSmallImg = g.newImage(Assets.gameIconSkullThrowMutedSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconShuffleBoardMutedSmallImg = g.newImage(Assets.gameIconShuffleBoardMutedSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconPatternGymnasticMutedSmallImg = g.newImage(Assets.gameIconPatternGymnasticMutedSmallImgPath, ImageFormat.RGB565);
        Assets.gameIconBallBalancingMutedSmallImg = g.newImage(Assets.gameIconBallBalancingMutedSmallImgPath, ImageFormat.RGB565);
        
        Assets.nationFinlandImg = g.newImage(Assets.nationFinlandImgPath, ImageFormat.RGB565);
        Assets.nationGermanyImg = g.newImage(Assets.nationGermanyImgPath, ImageFormat.RGB565);
        Assets.nationJapanImg = g.newImage(Assets.nationJapanImgPath, ImageFormat.RGB565);
        Assets.nationUsaImg = g.newImage(Assets.nationUsaImgPath, ImageFormat.RGB565);
        Assets.nationSwedenImg = g.newImage(Assets.nationSwedenImgPath, ImageFormat.RGB565);

        Assets.zombieHeadSmallFinlandImg = g.newImage(Assets.zombieHeadSmallFinlandImgPath, ImageFormat.RGB565);
        Assets.zombieHeadSmallGermanyImg = g.newImage(Assets.zombieHeadSmallGermanyImgPath, ImageFormat.RGB565);
        Assets.zombieHeadSmallJapanImg = g.newImage(Assets.zombieHeadSmallJapanImgPath, ImageFormat.RGB565);
        Assets.zombieHeadSmallUsaImg = g.newImage(Assets.zombieHeadSmallUsaImgPath, ImageFormat.RGB565);
        Assets.zombieHeadSmallSwedenImg = g.newImage(Assets.zombieHeadSmallSwedenImgPath, ImageFormat.RGB565);
        
        Assets.titleTextImg = g.newImage(Assets.titleTextImgPath, ImageFormat.RGB565);
        Assets.chooseCountryTextImg = g.newImage(Assets.chooseCountryTextImgPath, ImageFormat.RGB565);
        Assets.chooseGameTextImg = g.newImage(Assets.chooseGameTextImgPath, ImageFormat.RGB565);
        
        Assets.multiPlayerImg = g.newImage(Assets.multiPlayerImgPath, ImageFormat.RGB565);
        Assets.singlePlayerImg = g.newImage(Assets.singlePlayerImgPath, ImageFormat.RGB565);
        Assets.gearIcon = g.newImage(Assets.gearIconPath, ImageFormat.RGB565);
        Assets.infoIcon = g.newImage(Assets.infoIconPath, ImageFormat.RGB565);
        Assets.musicIcon = g.newImage(Assets.musicIconPath, ImageFormat.RGB565);
        Assets.musicXIcon = g.newImage(Assets.musicXIconPath, ImageFormat.RGB565);
        Assets.soundIcon = g.newImage(Assets.soundIconPath, ImageFormat.RGB565);
        Assets.soundXIcon = g.newImage(Assets.soundXIconPath, ImageFormat.RGB565);
        Assets.greyCircleSmall = g.newImage(Assets.greyCircleSmallPath, ImageFormat.RGB565);
        
        Assets.buttonYesImg = g.newImage(Assets.buttonYesImgPath, ImageFormat.RGB565);
        Assets.buttonNoImg = g.newImage(Assets.buttonNoImgPath, ImageFormat.RGB565);
        Assets.number1Img = g.newImage(Assets.number1ImgPath, ImageFormat.RGB565);
        Assets.number2Img = g.newImage(Assets.number2ImgPath, ImageFormat.RGB565);
        Assets.number3Img = g.newImage(Assets.number3ImgPath, ImageFormat.RGB565);
        Assets.number4Img = g.newImage(Assets.number4ImgPath, ImageFormat.RGB565);
        Assets.number5Img = g.newImage(Assets.number5ImgPath, ImageFormat.RGB565);
        Assets.number6Img = g.newImage(Assets.number6ImgPath, ImageFormat.RGB565);
        Assets.number7Img = g.newImage(Assets.number7ImgPath, ImageFormat.RGB565);
        Assets.number8Img = g.newImage(Assets.number8ImgPath, ImageFormat.RGB565);
        Assets.number9Img = g.newImage(Assets.number9ImgPath, ImageFormat.RGB565);
        Assets.number10Img = g.newImage(Assets.number10ImgPath, ImageFormat.RGB565);
        
        Assets.textRound1Img = g.newImage(Assets.textRound1ImgPath, ImageFormat.RGB565);
        Assets.textRound2Img = g.newImage(Assets.textRound2ImgPath, ImageFormat.RGB565);
        Assets.textRound3Img = g.newImage(Assets.textRound3ImgPath, ImageFormat.RGB565);
        Assets.textGoodImg = g.newImage(Assets.textGoodImgPath, ImageFormat.RGB565);
        Assets.textBadImg = g.newImage(Assets.textBadImgPath, ImageFormat.RGB565);
        Assets.textThrowImg = g.newImage(Assets.textThrowImgPath, ImageFormat.RGB565);
        
        Assets.skullThrowGameBg = g.newImage(Assets.skullThrowGameBgPath, ImageFormat.RGB565);
        Assets.skullThrowGameBgCorner = g.newImage(Assets.skullThrowGameBgCornerPath, ImageFormat.RGB565);
        Assets.skullFinlandImg = g.newImage(Assets.skullFinlandImgPath, ImageFormat.RGB565);
        Assets.skullGermanyImg = g.newImage(Assets.skullGermanyImgPath, ImageFormat.RGB565);
        Assets.skullJapanImg = g.newImage(Assets.skullJapanImgPath, ImageFormat.RGB565);
        Assets.skullUsaImg = g.newImage(Assets.skullUsaImgPath, ImageFormat.RGB565);
        Assets.skullSwedenImg = g.newImage(Assets.skullSwedenImgPath, ImageFormat.RGB565);
        Assets.flagSkullFinlandImg = g.newImage(Assets.flagSkullFinlandImgPath, ImageFormat.RGB565);
        Assets.flagSkullGermanyImg = g.newImage(Assets.flagSkullGermanyImgPath, ImageFormat.RGB565);
        Assets.flagSkullJapanImg = g.newImage(Assets.flagSkullJapanImgPath, ImageFormat.RGB565);
        Assets.flagSkullUsaImg = g.newImage(Assets.flagSkullUsaImgPath, ImageFormat.RGB565);
        Assets.flagSkullSwedenImg = g.newImage(Assets.flagSkullSwedenImgPath, ImageFormat.RGB565);
         
        Assets.zombieBaseCornerFinlandImg = g.newImage(Assets.zombieBaseCornerFinlandImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandCornerFinlandImg = g.newImage(Assets.zombieSkullHandCornerFinlandImgPath, ImageFormat.RGB565);
        Assets.zombieBaseFieldFinlandImg = g.newImage(Assets.zombieBaseFieldFinlandImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandFieldFinlandImg = g.newImage(Assets.zombieSkullHandFieldFinlandImgPath, ImageFormat.RGB565);
        Assets.zombieEmptyHandFieldFinlandImg = g.newImage(Assets.zombieEmptyHandFieldFinlandImgPath, ImageFormat.RGB565);
        
        Assets.zombieBaseCornerGermanyImg = g.newImage(Assets.zombieBaseCornerGermanyImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandCornerGermanyImg = g.newImage(Assets.zombieSkullHandCornerGermanyImgPath, ImageFormat.RGB565);
        Assets.zombieBaseFieldGermanyImg = g.newImage(Assets.zombieBaseFieldGermanyImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandFieldGermanyImg = g.newImage(Assets.zombieSkullHandFieldGermanyImgPath, ImageFormat.RGB565);
        Assets.zombieEmptyHandFieldGermanyImg = g.newImage(Assets.zombieEmptyHandFieldGermanyImgPath, ImageFormat.RGB565);
        
        Assets.zombieBaseCornerJapanImg = g.newImage(Assets.zombieBaseCornerJapanImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandCornerJapanImg = g.newImage(Assets.zombieSkullHandCornerJapanImgPath, ImageFormat.RGB565);
        Assets.zombieBaseFieldJapanImg = g.newImage(Assets.zombieBaseFieldJapanImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandFieldJapanImg = g.newImage(Assets.zombieSkullHandFieldJapanImgPath, ImageFormat.RGB565);
        Assets.zombieEmptyHandFieldJapanImg = g.newImage(Assets.zombieEmptyHandFieldJapanImgPath, ImageFormat.RGB565);
        
        Assets.zombieBaseCornerUsaImg = g.newImage(Assets.zombieBaseCornerUsaImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandCornerUsaImg = g.newImage(Assets.zombieSkullHandCornerUsaImgPath, ImageFormat.RGB565);
        Assets.zombieBaseFieldUsaImg = g.newImage(Assets.zombieBaseFieldUsaImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandFieldUsaImg = g.newImage(Assets.zombieSkullHandFieldUsaImgPath, ImageFormat.RGB565);
        Assets.zombieEmptyHandFieldUsaImg = g.newImage(Assets.zombieEmptyHandFieldUsaImgPath, ImageFormat.RGB565);
        
        Assets.zombieBaseCornerSwedenImg = g.newImage(Assets.zombieBaseCornerSwedenImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandCornerSwedenImg = g.newImage(Assets.zombieSkullHandCornerSwedenImgPath, ImageFormat.RGB565);
        Assets.zombieBaseFieldSwedenImg = g.newImage(Assets.zombieBaseFieldSwedenImgPath, ImageFormat.RGB565);
        Assets.zombieSkullHandFieldSwedenImg = g.newImage(Assets.zombieSkullHandFieldSwedenImgPath, ImageFormat.RGB565);
        Assets.zombieEmptyHandFieldSwedenImg = g.newImage(Assets.zombieEmptyHandFieldSwedenImgPath, ImageFormat.RGB565);
        
        // SFX
        Audio a = game.getAudio();
        
        Assets.sfx_wetTowel = a.createSound(Assets.sfx_wetTowelPath);
        //Assets.sfx_brainSplashCrack = a.createSound(Assets.sfx_brainSplashCrackPath);
        Assets.sfx_plipPlop = a.createSound(Assets.sfx_plipPlopPath);
        Assets.sfx_plip = a.createSound(Assets.sfx_plipPath);
        
        Assets.sfx_rotatingHandLoop = a.createMusic(Assets.sfx_rotatingHandPath);
        Assets.sfx_flyingBrainWhistle = a.createMusic(Assets.sfx_flyingBrainPath);
        Assets.sfx_flyingBrainWhistleSound = a.createSound(Assets.sfx_flyingBrainPath);
        Assets.music_tickTack = a.createMusic(Assets.music_tickTackPath);  
        Assets.music_mainTheme = a.createMusic(Assets.music_mainThemePath);
        Assets.music_backBeat = a.createMusic(Assets.music_backBeatPath);
        
        // Voices
        Assets.voice_zombieChoose = a.createSound(Assets.voice_zombieChoosePath);
        Assets.voice_zombieBad = a.createSound(Assets.voice_zombieBadPath);
        Assets.voice_zombieGood = a.createSound(Assets.voice_zombieGoodPath);
        Assets.voice_zombieThrow = a.createSound(Assets.voice_zombieThrowPath);
        
        Assets.voice_zombieRound1 = a.createSound(Assets.voice_zombieRound1Path);
        Assets.voice_zombieRound2 = a.createSound(Assets.voice_zombieRound2Path);
        Assets.voice_zombieRound3 = a.createSound(Assets.voice_zombieRound3Path);
        
        //Assets.voice_zombieBadZombie = a.createSound(Assets.voice_zombieBadZombiePath);
        //Assets.voice_zombieGoodZombie = a.createSound(Assets.voice_zombieGoodZombiePath);
        
        
        
        
        game.setScreen(new MainMenuScreen(game));
    } 


    @Override
    public void paint(float deltaTime) {
    	Graphics g = game.getGraphics();
    	
    	Paint paint = new Paint();
		paint.setTextSize(100);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		String text = "The first screen (loading screen), a picture needed";
        g.drawString(text, Assets.gameAreaWidth/2, Assets.gameAreaHeight/2, paint);
    }

    @Override
	public void onResume() {
	}
	
	@Override
	public void onPause() {
	}
}