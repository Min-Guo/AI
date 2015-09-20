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
    public static ArrayList<NodeStructure> nodeStructureList = new ArrayList<NodeStructure>();

    public class NodeStructure {
        private int value;
        private int time;
        ArrayList<String> parents = new ArrayList<String>();
        ArrayList<String> children = new ArrayList<String>();

        public void setValue (int value) {
            this.value = value;
        }

        public void setTime (int time) {
            this.time = time;
        }

        public void setParents (String parent) {
            parents.add(parent);
        }

        public void setChildren (String child) {
            children.add(child);
        }
    }

    public static void setParameter (String line) {
        String[] tempSplited = line.split("\\s+");
        taskNumber = Integer.parseInt(tempSplited[0]);
        targetValue = Integer.parseInt(tempSplited[1]);
        deadline = Integer.parseInt(tempSplited[2]);
        queueSize = Integer.parseInt(tempSplited[3]);
    }

    public static NodeStructure insertNode (int i, String line) {
        String[] tempSplited = line.split("\\s+");
        tempSplited = line.split("\\s+");
        BFS bfs = new BFS();
        NodeStructure temp = bfs.new NodeStructure();
        temp.setValue(Integer.parseInt(tempSplited[0]));
        temp.setTime(Integer.parseInt(tempSplited[1]));
        return temp;
    }

    public static NodeStructure updateNodeChildren (NodeStructure node, String child) {
        if (! node.children.contains(child)) {
            node.setChildren(child);
        }
        return node;
    }

    public static NodeStructure updateNodeParents (NodeStructure node, String parent) {
        if (! node.parents.contains(parent)) {
            node.setParents(parent);
        }
        return node;
    }


    public static void readFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();
            setParameter(line);
            for (int i = 0; i < taskNumber; i++) {
                line = bufferedReader.readLine();
                nodeStructureList.add(insertNode(i, line));
            }
            while ((line = bufferedReader.readLine()) != null) {
                String[] tempSplited = line.split("\\s+");
                tempSplited = line.split("\\s+");
                updateNodeChildren(nodeStructureList.get(Integer.parseInt(tempSplited[0])), tempSplited[1]);
                updateNodeParents(nodeStructureList.get(Integer.parseInt(tempSplited[1])), tempSplited[0]);
            }
            for(NodeStructure nodeStructure :nodeStructureList) {
                System.out.println("value is " + nodeStructure.value + ", time is " + nodeStructure.time + ", parents are " + nodeStructure.parents +", children are " + nodeStructure.children);
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
