package com.example.tiktek_lior_sagi.model;

public class Answer {
    int page;
    int QuestionNumber;
    String picAnswer;


    public Answer() {
    }

    public Answer(int page, int questionNumber, String picAnswer) {
        this.page = page;
        QuestionNumber = questionNumber;
        this.picAnswer = picAnswer;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getQuestionNumber() {
        return QuestionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        QuestionNumber = questionNumber;
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
                ", QuestionNumber=" + QuestionNumber +

                '}';
    }
}
