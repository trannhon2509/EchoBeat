package com.example.echobeat.activity.loginModel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echobeat.MainActivity;
import com.example.echobeat.R;
import com.example.echobeat.repository.ArtistRepository;
import com.example.echobeat.repository.UserRepository;
import com.example.echobeat.session.SessionManager;
import com.example.echobeat.modelSqlite.Artist;


public class AddInfomation extends AppCompatActivity {

    private ImageButton back;
    private EditText et_name;

    private MultiAutoCompleteTextView et_biography;
    private Button next;
    private ArtistRepository artistRepository;
    private UserRepository userRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_infomation);

        back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> { Intent intent = new Intent(AddInfomation.this, OptionRole.class);startActivity(intent);});

        // Initialize repositories
        artistRepository = new ArtistRepository(this);
        userRepository = new UserRepository(this);

        // Initialize UI components
        back = findViewById(R.id.btn_back);
        et_name = findViewById(R.id.et_name);
        et_biography = findViewById(R.id.et_biography);
        next = findViewById(R.id.btn_next);

        // Set up back button listener
        back.setOnClickListener(v -> {
            Intent intent = new Intent(AddInfomation.this, OptionRole.class);
            startActivity(intent);
        });

        // Set up next button listener
        next.setOnClickListener(v -> {
            // Get input values
            String name = et_name.getText().toString().trim();
            String biography = et_biography.getText().toString().trim();

            // Validate input
            if (name.isEmpty() || biography.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Artist object
            Artist artist = new Artist();

            SessionManager sessionManager = new SessionManager(this);
            String googleId = sessionManager.getGoogleId();
            String userId = userRepository.getUserIdByGoogleId(googleId);

            if (userId == null || userId.isEmpty()) {
                Toast.makeText(this, "User ID is missing!", Toast.LENGTH_SHORT).show();
                return;
            }

            artist.setUserId(userId);
            artist.setArtistName(name);
            artist.setBiography(biography);

            // Save artist information
            boolean isSaved = artistRepository.saveArtist(artist);

            if (isSaved) {
                // Update user role to 2
                boolean isUpdated = userRepository.updateRole(userId);

                if (isUpdated) {
                    Toast.makeText(this, "Artist information saved successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to another activity
                    Intent intent = new Intent(AddInfomation.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Failed to update user role!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to save artist information!", Toast.LENGTH_SHORT).show();
            }
            int newRoleId = 2; // Ví dụ, thay đổi roleId thành 2
            sessionManager.updateRoleId(newRoleId);

        });


    }
}
