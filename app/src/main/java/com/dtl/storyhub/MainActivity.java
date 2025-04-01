package com.dtl.storyhub;

import android.database.Cursor;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onResume() {
        DatabaseHelper.getInstance(this).openDatabase();
        super.onResume();
    }

    @Override
    protected void onPause() {
        DatabaseHelper.getInstance(this).closeDatabase();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DatabaseHelper.getInstance(this).closeDatabase();
        super.onDestroy();
    }

    private void initAdminAccount() {
        UserDAO userDAO = new UserDAO(this);
        User admin = new User();
        admin.setFirstName("Long");
        admin.setLastName("Do");
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setEmail("longdo.admin@gmail.com");
        admin.setRole(UserRole.ADMIN);

        User newUser = userDAO.insertUser(admin);
        if(newUser != null) {
            System.out.println("Create admin account!");
        }
        else {
            System.out.println("Already have admin account!");
        }

        DatabaseHelper.getInstance(this).closeDatabase();
    }
}