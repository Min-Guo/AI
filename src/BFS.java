/**
 * Created by Min on 9/19/15.
 */
import javax.xml.soap.Node;
import java.io.*;
import java.util.*;
import java.lang.*;

public class BFS {
    public static int taskNumber = 0;
    public static int targetValue = 0;
    public static int deadline = 0;
    public static int queueSize = 0;
    public static ArrayList<taskStructure> taskStructureList = new ArrayList<taskStructure>();
    public static Queue<String> parentQueue = new LinkedList<String>();

    public class taskStructure {
        private int value;
        private int time;
        private String taskID;
        ArrayList<String> parents = new ArrayList<String>();
        ArrayList<String> children = new ArrayList<String>();

        public void setNumber (String number) {
            this.taskID = number;
        }

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

    public static taskStructure insertNode (int i, String line) {
        String[] tempSplited = line.split("\\s+");
        tempSplited = line.split("\\s+");
        BFS bfs = new BFS();
        taskStructure temp = bfs.new taskStructure();
        temp.setNumber(tempSplited[0]);
        temp.setValue(Integer.parseInt(tempSplited[1]));
        temp.setTime(Integer.parseInt(tempSplited[2]));
        return temp;
    }

    public static taskStructure updateTaskChildren (taskStructure node, String child) {
        if (! node.children.contains(child)) {
            node.setChildren(child);
        }
        return node;
    }

    public static taskStructure updateTaskParents (taskStructure node, String parent) {
        if (! node.parents.contains(parent)) {
            node.setParents(parent);
        }
        return node;
    }

    public static void setDAG (String line) {
        String[] tempSplited = line.split("\\s+");
        tempSplited = line.split("\\s+");
        updateTaskChildren(taskStructureList.get(Integer.parseInt(tempSplited[0])), tempSplited[1]);
        updateTaskParents(taskStructureList.get(Integer.parseInt(tempSplited[1])), tempSplited[0]);
    }

    public static void readFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();
            setParameter(line);
            for (int i = 0; i < taskNumber; i++) {
                line = bufferedReader.readLine();
                taskStructureList.add(insertNode(i, line));
            }
            while ((line = bufferedReader.readLine()) != null) {
                setDAG(line);
            }
            for(taskStructure taskStructure :taskStructureList) {
                System.out.println("node is " + taskStructure.taskID + "value is " + taskStructure.value + ", time is " + taskStructure.time + ", parents are " + taskStructure.parents +", children are " + taskStructure.children);
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

    public static void expandRoot () {
        BFS bfs = new BFS();
        taskStructure source = bfs.new taskStructure();
        source.taskID = "root";
        for (taskStructure tempNode : taskStructureList) {
            if (tempNode.parents.isEmpty()) {
                source.setChildren(tempNode.taskID);
                parentQueue.add(tempNode.taskID);
            }
        }
        /*System.out.println("node is " + source.taskID + ", value is " + source.value + ", time is " + source.time + ", parents are " + source.parents +", children are " + source.children);

        return source;*/
    }

    public static boolean goalTest (String taskSchedule) {
        int tempValue = 0;
        int tempDeadline = 0;
        char[] cArray = taskSchedule.toCharArray();
        for (char tempChar: cArray) {
            tempValue += taskStructureList.get(Character.getNumericValue(tempChar)).value;
            tempDeadline += taskStructureList.get(Character.getNumericValue(tempChar)).time;

        }
        if (tempDeadline <= deadline && tempValue >= targetValue) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean parentsVisited (String taskSchedule, String task) {
        for (String tempParent : taskStructureList.get(Integer.parseInt(task)).parents) {
            if (!taskSchedule.contains(tempParent)) {
                return false;
            }
        }
        return true;
    }

    public static void addNextTask (String taskSchedule) {
        for (taskStructure tempTask : taskStructureList) {
            if (!taskSchedule.contains(tempTask.taskID) && tempTask.parents.isEmpty()) {
                parentQueue.add(taskSchedule + tempTask.taskID);
            }
            if (!taskSchedule.contains(tempTask.taskID) && parentsVisited(taskSchedule, tempTask.taskID)) {
                parentQueue.add(taskSchedule + tempTask.taskID);
            }
        }
        System.out.println("Q is " + parentQueue);
    }

    public static void BreathFirstSearch(String taskSchedule){
        System.out.println("task is " + taskSchedule);
        if (goalTest(taskSchedule)) {
            System.out.println("Correct Schedule is " + taskSchedule);
            return;
        } else {
            addNextTask(taskSchedule);
            String currentTaskSchedule = parentQueue.poll();
            BreathFirstSearch(currentTaskSchedule);
        }
    }


    public static void main (String [] args) {
        String fileName = null;
        if(args.length > 0) {
            fileName = args[0];
        }
        readFile(fileName);
        expandRoot();
        String firstNode = parentQueue.poll();
        System.out.println("first node is " + firstNode);
        BreathFirstSearch(firstNode);
    }
}
