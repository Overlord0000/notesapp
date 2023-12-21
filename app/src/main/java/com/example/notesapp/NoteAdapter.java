// NoteAdapter.java
package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> notes;
    private RecyclerItemClickListener.OnLongItemClickListener longItemClickListener;
    private DatabaseHelper dbHelper;
    private MainActivity mainActivity; // Add a reference to the MainActivity

    public NoteAdapter(ArrayList<Note> notes, DatabaseHelper dbHelper, MainActivity mainActivity) {
        this.notes = notes;
        this.dbHelper = dbHelper;
        this.mainActivity = mainActivity; // Initialize the reference
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());
        holder.timeTextView.setText(note.getFormattedTimestamp());

        // Set long click listener for item
        //skip multi select
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(holder.itemView, note.getId());
                return true;
            }
        });

        // Set click listeners for item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (longItemClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    longItemClickListener.onLongItemClick(v, adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getItem(int position) {
        return notes.get(position);
    }

    public void setOnLongItemClickListener(RecyclerItemClickListener.OnLongItemClickListener listener) {
        this.longItemClickListener = listener;
    }

    // Add this method to update the entire dataset
    public void updateDataSet(ArrayList<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    private void showPopupMenu(View view, long noteId) {
        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(view.getContext(), view);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        // Registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                // Handle item selection
                switch (item.getTitle().toString()) {
                    case "Delete":
                        // Delete the note
                        dbHelper.deleteNoteById(noteId);
                        updateDataSet(dbHelper.getAllNotes()); // Refresh the notes after deletion
                        mainActivity.onNotesUpdated(); // Notify MainActivity about the dataset change
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Showing the popup menu
        popup.show();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, timeTextView;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleoutput);
            descriptionTextView = itemView.findViewById(R.id.descriptionoutput);
            timeTextView = itemView.findViewById(R.id.timeoutput);
        }
    }
}