import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//Un oggetto di tipo Hospital contiene un insieme ordinato di 10 medici
//garantendo un accesso concorrenziale consistente tra tutti i pazienti.
public class Hospital {
    //medici[i] = true =>  l' i-esimo medico è pronto per effettuare una nuova visita.
    //medici[i] = false => l' i-esimo medico è occupato a visitare un paziente.
    boolean[] medici;
    public ReentrantLock lockMedici;
    public Condition yourTurn;
    //Contatore di pazienti con codice rosso in fila
    public int redPatients;
    //Contatore di pazienti con codice rosso in fila per l' i-esimo medico
    public int[] yellowPatients;


    public Hospital(){
        medici = new boolean[10];
        yellowPatients = new int[10];
        lockMedici = new ReentrantLock();
        yourTurn = lockMedici.newCondition();
        //tutti i medici sono liberi all' apertura del pronto soccorso
        for(int i = 0; i < 10; i++)
            medici[i] = true;
    }

    //Restituisce true se ci sono pazienti con codice rosso in attesa, false altrimenti.
    public boolean redPatientsInQueue(){
        return (redPatients > 0);
    }

    //Metodo invocato da un paziente rosso quando, iniziando la propria visita, occupa tutti i medici.
    public void redPatientGotIn(){
        int i;
        for(i = 0; i < 10; i++)
            medici[i] = false;
    }

    //Controllo per vedere se tutti i 10 medici a disposizione sono disponibili per iniziare la visita di un paziente rosso.
    public boolean redPatientCanGoIn(){
        int i;
        for(i = 0; i < 10; i++)
            if(medici[i] == false)
                return false;

        return true;
    }

    //Metodo invocato dopo una visita ad un paziente rosso, usato per rendere disponibili tutti i medici
    //ed avvisare gli altri pazienti che una visita è stata terminata.
    public void redPatientGettingOut(){
        int i;
        for(i = 0; i < 10; i++)
            medici[i] = true;
        redPatients--;
        this.yourTurn.signalAll();
    }

    //Controllo per vedere se il medico richiesto al momento è disponibile o occupato a visitare un altro paziente
    public boolean yellowPatientCanGoIn(int medicNeeded){
        return medici[medicNeeded] == true;
    }

    //Metodo invocato dai pazienti di tipo giallo/bianco all' inizio della propria visita, occupando il medico da loro richiesto.
    public void WYPatientGotIn(int medicNeeded, String patientCode){
        if(patientCode.equals("yellow"))
            yellowPatients[medicNeeded] --;
        medici[medicNeeded] = false;
    }

    //Controllo per vedere se un paziente bianco può effettuare la propria visita dal primo medico libero,
    //assicurandosi che il medico sia disponibile e non abbia pazienti con codice giallo in coda.
    //Se viene trovato un medico che soddisfa tale condizione questo metodo restituisce l' indice del medico
    //libero, -1 altrimenti.
    public int whitePatientCanGoIn(){
        int i;
        for(i = 0; i < 10; i++){
            if(medici[i] == true && yellowPatients[i] == 0)
                return i;
        }
        return -1;
    }

    //Metodo invocato dai pazienti con codice giallo/bianco al termine della loro visita, per rendere
    //disponibile il medico che li ha visitati e informare gli altri paienti che una visita è stata terminata.
    public void WYPatientGettingOut(int nowFreeMedic){
        medici[nowFreeMedic] = true;
        yourTurn.signalAll();
    }
}
