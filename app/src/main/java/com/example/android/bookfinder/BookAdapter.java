package com.example.android.bookfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bplewis5 on 7/2/16.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book,
                    parent, false);
        }

        Book book = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_textview);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.author_textview);

        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthorsStr());

        return convertView;
    }
}
