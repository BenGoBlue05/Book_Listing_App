package com.example.android.bookfinder;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class BookInfoFragment extends Fragment {

    String mKeyword;
    boolean mHasSearchResults;
    private BookAdapter mBookAdapter;

    public BookInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mKeyword = getArguments().getString("keywordKey");
        mBookAdapter = new BookAdapter(getActivity(), new ArrayList<Book>());


        View rootView = inflater.inflate(R.layout.fragment_book_info, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_book);
        listView.setAdapter(mBookAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateBooks();
    }

    private void updateBooks() {
        new FetchBookTask().execute(mKeyword);
    }

    public class FetchBookTask extends AsyncTask<String, Void, ArrayList<Book>> {

        private final String LOG_TAG = FetchBookTask.class.getSimpleName();

        private void displayNoResults() {
            startActivity(new Intent(getActivity(), BookInfoActivity.class)
                    .putExtra("NoResults", true));
        }

        private ArrayList<Book> getBooksFromJson(String booksJsonStr, int maxResults)
                throws JSONException {

            final String ITEMS = "items";
            final String AUTHORS = "authors";
            final String TITLE = "title";
            final String VOLUME_INFO = "volumeInfo";

            Log.v(LOG_TAG, "task running");

            ArrayList<Book> books = new ArrayList<>();

            JSONObject booksJson = new JSONObject(booksJsonStr);
            try {
                JSONArray bookListArray = booksJson.getJSONArray(ITEMS);
                mHasSearchResults = true;
                String[] resultStrs = new String[maxResults];
                for (int i = 0; i < bookListArray.length(); i++) {
                    String title;
                    ArrayList<String> authorsArrayList = new ArrayList<>();

                    JSONObject book = bookListArray.getJSONObject(i);
                    JSONObject volumeInfoJson = book.getJSONObject(VOLUME_INFO);
                    title = volumeInfoJson.getString(TITLE);
                    try {
                        JSONArray authorsJsonArray = volumeInfoJson.getJSONArray(AUTHORS);
                        for (int j = 0; j < authorsJsonArray.length(); j++) {
                            authorsArrayList.add(authorsJsonArray.getString(j));
                        }
                        books.add(new Book(title, authorsArrayList));
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "No Author", e);
                        books.add(new Book(title));
                    }
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "No items found", e);
                mHasSearchResults = false;
                displayNoResults();
            }
            return books;
        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String booksInfoJsonStr = null;


            try {
                final String QUERY_PARAM = "q";
                final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                booksInfoJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getBooksFromJson(booksInfoJsonStr, 15);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books != null) {
                mBookAdapter.clear();
                for (Book book : books) {
                    mBookAdapter.add(book);
                }
            }
        }
    }
}
