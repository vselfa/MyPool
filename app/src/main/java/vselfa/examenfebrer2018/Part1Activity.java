package vselfa.examenfebrer2018;

import android.os.Bundle;
import android.util.Log;

public class Part1Activity extends MainMenu {

    public static Part1View part1View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        part1View = new Part1View(this);
        setContentView(part1View);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SurfaceView", "onPause");
        // Per evitar l'error a l'eixir de l'aplicació
        if (part1View != null) part1View.stopThread();
    }
}
