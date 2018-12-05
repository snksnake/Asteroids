package maa.asteroids;

import java.util.ArrayList;
import java.util.List;

import interfaces.StorageScore;

public class StorageScoreList implements StorageScore {
    private List<String> scores;
    public StorageScoreList() {
        scores = new ArrayList<String>();
        scores.add("215031 APCAMACHO");
        scores.add("157855 CZDIAZ");
        scores.add("126680 ADZORZANO");
    }
    @Override
    public void storeScore(int puntos, String nombre, long fecha) {
        scores.add(0, puntos + " " + nombre);
    }
    @Override
    public List<String> listScore(int cantidad) {
        return scores;
    }
}
