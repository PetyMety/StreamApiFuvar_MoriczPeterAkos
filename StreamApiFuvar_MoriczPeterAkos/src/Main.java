import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class Main {
    private static  List<Fuvar> fuvarok = new ArrayList<>();
    public static void main(String[] args){
        try {
            readFile("fuvar.csv");
            System.out.printf("1. %d utazás került feljegyzésre az állományban!%n", fuvarok.size());
            System.out.printf("2. 6185-ös taxis bevétele: %s, fuvarok száma: %s!%n", getSumTaxis(), getCoutTaxi());
            System.out.printf("3. Összesen megtett mérföldek: %s.%n", getSumMerfold());
            System.out.printf("4. Leghosszabb fuvar: %s.%n", getMaxTavolsag());
            System.out.printf("5. Legbőkezűbb fuvar: %s%n", legbokezubbFuvar());
            System.out.printf("6. 4261-es taxis megtett km-ek: %s%n", getTaxisKm());
            System.out.printf("7. Hibás fuvarok száma: %d%n", getHibasFuvarokSzama());
            System.out.printf("8. Szerepel-e a 1452-es azonosítójú taxi: %b%n", isTaxiExists(1452));
            //System.out.printf("9. A 3 legrövidebb utazás: %s%n", getHaromLeghosszabbFuvar());
            System.out.printf("10. December 24.-én hány fuvar volt: %d%n", getFuvarokSzamaDecember24());
            System.out.printf("11. December 31.-én a borravaló arány: %.2f%%%n", getBorravaloAranyDecember31());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    // Beolvas
    private static void readFile(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        bufferedReader.readLine();
        String line = bufferedReader.readLine();
        while (line != null && !line.isEmpty()){
            fuvarok.add(new Fuvar(line));
            line= bufferedReader.readLine();
        }
    }

    // 2. Feladat
    private static double getSumTaxis(){
        return fuvarok.stream().filter(fuvar -> fuvar.getTaxi_id() == 6185)
                .mapToDouble(fuvar -> fuvar.getViteldij() + fuvar.getBorravalo())
                .sum();
    }
    private static long getCoutTaxi(){
        return fuvarok.stream().filter(fuvar -> fuvar.getTaxi_id()== 6185).count();
    }
    // 3.Feladat
    private static double getSumMerfold(){
        return fuvarok.stream().mapToDouble(Fuvar::getTavolsag).sum();
    }
    // 4. Feladat
    private static Fuvar getMaxTavolsag(){
        return fuvarok.stream().max(Comparator.comparingDouble(Fuvar::getTavolsag)).orElse(null);
    }
    // 5. Feladat
    private static Fuvar legbokezubbFuvar(){
        return fuvarok.stream().max(Comparator.comparingDouble(fuvar -> fuvar.getBorravalo() / fuvar.getViteldij())).orElse(null);
    }
    // 6. Feladat
    private static double getTaxisKm(){
        return fuvarok.stream().filter(fuvar -> fuvar.getTaxi_id()== 4261).mapToDouble(Fuvar::getTavolsag).sum()*1.6;
    }
    // 7. Feladat
    private static long getHibasFuvarokSzama() {
        return fuvarok.stream()
                .filter(fuvar -> fuvar.getIdotartam() > 0 && fuvar.getViteldij() > 0 && fuvar.getTavolsag() == 0)
                .count();
    }
    // 8. Feladat
    private static boolean isTaxiExists(int taxiId) {
        return fuvarok.stream().anyMatch(fuvar -> fuvar.getTaxi_id() == taxiId);
    }
    // 9. Feladat
    //private static List<Fuvar> getHaromLeghosszabbFuvar() {
    //    return fuvarok.stream()
    //            .filter(fuvar -> fuvar.getIdotartam() > 0)
    //            .sorted(Comparator.comparingInt(Fuvar::getIdotartam))
    //            .limit(3)
    //            .toList();
    //}
    // 10. Feladat
    private static long getFuvarokSzamaDecember24() {
        return fuvarok.stream()
                .filter(fuvar -> fuvar.getIndulasi_ido().startsWith("2016-12-24"))
                .count();
    }
    // 11. Feladat
    private static double getBorravaloAranyDecember31() {
        double totalViteldij = fuvarok.stream()
                .filter(fuvar -> fuvar.getIndulasi_ido().startsWith("2016-12-31"))
                .mapToDouble(Fuvar::getViteldij)
                .sum();

        double totalBorravalo = fuvarok.stream()
                .filter(fuvar -> fuvar.getIndulasi_ido().startsWith("2016-12-31"))
                .mapToDouble(Fuvar:: getBorravalo)
                .sum();

        return totalViteldij > 0 ? (totalBorravalo / totalViteldij) * 100 : 0;
    }
}