//Un paziente con codice giallo è un paziente che ha priorià media ed ha bisogno di essere visitato
//da uno specifico medico per essere curato.
public class YellowPatient extends Patient{

    public YellowPatient(Hospital newProntoSoccorso){
        super(newProntoSoccorso);
        this.visitTimeNeeded += 1000;
        Thread.currentThread().setPriority(5);
    }

    public void run(){
        prontoSoccorso.lockMedici.lock();
        try {
            givePriorityToRedPatients();

            waitMyTurn();

            prontoSoccorso.WYPatientGotIn(medicNeeded, "yellow");
            printPatientGotIn("giallo", medicNeeded);
            prontoSoccorso.yourTurn.signalAll();
        }
        finally {
            prontoSoccorso.lockMedici.unlock();
        }
        //inizia la visita
        MainClass.pause(visitTimeNeeded);
        //finita la visita
        prontoSoccorso.lockMedici.lock();
        try{
            prontoSoccorso.WYPatientGettingOut(medicNeeded);
            times--;
            printPatientGotOut("giallo", medicNeeded);
        }
        finally{
            prontoSoccorso.lockMedici.unlock();
        }

        if(times > 0){
            MainClass.pause(1500);
            run();
        }
    }

    //Quando un paziente con codice giallo arriva, aumenta il contatore dei pazienti in attesa del medico da lui richiesto, in modo da comunicare ad ogni
    //altro paziente con priorità minore di non poter entrare a fare alcuna visita. In seguito, attende il suo turno,
    //ovvero che il paziente al momento in visita esca e renda di nuovo disponibile il medico richiesto.
    protected void waitMyTurn(){
        prontoSoccorso.yellowPatients[medicNeeded]++;

        while (!prontoSoccorso.yellowPatientCanGoIn(medicNeeded)) {
            try {
                prontoSoccorso.yourTurn.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
