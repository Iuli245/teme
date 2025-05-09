import java.io.*;
        import java.util.*;
        import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Achizitie> achizitii = new ArrayList<>();
        // Citirea fisierului achizitii.txt
        try (BufferedReader br = new BufferedReader(new FileReader("achizitii.txt"))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                String[] split = linie.split(",");
                if (split.length == 6) {
                    String codProdus = split[0];
                    int an = Integer.parseInt(split[1]);
                    int luna = Integer.parseInt(split[2]);
                    int zi = Integer.parseInt(split[3]);
                    int cantitate = Integer.parseInt(split[4]);
                    float pretUnitar = Float.parseFloat(split[5]);
                    Achizitie achizitie = new Achizitie(codProdus, an, luna, zi, cantitate, pretUnitar);
                    achizitii.add(achizitie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2) Filtrarea achizitiilor din prima jumatate a lunii si cantitate > 100
        System.out.println("Achiziții efectuate în prima jumătate a lunii și cantitate > 100:");
        for (Achizitie a : achizitii) {
            if (a.getZi() <= 15 && a.getCantitate() > 100) {
                System.out.println(a);
            }
        }

        // 3) Gruparea si afisarea produselor in functie de numarul de achizitii si valoarea totala
        System.out.println("\nProduse grupate după numărul de achiziții și valoarea totală:");
        Map<String, List<Achizitie>> produseGrupate = achizitii.stream()
                .collect(Collectors.groupingBy(Achizitie::getCodProdus));

        List<Map.Entry<String, List<Achizitie>>> produseSortate = new ArrayList<>(produseGrupate.entrySet());
        produseSortate.sort((entry1, entry2) -> {
            double valoare1 = entry1.getValue().stream().mapToDouble(Achizitie::valoare).sum();
            double valoare2 = entry2.getValue().stream().mapToDouble(Achizitie::valoare).sum();
            return Double.compare(valoare2, valoare1);
        });

        for (Map.Entry<String, List<Achizitie>> entry : produseSortate) {
            String codProdus = entry.getKey();
            List<Achizitie> achizitiiProdus = entry.getValue();
            int numarAchizitii = achizitiiProdus.size();
            double valoareTotala = achizitiiProdus.stream().mapToDouble(Achizitie::valoare).sum();
            System.out.println("Produs " + codProdus + " -> " + numarAchizitii + " achiziții, valoare totală " + valoareTotala + " Lei");
        }

        // 4) Salvarea produselor frecvente in fisierul binar
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("produseFrecvente.dat"))) {
            for (Map.Entry<String, List<Achizitie>> entry : produseGrupate.entrySet()) {
                if (entry.getValue().size() > 3) {
                    oos.writeObject(entry.getValue().get(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5) Citirea produselor frecvente din fisierul binar
        System.out.println("\nAchizitii frecvente citite din fisierul binar:");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("produseFrecvente.dat"))) {
            while (true) {
                Achizitie a = (Achizitie) ois.readObject();
                System.out.println(a);
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


