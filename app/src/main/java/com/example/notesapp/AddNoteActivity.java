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

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput;
    private DatabaseHelper dbHelper;
    MaterialButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleInput = findViewById(R.id.titleinput);
        descriptionInput = findViewById(R.id.descriptioninput);
        save = findViewById(R.id.savebtn);
        dbHelper = new DatabaseHelper(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString().trim();
                String description = descriptionInput.getText().toString().trim();

                if (!title.isEmpty() && !description.isEmpty()) {
                    saveNoteToDatabase(title, description);
                    onSaveClick(view);
                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Please fill both title and description", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void onSaveClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveNoteToDatabase(String title, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
