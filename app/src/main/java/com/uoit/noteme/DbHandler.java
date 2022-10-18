package com.uoit.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notesManager";
    private static final String TABLE = "notes";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String SUBTITLE = "subtitle";
    private static final String TEXT = "text";
    private static final String COLOR = "color";
    private static final String IMAGE = "image";
    private static final String PATH = "path";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,%s TEXT,%s TEXT,%s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE, ID, TITLE, SUBTITLE, TEXT, COLOR, IMAGE, PATH);
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    void addNotes(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(SUBTITLE, note.getSubtitle());
        values.put(TEXT, note.getText());
        values.put(COLOR, note.getColor());
        values.put(IMAGE, note.getImage());
        values.put(PATH, note.getPath());

        db.insert(TABLE, null, values);
        db.close();
    }

    Note getNote(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] returnContent = {ID, TITLE, SUBTITLE, TEXT, COLOR, IMAGE, PATH};
        String[] searchParam = {String.valueOf(id)};

        Cursor cursor = db.query(TABLE, returnContent,ID + "=?", searchParam,null,null,null,null);
        if (cursor != null) cursor.moveToFirst();

        return new Note(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
    }

    void editNotes(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(SUBTITLE, note.getSubtitle());
        values.put(TEXT, note.getText());

        db.update(TABLE, values, "id = ?", new String[]{note.getId()});
        db.close();
    }

    void deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE, "id = ?", new String[]{id});
        db.close();
    }

    List<Note> getAllNote() {
        List<Note> noteList = new ArrayList<Note>();

        String query = "SELECT * FROM " + TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getString(0));
                note.setTitle(cursor.getString(1));
                note.setSubtitle(cursor.getString(2));
                note.setText(cursor.getString(3));
                note.setColor(cursor.getString(4));
                note.setImage(cursor.getString(5));
                note.setPath(cursor.getString(6));

                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return noteList;
    }

    List<Note> getNoteByTitle(String keyword) {
        List<Note> noteList = new ArrayList<Note>();

        String query = "SELECT * FROM " + TABLE + " WHERE " + TITLE + " LIKE '%" + keyword + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getString(0));
                note.setTitle(cursor.getString(1));
                note.setSubtitle(cursor.getString(2));
                note.setText(cursor.getString(3));
                note.setColor(cursor.getString(4));
                note.setImage(cursor.getString(5));
                note.setPath(cursor.getString(6));

                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return noteList;
    }
}
