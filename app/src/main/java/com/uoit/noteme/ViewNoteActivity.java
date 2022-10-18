package com.uoit.noteme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        DbHandler db = new DbHandler(this);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm").setMessage("Confirm deletion?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = getIntent();
                            String id = intent.getStringExtra("id");
                            db.deleteNote(id);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), EditNoteActivity.class).putExtra("id",getIntent().getStringExtra("id")));
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Note note = db.getNote(id);

        String color = note.getColor();
        View layout = findViewById(R.id.activity_view_note);

        if (color.equals("White")) {
            layout.setBackgroundResource(R.color.white);
        }
        else if (color.equals("Teal")) {
            layout.setBackgroundResource(R.color.teal_200);
        }

        TextView inputNoteTitle, inputNoteSubTitle, inputNote;
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNote = findViewById(R.id.inputNote);

        inputNoteTitle.setText(note.getTitle());
        inputNoteSubTitle.setText(note.getSubtitle());
        inputNote.setText(note.getText());

        ImageView showImage = findViewById(R.id.showImage);
        if (note.getImage() != null) {
//            Bitmap bitmap = Utility.getImage(note.getImage());
            try {
                ActivityCompat.requestPermissions(ViewNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(note.getImage()));
                showImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}