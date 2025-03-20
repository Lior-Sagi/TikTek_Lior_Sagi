package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;

public class SendBook implements Serializable {
    //class to send in Search.java
    String bookId;
    String bookName;
    int page;
    int questionNumber;

    public SendBook(String bookId, String bookName, int page, int questionNumber) {
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

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
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
