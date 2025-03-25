package com.AliceficialIntelligence.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class FlashcardTest {
    @Test
    public void BasicObjectTest() {
        Flashcard card = new Flashcard();
        assertEquals("Default question",card.getQuestion());
    }
    /*
    @Test
    public void ImportTest() {
        Flashcard card = new Flashcard("0||Default Question||Default Option||Default Answer");
        assertEquals("Default Question",card.getQuestion());
    }
    @Test
    public void MultiAnswerTest() {
        Flashcard card = new Flashcard("0,Default Question,Default Option,Default Answer One&&Default Answer Two");
        ArrayList<String> expectedAnswers = new ArrayList<String>();
        expectedAnswers.add("Default Answer One");
        expectedAnswers.add("Default Answer Two");
        assertEquals(expectedAnswers, card.getAnswers());
    }
    @Test
    public void setQuestionTest() {
        Flashcard card = new Flashcard();
        card.setQuestion("New Question");
        assertEquals("New Question",card.getQuestion());
    }
    */
}