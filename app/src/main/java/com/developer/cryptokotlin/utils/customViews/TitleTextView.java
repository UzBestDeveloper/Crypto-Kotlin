package com.developer.cryptokotlin.utils.customViews;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.developer.cryptokotlin.R;


public class TitleTextView extends LinearLayout {

    private Context mContext;

    private String mTitle;

    private String mValue;

    public TitleTextView(String title, String value, Context context) {
        super(context);
        initView(title, value, context);
    }

    public TitleTextView(Context context) {
        super(context);
    }

    private void initView(String title, String value, Context context) {
        this.mContext = context;
        this.mTitle = title;
        this.mValue = value;
        setupView();
    }

    private void setupView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.title_text_view, this);

        TextView textViewTitle = view.findViewById(R.id.text_view_title);
        TextView textViewValue = view.findViewById(R.id.text_view_value);

        textViewTitle.setLinkTextColor(ContextCompat.getColor(mContext,R.color.teal_700));
        textViewTitle.setClickable(true);
        textViewTitle.setMovementMethod(LinkMovementMethod.getInstance());

        textViewTitle.setText(null != mTitle ? Html.fromHtml(mTitle) : "");
        textViewValue.setText(null != mValue ? Html.fromHtml(mValue) : "");

    }

}
