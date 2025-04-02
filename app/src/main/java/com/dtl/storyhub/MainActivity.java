package com.dtl.storyhub;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dtl.storyhub.database.DatabaseHelper;
import com.dtl.storyhub.database.dao.UserDAO;
import com.dtl.storyhub.enums.UserRole;
import com.dtl.storyhub.models.User;
import com.dtl.storyhub.utils.SessionManager;


public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initAdminAccount();

        sessionManager = new SessionManager(this);
        handleLoginSession();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper.getInstance(this).openDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper.getInstance(this).closeDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.getInstance(this).closeDatabase();
    }

    private void initAdminAccount() {
        SharedPreferences prefs = getSharedPreferences("StoryHub", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            UserDAO userDAO = new UserDAO(this);
            User admin = new User();
            admin.setFirstName("Long");
            admin.setLastName("Do");
            admin.setPassword("123456");
            admin.setEmail("admin@gmail.com");
            admin.setRole(UserRole.ADMIN);

            User newUser = userDAO.insertUser(admin);
            if (newUser != null) {
                System.out.println("\n--> Create admin account!\n");
            } else {
                System.out.println("\n--> Already have admin account!\n");
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }

        System.out.println("\n--> Not first run\n");
        DatabaseHelper.getInstance(this).closeDatabase();
    }

    private void handleLoginSession() {
        String email = sessionManager.getEmail();

        if (!sessionManager.isLoggedIn()) {
            System.out.println("\n--> No user session found. Redirecting to Login...\n");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        System.out.println("\nUser logged in: " + email + "\n");
        setContentView(R.layout.activity_main);
    }
}