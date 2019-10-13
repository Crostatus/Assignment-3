//Un paziente con codice giallo è un paziente con priorià minima che può essere curato da qualsiasi medico.
public class WhitePatient extends Patient{

    public WhitePatient(Hospital newProntoSoccorso){
        super(newProntoSoccorso);
        Thread.currentThread().setPriority(1);
        this.visitTimeNeeded += 500;
    }

    public void run(){
        int medicNeeded;
        prontoSoccorso.lockMedici.lock();
        try {
            givePriorityToRedPatients();

            medicNeeded = waitMyTurn();

            prontoSoccorso.WYPatientGotIn(medicNeeded, "white");
            printPatientGotIn("bianco", medicNeeded);
            prontoSoccorso.yourTurn.signalAll();
        }
        finally{
            prontoSoccorso.lockMedici.unlock();
        }
        //inizio visita
        MainClass.pause(visitTimeNeeded);
        //termine visita
        prontoSoccorso.lockMedici.lock();
        try{
            prontoSoccorso.WYPatientGettingOut(medicNeeded);
            times--;
            printPatientGotOut("bianco", medicNeeded);
        }
        finally{
            prontoSoccorso.lockMedici.unlock();
        }

        if(times > 0){
            MainClass.pause(1000);
            run();
        }
    }

    //Quando un paziente con codice bianco arriva, resta in attesa di un medico che al momento non sta servendo o ha in coda pazienti con priorità più alta
    //di lui.
    protected int waitMyTurn(){
        int freeMedic;
        while ((freeMedic = prontoSoccorso.whitePatientCanGoIn()) < 0) {
            try {
                prontoSoccorso.yourTurn.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return freeMedic;
    }
    
}
