package com.example.echobeat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.echobeat.R;
import com.example.echobeat.Repository.UserRepository;
import com.example.echobeat.activity.loginModel.OptionRole;
import com.example.echobeat.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout googleSignInButton;

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(v -> {
            googleSingIn();
        });
    }
    private void googleSingIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    auth.signInWithCredential(credential).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(this, "User Signed In", Toast.LENGTH_SHORT).show();
                            firebaseAuthWithGoogle(account.getIdToken());
                        } else {
                            Toast.makeText(this, "Error" + task1.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, cập nhật UI với thông tin người dùng đã đăng nhập
                        FirebaseUser user = auth.getCurrentUser();
                        String fullname = user.getDisplayName();
                        String email = user.getEmail();
                        String avatar = user.getPhotoUrl().toString();
                        String googleId = user.getUid();

                        User userInfo = new User("",fullname, email, avatar, 1, googleId);
                        UserRepository userRepository = new UserRepository(getApplicationContext());
                        boolean check = userRepository.checkExistIdGoogle(googleId);

                        if (!check) {
                            Toast.makeText(this, "Not found " + googleId, Toast.LENGTH_SHORT).show();
                            userRepository.register(userInfo);
                        } else {
                            Toast.makeText(this, "Welcome!!!", Toast.LENGTH_SHORT).show();
                        }

                        // Lưu thông tin người dùng vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("fullname", fullname);
                        editor.putString("email", email);
                        editor.putString("avatar", avatar);
                        editor.putInt("role", 0); // Đổi role nếu cần thiết
                        editor.putString("googleId", googleId);
                        editor.apply();

                        // Chuyển đến activity tiếp theo sau khi đăng nhập thành công
                        Intent intent = new Intent(LoginActivity.this, OptionRole.class);
                        startActivity(intent);
                    } else {
                        // Đăng nhập thất bại, hiển thị thông báo cho người dùng
                        Toast.makeText(this, "Failed to sign in: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}