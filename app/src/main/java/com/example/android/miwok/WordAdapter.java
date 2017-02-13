package com.example.android.miwok;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word>{

    private int mBgColor = 0;

    public WordAdapter(Context context, List<Word> objects, int bgColor){
        super(context, 0, objects);
        mBgColor = bgColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        if(currentWord.getImageResourceId() == -1){
            imageView.setVisibility(View.GONE);
        }
        else{
            imageView.setImageResource(currentWord.getImageResourceId());
        }

        View textContainerLinearLayout = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mBgColor);
        textContainerLinearLayout.setBackgroundColor(color);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getDefaultTranslation());

        return listItemView;
    }
}
