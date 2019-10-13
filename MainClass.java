import java.io.*;
import java.util.Scanner;

public class MainClass {
    
    public static void main(String[] args) {
        //Parametri richiesti dalla MainClass: numberOfRedPatients numberOfYellowPatients numberOfWhitePatients
        //    (int) numberOfRedPatients    >= 0 corrisponde al numero di pazienti con codice rosso che si presenteranno.
        //    (int) numberOfYellowPatients >= 0 corrisponde al numero di pazienti con codice giallo che si presenteranno.
        //    (int) numberOfWhitePatients  >= 0 corrisponde al numero di pazienti con codice bianco che si presenteranno.
        String inputFile;
        try {
            inputFile = args[0];
        } catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("File di input assente.");
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(inputFile));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File di input non trovato.");
        }
        int[] patientsNumbers = new int[3];
        int i = 0;
        while (scanner.hasNextInt()){
            patientsNumbers[i] = scanner.nextInt();
            i++;
        }
        scanner.close();

        Hospital twoPointHospital = new Hospital();
        Patient nextPatient;

        for(i = 0; i < patientsNumbers[0]; i++) {
            nextPatient = new RedPatient(twoPointHospital);
            nextPatient.start();
        }
        for(i = 0; i < patientsNumbers[1]; i++) {
            nextPatient = new YellowPatient(twoPointHospital);
            nextPatient.start();
        }
        for(i = 0; i < patientsNumbers[2]; i++) {
            nextPatient = new WhitePatient(twoPointHospital);
            nextPatient.start();
        }

    }

    public static void pause(long millis){
        try{
            Thread.sleep(millis);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
