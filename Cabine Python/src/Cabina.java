import java.io.Serializable;

public class Cabina implements Serializable, Comparable<Cabina> {
    private String codCabina;
    private int nrPaturi;
    private float tarifZiNoapte;
    private int nrzileAn;
    private static final int nr_zile_an = 365;

    public Cabina(String codCabina, int nrPaturi, float tarifZiNoapte, int nrzileAn) {
        this.codCabina = codCabina;
        this.nrPaturi = nrPaturi;
        this.tarifZiNoapte = tarifZiNoapte;
        this.nrzileAn = nrzileAn;
    }

    public Cabina() {
        this.codCabina = "1";
        this.nrPaturi = 1;
        this.tarifZiNoapte = 150 ;
        this.nrzileAn = 320;
    }

    public String getCodCabina() {
        return codCabina;
    }

    public void setCodCabina(String codCabina) {
        this.codCabina = codCabina;
    }

    public int getNrPaturi() {
        return nrPaturi;
    }

    public void setNrPaturi(int nrPaturi) {
        this.nrPaturi = nrPaturi;
    }

    public float getTarifZiNoapte() {
        return tarifZiNoapte;
    }

    public void setTarifZiNoapte(float tarifZiNoapte) {
        this.tarifZiNoapte = tarifZiNoapte;
    }

    public int getNrzileAn() {
        return nrzileAn;
    }

    public void setNrzileAn(int nrzileAn) {
        this.nrzileAn = nrzileAn;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cabina{");
        sb.append("codCabina='").append(codCabina).append('\'');
        sb.append(", nrPaturi=").append(nrPaturi);
        sb.append(", tarifZiNoapte=").append(tarifZiNoapte);
        sb.append(", nrzileAn=").append(nrzileAn);
        sb.append('}');
        return sb.toString();
    }

    float tarifPerPat(){
        if(nrPaturi == 0)
            return 0;
        else {
            return tarifZiNoapte/nrPaturi;
        }
    }


    @Override
    public int compareTo(Cabina other) {
        return Float.compare(this.tarifPerPat(),other.tarifPerPat());
    }

    public float procent_ocupare(){
        return (nrzileAn * 100.0f)/nr_zile_an;
    }
}
