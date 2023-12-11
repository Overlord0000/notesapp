package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private DatabaseHelper dbHelper;

    MaterialButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        add = (MaterialButton) findViewById(R.id.addnewnotebtn);

        dbHelper = new DatabaseHelper(this);
        loadNotes();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewNoteClick(view);
            }
        });
    }

    private void loadNotes() {
        ArrayList<Note> notes = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(notes);
        recyclerView.setAdapter(noteAdapter);
    }




    public void onAddNewNoteClick(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}
