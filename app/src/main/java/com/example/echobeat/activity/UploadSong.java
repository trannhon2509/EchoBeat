package com.example.echobeat.activity;

import static com.example.echobeat.dbFirebase.SeedData.getRandomDate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.echobeat.R;

import com.example.echobeat.dbFirebase.FirebaseHelper;
import com.example.echobeat.modelFirebase.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;


public class UploadSong extends AppCompatActivity {

    private static final int PICK_SONG_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private Button btnUploadSong;
    private TextView tvSelectedSong;
    private EditText etSongTitle;
    private ProgressBar progressBar;
    private ImageView ivSongImage;
    private Uri songUri;
    private Uri imageUri;
    private FirebaseHelper<Song> firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_song);

        Button btnSelectSong = findViewById(R.id.btnSelectSong);
        btnUploadSong = findViewById(R.id.btnUploadSong);
        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        tvSelectedSong = findViewById(R.id.tvSelectedSong);
        etSongTitle = findViewById(R.id.etSongTitle);
        progressBar = findViewById(R.id.progressBar);
        ivSongImage = findViewById(R.id.ivSongImage);

        firebaseHelper = new FirebaseHelper<>();

        btnSelectSong.setOnClickListener(v -> openFileChooser(PICK_SONG_REQUEST));

        btnUploadImage.setOnClickListener(v -> openFileChooser(PICK_IMAGE_REQUEST));

        btnUploadSong.setOnClickListener(v -> {
            if (songUri != null && imageUri != null && !etSongTitle.getText().toString().isEmpty()) {
                uploadImageToFirebase();
            } else {
                Toast.makeText(UploadSong.this, "Please select a song, an image, and enter a title", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        if (requestCode == PICK_SONG_REQUEST) {
            intent.setType("audio/*");
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            intent.setType("image/*");
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_SONG_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            songUri = data.getData();
            String fileName = getFileName(songUri);
            tvSelectedSong.setText(fileName);
            btnUploadSong.setEnabled(true);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(ivSongImage);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadImageToFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        String storagePath = "images/" + System.currentTimeMillis() + "-" + getFileName(imageUri);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(storagePath);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    uploadSongToFirebase(downloadUrl);
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSong.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSong.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadSongToFirebase(String imageUrl) {
        String songStoragePath = "songs/" + System.currentTimeMillis() + "-" + getFileName(songUri);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(songStoragePath);

        storageReference.putFile(songUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String songDownloadUrl = uri.toString();
                    Song song = new Song();
                    song.setSongUrl(songDownloadUrl);
                    song.setSongId(String.valueOf(2));
                    song.setUserId(String.valueOf(1));
                    song.setTitle(etSongTitle.getText().toString());
                    song.setDuration(220);
                    song.setReleaseYear(getRandomDate());
                    song.setPictureSong(imageUrl);
                    song.setCategoryId(String.valueOf(1));
                    firebaseHelper.addData("songs", song);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSong.this, "Song uploaded successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSong.this, "Failed to get song download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSong.this, "Failed to upload song: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
