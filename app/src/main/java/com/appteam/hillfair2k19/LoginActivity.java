package com.appteam.hillfair2k19;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 200;
    public static String fireBaseId;
    public int newBranch = 0;
    private Button phone_button;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        final SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String Login = sharedPreferences.getString("Login", "false");
        String Profile = sharedPreferences.getString("ProfileCreated", "false");
        if (Login.equals("Complete") && Profile.equals("true")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (Login.equals("Complete") && Profile.equals("false")) {
            startActivity(new Intent(this, Profile.class));
            finish();
        } else {
            phone_button = findViewById(R.id.phone_login);
            phone_button.setOnClickListener(this);
        }
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("newBranch",newBranch);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_login:
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                final SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();

                Toast.makeText(LoginActivity.this, "Authenticated.", Toast.LENGTH_SHORT).show();
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = mUser.getUid();
                fireBaseId = uid;
                editor.putString("Login", "Complete");
                editor.putString("fireBaseId", uid);
                editor.commit();

                Intent intent = new Intent(context, Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                finish();

//                mUser.getIdToken(true)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                String idToken = task.getResult().getToken();
//
////                                Intent intent = new Intent(context, MainActivity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                startActivity(intent);
////
////                                finish();
//
//                                // Send token to your backend via HTTPS
//                                takeSignupOrSignin(response, uid, idToken,mUser.getPhoneNumber());
//
//                            } else {
//                                // Handle error -> task.getException();
//                                Log.d("Login Error", task.getException().toString());
//                            }
//                        });

//                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}