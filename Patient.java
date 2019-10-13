import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

//Un paziente è un Thread che utilizza, in concorrenza con altri pazienti, la risorsa "prontoSoccorso"
//di tipo "Hospital" per essere visitato (visita interpretata con il metodo sleep una volta ottenuto l' accesso
//esclusivo alla risorsa richiesta). Ogni paziente deve essere visitato #visite volte generato casualmente
//(1 <= #visite <= 5). Dopo ogni visita, il paziente attende 1s prima di tornare nella sala di attesa
//del prontoSoccorso.
public abstract class Patient extends Thread {

    protected static int patientNumber = 1;
    protected int id;
    protected Hospital prontoSoccorso;
    protected long visitTimeNeeded;
    protected int times;
    protected SimpleDateFormat formatter;
    protected int medicNeeded;

    public Patient(Hospital newProntoSoccorso) {
        Random randomNumberGenerator = new Random();
        this.medicNeeded = randomNumberGenerator.nextInt(9);
        this.prontoSoccorso = newProntoSoccorso;
        this.formatter = new SimpleDateFormat("[HH:mm:ss]");
        this.times = randomNumberGenerator.nextInt(4) + 1;
        this.visitTimeNeeded = randomNumberGenerator.nextInt(2000) + 250;
        this.id = patientNumber;
        patientNumber++;
    }

    //Metodo da implementare, visto i diversi comportamenti dei pazienti in base alla loro priorità.
    public abstract void run();

    //Metodo che permette ad ogni paziente di aspettare se ci sono pazienti rossi in coda.
    protected void givePriorityToRedPatients() {
        while (prontoSoccorso.redPatientsInQueue()) {
            try {
                prontoSoccorso.yourTurn.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Stampa per informare che il paziente è in cura.
    protected void printPatientGotIn(String code, int medicNeeded) throws NullPointerException {
        if (code.equals(null))
            throw new NullPointerException();

        Date date = new Date();
        System.out.print(formatter.format(date) + "[" + code + "]" + " Paziente " + this.id);
        switch (code) {
            case "rosso":
                System.out.println(" è in cura da tutti i medici.");
                break;
            default:
                System.out.println(" è in cura dal medico " + (medicNeeded + 1));
                break;
        }
    }

    //Stampa per informare che il paziente ha terminato una visita, oltre a quante altre visite necessita.
    protected void printPatientGotOut(String code, int medicNeeded) throws NullPointerException {
        if (code.equals(null))
            throw new NullPointerException();
        Date date = new Date();
        System.out.print(formatter.format(date) + "[" + code + "]" + " Paziente " + this.id);
        switch (code) {
            case "rosso":
                System.out.print(" è stato curato.");
                break;
            default:
                System.out.print(" è stato curato dal medico " + (medicNeeded + 1) + ".");
        }
        if(times == 0)
            System.out.println(" [CURATO]");
        else
            System.out.println(" Ha bisogno di altre " + times + " visite");

    }

    //Stampa per informare che il paziente è arrivato in coda di attesa dell' ospedale.
    protected void printPatientArrived(String code) throws NullPointerException{
        if(code.equals(null))
            throw new NullPointerException();

        Date date = new Date();
        System.out.println(formatter.format(date) + "[" + code + "] Paziente " + this.id + " è arrivato in coda.");
    }
}