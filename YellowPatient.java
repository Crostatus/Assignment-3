import java.util.Random;

public class YellowPatient extends Thread{

    Hospital prontoSoccorso;
    int medicNeeded;
    int times;
    long visitTimeNeeded;

    public YellowPatient(Hospital newProntoSoccorso){
        Random randomNumberGenerator = new Random();
        this.prontoSoccorso = newProntoSoccorso;
        this.times = 3;
        this.medicNeeded = randomNumberGenerator.nextInt(9);
        this.visitTimeNeeded = 3000;
        Thread.currentThread().setPriority(5);
    }

    public void run(){
            System.out.println("                                                    Sono giallo, " + Thread.currentThread().getName() + " e sono sveglio!");
            prontoSoccorso.lockMedici.lock();
            try {
                while (prontoSoccorso.redPatientsInQueue()) {
                    try {
                        prontoSoccorso.yourTurn.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!prontoSoccorso.yellowPatientCanGoIn(medicNeeded))
                    prontoSoccorso.yellowPatients[medicNeeded]++;

                while (!prontoSoccorso.yellowPatientCanGoIn(medicNeeded)) {
                    try {
                        prontoSoccorso.yourTurn.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("E' entrato il paziente giallo " + Thread.currentThread().getName() + " dal medico " + medicNeeded);
                prontoSoccorso.yellowPatientGotIn(medicNeeded);
                prontoSoccorso.yourTurn.signalAll();
            }
        finally {
            prontoSoccorso.lockMedici.unlock();
        }

        Main.pause(visitTimeNeeded);

        prontoSoccorso.lockMedici.lock();
        try{
            System.out.println("E' uscito il paziente giallo " + Thread.currentThread().getName());
            prontoSoccorso.yellowPatientGettingOut(medicNeeded);
        }
        finally{
            prontoSoccorso.lockMedici.unlock();
        }

        times--;
        if(times > 0){
            Main.pause(1000);
            run();
        }
    }


}
