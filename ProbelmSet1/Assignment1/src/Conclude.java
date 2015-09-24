/**
 * Created by Min on 9/23/15.
 */
public class Conclude {
    public static int solutionNumber = 0;
    public static int noSolutionNumber = 0;
    public static int tempNumber = 0;
    public static void reset () {
        if (Conclude.tempNumber != 0) {
            Conclude.tempNumber = 0;
            /*System.out.println("reset" + Conclude.tempNumber + "\n");*/
        }
    }
    public static void main (String args[]) throws Exception{

        String number = args[0];
        int taskNumber = Integer.parseInt(number);
        CreateInput input = new CreateInput();
        Assignment1 assignment1 = new Assignment1();
        for (int i = 0; i < 500 ; i ++) {

            String file = input.main(taskNumber);
            Conclude.tempNumber = assignment1.main(file);
            System.out.println("temp " + Conclude.tempNumber);
            if (Conclude.tempNumber == 1) {
                solutionNumber ++;
                System.out.println("YES " + solutionNumber);
                reset();
            } else {
                noSolutionNumber ++;
                System.out.println("NO " + noSolutionNumber);
                reset();
            }
            System.out.println("file " + i + "\n");

        }
        System.out.println("YES " + solutionNumber);
        System.out.println("NO " + noSolutionNumber);

    }
}
