package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sksho on 11-Apr-17.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter( Context context,  List<Books> booksList) {
        super(context,0, booksList);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view, parent, false);
        }

        //Get book at current position
        Books currentBooks = getItem(position);

        //find views to display book information
        TextView bookTitle = (TextView)convertView.findViewById(R.id.book_title_text_view);
        TextView bookAuthor = (TextView)convertView.findViewById(R.id.authors_name_text_view);
        TextView publishedDate = (TextView)convertView.findViewById(R.id.date_text_view);

        //set data to display
        bookTitle.setText(currentBooks.getTitle());
        publishedDate.setText(currentBooks.getPublishedDate());
        bookAuthor.setText(currentBooks.getAuthors());


        return convertView;
    }
}
