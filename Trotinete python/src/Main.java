import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Citirea fisierului trotinete.txt si stocarea datelor intr-un Map
        Map<String, Trotineta> trotineteMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("trotinete.txt"))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                String[] split = linie.split("\t");
                if (split.length == 4) {
                    String id = split[0];
                    float distantaTotala = Float.parseFloat(split[1]);
                    float vitezaMedie = Float.parseFloat(split[2]);
                    float vitezaMaxima = Float.parseFloat(split[3]);
                    Trotineta trotineta = new Trotineta(id, distantaTotala, vitezaMedie, vitezaMaxima);
                    trotineteMap.put(id, trotineta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. Afisarea trotinetelor cu viteza maxima mai mare de 50 km/h
        System.out.println("Trotinete cu viteza maximă mai mare de 50 km/h:");
        for (Trotineta t : trotineteMap.values()) {
            if (t.getVitezaMaxima() > 50) {
                System.out.println(t);
            }
        }

        // 2. Gruparea trotinetelor dupa viteza medie si afisarea numarului de trotinete si suma distantelor
        System.out.println("\nTrotinete grupate după viteza medie:");
        Map<Float, List<Trotineta>> grupate = trotineteMap.values().stream()
                .collect(Collectors.groupingBy(Trotineta::getVitezaMedie));

        for (Map.Entry<Float, List<Trotineta>> entry : grupate.entrySet()) {
            float vitezaMedie = entry.getKey();
            List<Trotineta> trotinete = entry.getValue();

            double sumaDistante = trotinete.stream()
                    .mapToDouble(Trotineta::getDistantaTotala)
                    .sum();

            System.out.println("Viteza medie " + vitezaMedie + " km/h -> " + trotinete.size() + " trotinete, suma distanțelor parcurse " + sumaDistante + " km");
        }

        // 3. Salvarea trotinetelor rapide intr-un fisier binar
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("trotineteRapide.dat"))) {
            for (Trotineta t : trotineteMap.values()) {
                if (t.getVitezaMedie() > 14 || t.getVitezaMaxima() > 50) {
                    oos.writeObject(t);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. Citirea trotinetelor din fișierul binar si afisarea lor
        System.out.println("\nTrotinete citite din fișierul binar:");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("trotineteRapide.dat"))) {
            Trotineta t;
            while (true) {
                t = (Trotineta) ois.readObject();
                System.out.println(t);
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
