package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.NodeChangeEvent;

public class MainActivity extends AppCompatActivity implements NoteListAdapter.ItemClickListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    NoteListAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbHandler db = new DbHandler(this);

        recyclerView = findViewById(R.id.notesRecyclerView);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(v -> startActivityForResult(new Intent(
                getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE)
        );

        ImageView search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
            String keyword = inputSearch.getText().toString();
            List<Note> notes = db.getNoteByTitle(keyword);
            appendToView(notes);
        });

        ImageView export = findViewById(R.id.export);
        export.setOnClickListener(v -> {
            List<Note> list = new ArrayList<>();
            list = db.getAllNote();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray array = gson.toJsonTree(list).getAsJsonArray();

            String data = array.toString();

            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("export.txt", this.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
                Toast.makeText(this, "File exported to export.txt", Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

        });

        List<Note> notes = db.getAllNote();

        appendToView(notes);
    }

    String listToJSON(List<Note> list) {
        String res = "{";
        return res;
    }

    void appendToView(List<Note> notes) {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteListAdapter(this, notes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        //send intent to ViewNoteActivity
        String id = adapter.getNotes().get(position).getId();
        Intent intent = new Intent(getApplicationContext(), ViewNoteActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}