package vselfa.examenfebrer2018;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DrawLineActivity extends MainMenu {

    public static DrawLineView drawLineView;
    public static SoundManager sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El so
        sound = new SoundManager(getApplicationContext());
        // Set volume rocker mode to media volume
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // La vista
        drawLineView = new DrawLineView(this);
        setContentView(drawLineView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SurfaceView", "onPause");
        // Per evitar l'error a l'eixir de l'aplicació
        if (drawLineView != null) drawLineView.stopThread();
    }
}

