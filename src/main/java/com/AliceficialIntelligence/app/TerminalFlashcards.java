package com.AliceficialIntelligence.app;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class TerminalFlashcards {
    public static void main(String[] args) {

    }
}
class Flashcard {
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
    public Flashcard(String csvLine) {
        try (Scanner scanr = new Scanner(csvLine).useDelimiter("\\s*,\\s*")) {
            this.type = scanr.nextInt();
            this.question = scanr.next();
            try (Scanner subscanr = new Scanner(scanr.next()).useDelimiter("\\s*&&\\s*")) {
                while (subscanr.hasNext()) {
                    this.options.add(subscanr.next());
                }
            }
            try (Scanner subscanr = new Scanner(scanr.next()).useDelimiter("\\s*&&\\s*")) {
                while (subscanr.hasNext()) {
                    this.answers.add(subscanr.next());
                }
            }
        }
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
            case (0): System.out.println("Something has gone wrong. Send Alice your log.txt file (found in the same directory as this executable).");
            case (1): for (String option : this.getOptionsRandomized()) {System.out.println(option);} System.out.println(String.format("Expecting %s1 answers.",String.valueOf(this.answers.size())));
            case (2): System.out.println("");
            case (3): this.runQuestion();
        }
    }
    private void runQuestion() {
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
}