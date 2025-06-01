package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;

public class Answer  implements Serializable {
    String id;//the id for the answer
    String bookId;//the id of the book the answer is for
    int page;
    int questionNumber;
    String picAnswer;

    public Answer() {

    }


    public Answer(String id, String bookId, int page, int questionNumber, String picAnswer) {
        this.id = id;
        this.bookId = bookId;
        this.page = page;
        this.questionNumber = questionNumber;
        this.picAnswer = picAnswer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", page=" + page +
                ", questionNumber=" + questionNumber +
                ", picAnswer='" + picAnswer + '\'' +
                '}';
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        this.questionNumber = questionNumber;
    }

    public String getPicAnswer() {
        return picAnswer;
    }

    public void setPicAnswer(String picAnswer) {
        this.picAnswer = picAnswer;
    }
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
