package com.example.tiktek_lior_sagi.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book implements Serializable {

    protected String id;
    protected String subject;
    protected String bookName;
    protected String bookCover;

    // page_number -> (answer_id -> answer)
    //all of the answers of the book
    protected Map<String, Map<String, Answer>> pagesList;
    protected int maxPages;

    public Book() {
        pagesList = new HashMap<>();
    }

    public Book(String id, String subject, String bookName, String bookCover, Map<String, Map<String, Answer>> pagesList, int maxPages) {
        this.id = id;
        this.subject = subject;
        this.bookName = bookName;
        this.bookCover = bookCover;
        this.pagesList = pagesList;
        this.maxPages = maxPages;
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

    public Map<String, Map<String, Answer>> getPagesList() {
        return pagesList;
    }

    public void setPagesList(Map<String, Map<String, Answer>> pagesList) {
        this.pagesList = pagesList;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", bookName='" + bookName + '\'' +
//                ", bookCover='" + bookCover + '\'' +
                ", pagesList=" + pagesList +
                ", maxPages=" + maxPages +
                '}';
    }
    //gives the answers for a specific page
    public List<Answer> getAnswerListByPage(int pageNumber) {
        //checks if pageNumber value is valid
        if (pageNumber < 0 || this.maxPages <= pageNumber) {
            return new ArrayList<>();
        }

        String key = String.valueOf(pageNumber);
        //creates a HashMap of all of the answers
        Map<String, Answer> pagesListOrDefault = this.pagesList.getOrDefault(key, new HashMap<>());

        Log.d("@@@@@@@@@@@@@@", pagesList.toString());
        Log.d("@@@@@@@@@@@@@@", pagesListOrDefault.toString());

        if (pagesListOrDefault.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(pagesListOrDefault.values());
    }
    //gives the answers for a specific page and question number
    public List<Answer> getAnswerListByQuestionNumber(int pageNumber, int questionNumber) {
        //checks if pageNumber value is valid
        if (pageNumber < 0 || this.maxPages <= pageNumber) {
            return new ArrayList<>();
        }
        String pageKey = String.valueOf(pageNumber);
        Map<String, Answer> pageAnswers = this.pagesList.getOrDefault(pageKey, new HashMap<>());

        Log.d("PAGE_DATA", "All pages: " + pagesList.toString());
        Log.d("PAGE_DATA", "Current page: " + pageAnswers.toString());

        if (pageAnswers.isEmpty()) {
            return new ArrayList<>();
        }
        List<Answer> answersForQuestion = new ArrayList<>();
        for (Answer answer : pageAnswers.values()) {
            if (answer.getQuestionNumber() == questionNumber) {
                answersForQuestion.add(answer);
            }
        }
        Log.d("ANSWERS_FOUND", "Answers found for page " + pageNumber +
                ", question " + questionNumber + ": " + answersForQuestion.size());

        return answersForQuestion;
    }
}