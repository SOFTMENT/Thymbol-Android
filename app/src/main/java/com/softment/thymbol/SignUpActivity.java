package com.softment.thymbol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText name = findViewById(R.id.fullName);
        EditText emailAddress = findViewById(R.id.emailaddress);
        EditText password = findViewById(R.id.password);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = name.getText().toString().trim();
                String sEmail = emailAddress.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (sName.isEmpty()) {
                    Services.showCenterToast(SignUpActivity.this,getString(R.string.enterfullname));
                }
                else if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignUpActivity.this,getString(R.string.enteremail));
                }
                else if (sPassword.isEmpty()) {
                    Services.showCenterToast(SignUpActivity.this,getString(R.string.enterpassword));
                }
                else {
                    ProgressHud.show(SignUpActivity.this,getString(R.string.creatingaccount));
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                UserModel userModel = new UserModel();
                                userModel.email = sEmail;
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                userModel.profilePic = "https://firebasestorage.googleapis.com/v0/b/eventkreyol.appspot.com/o/profile-placeholder.jpg?alt=media&token=cb60876c-f59f-4eb4-bcdc-ccb80e5d9a4f";
                                userModel.fullName = sName;
                                userModel.registredAt = new Date();
                                userModel.regiType = "custom";
                                Services.addUserDataOnServer(SignUpActivity.this,userModel);
                            }
                            else {
                                Services.showDialog(SignUpActivity.this,getString(R.string.ERROR),task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
