
import java.lang.Math;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.Lock;

public class patient extends Thread {
    private static Integer i = 1;

    private Vector<Lock> medics;
    private int priority;
    private int patientId;
    private long visitTimeNeeded; //tempo richiesto per essere operato
    private int times; //quante volte il paziente deve essere operato
    private final patientQueueArrayList waitingRoom;

    //il paziente con codice giallo ha anche uno specifico dottore richiesto
    private int medicNeeded;
    //il paziente con codice rosso ha bisogno di tutti i medici contemporaneamente
    //il paziente con codice bianco ha bisogno di un medico a caso

    public patient(String code, patientQueueArrayList newWaitingRoom) {
        this.medics = new Vector<>();
        this.waitingRoom = newWaitingRoom;
        medicNeeded = 0;
        Random randomNumberGenerator = new Random();
        times = randomNumberGenerator.nextInt(5) + 1;
        switch (code) {
            case "red": {
                this.priority = 3;
                visitTimeNeeded = timeNeeded(3);
                break;
            }
            case "yellow": {
                this.priority = 2;
                this.medicNeeded = randomNumberGenerator.nextInt(9);
                visitTimeNeeded = timeNeeded(2);
                break;
            }
            case "white": {
                this.priority = 1;
                visitTimeNeeded = timeNeeded(1);
                break;
            }
        }
        this.patientId = i;
        i++;
    }

    public void run() {
        int i;
        for(i = 0; i < medics.size(); i++){
            try{
                medics.elementAt(i).lock();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": Mi stanno a servì tutti");
        Main.pause(visitTimeNeeded);
        for(i = 0; i < medics.size(); i++){
            try{
                medics.elementAt(i).unlock();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": M' hanno servito tutti.");
        if(times > 0){
            times--;
            Main.pause(1000);
            //waitingRoom.put(this);
        }
    }

    public void addReadyMedic(Lock readyMedic){
        medics.add(readyMedic);
    }

    public int getTimes(){
        return this.times;
    }

    private static long timeNeeded(int priority) {
        Random randomNumberGenerator = new Random();
        long time = 0;
        switch (priority) {
            case 3: {
                time = randomNumberGenerator.nextInt(3000) + 7000;
                break;
            }
            case 2: {
                time = randomNumberGenerator.nextInt(3000) + 4000;
                break;
            }
            case 1: {
                time = randomNumberGenerator.nextInt(3000) + 1000;
                break;
            }
        }
        return time;
    }

    public void presentation() {
        System.out.println("---------------------------------------------------------");
        System.out.println("Paziente " + this.patientId);
        System.out.println("Tempo di visita richiesto: " + visitTimeNeeded);
        System.out.println("Priorità: " + this.priority);
        System.out.println("Volte che devono operarmi: " + this.times);
        if (this.priority == 2)
            System.out.println("Medico richiesto: " + this.medicNeeded);
        System.out.println("---------------------------------------------------------");
    }

    public int getCode() {
        return this.priority;
    }

}

