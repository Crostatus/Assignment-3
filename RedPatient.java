//Un paziente con codice rosso è un paziente che ha priorià massima ed ha bisogno di essere visitato

public class RedPatient extends Patient {

    public RedPatient(Hospital newProntoSoccorso) {
        super(newProntoSoccorso);
        this.visitTimeNeeded += 2000;
        Thread.currentThread().setPriority(10);
    }

    public void run() {
        prontoSoccorso.lockMedici.lock();
        try {
            waitMyTurn();

            prontoSoccorso.redPatientGotIn();
            printPatientGotIn("rosso", -1);

        } finally {
            prontoSoccorso.lockMedici.unlock();
        }
        //inizia la visita
        MainClass.pause(visitTimeNeeded);
        //fine della visita
        prontoSoccorso.lockMedici.lock();
        try {
            prontoSoccorso.redPatientGettingOut();
            times--;
            printPatientGotOut("rosso", -1);
        } finally {
            prontoSoccorso.lockMedici.unlock();
        }

        if (times > 0) {
            MainClass.pause(2000);
            run();
        }
    }

    //Quando un paziente con codice rosso arriva, aumenta il contatore dei pazienti in attesa con tale codice, in modo da comunicare ad ogni
    //altro paziente con priorità minore di non poter entrare a fare alcuna visita. In seguito, attende il suo turno,
    //ovvero che i pazienti al momento in visita escano e rendano di nuovo disponibili i medici richiesti.
    protected void waitMyTurn() {
        prontoSoccorso.redPatients++;
        while (!prontoSoccorso.redPatientCanGoIn()) {
            try {
                prontoSoccorso.yourTurn.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
