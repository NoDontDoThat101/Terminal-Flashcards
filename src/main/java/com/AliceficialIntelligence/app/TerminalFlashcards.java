package com.AliceficialIntelligence.app;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.IntConsumer;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Calendar;
import java.lang.System;

import org.jline.terminal.*;

public class TerminalFlashcards {
    static int terminalWidth;
    static int terminalHeight;
    static Cursor cursor;
    static IntConsumer discard;
    public static void main(String[] args) {
    }    
    @SuppressWarnings("unused")
    private static void updateTerminalCondition() {
        try (Terminal terminal = TerminalBuilder.builder().build();) {
            terminalWidth = terminal.getWidth();
            terminalHeight = terminal.getHeight();
            cursor = terminal.getCursorPosition(discard);
        } catch (IOException except) {
            System.out.println("Oops.");
        }
    }
}
class Flashcard {
    public static void main(String[] args) throws IOException {
        ArrayList<String> myAnswers = new ArrayList<String>();
        myAnswers.add("Correctn't");
        ArrayList<String> myOptions = new ArrayList<String>();
        myOptions.add("Correct");
        myOptions.add("Wrong");
        ArrayList<String> expectedOptions = new ArrayList<String>();
        expectedOptions.add("Correct");
        expectedOptions.add("Wrong");
        expectedOptions.add("Wronger");
        ArrayList<String> expectedAnswers = new ArrayList<String>();
        expectedAnswers.add("Correct");
        Flashcard card = new Flashcard(1, "Test", myOptions, myAnswers);
        card.addOption("Wronger");
        card.resetAnswers();
        card.addAnswer("Correct");
        card.runCard(true);
        card.runCard(true);
    }
    static Scanner scanr = new Scanner(System.in);
    int type;
    String question;
    ArrayList<String> options = new ArrayList<String>();
    ArrayList<String> answers = new ArrayList<String>();
    public Flashcard() {
        this.type = 0;
        this.question = "Default question";
        this.options.add("Default Option");
        this.answers.add("Default Answer");
    }
    public Flashcard(int type, String question, ArrayList<String> options, ArrayList<String> answers) {
        this.type = type;
        this.question = question;
        this.options = options;
        this.answers = answers;
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
            case (0): return "default question type";
            case (1): return "Multiple Choice";
            case (2): return "Short Answer";
            case (3): return "Match to Definitions";
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
        float timeTaken = ((float)(System.currentTimeMillis()) / 1000 - (float)(startMillisecond / 1000));
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
    private void log(int points) {
        // TODO: copy-paste log(time) for points. MAKE SURE THE LOG STILL LOGS IN FLOAT! log points% (= points / max points)
        }
    private void log(float time) {
        try {
            File file = new File("timeHistory.csv");
            File tempFile = new File("timeHistoryWorking.csv");
            FileWriter writer = new FileWriter(tempFile);
            if (file.createNewFile()) {writer.write("Date,Multiple Choice Score,Multiple Choice Total,Short Answer Score,Short Answer Total,Match Definitions Score,Match Definitions Total");writer.write(System.getProperty("line.separator"));}
            Scanner reader = new Scanner(file).useDelimiter("\\n");
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String dateString = String.format("%1$s/%2$s/%3$s",String.valueOf(calendar.get(Calendar.MONTH)+1),String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),String.valueOf(calendar.get(Calendar.YEAR)));
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
                    writer.write(line.substring(0,indexStart+1) + String.valueOf(Float.valueOf(line.substring(indexStart+1,indexMiddle))+time) + "," + String.valueOf(Integer.valueOf(line.substring(indexMiddle+1,indexEnd))+1) + line.substring(indexEnd));
                } else {
                    writer.write(line);
                }
                writer.write(System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
        } catch (IOException except) {
            except.printStackTrace();
        }
    }
}