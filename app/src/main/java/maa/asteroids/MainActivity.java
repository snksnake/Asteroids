package maa.asteroids;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.prefs.AbstractPreferences;

import interfaces.StorageScore;

public class MainActivity extends AppCompatActivity {
    Button btnAbout, btnExit, btnSettings, btnPlay;
    public static StorageScore scores = new StorageScoreList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setContentView(R.layout.main_landscape);
        }
        else
        {
            setContentView(R.layout.main_portrait);
        }

        btnAbout = findViewById(R.id.btn_about);
        btnExit = findViewById(R.id.btn_exit);
        btnSettings = findViewById(R.id.btn_confi);
        btnPlay = findViewById(R.id.btn_play);

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, Game.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("CONFIG", "Pantalla en Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d("CONFIG", "Pantalla en Portrait");
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true; /** true -> el menú ya está visible */
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.aboutId) {
            final Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.score_list) {
            final Intent intent = new Intent(MainActivity.this, Score.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPreferences(){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica",true)
                +", gráficos: " + pref.getString("graficos","?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
