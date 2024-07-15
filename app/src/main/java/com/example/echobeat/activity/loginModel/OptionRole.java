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
import com.example.echobeat.dbFirebase.FirebaseHelper;
import com.example.echobeat.modelSqlite.User;
import com.example.echobeat.repository.UserRepository;
import com.example.echobeat.session.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class OptionRole extends AppCompatActivity {
    private ImageView logout;
    private LinearLayout listener;
    private LinearLayout artist;

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseHelper<com.example.echobeat.modelFirebase.User> userHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option_role);

        SessionManager sessionManager = new SessionManager(this);
        String googleId = sessionManager.getGoogleId();
        String userId = sessionManager.getUserid();
        String userName = sessionManager.getUsername();
        String email = sessionManager.getEmail();
        String image = sessionManager.getImage();
        int roleId =sessionManager.getRoleId();



        auth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        logout = findViewById(R.id.logo);
        logout.setOnClickListener(v -> {
            signOut();
        });

        listener = findViewById(R.id.listener_sign_in_button);
        listener.setOnClickListener(v -> {
            //luu vao sql lite
            User userInfo = new User(userId,userName, email, image, 1, googleId);
            UserRepository userRepository = new UserRepository(getApplicationContext());
            userRepository.register(userInfo);
            //luu vao firebase
            com.example.echobeat.modelFirebase.User userfb = new com.example.echobeat.modelFirebase.User(userId,userName, email, image, roleId, googleId);
            userHelper.addData("users", userfb);
            Toast.makeText(this, "Welcome Listener", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OptionRole.this, MainActivity.class);startActivity(intent);
        });

        artist = findViewById(R.id.artist_sign_in_button);
        artist.setOnClickListener(v ->{

            Toast.makeText(this, "Welcome Artirs", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OptionRole.this, AddInfomation.class); startActivity(intent);
        });
    }

    private void signOut() {
        auth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(OptionRole.this, "Signed Out", Toast.LENGTH_SHORT).show();

            // Xóa thông tin người dùng đã lưu trữ trong SharedPreferences
//            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.remove("fullname");
//            editor.remove("email");
//            editor.remove("avatar");
//            editor.remove("role");
//            editor.remove("googleId");
//            editor.apply();

            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(OptionRole.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }


}
