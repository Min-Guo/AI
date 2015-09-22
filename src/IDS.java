/**
 * Created by Min on 9/19/15.
 */
import javax.xml.soap.Node;
import java.io.*;
import java.util.*;
import java.lang.*;

public class IDS {
    public static int taskNumber = 0;
    public static int targetValue = 0;
    public static int deadline = 0;
    public static int queueSize = 0;
    public static int depth = 0;
    public static ArrayList<taskStructure> taskStructureList = new ArrayList<taskStructure>();
    public static Stack<String> parentStack = new Stack<String>();

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
        IDS ids = new IDS();
        taskStructure temp = ids.new taskStructure();
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
        IDS ids = new IDS();
        taskStructure root = ids.new taskStructure();
        root.taskID = "root";
        for (taskStructure tempNode : taskStructureList) {
            if (tempNode.parents.isEmpty()) {
                root.setChildren(tempNode.taskID);
            }
        }
        /*System.out.println("node is " + source.taskID + ", value is " + source.value + ", time is " + source.time + ", parents are " + source.parents +", children are " + source.children);*/

        return root;
    }

    public static boolean goalTest (String taskSchedule) {
        int tempValue = 0;
        int tempDeadline = 0;
        if (taskSchedule.equals("root")) {
            return false;
        } else {
            char[] cArray = taskSchedule.toCharArray();
            for (char tempChar : cArray) {
                tempValue += taskStructureList.get(Character.getNumericValue(tempChar)).value;
                tempDeadline += taskStructureList.get(Character.getNumericValue(tempChar)).time;

            }
            if (tempDeadline <= deadline && tempValue >= targetValue) {
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
        System.out.println("stack is " + parentStack);

    }

    public static int levelNumber (){
        int level = 0;
        if (parentStack.peek().equals("root")) {
            return level;
        } else {
            level = parentStack.peek().length();
            return level;
        }
    }

    public static void IterativeDeepeningSearch(String taskSchedule){

        String temp;
        parentStack.push(taskSchedule);
        while ( depth < taskNumber) {
            while (!parentStack.isEmpty()) {
                System.out.println(parentStack);
                if (levelNumber() == depth) {
                    temp = parentStack.pop();
                    if (goalTest(temp)) {
                        System.out.println("A correct Schedule is " + temp);
                        return;
                    }
                } else {
                        while ((!parentStack.isEmpty()) && (levelNumber() != depth)){
                            temp = parentStack.pop();
                            addNextTask(temp);
                            /*System.out.println("After add : pop is " + temp + ",  Depth is " + depth + ",  stack is " + parentStack);*/
                        }
                    System.out.println("stack empty " + parentStack.isEmpty() );
                }
            }
            depth ++ ;
            System.out.println("depth ++ is " + depth);
            IterativeDeepeningSearch(setRoot().taskID);
        }
        System.out.println("No Answer.");
    }


    public static void main (String [] args) {
        String fileName = null;
        if(args.length > 0) {
            fileName = args[0];
        }
        readFile(fileName);
        taskStructure root = setRoot();
        IterativeDeepeningSearch(root.taskID);
    }
}
