package vselfa.examenfebrer2018;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

public class PoolActivity extends MainMenu {

    public static PoolView poolView;
    public static SoundManager sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El so
        sound = new SoundManager(getApplicationContext());
        // Set volume rocker mode to media volume
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // La vista
        poolView = new PoolView(this);
        setContentView(poolView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SurfaceView", "onPause");
        // Per evitar l'error a l'eixir de l'aplicació
        if (poolView != null) poolView.stopThread();
    }
}

