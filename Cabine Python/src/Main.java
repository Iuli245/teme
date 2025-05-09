import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {

        String numeFisier = "cabineCroaziera.txt";
        String fisierBinar = "cabineCroaziera.dat";
        Map<String, Cabina> cabinaMap = new HashMap<>();

        //citire
        try(BufferedReader br = new BufferedReader(new FileReader(numeFisier))){
            String linie;
            while((linie = br.readLine()) != null )
            {
                String[] tokens = linie.split(",");
                if(tokens.length == 4)
                {
                    String cod = tokens[0].trim();
                    int nrPaturi = Integer.parseInt(tokens[1].trim());
                    float tarifZiNoapte = Float.parseFloat(tokens[2].trim());
                    int nrzileAn = Integer.parseInt(tokens[3].trim());
                    Cabina c = new Cabina(cod,nrPaturi,tarifZiNoapte,nrzileAn);
                    cabinaMap.put(cod, c);
                }
            }
        }
        catch (IOException e){
            System.err.println("Eroare la citirea fisierului: " +e.getMessage());
        }

        //Cabinele cu un procent de ocupare < 45%
        System.out.println("Cabinele cu un procentaj de ocupare mai mic de 45%: ");
        for(Map.Entry<String, Cabina> entry : cabinaMap.entrySet())
        {
            if(entry.getValue().procent_ocupare()<45.0f)
                System.out.println(entry.getValue());
        }

        //Grupare dupa nr paturi si media zilelor ocupate
        System.out.println("\n Media zilelor ocupate pentru fiecare tip de cabina: ");
        Map<Integer, List<Cabina>> grupare = cabinaMap.values().stream().collect(Collectors.groupingBy(Cabina::getNrPaturi));

        for(Map.Entry<Integer, List<Cabina>> entry: grupare.entrySet()){
            double medie = entry.getValue().stream().mapToInt(Cabina::getNrzileAn).average().orElse(0);
            System.out.printf("%d paturi -> %.2f zile\n", entry.getKey(), medie);
        }

        //salvare cabine %ocupare >75%
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisierBinar))){
            for(Cabina c : cabinaMap.values())
            {
                if(c.procent_ocupare() > 75.0f)
                    out.writeObject(c);
            }
            System.out.println("\n Cabinele profitabile au fost salvate in fisierul: "+fisierBinar+".");
        } catch (IOException e) {
            System.err.println("\n Eroare la salvarea in fisierul binar. "+ e.getMessage());
        }

        //citirea din fisierul binar
        System.out.println("\n Citirea din fisierul cabineCroaziera.dat");
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fisierBinar))){
            while(true){
                try{
                    Cabina c = (Cabina) in.readObject();
                    System.out.println(c);
                }
                catch (EOFException eof){
                    break;
                }
            }
        }
        catch(IOException | ClassNotFoundException e){
            System.err.println("\n Eroare la citirea din fisierul binar. " +e.getMessage());
        }




    }
}