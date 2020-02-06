package vselfa.examenfebrer2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Part2Activity extends MainMenu {

    public static Part2View part2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        part2View = new Part2View(this);
        setContentView(part2View);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SurfaceView", "onPause");
        // Per evitar l'error a l'eixir de l'aplicació
        if (part2View != null) part2View.stopThread();
    }
}
