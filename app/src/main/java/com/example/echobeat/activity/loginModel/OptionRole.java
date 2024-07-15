package com.example.echobeat.activity.loginModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echobeat.MainActivity;
import com.example.echobeat.R;
import com.example.echobeat.activity.LoginActivity;
import com.example.echobeat.activity.LoginGoogle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class OptionRole extends AppCompatActivity {
    private ImageView logout;
    private LinearLayout listener;
    private LinearLayout artist;

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option_role);

        auth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        logout = findViewById(R.id.logo);
        logout.setOnClickListener(v -> {
            signOut();
        });
        listener = findViewById(R.id.listener_sign_in_button);
        listener.setOnClickListener(v -> { Intent intent = new Intent(OptionRole.this, MainActivity.class);startActivity(intent);});

        artist = findViewById(R.id.artist_sign_in_button);
        artist.setOnClickListener(v ->{Intent intent = new Intent(OptionRole.this, AddInfomation.class); startActivity(intent);});
    }

    private void signOut() {
        auth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(OptionRole.this, "Signed Out", Toast.LENGTH_SHORT).show();

            // Xóa thông tin người dùng đã lưu trữ trong SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("fullname");
            editor.remove("email");
            editor.remove("avatar");
            editor.remove("role");
            editor.remove("googleId");
            editor.apply();

            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(OptionRole.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
