package com.uoit.noteme;

import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public static final int CAMERA = 2;
    String currentPhotoPath;

    ImageView showImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        DbHandler db = new DbHandler(this);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView imageDone = findViewById(R.id.imageSave);
        imageDone.setOnClickListener(v -> {
            EditText inputNoteTitle, inputNoteSubTitle, inputNote;
            Spinner colorSpinner;
            inputNoteTitle = (EditText) findViewById(R.id.inputNoteTitle);
            inputNoteSubTitle = (EditText) findViewById(R.id.inputNoteSubTitle);
            inputNote = (EditText) findViewById(R.id.inputNote);
            colorSpinner = (Spinner) findViewById(R.id.colorSpinner);

            String title = inputNoteTitle.getText().toString();
            String subtitle = inputNoteSubTitle.getText().toString();
            String text = inputNote.getText().toString();
            String color = colorSpinner.getSelectedItem().toString();

            if (showImage.getTag() != null) {

                //                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(showImage.getTag().toString()));
                String image = showImage.getTag().toString();
//                    byte[] data = Utility.getBytes(bitmap);
                String path = Uri.parse(image).getPath();
                Note note = new Note(title, subtitle, text, color, image, path);
                db.addNotes(note);

            }
            else {
                Note note = new Note(title, subtitle, text, color, null, null);
                db.addNotes(note);
            }

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        Button addImage = findViewById(R.id.addImage);
        showImage = findViewById(R.id.showImage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        addImage.setOnClickListener(v -> {

            //Uncomment the below code to Set the message and title from the strings.xml file
            builder.setMessage("Select") .setTitle("Select");

            //Setting message manually and performing action on button click
            builder.setMessage("Select an image source")
                    .setCancelable(false)
                    .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.CAMERA}, 2);
                        }
                    })
                    .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Select");
            alert.show();


        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedImageUri = data.getData();
//                File f = new File(currentPhotoPath);
//                Uri selectedImageUri = Uri.fromFile(f);
                //                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                if (selectedImageUri != null) {
                    showImage.setImageURI(selectedImageUri);
                    showImage.setTag(selectedImageUri);
                }

            }
            else if (requestCode == CAMERA) {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                showImage.setImageBitmap(imageBitmap);

                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                if (contentUri != null) {
                    showImage.setImageURI(contentUri);
                    showImage.setTag(contentUri);
                }

//                Uri selectedImageUri = data.getData();
//                if (selectedImageUri != null) {
//                    showImage.setImageURI(selectedImageUri);
//                    showImage.setTag(selectedImageUri);
//                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    // display error state to the user
                    Toast.makeText(this, "Error starting activity", Toast.LENGTH_SHORT).show();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.uoit.noteme",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
                }

            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        // display error state to the user
                        Toast.makeText(this, "Error starting activity", Toast.LENGTH_SHORT).show();
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.uoit.noteme",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA);
                    }

                }

            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}