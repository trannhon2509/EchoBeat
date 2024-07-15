package com.example.echobeat.activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.echobeat.R;
import com.example.echobeat.dbFirebase.FirebaseHelper;
import com.example.echobeat.modelFirebase.Song;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private ImageView songImageView;
    private ImageButton buttonPlay;
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private String songUrl;
    private int currentPosition = 0; // Variable to keep track of the current position
    private ImageButton buttonNext;
    private ImageButton buttonPrevious;
    private SeekBar playerSeeBarTime;
    private TextView textCurrentTime;
    private TextView textTotalTime;
    private FirebaseHelper<Song> firebaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songTitleTextView = findViewById(R.id.song_title);
        songImageView = findViewById(R.id.imageSong);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        playerSeeBarTime = findViewById(R.id.playerSeeBarTime);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalTime = findViewById(R.id.textTotalTime);
        firebaseHelper = new FirebaseHelper<>();

        // Get song data from Intent
        Song song = getIntent().getParcelableExtra("SONG_DATA");
        songUrl = getIntent().getStringExtra("SONG_URL");

        // Display song information
        if (song != null) {
            songTitleTextView.setText(song.getTitle());
            Log.d("PlayerActivity", "Song clicked: " + songUrl);

            // Load image using Glide
            Glide.with(this)
                    .load(song.getPictureSong()) // URL of the image from Firebase
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_loading) // Placeholder image
                            .error(R.drawable.ic_error)) // Error image
                    .into(songImageView);
        }

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mp -> {
            // Ready to play music
            mediaPlayer.seekTo(currentPosition); // Seek to the last position
            mediaPlayer.start();

            int duration = mediaPlayer.getDuration();
            textTotalTime.setText(millisecondsToTimer(duration));
            playerSeeBarTime.setMax(duration);

            // Update current time and seek bar progress
            updateSeekBar();
        });
        // Thiết lập sự kiện onCompletionListener của MediaPlayer
        mediaPlayer.setOnCompletionListener(mp -> {
            // Gọi phương thức để lấy bài hát ngẫu nhiên từ Firestore và phát lại
            firebaseHelper.getRandomSong(new FirebaseHelper.SongCallback() {
                @Override
                public void onSongLoaded(Song song) {
                    if (song != null) {
                        // Update UI with new song data
                        songTitleTextView.setText(song.getTitle());
                        Glide.with(PlayerActivity.this)
                                .load(song.getPictureSong())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.ic_loading)
                                        .error(R.drawable.ic_error))
                                .into(songImageView);

                        // Cập nhật songUrl và currentPosition cho bài hát mới
                        songUrl = song.getSongUrl();
                        currentPosition = 0; // Reset current position
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(songUrl);
                            mediaPlayer.prepareAsync(); // Chuẩn bị phát nhạc mới
                        } catch (IOException e) {
                            Log.e("PlayActivity", "Error setting data source for new song", e);
                        }
                    } else {
                        Log.e("PlayActivity", "No song loaded from Firebase");
                        // Xử lý trường hợp không tìm thấy bài hát
                    }
                }
            });
        });

        buttonPlay.setOnClickListener(v -> {
            if (songUrl != null) {
                if (isPlaying) {
                    // Pause playback
                    mediaPlayer.pause();
                    pausePlayback();
                } else {
                    // Start or resume playback
                    try {
                        if (currentPosition > 0) {
                            mediaPlayer.seekTo(currentPosition); // Seek to last position if paused
                            mediaPlayer.start();
                            startPlayback();
                        } else {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songUrl);
                            mediaPlayer.prepareAsync(); // Prepare asynchronously
                            startPlayback();
                        }
                    } catch (IOException e) {
                        Log.e("PlayerActivity", "Error setting data source", e);
                    }
                }
            } else {
                // Handle null song or song URL case
                Log.e("PlayerActivity", "Song or song URL is null");
                // Display an error message or handle accordingly
            }
        });

        // Handle seek bar change
        playerSeeBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    playerSeeBarTime.setProgress(progress);
                    textCurrentTime.setText(millisecondsToTimer(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Initialize buttonNext and buttonPrevious listeners
        // Implement your logic for these buttons as needed
        buttonNext.setOnClickListener(v -> {
            // Logic for playing the next song
        });

        buttonPrevious.setOnClickListener(v -> {
            // Logic for playing the previous song
        });
    }



    private void startPlayback() {
        // Start playback logic
        isPlaying = true;
        buttonPlay.setImageResource(R.drawable.baseline_pause);
        updateSeekBar(); // Start updating SeekBar
    }

    private void pausePlayback() {
        // Pause playback logic
        isPlaying = false;
        currentPosition = mediaPlayer.getCurrentPosition(); // Save the current position
        buttonPlay.setImageResource(R.drawable.baseline_play);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPlaying = false; // Stop the updateSeekBar thread
    }

    // Helper method to update SeekBar progress and current time
    private void updateSeekBar() {
        new Thread(() -> {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    runOnUiThread(() -> {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        playerSeeBarTime.setProgress(currentPosition);
                        textCurrentTime.setText(millisecondsToTimer(currentPosition));
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // Helper method to convert milliseconds to time format (mm:ss)
    private String millisecondsToTimer(long milliseconds) {
        String timerString = "";
        String secondsString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;
        return timerString;
    }
}
