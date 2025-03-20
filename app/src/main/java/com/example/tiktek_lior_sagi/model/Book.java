package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

  protected String id;
  protected String subject;
  protected String bookName;
   protected String bookCover;
  protected  ArrayList<ArrayList<Answer>> pagesList;
    public Book() {
        this.pagesList = new ArrayList<>();
    }

    public Book(String id, String subject, String bookName, String bookCover, ArrayList<ArrayList<Answer>> pagesList) {
        this.id = id;
        this.subject = subject;
        this.bookName = bookName;
        this.bookCover = bookCover;
        this.pagesList = pagesList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public List<ArrayList<Answer>> getPagesList() {
        return pagesList;
    }

    public void setPagesList(ArrayList<ArrayList<Answer>> pagesList) {
        this.pagesList = pagesList;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", bookName='" + bookName + '\'' +
//                ", bookCover='" + bookCover + '\'' +
                ", pagesList=" + pagesList +
                '}';
    }

    public List<Answer> getAnswerListByPage(int pageNumber) {
        if (pageNumber < 0 || this.pagesList.size() <= pageNumber) {
            return null;
        }
        return this.pagesList.get(pageNumber);
    }
}
