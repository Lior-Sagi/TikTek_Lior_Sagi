package com.example.tiktek_lior_sagi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

  protected   String id;
  protected   String subject;
  protected   String bookName;
  protected   int pagesNumber;
   protected String bookCover;
  protected  List<ArrayList<Answer>> pagesList;
    public Book() {
    }

    public Book(String id, String subject, String bookName, int pagesNumber, String bookCover) {
        this.id = id;
        this.subject = subject;
        this.bookName = bookName;
        this.pagesNumber = pagesNumber;
        this.bookCover = bookCover;
        this.pagesList =setPagesAnswerList(pagesNumber) ;
    }

    public Book(String id, String subject, String bookName, int pagesNumber, String bookCover, List<ArrayList<Answer>> pagesList) {
        this.id = id;
        this.subject = subject;
        this.bookName = bookName;
        this.pagesNumber = pagesNumber;
        this.bookCover = bookCover;
        this.pagesList = pagesList;
    }

    public List<ArrayList<Answer>> setPagesAnswerList(int pagesNumber) {
        ArrayList<ArrayList<Answer>>answersList=new ArrayList<>();
        for(int i=1;i<=pagesNumber;i++)
        {
         ArrayList<Answer>pagesAnswer=new ArrayList<>();
         pagesAnswer.add(new Answer());
            answersList.add(pagesAnswer);
        }
        return answersList;
    }

    public  List<Answer> getAnswerListByPage(int pageNumber){
      return this.pagesList.get(pageNumber);
    }

    public  Answer getAnswerByPageAndQuestionNumber(int pageNumber, int numQuestion){
        return this.pagesList.get(pageNumber).get(numQuestion);
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

    public void setBookName(String book) {
        this.bookName = book;
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

    public List<ArrayList<Answer>> getPagesList() {
        return pagesList;
    }

    public void setPagesList(List<ArrayList<Answer>> pagesList) {
        this.pagesList = pagesList;
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
                ", bookName='" + bookName + '\'' +
                ", pagesNumber=" + pagesNumber +
                ", bookCover='" + bookCover + '\'' +
                ", pagesList=" + pagesList +
                '}';
    }
}
