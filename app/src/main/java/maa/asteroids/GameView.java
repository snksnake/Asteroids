package maa.asteroids;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameView extends View {
    private List<Graphic> asteroides; // Lista con los Asteroides
    private int numAsteroides = 5; // Número inicial de asteroides
    private int giroNave; // Incremento de dirección
    private double aceleracionNave; // aumento de velocidad
    private static final int MAX_VELOCIDAD_NAVE = 20;
    // Incremento estándar de giro y aceleración
    private int numFragmentos = 3; // Fragmentos en que se divide
    // //// NAVE //////
    private Graphic nave; // Gráfico de la nave
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;
    // //// THREAD Y TIEMPO //////
// Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;
    Drawable drawableNave;
    Drawable drawableAsteroide;
    Drawable drawableMisil;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = context.getResources();
        //drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.asteroide1);

        SharedPreferences pref = PreferenceManager. getDefaultSharedPreferences(getContext());
        if (pref.getString("graficos", "1").equals("0")) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);
            ShapeDrawable dAsteroide = new ShapeDrawable( new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide =
                    AppCompatResources.getDrawable(context, R.drawable.asteroide1);
            drawableNave =
                    AppCompatResources.getDrawable(context, R.drawable.nave);
        }

        asteroides = new ArrayList<>();
        for (int i = 0; i < numAsteroides; i++) {
            Graphic asteroide = new Graphic(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
        nave = new Graphic(this, drawableNave);
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        nave.setCenX(ancho / 2);
        nave.setCenY(alto / 2);
// Una vez que conocemos nuestro ancho y alto.
        for (Graphic asteroide : asteroides) {
            do {
                asteroide.setCenX((int) (Math.random()*ancho));
                asteroide.setCenY((int) (Math.random()*alto));
            } while(asteroide.distancia(nave) < (ancho+alto)/5);
        }
        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Graphic asteroide : asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
        nave.dibujaGrafico(canvas);
    }
    protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return; // Salir si el período de proceso no se ha cumplido.
        }
        // Para una ejecución en tiempo real calculamos el factor de movimiento
        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
// Actualizamos velocidad y dirección de la nave a partir de // giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;
// Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX, nIncY) <= MAX_VELOCIDAD_NAVE) {
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(factorMov); // Actualizamos posición
        for (Graphic asteroide : asteroides) {
            asteroide.incrementaPos(factorMov);
        }
    }

    private class ThreadJuego extends Thread {
        @Override
        public void run() {
            while (true) {
                actualizaFisica();
            }
        }
    }
}
