// MainActivity.java

package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        add = findViewById(R.id.addnewnotebtn);

        dbHelper = new DatabaseHelper(this);
        loadNotes();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewNoteClick(view);
            }
        });

        // Add a click listener for RecyclerView items
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Handle click on a note item
                        onNoteItemClick(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Handle long click on a note item
                        onNoteItemLongClick(position);
                    }
                }
        ));
    }

    // Updated method to refresh notes after deletion or update
    private void loadNotes() {
        ArrayList<Note> notes = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(notes, dbHelper, this); // Pass reference to MainActivity

        // Set the long item click listener
        noteAdapter.setOnLongItemClickListener(new RecyclerItemClickListener.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long click on a note item
                onNoteItemLongClick(position);
            }
        });

        recyclerView.setAdapter(noteAdapter);
    }

    // Updated method to refresh notes after deletion or update
    public void onNotesUpdated() {
        loadNotes();
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

    // Handle long click on a note item
    private void onNoteItemLongClick(int position) {
        Note longClickedNote = noteAdapter.getItem(position);
        // Implement your logic to delete the note or perform any other action
        // dbHelper.deleteNoteById(longClickedNote.getId());
        // loadNotes(); // Refresh the notes after deletion
    }

    // Open EditNoteActivity with the noteId
    private void openEditNoteActivity(long noteId) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }
}
