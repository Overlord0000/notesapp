package com.example.notesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "extra_note_id";

    private EditText titleInput, descriptionInput;
    private DatabaseHelper dbHelper;
    private long noteId;

    MaterialButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleInput = findViewById(R.id.titleinput_edit);
        descriptionInput = findViewById(R.id.descriptioninput_edit);
        save = findViewById(R.id.savebtn_edit);
        dbHelper = new DatabaseHelper(this);

        // Retrieve note id from intent
        noteId = getIntent().getLongExtra("NOTE_ID", -1);

        if (noteId != -1) {
            // Load existing note data
            loadNoteData();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });
    }

    private void loadNoteData() {
        Note note = dbHelper.getNoteById(noteId);
        if (note != null) {
            titleInput.setText(note.getTitle());
            descriptionInput.setText(note.getDescription());
        }
    }

    private void updateNote() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (!title.isEmpty() && !description.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TITLE, title);
            values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
            values.put(DatabaseHelper.COLUMN_TIMESTAMP, getFormattedCurrentTimestamp());

            int numRowsAffected = db.update(
                    DatabaseHelper.TABLE_NAME,
                    values,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)}
            );

            if (numRowsAffected > 0) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } else {
            Toast.makeText(this, "Please fill both title and description", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFormattedCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

}
