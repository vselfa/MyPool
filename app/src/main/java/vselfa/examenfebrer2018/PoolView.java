package vselfa.examenfebrer2018;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PoolView extends SurfaceView implements SurfaceHolder.Callback{

    // Part 3: Mostra la  interacció entre paleta, pilota i asteroid
    // The thread
    private DrawLineThread drawLineThread = null;

    // Els sons
    int strike;

    // The balls
    private float x0,  y0; // Valors inicials bola 1
    private float x1, y1, x2, y2; // Les dues boles: 1 la que es llança, 2 la parada
    private static int radi1 = 30, radi2 = 30;
    Boolean enMovimentBola1 = false, enMovimentBola2 = false, xoc = false;

    // Per simular moviment per a cada bola
    private int xDirection1 = 20;     private int yDirection1= 20;
    private int xDirection2 = 20;     private int yDirection2 = 20;
    // Valors inicials pera quan canviem el signe en funció d'on peguem a la pilota
    private int xDirection10 = 20;     private int yDirection10 = 20;
    private int xDirection20 = 20;     private int yDirection20 = 20;
    // Angles
    float alfa1, alfa2;

    // La bola que representa el meu dit
    private float xDit, yDit; int radi = 10;
    // Emprenta: volem que aparega el cercle que representa al dit
    boolean dibuixaLinia = false, emprenta = false;
    // Acceleració amb la que eixirà la bola
    float potenciaDispar1, potenciaDispar2, potenciaMax;

    float incX, incY;

// Paints i colors
    private static int backgroundColor = Color.WHITE;
    private static int ball1Color = Color.BLUE;
    private static int ball2Color = Color.RED;
    private static int fingerColor = Color.RED;

    Paint text = new Paint ();
    Paint background = new Paint();
    Paint ball1 = new Paint();
    Paint ball2 = new Paint();
    Paint finger = new Paint();
    Paint linia = new Paint();

    private float mLastTouchX, mLastTouchY;

    public PoolView(Context context) {
        super(context);
        getHolder().addCallback(this);
        // El tauler
        background.setColor(backgroundColor);
        // Les boles
        ball1.setColor(ball1Color);
        ball2.setColor(ball2Color);
        // L'emprenta
        finger.setColor(fingerColor);
        // La linia
        linia.setColor(fingerColor);
        // Els sons
    }

    public void newDraw(Canvas canvas) {
        // L'emprenta
        if (emprenta) {
            canvas.drawCircle(xDit, yDit, radi, ball1);
        }
        // Dibuixem linia entre emprenta i pilota
        if (dibuixaLinia) {
            canvas.drawLine(xDit, yDit, x1, y1, linia);
        }
        // Pintar la bola en moviment
        canvas.drawCircle(x1, y1, radi1, ball1);
        // Pintar la bola parada en principi
        canvas.drawCircle(x2, y2, radi2, ball2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastTouchX = ev.getX();       mLastTouchY = ev.getY();
                // Posició original del dit
                emprenta = true;
                // parem la bola 1 per agilitzar el joc
                enMovimentBola1 = false;
                xDit = mLastTouchX; yDit = mLastTouchY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Calculate the distance moved
                final float xAux = ev.getX();
                final float yAux = ev.getY();
                // Calculate the distance moved
                final float dx = xAux - mLastTouchX;
                final float dy = yAux - mLastTouchY;

                enMovimentBola2 = false; // La bola 2 ha d'estar parada pertornar a tirar
                // Cercle que representa el moviment del dit
                xDit += dx; yDit += dy;
                // Remember this touch position for the next move event
                mLastTouchX = xDit;         mLastTouchY = yDit;
                // Per divuixar la recta entre pilota i dit
                dibuixaLinia = true;
                // Calcul de l'angle en radians.
                // Ho redüim sempre al primer quadrant!! Inc x, Inc y sempre positius!!
                incX = x1 - xDit; incY = y1 - yDit;

                Log.d ("inc", "incX" + incX + " incY: " + incY );
                Log.d ("inc", "Angle en radians: " +  alfa1 + " En graus: " + Math.toDegrees(alfa1));

                // Controlem des de quin quadrant li peguem a la bola
                // Sempre: Quadrant1 => Quadrant3; Quadrant 2 => Quadrant 4 ...
                // Quadrat M  = (Quadrat N + 1) % 4 + 1. Fórmula
                // Disparem des del quadrat 1: incX < 0; incy > 0.
                if ((incX < 0) && (incY > 0)) {
                    // Passem al quadrant 4
                    xDirection1 = -xDirection10;  yDirection1 = yDirection10;
                    // Futures direccions per la segona bola després del xoc
                    xDirection2 = -xDirection20;  yDirection2 = yDirection20;
                    // Per calcular els angles sempre en el primer quadrant
                    incX = - incX;
                    Log.d ("inc", "Quadrant 1 xD " +  xDirection1 + " yD: " + yDirection1);
                }
                else { // Per evitar el canvi de signe incX que fa tornar a entrar en quadrat 2
                    // Disparem des del quadrat 2: incX > 0 0; incy > 0.
                    if ((incX > 0) && (incY > 0)) {
                        // Passem al quadrant 3
                        xDirection1 = xDirection10;  yDirection1 = yDirection10;
                        // Futures direccions per la segona bola després del xoc
                        xDirection2 = xDirection20;  yDirection2 = yDirection20;
                        // Per calcular els angles sempre en el primer quadrant
                        Log.d("inc", "Quadrant 2 xD " + xDirection1 + " yD: " + yDirection1);

                    }
                    // Disparem des del quadrat 3: incX > 0; incy < 0.
                    if ((incX > 0) && (incY < 0)) {
                        // Passem al quadrant 1
                        xDirection1 = xDirection10; yDirection1 = -yDirection10;
                        // Futures direccions per la segona bola després del xoc
                        xDirection2 = xDirection20; yDirection2 = -yDirection20;
                        // Per calcular els angles sempre en el primer quadrant
                        incY = -incY;
                        Log.d("inc", "Quadrant 3 xD " + xDirection1 + " yD: " + yDirection1);

                    }
                    // Disparem des del quadrat 4: incX > 0; incy > 0.
                    if ((incX < 0) && (incY < 0)) {
                        // Passem al quadrant 2
                        xDirection1 = -xDirection10; yDirection1 = -yDirection10;
                        xDirection2 = -xDirection20; yDirection2 = -yDirection20;
                        // Per calcular els angles sempre en el primer quadrant
                        incX = -incX;
                        incY = -incY;
                        Log.d("inc", "Quadrant 4 xD " + xDirection1 + " yD: " + yDirection1);
                    }
                }
                // Calcul de l'angle: En el quadrant 1. De 0 a 90º
                alfa1 = (float) Math.atan(incY/incX);

                // Calcul de la distancia => factor acceleració
                potenciaDispar1 = (float) Math.hypot(x1 - xDit, y1 - yDit) / potenciaMax;
                Log.d ("posicio", "Potencia: " + potenciaDispar1);
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
                enMovimentBola1 = true;
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
        x1 = getWidth() / 2;
        y1 = getHeight() / 2 ;
        // Valors inicials
        x0 = x1;  y0 = y1;   // La bola que es mou
        x2 = x1;  y2 = 3*y1/2; // La bola parada
        // Potencia de dispar màxima: des de la pilota a l'extrem inferior esquerre
        potenciaMax =(float) Math.hypot(x1 - 0, y1 - getHeight()) / 10;
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
                Log.d ("posicio", "x: " + x1 + " y: " + y1);
                // Hem posat la bola en moviment:
                int k; // Constant per separar les dues boles i evitar entrar en bucle després del xoc
                // Disminució de velocitat
                if (xoc) {
                    k = 1; xoc = false;
                }
                else k = 10;
                if (enMovimentBola1) { // Variables inicials despres del dispar
                    x1 += xDirection1 * Math.cos(alfa1)* potenciaDispar1 / k;
                    y1 += yDirection1 * Math.sin(alfa1)* potenciaDispar1 / k;
                    potenciaDispar1 = (float) (potenciaDispar1 - 0.01);
                    // Es para la bola
                    if (potenciaDispar1 < 0)  enMovimentBola1 = false;
                }
                Log.d ("posicio", "Moviment bola 2: " + enMovimentBola2);
                if (enMovimentBola2) { // Variables inicials despres del dispar
                    // Disminució de velocitat
                    x2 += xDirection2 * Math.cos(alfa2)* potenciaDispar2 / k;
                    y2 += yDirection2 * Math.sin(alfa2)* potenciaDispar2 / k;
                    potenciaDispar2 = (float) (potenciaDispar2 - 0.01);
                    // Es para la bola
                    if (potenciaDispar2 < 0)  enMovimentBola2 = false;
                }

                // La pilotes se'n ixen de la pantalla => Rebot
                if ((x1 + radi1 > getWidth()) || (x1 - radi1 < 0 )){
                    xDirection1 = -xDirection1;
                    // Per evitar bucles en els bordes
                    if (x1 + radi1 >  getWidth()) {
                        x1 = getWidth() - radi1;
                    }
                    if (x1 - radi1 < 0) {
                        x1 = radi1;
                    }
                }
                if ((y1 + radi1 > getHeight()) || (y1 - radi1 < 0 )){
                    yDirection1 = -yDirection1;
                    // Per evitar bucles en els bordes
                    if (y1 + radi1 >  getHeight()) {
                        y1 = getHeight() - radi1;
                    }
                    if (y1 - radi1 < 0) {
                        y1 = radi1;
                    }
                }
                if ((x2 + radi2 > getWidth()) || (x2 - radi2 < 0 )){
                    xDirection2 = -xDirection2;
                    // Per evitar bucles en els bordes
                    if (x2 + radi2 >  getWidth()) {
                        x2 = getWidth() - radi2;
                    }
                    if (x2 - radi2 < 0) {
                        x2 = radi2;
                    }
                }
                if ((y2 + radi2 > getHeight()) || (y2 - radi2 < 0 )){
                    yDirection2 = -yDirection2;
                    // Per evitar bucles en els bordes
                    if (y2 + radi2 >  getHeight()) {
                        y2 = getHeight() - radi2;
                    }
                    if (y2 - radi2 < 0) {
                        y2 = radi2;
                    }
                }


                // Xoc entre boles
                // Fórmula: Distància entre centres <= suma de radis
                // Distància
                double distanciaCentres = Math.hypot( (x2-x1), (y2 - y1));
                Log.d ("posicio", "Distancia centres: " + distanciaCentres + " Suma radis: " + radi1 + radi2);
                // Han xocat!!
                if (distanciaCentres < radi1 + radi2 + 10) { // Sumem 5 per evitar entraren bucle
                     Log.d ("posicio", "Ha xocat! Distancia centres: " + distanciaCentres + " Suma radis: " + radi1 + radi2);
                    // m1 = tan (alfa1)
                    double m1 = Math.tan(alfa1);  //Recta bola en moviment
                    // m2 = pendent recta que passa pels centres de les dues boles
                    double m2 = (x2 - x1) / (y2 - y1);

                    // Angle format per les dues rectes: tan (beta) = (m2 - m1) / (1 + m1*m2))
                    double beta = Math.atan((m2 - m1) / (1 + m1*m2));

                    Log.d ("posicio", "Angle: " + Math.toDegrees(beta));
                    // Noves direccions  per a les boles
                    incX = x2 - x1; incY = y2 - y1;
                    if ((incX < 0) && (incY > 0)) { // Bola 1 ve del 1r quadrant
                        xDirection1 = -xDirection10;  yDirection1 = yDirection10;
                        // Direcció bola 2
                        xDirection2 = -xDirection20;  yDirection2 = yDirection20;
                    }
                    if ((incX > 0) && (incY > 0)) { // Bola 1 ve del 2n quadrant
                        xDirection1 = -xDirection10;  yDirection1 = yDirection10;
                        xDirection2 = xDirection20;  yDirection2 = yDirection20;
                    }
                    if ((incX > 0) && (incY < 0)) {
                        xDirection1 = -xDirection10;  yDirection1 = yDirection10;
                        xDirection2 = xDirection20; yDirection2 = -yDirection20;
                    }
                    if ((incX < 0) && (incY < 0)) {
                        xDirection1 = -xDirection10;  yDirection1 = yDirection10;
                        xDirection2 = -xDirection20; yDirection2 = yDirection20;
                    }
                    // Nous vectors de velocitat
                    // Abans de modificar potenciaDispar1 hem de calcular potenciaDispar2!!
                    potenciaDispar2 = (float) Math.abs(potenciaDispar1* Math.sin(beta));
                    // Nou valor per a potenciaDispar1
                    potenciaDispar1 = (float) Math.abs(potenciaDispar1* Math.cos(beta));
                    Log.d ("posicio", "Potencia dispar 1: " + potenciaDispar1);
                    Log.d ("posicio", "Potencia dispar 2: " + potenciaDispar2);
                    Log.d ("posicio", "Angle: " + Math.toDegrees(beta));
                    // Nous valors xDirection, yDirection
                    // Nous angles
                    alfa1 = (float) beta;
                    alfa2 = (float) (Math.PI/ 2 - beta);
                    // Moviment de les dues boles
                    enMovimentBola2 = true;
                    xoc = true;
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




