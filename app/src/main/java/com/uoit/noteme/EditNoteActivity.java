package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        DbHandler db = new DbHandler(this);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Note note = db.getNote(id);

        EditText inputNoteTitle, inputNoteSubTitle, inputNote;
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNote = findViewById(R.id.inputNote);

        inputNoteTitle.setText(note.getTitle());
        inputNoteSubTitle.setText(note.getSubtitle());
        inputNote.setText(note.getText());

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            String title = inputNoteTitle.getText().toString();
            String subtitle = inputNoteSubTitle.getText().toString();
            String text = inputNote.getText().toString();

            Note editNote = new Note();
            editNote.setTitle(title);
            editNote.setSubtitle(subtitle);
            editNote.setText(text);
            editNote.setId(id);

            db.editNotes(editNote);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

    }
}