package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;

public class SendBook implements Serializable {
    //class to send in Search.java
    String BookName;
    int page;
    int questionNumber;

    public SendBook(String bookName, int page, int questionNumber) {
        BookName = bookName;
        this.page = page;
        this.questionNumber = questionNumber;
    }

    public SendBook() {
    }

    @Override
    public String toString() {
        return "SendBook{" +
                "BookName='" + BookName + '\'' +
                ", page=" + page +
                ", questionNumber=" + questionNumber +
                '}';
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
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
}
