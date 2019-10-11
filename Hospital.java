import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Hospital {
    boolean[] medici;
    public ReentrantLock lockMedici;
    public Condition yourTurn;
    public int redPatients;
    public int[] yellowPatients;


    public Hospital(){
        this.medici = new boolean[10];
        this.yellowPatients = new int[10];
        this.lockMedici = new ReentrantLock();
        this.yourTurn = lockMedici.newCondition();
        //tutti i medici sono liberi all' apertura del pronto soccorso
        for(int i = 0; i < 10; i++)
            medici[i] = true;
    }

    public boolean redPatientsInQueue(){
        return (redPatients > 0);
    }

    public void redPatientGotIn(){
        int i;
        for(i = 0; i < 10; i++)
            medici[i] = false;
        redPatients--;
    }

    public boolean redPatientCanGoIn(){
        int i;
        for(i = 0; i < 10; i++)
            if(medici[i] == false)
                return false;

        return true;
    }

    public void redPatientGettingOut(){
        int i;
        for(i = 0; i < 10; i++)
            medici[i] = true;
        this.yourTurn.signalAll();
    }

    public boolean yellowPatientCanGoIn(int medicNeeded){
        return medici[medicNeeded] == true;
    }

    public void yellowPatientGotIn(int medicNeeded){
        this.yellowPatients[medicNeeded]--;
        medici[medicNeeded] = false;
    }

    public void yellowPatientGettingOut(int medicNeeded){
        medici[medicNeeded] = true;
        this.yourTurn.signalAll();
    }

    public int whitePatientCanGoIn(){
        int i;
        for(i = 0; i < 10; i++){
            if(medici[i] == true && yellowPatients[i] == 0)
                return i;
        }
        return -1;
    }

    public void whitePatientGotIn(int medicNeeded){
        medici[medicNeeded] = false;
    }

    public void whitePatientGettingOut(int medicNeeded){
        medici[medicNeeded] = true;
        this.yourTurn.signalAll();
    }
}
