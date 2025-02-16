package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;

public class Answer  implements Serializable {
    int page;
    int questionNumber;
    String picAnswer;

    public Answer() {
    }

    public Answer(int page, int questionNumber, String picAnswer) {
        this.page = page;
        this.questionNumber = questionNumber;
        this.picAnswer = picAnswer;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        questionNumber = questionNumber;
    }

    public String getPicAnswer() {
        return picAnswer;
    }

    public void setPicAnswer(String picAnswer) {
        this.picAnswer = picAnswer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "page=" + page +
                ", questionNumber=" + questionNumber +

                '}';
    }
}
