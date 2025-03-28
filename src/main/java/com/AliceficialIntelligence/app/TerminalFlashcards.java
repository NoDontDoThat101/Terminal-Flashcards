package com.AliceficialIntelligence.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.function.IntConsumer;

import org.jline.terminal.Cursor;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class TerminalFlashcards {
    static int terminalWidth;
    static int terminalHeight;
    static Cursor cursor;
    static IntConsumer discard;
    public static void main(String[] args) {
        System.out.println("\033[2J\033[H");
        funnyDVDLogo(7,4);
    }
    private static void funnyDVDLogo(int width, int height) {
        int x = 1;
        int y = 1;
        boolean right = true;
        boolean up = false;
        int color = 1;
        while (true) {
            updateTerminalCondition();
            if (right) {
                if (x + width + 1 > terminalWidth) {right = false;color += 1;}
            } else {
                if (x == 1) {right = true;color += 1;}
            }
            if (up) {
                if (y + height + 1 > terminalHeight) {up = false;color += 1;}
            } else {
                if (y == 1) {up = true;color += 1;}
            }
            if (color > 7) {
                color = 1;
            }
            if (right) {
                x++;
            } else {
                x--;
            }
            if (up) {
                y++;
            } else {
                y--;
            }
            drawBox(x,y,width,height,color);
        }
    }
    private static void showMetrics() {
        int boxWidth = terminalWidth / 3 - 2;
        int boxHeight = terminalHeight / 2;
        drawGraph(1,1,boxWidth,boxHeight,2,"scoreHistory.csv",1);
        drawGraph(3+boxWidth,1,boxWidth,boxHeight,2,"scoreHistory.csv",2);
        drawGraph(5+boxWidth*2,1,boxWidth,boxHeight,2,"scoreHistory.csv",3);
        drawGraph(1,1+boxHeight,boxWidth,boxHeight,2,"timeHistory.csv",1);
        drawGraph(3+boxWidth,1+boxHeight,boxWidth,boxHeight,2,"timeHistory.csv",2);
        drawGraph(5+boxWidth*2,1+boxHeight,boxWidth,boxHeight,2,"timeHistory.csv",3);
    }
    public static void updateTerminalCondition() {
        try (Terminal terminal = TerminalBuilder.builder().build();) {
            terminalWidth = terminal.getWidth();
            terminalHeight = terminal.getHeight();
            cursor = terminal.getCursorPosition(discard);
        } catch (IOException except) {
            System.out.println("Oops.");
        }
    }
    private static void drawGraph(int x, int y, int width, int height, int color, String filePath, int type) {
        try {
            File file = new File(filePath);
            Scanner scanr = new Scanner(file).useDelimiter("\n");
            drawBox(x,y,width,height,color);
            int length = -1;
            while (scanr.hasNext()) {
                scanr.next();
                length++;
            }
            scanr.close();
            Scanner scanrToo = new Scanner(file).useDelimiter("\n");
            scanrToo.next();
            ArrayList<Float> tracked = new ArrayList<>();
            if (length > width) {

            } else {
                float maximum = 0;
                for (int index = 0;index < length;index++) {
                    String line = scanrToo.next();
                    int indexStart = -1;
                    int indexMiddle = -1;
                    int indexEnd = -1;
                    for (int iteration = 0;iteration < type;iteration++) {
                        indexStart = line.indexOf(",",indexEnd+1);
                        indexMiddle = line.indexOf(",",indexStart+1);
                        indexEnd = line.indexOf(",",indexMiddle+1); // TODO: wtf
                    }
                    float relative = Float.valueOf(line.substring(indexStart+1,indexMiddle)) / Float.valueOf(line.substring(indexMiddle+1,indexEnd));
                    if (relative >= maximum) {
                        maximum = relative;
                    }
                    tracked.add(relative);
                }
                int iteration = 1;
                for (float datapoint : tracked) {
                    float percentage = datapoint / maximum;
                    int position = (int)(percentage * (height - 2)) - 1;
                    System.out.print("\033[1;3" + color + "m\033[" + (y+height-position) + ";" + (x+iteration) + "f");
                    iteration++;
                }
            }
            scanrToo.close();
        } catch (FileNotFoundException e) {
            System.out.print("\033[1;31m\033[" + (y+height) + ";" + (x+(width/2-2)) + "fERROR");
            drawBox(x,y,width,height,1);
        }
    }
    public static void drawBox(int x, int y, int width, int height, int color) {
        updateTerminalCondition();
        System.out.print("\033[1;3" + color + "m\033[" + y + ";" + x + "f");
        System.out.print("╔");
        for (int iteration = 1;iteration < width-2;iteration++) {
            System.out.print("═");
        }
        System.out.print("╗");
        System.out.print("\033[" + (y+1) + ";" + x + "f");
        for (int iteration = 1;iteration < height-2;iteration++) {
            System.out.print("\033[" + x + "G║\033[" + (width-3) + "C║\033[1E");
        }
        System.out.print("\033[" + (y+height-2) + ";" + x + "f");
        System.out.print("╚");
        for (int iteration = 1;iteration < width-2;iteration++) {
            System.out.print("═");
        }
        System.out.print("╝");
    }
}
class Flashcard {
    public static void main(String[] args) throws IOException {
        Flashcard card = new Flashcard("questions.txt", 0);
        card.runCard(true);
    }
    static Scanner scanr = new Scanner(System.in);
    String question;
    int type;
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    String directory;
    public Flashcard() {
        this.question = "Default question";
        this.type = 0;
        this.options.add("Default Option");
        this.answers.add("Default Answer");
        this.directory = "";
    }
    public Flashcard(String path, int index) throws FileNotFoundException {
        File file = new File(path);
        String cardString;
        try (Scanner scanr = new Scanner(file).useDelimiter("\n")) {
            for (int iteration = 0;iteration < index;iteration++) {
                scanr.nextLine();
            }   cardString = scanr.nextLine();
        }
        Scanner scanrToo = new Scanner(cardString).useDelimiter("%&%");
        this.question = scanrToo.next();
        this.type = scanrToo.nextInt();
        this.options = subScan(scanrToo.next());
        this.answers = subScan(scanrToo.next());
        this.directory = path.substring(0,path.indexOf("questions.txt"));
        scanrToo.close();
    }
    public Flashcard(int type, String question, ArrayList<String> options, ArrayList<String> answers) {
        this.question = question;
        this.type = type;
        this.options = options;
        this.answers = answers;
        this.directory = "";
    }
    public ArrayList<String> subScan(String line) {
        ArrayList<String> output = new ArrayList<>();
        Scanner scanr = new Scanner(line).useDelimiter("%.%");
        while (scanr.hasNext()) {
            output.add(scanr.next());
            System.out.println(output);
        }
        scanr.close();
        return output;
    }
    public void setQuestion(String newQuestion) {
        this.question = newQuestion;
    }
    public String getQuestion() {
        return this.question;
    }
    public void addOption(String newOption) {
        this.options.add(newOption);
    }
    public void removeOption(String badOption) {
        this.answers.remove(badOption);
    }
    public void resetOptions() {
        this.options.clear();
    }
    public ArrayList<String> getOptions() {
        return this.options;
    }
    public ArrayList<String> getOptionsRandomized() {
        ArrayList<String> myOptions = this.options;
        Collections.shuffle(myOptions);
        return myOptions;
    }
    public void addAnswer(String newAnswer) {
        this.answers.add(newAnswer);
    }
    public void removeAnswer(String badAnswer) {
        this.answers.remove(badAnswer);
    }
    public void resetAnswers() {
        this.answers.clear();
    }
    public ArrayList<String> getAnswers() {
        return this.answers;
    }
    public ArrayList<String> getAnswersRandomized() {
        ArrayList<String> myAnswers = this.answers;
        Collections.shuffle(myAnswers);
        return myAnswers;
    }
    public void setType(String newType) {
        if (newType.toLowerCase().contains("choice") || newType.toLowerCase().contains("box")) {
            this.type = 1; // Multiple choice
        } else if (newType.toLowerCase().contains("text") || newType.toLowerCase().contains("short")) {
            this.type = 2; // Short answer
        } else if (newType.toLowerCase().contains("match")) {
            this.type = 3; // Matching definitions
        } else {
            this.type = 0; // Default & fallback type. Shouldn't appear, ever
        }
    }
    public void setType(int newType) {
        this.type = newType;
    }
    public int getType() {
        return this.type;
    }
    public String getTypeString() {
        switch (this.getType()) {
            case (0) -> {
                return "default question type";
            }
            case (1) -> {
                return "Multiple Choice";
            }
            case (2) -> {
                return "Short Answer";
            }
            case (3) -> {
                return "Match to Definitions";
            }
        }
        return "invalid";
    }
    public void runCard(boolean log) {
        System.out.println(this.getTypeString());
        System.out.println(this.question);
        switch (this.type) {
            case (0): System.out.println("Something has gone wrong if you're seeing this!"); break;
            case (1): this.runChoiceQuestion(); break;
            case (2): break;
            case (3): this.runMatchQuestion(); break;
        }
        this.checkUserAnswer(this.getUserAnswer(log), log);
    }
    private void runChoiceQuestion() {
        System.out.println(String.format("Expecting %s answers from:",String.valueOf(this.answers.size())));
        for (String option : this.getOptionsRandomized()) {
            System.out.println(option);
        }
    }
    private void runMatchQuestion() {
        System.out.println("Definitions:");
        char letter = 'A';
        ArrayList<String> localAnswers = this.getAnswersRandomized();
        for (String answer : localAnswers) {
            System.out.println(String.format("%c: %s",letter,answer));
            letter++;
        }
        System.out.println("Options:");
        for (String option : this.getOptionsRandomized()) {
            System.out.println(option);
        }
    }
    private String getUserAnswer(boolean log) {
        long startMillisecond = System.currentTimeMillis();
        String userAnswer = scanr.nextLine();
        float timeTaken = (float)((System.currentTimeMillis() - startMillisecond) / 1000);
        if (log) {
            System.out.println(String.format("That took %fs.",timeTaken));
            this.log(timeTaken);
        }
        System.out.print("Expected: ");
        for (String answer : this.answers) {
            System.out.print(String.format("%s, ", answer));
        }
        System.out.println("\b\b  ");
        return userAnswer;
    }
    private void checkUserAnswer(String userAnswer, boolean log) {
        userAnswer = userAnswer.toLowerCase();
        int earnedPoints = 0;
        for (String answer : this.answers) {
            if (userAnswer.contains(answer.toLowerCase())) {
                earnedPoints++;
            }
        }
        System.out.println(String.format("The program thinks you got %1$d of %2$d points.",earnedPoints,this.answers.size()));
        if (log == true) {
            System.out.println("If this is right, just press enter. Otherwise, input the number of points you should've gotten.");
            System.out.println("(no this isn't a stupid AI. it's because you have personal logging on)");
            int points;
            try {
                points = Integer.parseInt(scanr.nextLine());
            } catch (NumberFormatException except) {
                points = earnedPoints;
            }
            this.log(points);
        }
    }
    @SuppressWarnings("ConvertToTryWithResources")
    private void log(int points) {
        float score = points / this.answers.size();
        try {
            File file = new File(this.directory + "scoreHistory.csv");
            File tempFile = new File(this.directory + "scoreHistoryWorking.csv");
            FileWriter writer = new FileWriter(tempFile);
            if (file.createNewFile()) {writer.write("Date,Multiple Choice Score,Multiple Choice Total,Short Answer Score,Short Answer Total,Match Definitions Score,Match Definitions Total,");writer.write(System.getProperty("line.separator"));}
            Scanner reader = new Scanner(file).useDelimiter("\\n");
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String dateString = String.format("%1$s/%2$s/%3$s",String.valueOf(calendar.get(Calendar.MONTH)+1),String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),String.valueOf(calendar.get(Calendar.YEAR)));
            boolean found = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.substring(0,line.indexOf(",")).contains(dateString)) {
                    int indexStart = -1;
                    int indexMiddle = -1;
                    int indexEnd = -1;
                    for (int iteration = 0;iteration < this.type;iteration++) {
                        indexStart = line.indexOf(",",indexEnd+1);
                        indexMiddle = line.indexOf(",",indexStart+1);
                        indexEnd = line.indexOf(",",indexMiddle+1);
                    }
                    writer.write(line.substring(0,indexStart+1) + String.valueOf(Float.parseFloat(line.substring(indexStart+1,indexMiddle))+score) + "," + String.valueOf(Integer.parseInt(line.substring(indexMiddle+1,indexEnd))+1) + line.substring(indexEnd));
                    found = true;
                } else {
                    writer.write(line);
                }
                writer.write(System.getProperty("line.separator"));
                }
                if (!found) {
                String line = dateString + ",0,0,0,0,0,0,";
                int indexStart = -1;
                int indexMiddle = -1;
                int indexEnd = -1;
                for (int iteration = 0;iteration < this.type;iteration++) {
                    indexStart = line.indexOf(",",indexEnd+1);
                    indexMiddle = line.indexOf(",",indexStart+1);
                    indexEnd = line.indexOf(",",indexMiddle+1);
                }
                writer.write(line.substring(0,indexStart+1) + String.valueOf(Float.parseFloat(line.substring(indexStart+1,indexMiddle))+score) + "," + String.valueOf(Integer.parseInt(line.substring(indexMiddle+1,indexEnd))+1) + line.substring(indexEnd));
                writer.write(System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            file.delete();
            tempFile.renameTo(file);
        } catch (IOException except) {
        }
    }
    @SuppressWarnings("ConvertToTryWithResources")
    private void log(float time) {
        try {
            File file = new File(this.directory + "timeHistory.csv");
            File tempFile = new File(this.directory + "timeHistoryWorking.csv");
            FileWriter writer = new FileWriter(tempFile);
            if (file.createNewFile()) {writer.write("Date,Multiple Choice Score,Multiple Choice Total,Short Answer Score,Short Answer Total,Match Definitions Score,Match Definitions Total,");writer.write(System.getProperty("line.separator"));}
            Scanner reader = new Scanner(file).useDelimiter("\\n");
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String dateString = String.format("%1$s/%2$s/%3$s",String.valueOf(calendar.get(Calendar.MONTH)+1),String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),String.valueOf(calendar.get(Calendar.YEAR)));
            boolean found = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.substring(0,line.indexOf(",")).contains(dateString)) {
                    int indexStart = -1;
                    int indexMiddle = -1;
                    int indexEnd = -1;
                    for (int iteration = 0;iteration < this.type;iteration++) {
                        indexStart = line.indexOf(",",indexEnd+1);
                        indexMiddle = line.indexOf(",",indexStart+1);
                        indexEnd = line.indexOf(",",indexMiddle+1);
                    }
                    writer.write(line.substring(0,indexStart+1) + String.valueOf(Float.parseFloat(line.substring(indexStart+1,indexMiddle))+time) + "," + String.valueOf(Integer.parseInt(line.substring(indexMiddle+1,indexEnd))+1) + line.substring(indexEnd));
                    found = true;
                } else {
                    writer.write(line);
                }
                writer.write(System.getProperty("line.separator"));
            }
            if (!found) {
                String line = dateString + ",0,0,0,0,0,0,";
                int indexStart = -1;
                int indexMiddle = -1;
                int indexEnd = -1;
                for (int iteration = 0;iteration < this.type;iteration++) {
                    indexStart = line.indexOf(",",indexEnd+1);
                    indexMiddle = line.indexOf(",",indexStart+1);
                    indexEnd = line.indexOf(",",indexMiddle+1);
                }
                writer.write(line.substring(0,indexStart+1) + String.valueOf(Float.parseFloat(line.substring(indexStart+1,indexMiddle))+time) + "," + String.valueOf(Integer.parseInt(line.substring(indexMiddle+1,indexEnd))+1) + line.substring(indexEnd));
                writer.write(System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            file.delete();
            tempFile.renameTo(file);
        } catch (IOException except) {
        }
    }
}