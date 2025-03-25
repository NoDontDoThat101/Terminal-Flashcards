package com.AliceficialIntelligence.app;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.IntConsumer;
import java.util.Collections;
import java.lang.System;

import org.jline.terminal.*;

public class TerminalFlashcards {
    public static void main(String[] args) {
    }
}
class Flashcard {
    public static void main(String[] args) throws IOException {
        ArrayList<String> myAnswers = new ArrayList<String>();
        myAnswers.add("Correct");
        ArrayList<String> myOptions = new ArrayList<String>();
        myOptions.add("Correct");
        myOptions.add("Wrong");
        myOptions.add("Wronger");
        Flashcard card = new Flashcard(1, "Test", myOptions, myAnswers);
        card.runCard();
    }
    static int terminalWidth;
    static int terminalHeight;
    static char stallChar;
    static Cursor cursor;
    static IntConsumer discard;
    int type;
    String question;
    ArrayList<String> options = new ArrayList<String>();
    ArrayList<String> answers = new ArrayList<String>();
    public static void stallingIcon() {
        switch ((int)(System.currentTimeMillis() / 100 % 4)) {
            case (0): stallChar = '-'; break;
            case (1): stallChar = '\\'; break;
            case (2): stallChar = '|'; break;
            case (3): stallChar = '/'; break;
        }
        System.out.print(String.format("\033[1D%c",stallChar));
    }
    private static String waitForInput() {
        try (Scanner scanr = new Scanner(System.in)) {
            while (scanr.hasNextLine() == false) {
                stallingIcon();
            }
            return scanr.nextLine();
        }
    }
    private static void updateTerminalCondition() {
        try (Terminal terminal = TerminalBuilder.builder().build();) {
            terminalWidth = terminal.getWidth();
            terminalHeight = terminal.getHeight();
            cursor = terminal.getCursorPosition(discard);
        } catch (IOException except) {
            System.out.println("Oops.");
        }
    }
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
        this.answers.add(newOption);
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
    public void runCard() {
        System.out.println(this.getTypeString());
        System.out.println(this.question);
        switch (this.type) {
            case (0): System.out.println("Something has gone wrong. Send Alice your log.txt file (found in the same directory as this executable)."); break;
            case (1): this.runChoiceQuestion(); break;
            case (2): break;
            case (3): this.runMatchQuestion(); break;
        }
        this.getUserAnswer();
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
    private void getUserAnswer() {
        String userAnswer = waitForInput();
        System.out.print("Expected: ");
        for (String answer : this.answers) {
            System.out.print(answer);
        }
    }
}