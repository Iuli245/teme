import java.io.Serializable;
import java.util.Objects;

public class Trotineta implements Serializable, Comparable<Trotineta> {
    private static final long serialVersionUID = 1L;

    private String id;
    private float distantaTotala;
    private float vitezaMedie;
    private float vitezaMaxima;

    public Trotineta() {
        this.id = "";
        this.distantaTotala = 0f;
        this.vitezaMedie = 0f;
        this.vitezaMaxima = 0f;
    }

    public Trotineta(String id, float distantaTotala, float vitezaMedie, float vitezaMaxima) {
        this.id = id;
        this.distantaTotala = distantaTotala;
        this.vitezaMedie = vitezaMedie;
        this.vitezaMaxima = vitezaMaxima;
    }

    public String getId() { return id; }
    public float getDistantaTotala() { return distantaTotala; }
    public float getVitezaMedie() { return vitezaMedie; }
    public float getVitezaMaxima() { return vitezaMaxima; }

    @Override
    public String toString() {
        return "ID: " + id + ", Distanta: " + distantaTotala + " km, Viteza medie: " + vitezaMedie + " km/h, Viteza maxima: " + vitezaMaxima + " km/h";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trotineta trotineta = (Trotineta) obj;
        return Math.abs(this.distantaTotala - trotineta.distantaTotala) < 10;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distantaTotala);
    }

    @Override
    public int compareTo(Trotineta o) {
        if (Math.abs(this.distantaTotala - o.distantaTotala) < 10) {
            return 0;
        }
        return Float.compare(this.distantaTotala, o.distantaTotala);
    }
}