package vselfa.examenfebrer2018;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Part1View extends SurfaceView implements SurfaceHolder.Callback {
    // Part 1: Mostra la paleta que es mou amb el dits
    // I una pilota que apareix on està la paleta i es dispara al soltar-la

    // The thread
    private Part1Thread part1Thread = null;

    // The ball
    private int x, x0;
    private int y, y0;
    Boolean dispara = false;
    // Speed
    private int xDirection = 10;
    private int yDirection = 10;
    private static int radius = 20;
    private static int ballColor = Color.BLUE;
    private static int backgroundColor = Color.WHITE;

    // La paleta
    private float paletaX, paletaY;
    private float ample = 20, alt = 150;

    // Paints
    Paint paleta = new Paint();
    Paint background = new Paint();
    Paint ball = new Paint();

    private float mLastTouchX, mLastTouchY;

    public Part1View(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public void newDraw(Canvas canvas) {
        // El tauler
        background.setColor(backgroundColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        // La paleta
        paleta.setColor(Color.RED);
        canvas.drawRect(paletaX, paletaY, paletaX + ample, paletaY + alt, paleta);
        Log.d ("examen", "px = " + paletaX + "  py = " + paletaY );
        // La pilota
        if (dispara) {
            // La pilota
            ball.setColor(ballColor);
            canvas.drawCircle(x, y, radius, ball);
        } else { // Amagem la pilota
            ball.setColor(backgroundColor);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // dispara = false;
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Calculate the distance moved
                final float x = ev.getX();
                final float y = ev.getY();
                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                // Sols moviment vertical de la paleta 1
                paletaX += dx;
                // Evitem que la paleta se'n isca pels estrems
                if (paletaX < 0) paletaX = 0;
                if (paletaX + ample > getHeight()) paletaX = getHeight() - ample;
                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                // La pilota apareixerà dalt de la paleta
                x = (int) paletaX;
                y = y0; // Dalt de la paleta
                // I eixirà disparada
                dispara = true;
                break;
            }

        }
        return true;// Fi  onTouchEvent
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Situació inicial pilota
        x = getWidth() / 2;
        y = (int) (getHeight() - alt);
        // Pilota dalt de la paleta per a cada vegada que disparem
        y0 = y;
        // Situació inicial paleta
        paletaX = getWidth() / 2;
        paletaY = getHeight() - alt;
        if (part1Thread != null) return;
        part1Thread = new Part1Thread(getHolder());
        part1Thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    public void stopThread() {
        if (part1Thread != null) part1Thread.stop = true;
    }

    // The thread -----------------------------------------------------------
    private class Part1Thread extends Thread {

        public boolean stop = false;
        private SurfaceHolder surfaceHolder;

        public Part1Thread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void run() {
            while (!stop) {
                if (dispara) {
                    // El moviment de la pilota: Sols es desplaça cap amunt
                    // x += xDirection;
                    y -= yDirection;
                    // Rebot dalt
                    if (y < 0) {
                        // La pilota despareix
                        dispara = false;
                    }
                }
                // Control del xoc ab l'asteroid en un futur
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        newDraw(c);
                    }
                } finally {
                    if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}


