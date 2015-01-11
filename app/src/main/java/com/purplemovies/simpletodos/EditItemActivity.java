package com.purplemovies.simpletodos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    private EditText etItem;
    private int itemPosition;
    private String itemString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItem = (EditText) findViewById(R.id.etItem);

        final Intent intent = getIntent();
        itemPosition = intent.getIntExtra(TodoActivity.ITEM_POSITION, 0);
        itemString = intent.getStringExtra(TodoActivity.ITEM_STRING);

        etItem.setText(itemString);
        etItem.requestFocus();
    }

    public void onUpdateItem(View view) {
        Intent data = new Intent();
        data.putExtra(TodoActivity.ITEM_POSITION, itemPosition);
        data.putExtra(TodoActivity.ITEM_STRING, etItem.getText().toString());
        setResult(TodoActivity.REQUEST_CODE, data);
        finish();
    }
}
