package com.purplemovies.simpletodos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {

    private static final String TODO_FILE_NAME = "todo.txt";
    public static final String ITEM_POSITION = "itemPosition";
    public static final String ITEM_STRING = "itemString";
    public static final int REQUEST_CODE = 30;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListeners();
    }

    private void readItems() {

        File filersDir = getFilesDir();
        File todoFile = new File(filersDir, TODO_FILE_NAME);
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {

        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == resultCode) {

            final int itemPos = data.getIntExtra(ITEM_POSITION, 0);
            final String itemString = data.getStringExtra(ITEM_STRING);

            items.set(itemPos, itemString);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void setupListViewListeners() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), EditItemActivity.class);

                String itemString = ((TextView) view).getText().toString();

                intent.putExtra(ITEM_POSITION, position);
                intent.putExtra(ITEM_STRING, itemString);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }
}
