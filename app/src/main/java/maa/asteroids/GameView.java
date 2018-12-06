package maa.asteroids;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import maa.asteroids.Graphic;
import maa.asteroids.R;

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

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide, drawableMisil;
        Resources resources = context.getResources();
        drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.asteroide1);
        nave = new Graphic(this, resources.getDrawable(R.drawable.nave));
        asteroides = new ArrayList<Graphic>();
        for (int i = 0; i < numAsteroides; i++) {
            Graphic asteroide = new Graphic(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
// Una vez que conocemos nuestro ancho y alto.
        for (Graphic asteroide : asteroides) {
            //do {
                asteroide.setPosX((int) (Math.random()*ancho));
                asteroide.setPosY((int) (Math.random()*alto));
            //} while(asteroide.distancia(nave) < (ancho+alto)/5);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Graphic asteroide : asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
    }
}
