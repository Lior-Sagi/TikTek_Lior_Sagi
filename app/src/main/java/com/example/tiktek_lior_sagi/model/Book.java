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

    public List<Answer> getAnswerListByPage(int pageNumber) {
        if (pageNumber < 0 || this.maxPages <= pageNumber) {
            return new ArrayList<>();
        }
        Map<String, Answer> pagesListOrDefault = this.pagesList.getOrDefault("\""+pageNumber+"\"", new HashMap<>());
        assert pagesListOrDefault != null;
        System.out.println(pagesList.toString());
        System.out.println(pagesListOrDefault);
        Log.d("@@@@@@@@@@@@@@", pagesList.toString());
        Log.d("@@@@@@@@@@@@@@", pagesListOrDefault.toString());
        if (pagesListOrDefault.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(pagesListOrDefault.values());
    }


    public void addNewAnswer(int pageNumber, Answer answer) {
        if (pageNumber < 0 || pageNumber >= maxPages) return;
        Map<String, Answer> map = this.pagesList.getOrDefault("\""+pageNumber+"\"", new HashMap<>());
        assert map != null;
        map.put(answer.getId(), answer);
        this.pagesList.put("\""+pageNumber+"\"", map);
    }

}
