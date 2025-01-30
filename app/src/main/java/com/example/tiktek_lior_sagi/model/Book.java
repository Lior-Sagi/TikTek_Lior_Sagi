package com.example.tiktek_lior_sagi.model;

import java.util.List;

public class Book {

  protected   String id;
  protected   String subject;
  protected   String book;


  protected   int pagesNumber;
   protected String bookCover;


  protected   List<Answer> answerList;

    public Book() {
    }



    public Book(String id, String subject, String book, int pagesNumber, String bookCover, List<Answer> answerList) {
        this.id = id;
        this.subject = subject;
        this.book = book;
        this.pagesNumber = pagesNumber;
        this.bookCover = bookCover;
        this.answerList = answerList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }


    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", book='" + book + '\'' +
                ", pagesNumber=" + pagesNumber +
                ", bookCover='" + bookCover + '\'' +
                ", answerList=" + answerList +
                '}';
    }
}
