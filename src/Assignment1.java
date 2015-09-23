/**
 * Created by Min on 9/19/15.
 */
import javax.xml.soap.Node;
import java.io.*;
import java.util.*;
import java.lang.*;

public class Assignment1 {
    public static int taskNumber = 0;
    public static int targetValue = 0;
    public static int deadline = 0;
    public static int queueSize = 0;
    public static int actualValue = 0;
    public static int actualTime = 0;
    public static ArrayList<taskStructure> taskStructureList = new ArrayList<taskStructure>();
    public static Stack<String> parentStack = new Stack<String>();
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
        Assignment1 assignment1 = new Assignment1();
        taskStructure temp = assignment1.new taskStructure();
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
            /*for(taskStructure taskStructure :taskStructureList) {
                System.out.println("node is " + taskStructure.taskID + "value is " + taskStructure.value + ", time is " + taskStructure.time + ", parents are " + taskStructure.parents +", children are " + taskStructure.children);
            }*/
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

    public static taskStructure setRoot () {
        Assignment1 assignment1 = new Assignment1();
        taskStructure root = assignment1.new taskStructure();
        root.taskID = "root";
        for (taskStructure tempNode : taskStructureList) {
            if (tempNode.parents.isEmpty()) {
                root.setChildren(tempNode.taskID);
            }
        }
        return root;
    }

    public static boolean goalTest (String taskSchedule) {
        actualValue = 0;
        actualTime = 0;
        if (taskSchedule.equals("root")) {
            return false;
        } else {
            char[] cArray = taskSchedule.toCharArray();
            for (char tempChar : cArray) {
                actualValue += taskStructureList.get(Character.getNumericValue(tempChar)).value;
                actualTime += taskStructureList.get(Character.getNumericValue(tempChar)).time;

            }
            if (actualTime <= deadline && actualValue >= targetValue) {
                return true;
            } else {
                return false;
            }
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

    public static void bfsAddTask (String taskSchedule) {
        if (taskSchedule.equals("root")) {
            for (String tempTask : setRoot().children) {
                parentQueue.add(tempTask);
            }
        } else {
            for (taskStructure tempTask : taskStructureList) {
                if (parentQueue.size() < queueSize) {
                    if (!taskSchedule.contains(tempTask.taskID)) {
                        if ((tempTask.parents.isEmpty()) && (Integer.parseInt(tempTask.taskID) > Character.getNumericValue(taskSchedule.charAt(taskSchedule.length() - 1)))) {
                            parentQueue.add(taskSchedule + tempTask.taskID);
                        } else if ((!tempTask.parents.isEmpty()) && (parentsVisited(taskSchedule, tempTask.taskID))) {
                            parentQueue.add(taskSchedule + tempTask.taskID);
                        }
                    }
                } else {
                    return;
                }

            }
        }
        System.out.println("Q is " + parentQueue);
    }

    public static void addNextTask (String taskSchedule) {
        if (taskSchedule.equals("root")) {
            for (int i = setRoot().children.size() - 1; i >= 0; i --) {
                String tempStr = setRoot().children.get(i);
                parentStack.push(tempStr);
            }
        } else {
            for (int i = taskStructureList.size() - 1; i >= 0; i--) {
                taskStructure tempTask = taskStructureList.get(i);
                if (!taskSchedule.contains(tempTask.taskID)) {
                    if ((tempTask.parents.isEmpty()) && (Integer.parseInt(tempTask.taskID) > Character.getNumericValue(taskSchedule.charAt(taskSchedule.length() - 1)))) {
                        parentStack.push(taskSchedule + tempTask.taskID);
                    } else if ((!tempTask.parents.isEmpty()) && (parentsVisited(taskSchedule, tempTask.taskID))) {
                        parentStack.push(taskSchedule + tempTask.taskID);
                    }
                }

            }
        }

    }

    public static int levelNumber (){
        int level = 0;
        level = parentStack.peek().length();
        return level;
    }

    public static boolean BreathFirstSearch(String taskSchedule){
        if (parentQueue.size() < queueSize) {
            System.out.println("task is " + taskSchedule);
            if (goalTest(taskSchedule)) {
                System.out.println("Correct Schedule is " + taskSchedule + " " + actualValue + " " + actualTime);
                return true;
            } else {
                bfsAddTask(taskSchedule);
                if (parentQueue.size() < queueSize) {
                    String currentTaskSchedule = parentQueue.poll();
                    BreathFirstSearch(currentTaskSchedule);
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    public static boolean IDS (String taskSchedule){
        String source;
        if (!BreathFirstSearch(taskSchedule)) {
            while (!parentQueue.isEmpty()) {
                source = parentQueue.poll();
                int depth = source.length();
                if (IterativeDeepeningSearch(source, depth)) {
                    return true;
                }
            }
            System.out.println("No Solution.");
            return false;
        }
        return false;
    }

    public static boolean IterativeDeepeningSearch(String source, int depth){
        String temp;
        parentStack.push(source);
        while (depth < taskNumber) {
            while (!parentStack.isEmpty()) {
                System.out.println(parentStack);
                if (levelNumber() == depth) {
                    temp = parentStack.pop();
                    if (goalTest(temp)) {
                        System.out.println("Correct Schedule is " + temp + " " + actualValue + " " + actualTime);
                        return true;
                    }
                } else {
                    while ((!parentStack.isEmpty()) && (levelNumber() != depth)) {
                        temp = parentStack.pop();
                        addNextTask(temp);
                    }
                }
            }
            depth++;
            if (IterativeDeepeningSearch(source, depth)) {
                return true;
            }
        }
        return false;
    }

    public static void main (String [] args) {
        String fileName = null;
        if(args.length > 0) {
            fileName = args[0];
        }
        readFile(fileName);
        taskStructure root = setRoot();
        BreathFirstSearch(root.taskID);
        IDS(root.taskID);
    }
}
