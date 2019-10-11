public class WhitePatient extends Thread{

    Hospital prontoSoccorso;
    int times;
    long visitTimeNeeded;

    public WhitePatient(Hospital newProntoSoccorso){
        this.prontoSoccorso = newProntoSoccorso;
        this.times = 3;
        this.visitTimeNeeded = 2000;
        Thread.currentThread().setPriority(1);
    }

    public void run(){
        System.out.println("                                                    Sono bianco, " + Thread.currentThread().getName() + " e sono sveglio!");
        int medicNeeded;
        prontoSoccorso.lockMedici.lock();
        try {
            while (prontoSoccorso.redPatientsInQueue()) {
                try {
                    prontoSoccorso.yourTurn.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while ((medicNeeded = prontoSoccorso.whitePatientCanGoIn()) < 0) {
                try {
                    prontoSoccorso.yourTurn.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("E' entrato il paziente bianco " + Thread.currentThread().getName() + " dal medico " + medicNeeded);
            prontoSoccorso.whitePatientGotIn(medicNeeded);
            prontoSoccorso.yourTurn.signalAll();
        }
        finally{
            prontoSoccorso.lockMedici.unlock();
        }

        Main.pause(visitTimeNeeded);

        prontoSoccorso.lockMedici.lock();
        try{
            System.out.println("E' uscito il paziente bianco " + Thread.currentThread().getName());
            prontoSoccorso.whitePatientGettingOut(medicNeeded);
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
