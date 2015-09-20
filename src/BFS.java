/**
 * Created by Min on 9/19/15.
 */
import java.io.*;
import java.util.ArrayList;

public class BFS {
    public static int taskNumber = 0;
    public static int targetValue = 0;
    public static int deadline = 0;
    public static int queueSize = 0;

    public class NodeStructure {
        private int value;
        private int time;

        public void setValue (int value) {
            this.value = value;
        }

        public void setTime (int time) {
            this.time = time;
        }

    }


    public  static void readFile(String fileName) {
        ArrayList<NodeStructure> nodeStructureList = new ArrayList<NodeStructure>();
        String line = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            line = bufferedReader.readLine();
            String[] splited = line.split("\\s+");
            taskNumber = Integer.parseInt(splited[0]);
            targetValue = Integer.parseInt(splited[1]);
            deadline = Integer.parseInt(splited[2]);
            queueSize = Integer.parseInt(splited[3]);
            for (int i = 0; i < taskNumber; i++) {
                line = bufferedReader.readLine();
                splited = line.split("\\s+");
                BFS bfs = new BFS();
                NodeStructure temp = bfs.new NodeStructure();
                temp.setValue(Integer.parseInt(splited[0]));
                temp.setTime(Integer.parseInt(splited[1]));
                nodeStructureList.add(temp);
            }
            for(NodeStructure nodeStructure :nodeStructureList) {
                System.out.println("value is " + nodeStructure.value + ", time is " + nodeStructure.time);
            }
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
