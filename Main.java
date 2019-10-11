public class Main {

    public static void main(String[] args){

        Hospital test = new Hospital();
        for(int i = 0; i < 5; i++) {
            RedPatient x1 = new RedPatient(test);
            x1.start();
        }
        for(int i = 0; i < 2; i++) {
            YellowPatient x2 = new YellowPatient(test);
            x2.start();
        }
        for(int i = 0; i < 2; i++) {
            WhitePatient x3 = new WhitePatient(test);
            x3.start();
        }

    }

    public static void pause(long millis){
        try{
            Thread.sleep(millis);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
