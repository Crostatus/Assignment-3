package com.company;

import javax.management.monitor.*;
import java.util.*;

//il cliente può entrare k volte ed ogni volta con lo stesso codice di soccorso,
//k generato a random possibilmente.

//la priorità è meglio farla da me
public class Main {

    //serve una lista unica di attesa, la fa prendere peggio nell inserimento ma molto meglio nell estrazione
    //serve un array con le richieste di tutti i pazienti gialli, i rossi sono ez da eseguire
    public static void main(String[] args) {




    }


    public static void pause(long millis){
        try{
            Thread.sleep(millis);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
