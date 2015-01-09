package com.purplemovies.simpletodos.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "todos")
public class ToDoItem extends Model {

    @Column(name = "Description")
    public String mDescription;

    @Column(name = "due_date")
    public Date dueDate;

    @Column(name = "done")
    public boolean done;

    public ToDoItem() {
        super();
    }

    public ToDoItem(String description) {
        super();
        mDescription = description;
    }
}
