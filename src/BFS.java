/**
 * Created by Min on 9/19/15.
 */
import java.io.*;

public class BFS {
    int taskNumber = 0;
    int targetValue = 0;
    int deadline = 0;
    int queueSize = 0;

    public  static void readFile(String fileName) {
        String line = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            line = bufferedReader.readLine();
            System.out.println(line);
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
    }

    public static void main (String [] args) {
        String fileName = null;
        if(args.length > 0) {
            fileName = args[0];
        }
        readFile(fileName);
    }
}
