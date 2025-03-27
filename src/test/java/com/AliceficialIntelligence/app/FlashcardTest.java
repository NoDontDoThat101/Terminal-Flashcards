package com.AliceficialIntelligence.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class FlashcardTest {
    @Test
    public void DefaultConstructorTest() {
        Flashcard card = new Flashcard();
        assertEquals("Default question",card.getQuestion());
    }
    @Test
    public void GetSetTest() {
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
    assertEquals(1,card.type);
    assertEquals("Test",card.question);
    assertEquals(expectedOptions,card.options);
    assertEquals(expectedAnswers,card.answers);
    }
}