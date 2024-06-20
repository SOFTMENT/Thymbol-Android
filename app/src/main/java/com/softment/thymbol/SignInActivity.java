package com.softment.thymbol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softment.thymbol.Model.CountryModel;
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Utils.Constants;
import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    EditText emailAddress, password;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailAddress = findViewById(R.id.emailaddress);
        password = findViewById(R.id.password);




        //Reset Btn Clicked
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = emailAddress.getText().toString().trim();

                if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,getString(R.string.enteremail));
                }
                else {
                    ProgressHud.show(SignInActivity.this,getString(R.string.wait));
                    FirebaseAuth.getInstance().sendPasswordResetEmail(sEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                Services.showDialog(SignInActivity.this,getString(R.string.resetpassword),getString(R.string.wehavesentreset));
                            }
                            else {
                                Services.showDialog(SignInActivity.this,getString(R.string.ERROR),task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        //RegisterBtnClicked
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.googleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 909);

            }
        });

        //Apple Login
        findViewById(R.id.apple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OAuthProvider.Builder provider = OAuthProvider.newBuilder("apple.com");

                List<String> scopes =
                        new ArrayList<String>() {
                            {
                                add("email");
                                add("name");
                            }
                        };
                provider.setScopes(scopes);


                Task<AuthResult> pending = FirebaseAuth.getInstance().getPendingAuthResult();
                if (pending != null) {
                    pending.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            appleSignIn(provider);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("APPLE_ERROR",e.getLocalizedMessage());
                        }
                    });
                }
                else {
                    appleSignIn(provider);
                }


            }
        });

        //LoginBtnClicked
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = emailAddress.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,getString(R.string.enteremail));
                }
                else if (sPassword.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,getString(R.string.enterpassword));
                }
                else {
                    ProgressHud.show(SignInActivity.this,getString(R.string.signin));
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    Services.getCurrentUserData(SignInActivity.this,user.getUid(),true);
                                }
                            }
                            else {
                                Services.showDialog(SignInActivity.this,getString(R.string.ERROR),task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public void appleSignIn(OAuthProvider.Builder provider){
        FirebaseAuth.getInstance().startActivityForSignInWithProvider(SignInActivity.this, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                ProgressHud.show(SignInActivity.this,"");
                                FirebaseFirestore.getInstance().collection("Users").document(authResult.getUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        ProgressHud.dialog.dismiss();
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null && task.getResult().exists()) {
                                                Services.getCurrentUserData(SignInActivity.this, authResult.getUser().getUid(),true);
                                            }
                                            else {
                                                String emailId = "";
                                                for (UserInfo firUserInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()){
                                                    emailId = firUserInfo.getEmail();
                                                }

                                                UserModel userModel = new UserModel();
                                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                userModel.profilePic = "https://firebasestorage.googleapis.com/v0/b/eventkreyol.appspot.com/o/profile-placeholder.jpg?alt=media&token=cb60876c-f59f-4eb4-bcdc-ccb80e5d9a4f";
                                                userModel.email = emailId;
                                                userModel.fullName = authResult.getAdditionalUserInfo().getUsername();
                                                userModel.registredAt = new Date();
                                                userModel.regiType = "apple";


                                                Services.addUserDataOnServer(SignInActivity.this,userModel);
                                            }
                                        }

                                    }
                                });



                            }
                        })
                        .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("APPLE_ERROR",e.getLocalizedMessage());
                            }
                        });
    }


    private void firebaseAuth(AuthCredential credential) {

        ProgressHud.show(this,"");
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProgressHud.dialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null && task.getResult().exists()) {
                                                Services.getCurrentUserData(SignInActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                                            }
                                            else {

                                                String emailId = "";
                                                for (UserInfo firUserInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()){
                                                    emailId = firUserInfo.getEmail();
                                                }

                                                UserModel userModel = new UserModel();
                                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                userModel.profilePic = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString().replace("s96-c","s512-c");
                                                userModel.email = emailId;
                                                userModel.fullName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                                userModel.registredAt = new Date();
                                                userModel.regiType = "google";


                                                Services.addUserDataOnServer(SignInActivity.this,userModel);
                                            }
                                        }
                                        else {
                                            Services.showDialog(SignInActivity.this,getString(R.string.ERROR),task.getException().getLocalizedMessage());
                                        }
                                    }
                                });

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Services.showDialog(SignInActivity.this,getString(R.string.ERROR), Objects.requireNonNull(task.getException()).getLocalizedMessage());
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 909) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth(credential);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Services.showDialog(SignInActivity.this,getString(R.string.ERROR),e.getLocalizedMessage());
            }
        }


    }
}
