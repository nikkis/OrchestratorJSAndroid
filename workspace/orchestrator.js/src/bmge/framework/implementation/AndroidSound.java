package bmge.framework.implementation;

import android.media.SoundPool;

import bmge.framework.Sound;

public class AndroidSound implements Sound {
    int soundId;
    int streamId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        streamId = soundPool.play(soundId, volume, volume, 0, 0, 1);
    }
    
    @Override
    public void stop() {
    	soundPool.stop(streamId);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }

}