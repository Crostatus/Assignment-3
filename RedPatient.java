public class RedPatient extends Thread{

    Hospital prontoSoccorso;
    private long visitTimeNeeded;
    private int times;

    public RedPatient(Hospital newProntoSoccorso){
        this.prontoSoccorso = newProntoSoccorso;
        this.times = 3;
        this.visitTimeNeeded = 5000;
        Thread.currentThread().setPriority(10);
    }

    public void run(){
        System.out.println("                                                    Sono rosso, " + Thread.currentThread().getName() + " e sono sveglio!");
        prontoSoccorso.lockMedici.lock();
        try {
            //if (!prontoSoccorso.redPatientCanGoIn()) {
                prontoSoccorso.redPatients++;
            //}
            while (!prontoSoccorso.redPatientCanGoIn()) {
                try {
                    prontoSoccorso.yourTurn.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            prontoSoccorso.redPatientGotIn();
            System.out.println("E' entrato il paziente rosso " + Thread.currentThread().getName());
        }
        finally {
            prontoSoccorso.lockMedici.unlock();
        }

        Main.pause(visitTimeNeeded);

        prontoSoccorso.lockMedici.lock();
        try {
            System.out.println("E' uscito il paziente rosso " + Thread.currentThread().getName());
            prontoSoccorso.redPatientGettingOut();
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
