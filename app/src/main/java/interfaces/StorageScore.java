package interfaces;

import java.util.List;

public interface StorageScore {
    void storeScore(int puntos,String nombre,long fecha);
    List<String> listScore(int cantidad);
}
