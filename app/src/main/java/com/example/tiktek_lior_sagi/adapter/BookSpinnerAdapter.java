package com.example.tiktek_lior_sagi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

import java.util.List;

/// Adapter for the Book spinner
/// @see ArrayAdapter
/// @see Book

public class BookSpinnerAdapter extends ArrayAdapter<Book> {

    /// inflater for the layout
    /// @see LayoutInflater
    private final LayoutInflater inflater;
    /// resource for the layout
    /// @see android.R.layout#simple_spinner_item
    private final int resource;

    /// constructor
    /// @param context the context
    /// @param resource the resource
    /// @param objects the list of objects
    public BookSpinnerAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    /// get the view for the spinner
    /// @param position the position of the item in the list
    /// @param convertView the view to convert
    /// @param parent the parent view group
    /// @return the view for the spinner
    /// @see View
    /// @see ViewGroup
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }
        Book Book = getItem(position);
        if (Book == null) {
            return convertView;
        }

        /// Set the text for the TextView
        TextView textView = convertView.findViewById(android.R.id.text1);

        textView.setText(Book.getBookName());

        return convertView;
    }

    /// get the view for the dropdown
    /// @param position the position of the item in the list
    /// @param convertView the view to convert
    /// @param parent the parent view group
    /// @return the view for the dropdown
    /// @see View
    /// @see ViewGroup
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        /// inflate the item_selected_Book layout
        if (convertView == null) {
            /// @see R.layout#item_selected_Book
            convertView = inflater.inflate(R.layout.itemselectedbook, parent, false);
        }
        /// get the Book at the position
        Book Book = getItem(position);

        /// return the view if the Book is null
        if (Book == null) return convertView;

        /// Set the text for the TextView
      // TextView textView = convertView.findViewById(R.id.tv);
    //  textView.setText(Book.getName());

        /// Set the image for the ImageView
      //  ImageView imageView = convertView.findViewById(R.id.Book_image_view);
        /// convert the image from base64 to bitmap
        /// @see ImageUtil#convertFrom64base(String)
    //    imageView.setImageBitmap(ImageUtil.convertFrom64base(Book.getImageBase64()));


        return convertView;
    }
}