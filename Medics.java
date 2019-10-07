import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Medics {
    private final Vector<Lock> medics;

    //creazione della stanza con 10 medici, 10 locks
    //i pazienti urgenti dovranno acquisirli tutti, quelli gialli uno specifico e quelli bianci uno a caso
    //qui facciamo solo acquisizione delle lock, altro thread gestirà la divisione dei pazienti.
    //questo oggetto verrà acceduto solo dal gestore, no need to sincronizzare
    //i pazienti però accedereanno sia alla fila per tornarci che all' array del gestore per contare i pazienti disponibili
    public Medics(){
        this.medics = new Vector<>(10);
        int i;
        for(i = 0; i < medics.capacity(); i++){
            Lock newMedic = new ReentrantLock();
            medics.add(newMedic);
        }
    }

    public Lock getMedic(int index){
        return this.medics.elementAt(index);
    }

    public void printTest(){
        for(int i = 0; i < medics.size(); i++)
            System.out.println(medics.elementAt(i));
    }

}
