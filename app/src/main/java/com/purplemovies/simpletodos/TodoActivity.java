package com.purplemovies.simpletodos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.purplemovies.simpletodos.models.ToDoItem;

import java.util.ArrayList;
import java.util.Date;


public class TodoActivity extends ActionBarActivity implements EditItemFragment.EditItemDialogListenerInterface {

    public static final String ITEM_POSITION = "itemPosition";
    public static final String ITEM_STRING = "itemString";
    public static final int REQUEST_CODE = 30;

    private ToDosArrayAdapter mItemsAdapter;
    private ArrayList<ToDoItem> mItems;
    private ListView mLvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        ActiveAndroid.dispose();
        ActiveAndroid.initialize(getApplication());

        readItems();
        mLvItems = (ListView) findViewById(R.id.lvItems);
        mItemsAdapter = new ToDosArrayAdapter(getApplicationContext(), mItems);
        mLvItems.setAdapter(mItemsAdapter);

        setupListViewListeners();
    }

    private void readItems() {
        mItems = new Select().from(ToDoItem.class).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == resultCode) {

            final int itemPos = data.getIntExtra(ITEM_POSITION, 0);
            final String itemString = data.getStringExtra(ITEM_STRING);

            final ToDoItem item = mItems.get(itemPos);
            item.mDescription = itemString;
            item.dueDate = new Date();
            item.save();
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    private void setupListViewListeners() {

        mLvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemString = ((TextView) view).getText().toString();

                FragmentManager fm = getSupportFragmentManager();
                EditItemFragment editItemFragment = EditItemFragment.
                        newInstance(position, itemString);

                editItemFragment.show(fm, "edit_item_dialog");
            }
        });

        mLvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        final ToDoItem item = mItems.get(position);
                        mItems.remove(position);
                        item.delete();

                        mItemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        ToDoItem item = new ToDoItem(itemText);
        item.save();

        mItems.add(item);
        mItemsAdapter.notifyDataSetChanged();
        etNewItem.setText("");
    }

    @Override
    public void onFinishedEditing(int position, String inputText) {
        final ToDoItem item = mItems.get(position);
        item.mDescription = inputText;
        item.dueDate = new Date();
        item.save();
        mItemsAdapter.notifyDataSetChanged();
    }


    // ::: List View Adapter :::
    private static final class ToDosArrayAdapter extends ArrayAdapter<ToDoItem> {

        public static final int TODO_CELL_LAYOUT = R.layout.todo_cell;
        private ArrayList<ToDoItem> mListItems = new ArrayList<>();

        public ToDosArrayAdapter(Context context, ArrayList<ToDoItem> listItems) {
            super(context, TODO_CELL_LAYOUT);
            mListItems = listItems;
        }

        public View getView(int position, View conversionView, ViewGroup parentView) {
            CheckedTextView v = (CheckedTextView) conversionView;

            if (v == null) {
                final LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = (CheckedTextView) inflater.inflate(TODO_CELL_LAYOUT, null);
            }
            final ToDoItem item = mListItems.get(position);
            v.setText(item.mDescription);
            v.setChecked(item.done);
            return v;
        }

        @Override
        public int getCount() {
            return mListItems.size();
        }

        @Override
        public ToDoItem getItem(int index) {
            return mListItems.get(index);
        }
    }
}
