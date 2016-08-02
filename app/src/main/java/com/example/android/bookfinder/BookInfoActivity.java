package com.example.android.bookfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BookInfoActivity extends AppCompatActivity {

    String LOG_TAG = "BookInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        boolean hasNoResults = getIntent().getBooleanExtra("noResults", false);
        Log.v(LOG_TAG, Boolean.toString(hasNoResults));
        if (hasNoResults){
            displayNoResultsMessage();
        }
        String keyword = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        Bundle bundle = new Bundle();
        bundle.putString("keywordKey", keyword);
        BookInfoFragment bookInfoFragment = new BookInfoFragment();
        bookInfoFragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, bookInfoFragment)
                    .commit();
        }
    }

    public void displayNoResultsMessage() {
        TextView textView = (TextView) findViewById(R.id.no_results_message);
        textView.setVisibility(View.VISIBLE);

    }


}
