package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;

public class SendBook implements Serializable {
    //class to send in Search.java to Answers.java
    String bookId;
    String bookName;
    int page;
    String questionNumber;

    public SendBook(String bookId, String bookName, int page, String questionNumber) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.page = page;
        this.questionNumber = questionNumber;
    }

    public SendBook() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    @Override
    public String toString() {
        return "SendBook{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", page=" + page +
                ", questionNumber=" + questionNumber +
                '}';
    }
}
