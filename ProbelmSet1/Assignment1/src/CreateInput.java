/**
 * Created by Min on 9/23/15.
 */
import java.io.*;
import java.lang.*;
import java.util.*;

public class CreateInput {
    public static int targetValue = 0;
    public static int deadLine = 0;
    public static int queueSize = 0;
    public static int value = 0;
    public static int time = 0;
    public static ArrayList<Integer> taskList = new ArrayList<Integer>() ;

    public static void generateFirstLine (int taskNumber) {
        Random randTV = new Random();
        Random randDL = new Random();
        Random randQ = new Random();
        queueSize = randQ.nextInt(taskNumber + 1);
        targetValue = randTV.nextInt(((int)((Math.pow(taskNumber, 2) * (1 + 2 / Math.sqrt(taskNumber)))/4) - (int)((Math.pow(taskNumber, 2) * (1 - 2 / Math.sqrt(taskNumber)))/4)) + 1) + (int)((Math.pow(taskNumber, 2) * (1 - 2 / Math.sqrt(taskNumber)))/4);
        deadLine = randDL.nextInt(((int)((Math.pow(taskNumber, 2) * (1 + 2 / Math.sqrt(taskNumber)))/4) - (int)((Math.pow(taskNumber, 2) * (1 - 2 / Math.sqrt(taskNumber)))/4)) + 1) + (int)((Math.pow(taskNumber, 2) * (1 - 2 / Math.sqrt(taskNumber)))/4);
    }

    public static void assignVT (int taskNumber) {
        Random randV = new Random();
        Random randT = new Random();
        value = randV.nextInt(taskNumber + 1);
        time = randT.nextInt(taskNumber + 1);
    }

    public static void taskArray (int taskNumber) {
        Random rand = new Random();
        for (int i = 0; i < taskNumber ; i++){
            taskList.add(i);
        }
        for (int i = 0; i < taskNumber ; i++) {
            int j = rand.nextInt((taskNumber - i)) + i;
            Collections.swap(taskList, i, j);
        }
    }


    public static String main (int taskNumber) throws Exception {
        /*String number = args[0];
        int taskNumber = Integer.parseInt(number);*/
        generateFirstLine(taskNumber);
        PrintWriter writer = new PrintWriter("newInput.txt", "UTF-8");
        writer.println(taskNumber + " " + targetValue + " " + deadLine + " " + queueSize);
        for (int i = 0; i < taskNumber; i++) {
            assignVT(taskNumber);
            writer.println(i + " " + value + " " + time);
        }
        Random randNumber = new Random();
        int probability;
        taskArray(taskNumber);
        for (int i = 0; i < taskNumber; i++) {
            for (int j = i + 1; j < taskNumber; j++) {
                probability = randNumber.nextInt(2) + 1;
                if (probability == 2) {
                    writer.println(taskList.get(i) + " " + taskList.get(j));
                }
            }
        }
        writer.close();
        return "newInput.txt";
    }
}