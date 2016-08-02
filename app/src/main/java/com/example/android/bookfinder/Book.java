package com.example.android.bookfinder;

import java.util.ArrayList;

/**
 * Created by bplewis5 on 7/2/16.
 */
public class Book {

    private String mTitle;
    private ArrayList<String> mAuthors;

    public Book(String title){
        mTitle = title;
    }

    public Book(String title, ArrayList<String> authors) {
        mTitle = title;
        mAuthors = authors;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<String> getAuthorsArrayList() {
        return mAuthors;
    }

    public void setAuthors(ArrayList<String> authors) {
        mAuthors = authors;
    }

    public String getAuthorsStr(){
        String authorsString = "";
        if (mAuthors == null){
            return authorsString;
        }
        for (int i = 0; i < mAuthors.size(); i++){
            if (i == 0){
                authorsString += mAuthors.get(i);
            }
            else{
                authorsString += ", " + mAuthors.get(i);
            }
        }

        return authorsString;
    }

}
