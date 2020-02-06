package vselfa.examenfebrer2018;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawLineView extends SurfaceView implements SurfaceHolder.Callback{

    // Part 3: Mostra la  interacció entre paleta, pilota i asteroid
    // The thread
    private DrawLineThread drawLineThread = null;

    // Els sons
    int strike;

        // The ball
        private float x, x0, y, y0;
        private static int radius = 20;
        Boolean dispara = false, esMou = false;


        // Speed
        private int xDirection = 20;     private int yDirection = 20;
        // Valors inicials pera quan canviem el signe en funció d'on peguem a la pilota
        private int xDirection0 = 20;     private int yDirection0 = 20;
        // Angle
        float alfa;

        // La bola que representa el meu dit
        private float x1, y1; int radius1 = 10;
        // Emprenta: volem que aparega el cercle que representa al dit
        boolean dibuixaLinia = false, emprenta = false;
        // Acceleració amb la que eixirà la bola
        float potenciaDispar, potenciaMax;


    // Paints i colors
        private static int backgroundColor = Color.WHITE;
        private static int ballColor = Color.BLUE;
        private static int fingerColor = Color.RED;

        Paint text = new Paint ();
        Paint background = new Paint();
        Paint ball = new Paint();
        Paint finger = new Paint();
        Paint linia = new Paint();

        private float mLastTouchX, mLastTouchY;

        public DrawLineView(Context context) {
            super(context);
            getHolder().addCallback(this);
            // El tauler
            background.setColor(backgroundColor);
            // La pilota
            ball.setColor(ballColor);
            // L'emprenta
            finger.setColor(fingerColor);
            // La linia
            linia.setColor(fingerColor);
            // Els sons
        }

        public void newDraw(Canvas canvas) {
            // L'emprenta
            if (emprenta) {
                canvas.drawCircle(x1, y1, radius1, ball);
            }
            // Dibuixem linia entre emprenta i pilota
            if (dibuixaLinia) {
                canvas.drawLine(x1, y1, x, y, linia);
            }
            // Pintar la pilota
            canvas.drawCircle(x, y, radius, ball);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            final int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mLastTouchX = ev.getX();       mLastTouchY = ev.getY();
                    // Posició original del dit
                    emprenta = true; dispara = false;
                    x1 = mLastTouchX; y1 = mLastTouchY;
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    // Calculate the distance moved
                    final float xAux = ev.getX();
                    final float yAux = ev.getY();
                    // Calculate the distance moved
                    final float dx = xAux - mLastTouchX;
                    final float dy = yAux - mLastTouchY;

                    // Cercle que representa el moviment del dit
                    x1 += dx; y1 += dy;
                    // Remember this touch position for the next move event
                    mLastTouchX = x1;         mLastTouchY = y1;
                    // Per divuixar la recta entre pilota i dit
                    dibuixaLinia = true;
                    // Calcul de l'angle en radians.
                    // Ho redüim sempre al primer quadrant!! Inc x, Inc y sempre positius!!
                    float incX = x - x1; float incY = y - y1;

                    Log.d ("inc", "incX" + incX + " incY: " + incY );
                    Log.d ("inc", "Angle en radians: " +  alfa + " En graus: " + Math.toDegrees(alfa));

                    // Controlem des de quin quadrant li peguem a la bola
                    // Sempre: Quadrant1 => Quadrant3; Quadrant 2 => Quadrant 4 ...
                    // Quadrat M  = (Quadrat N + 1) % 4 + 1. Fórmula
                    // Disparem des del quadrat 1: incX < 0; incy > 0.
                    if ((incX < 0) && (incY > 0)) {
                        // Passem al quadrant 4
                        xDirection = -xDirection0;
                        yDirection = yDirection0;
                        // Per calcular els angles sempre en el primer quadrant
                        incX = - incX;
                        Log.d ("inc", "Quadrant 1 xD " +  xDirection + " yD: " + yDirection);
                    }
                    else { // Per evitar el canvi de signe incX que fa tornar a entrar en quadrat 2
                        // Disparem des del quadrat 2: incX > 0 0; incy > 0.
                        if ((incX > 0) && (incY > 0)) {
                            // Passem al quadrant 3
                            xDirection = xDirection0;
                            yDirection = yDirection0;
                            // Per calcular els angles sempre en el primer quadrant
                            Log.d("inc", "Quadrant 2 xD " + xDirection + " yD: " + yDirection);

                        }
                        // Disparem des del quadrat 3: incX > 0; incy < 0.
                        if ((incX > 0) && (incY < 0)) {
                            // Passem al quadrant 1
                            xDirection = xDirection0;
                            yDirection = -yDirection0;
                            // Per calcular els angles sempre en el primer quadrant
                            incY = -incY;
                            Log.d("inc", "Quadrant 3 xD " + xDirection + " yD: " + yDirection);

                        }
                        // Disparem des del quadrat 4: incX > 0; incy > 0.
                        if ((incX < 0) && (incY < 0)) {
                            // Passem al quadrant 2
                            xDirection = -xDirection0;
                            yDirection = -yDirection0;
                            // Per calcular els angles sempre en el primer quadrant
                            incX = -incX;
                            incY = -incY;
                            Log.d("inc", "Quadrant 4 xD " + xDirection + " yD: " + yDirection);
                        }
                    }
                    // Calcul de l'angle: En el quadrant 1. De 0 a 90º
                    alfa = (float) Math.atan(incY/incX);

                    // Calcul de la distancia => factor acceleració
                    potenciaDispar = (float) Math.hypot(x - x1, y - y1) / potenciaMax;
                    // Log.d ("potencia", "Potencia: " + potenciaDispar);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    // El dit desapareix
                    emprenta = false;
                    // La línia desapareix
                    dibuixaLinia = false;
                    // Escoltem el so
                    DrawLineActivity.sound.play(strike);
                    // I la pilota ix disparada
                    dispara = true;
                    break;
                }

            }
            return true;// Fi  onTouchEvent
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initGame();
            // El so
            // Selecionem un so
            strike = DrawLineActivity.sound.load(R.raw.so_boles_billar);
            if (drawLineThread != null) return;
            drawLineThread = new DrawLineThread(getHolder());
            drawLineThread.start();
        }

        public void initGame () {
            // Situació inicial pilota
            x = getWidth() / 2;
            y = getHeight() / 2 ;
            // Valors inicials
            x0 = x;  y0 = y;
            // Potencia de dispar màxima: des de la pilota a l'extrem inferior esquerre
            potenciaMax =(float) Math.hypot(x - 0, y - getHeight());

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}


        @Override
        public void surfaceDestroyed(SurfaceHolder holder)         {
            stopThread();
        }

        public void stopThread() {
            if (drawLineThread != null) drawLineThread.stop = true;
        }


        public void resumeThread() {
            Log.d ("fi", "Parat?: " + drawLineThread.stop);
            if (drawLineThread.stop) drawLineThread.stop = false;
        }

        // The thread -----------------------------------------------------------
        private class DrawLineThread extends Thread {

            public boolean stop = false;
            private SurfaceHolder surfaceHolder;

            public DrawLineThread(SurfaceHolder surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
            }

            public void run() {
                while (!stop) {
                    Log.d ("posicio", "x: " + x + " y: " + y);
                    // Hem disparat:
                    if (dispara) {
                        x += xDirection * Math.cos(alfa)* potenciaDispar;
                        y += yDirection * Math.sin(alfa)* potenciaDispar;
                        // Disminució de velocitat
                        potenciaDispar = (float) (potenciaDispar - 0.001);
                        // Es para la bola
                        if (potenciaDispar < 0)  dispara = false;
                    }
                    // La pilota se'n ix de la pantalla => Rebot
                    if ((x + radius > getWidth()) || (x - radius < 0 )){
                        // x = x0; y = y0;
                        // dispara = false;
                        xDirection = -xDirection;
                        //Log.d ("posicio", "xoc lateral");

                    }
                    if ((y + radius > getHeight()) || (y - radius < 0 )){
                        // x = x0; y = y0;
                        // dispara = false;
                        // Log.d ("posicio", "xoc dalt o baix");
                        yDirection = -yDirection;
                    }
                    Canvas c = null;
                    try {
                        c = surfaceHolder.lockCanvas(null);
                        synchronized (surfaceHolder) {
                            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            newDraw(c);
                        }
                    } finally {
                        if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }




