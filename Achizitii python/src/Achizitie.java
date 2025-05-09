import java.io.Serializable;

public class Achizitie implements Comparable<Achizitie>, Serializable {
    private String codProdus;
    private int an, luna, zi;
    private int cantitate;
    private float pretUnitar;

    public Achizitie(String codProdus, int an, int luna, int zi, int cantitate, float pretUnitar) {
        this.codProdus = codProdus;
        this.an = an;
        this.luna = luna;
        this.zi = zi;
        this.cantitate = cantitate;
        this.pretUnitar = pretUnitar;
    }

    public Achizitie() {}

    public String getCodProdus() {
        return codProdus;
    }

    public int getAn() {
        return an;
    }

    public int getLuna() {
        return luna;
    }

    public int getZi() {
        return zi;
    }

    public int getCantitate() {
        return cantitate;
    }

    public float getPretUnitar() {
        return pretUnitar;
    }

    public float valoare() {
        return cantitate * pretUnitar;
    }

    @Override
    public String toString() {
        return "Cod produs: " + codProdus + ", Data achiziției: " + an + "-" + luna + "-" + zi +
                ", Cantitate: " + cantitate + ", Preț unitar: " + pretUnitar + " Lei";
    }

    @Override
    public int compareTo(Achizitie o) {
        return Float.compare(this.valoare(), o.valoare());
    }
}