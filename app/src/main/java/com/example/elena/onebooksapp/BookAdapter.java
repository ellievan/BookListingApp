package com.example.elena.onebooksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elena on 17/06/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter (Context context, List<Book> books) {
        super(context, 0, books);
    }

    //Creating a new adapter
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        //Get the object located at this position in the list
        Book currentBook = getItem(position);

        //Find the TextView in the book_list_item.xml layout with the corresponding id & set the text
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        titleTextView.setText(currentBook.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        authorTextView.setText(currentBook.getAuthor());

        //Return the whole list item layout
        return listItemView;

    }
}
