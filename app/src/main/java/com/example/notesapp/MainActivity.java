// MainActivity.java

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

        // Add a click listener for RecyclerView items
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Handle click on a note item
                        onNoteItemClick(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Handle long click if needed
                    }
                }));
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

    // Handle click on a note item
    private void onNoteItemClick(int position) {
        Note clickedNote = noteAdapter.getItem(position);
        if (clickedNote != null) {
            openEditNoteActivity(clickedNote.getId());
        }
    }

    // Open EditNoteActivity with the noteId
    private void openEditNoteActivity(long noteId) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }
}
