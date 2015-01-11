package com.purplemovies.simpletodos;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class EditItemFragment extends DialogFragment {

    private static final String ITEM_POSITION = "item_position";
    private static final String ITEM_TEXT = "item_text";

    private EditItemDialogListenerInterface dialogListener;

    private int mItemPosition;
    private String mItemString;

    public static EditItemFragment newInstance(int itemPosition, String itemText) {

        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();

        args.putInt(ITEM_POSITION, itemPosition);
        args.putString(ITEM_TEXT, itemText);

        fragment.setArguments(args);
        return fragment;
    }

    public EditItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemPosition = getArguments().getInt(ITEM_POSITION);
            mItemString = getArguments().getString(ITEM_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        final Button btnSave = (Button) view.findViewById(R.id.btnUpdateItem);
        final Button btnDelete = (Button) view.findViewById(R.id.btnDeleteItem);
        final EditText editText = (EditText) view.findViewById(R.id.etItem);

        dialogListener = (EditItemDialogListenerInterface) getActivity();
        editText.setText(mItemString);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.onFinishedEditing(mItemPosition, editText.getText().toString());
                getDialog().dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.deleteItem(mItemPosition);
                getDialog().dismiss();
            }
        });
        return view;
    }

    public interface EditItemDialogListenerInterface {
        void onFinishedEditing(int position, String inputText);
        void deleteItem(int itemPosition);
    }
}
