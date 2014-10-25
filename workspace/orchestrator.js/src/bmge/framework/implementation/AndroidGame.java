package bmge.framework.implementation;

import java.util.Currency;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import bmge.framework.Audio;
import bmge.framework.FileIO;
import bmge.framework.Game;
import bmge.framework.Graphics;
import bmge.framework.Input;
import bmge.framework.Screen;
import bmge.zombiegame.Assets;
import bmge.zombiegame.RemoteMethodListener;

import com.ojs.capabilities.apocalymbics.Apocalymbics;

public abstract class AndroidGame extends Activity implements Game {
	
	AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
    
    private static final String TAG = AndroidGame.class.getSimpleName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        
        //int frameBufferWidth = isPortrait ? 800: 1280;
        //int frameBufferHeight = isPortrait ? 1280: 800;
        int frameBufferWidth = isPortrait ? Assets.gameAreaHeight : Assets.gameAreaWidth;
        int frameBufferHeight = isPortrait ? Assets.gameAreaWidth: Assets.gameAreaHeight;
        
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        
        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        
        Apocalymbics.setGame(this);
        //screen.setListener(new RemoteMethodListener(this));
        setContentView(renderView);
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");
        
        Log.d(TAG, "onCreate executed!");
    }
	
    

    public void deleteAllReferences() {
        Apocalymbics.setGame(null);
        screen = null;
        renderView = null;
        graphics = null;
        fileIO = null;
        audio = null;
        input = null;
        screen = null;

        finish();
        Log.d(TAG, "All references deleted!");
    }
    
    @Override
	public void onBackPressed() {
		super.onBackPressed();
		//deleteAllReferences();
        
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		deleteAllReferences();
		Log.d(TAG, "destroyed!");
	}



	@Override
    public void onResume() {
        super.onResume();
        if(wakeLock != null)
        	wakeLock.acquire();
        if(renderView != null)
        	renderView.resume();
        Screen c = getCurrentScreen();
        if(c != null)
        	c.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(wakeLock != null)
        	wakeLock.release();
        if(renderView != null)
        	renderView.pause();
        Screen c = getCurrentScreen();
        if(c != null)
        	c.onPause();
    }
    

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
        return screen;
    }
}
