package us.master.entregable1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import us.master.entregable1.entity.Trip;

public class LoginActivity extends AppCompatActivity {

        private static final int RC_SIGN_IN = 0x152;
        private FirebaseAuth mAuth;
        private Button signinButtonGoogle;
        private Button signinButtonMail;
        private Button loginButtonSignUp;
        private ProgressBar progressBar;
        private TextInputLayout loginEmailParent;
        private TextInputLayout loginPassParent;
        private AutoCompleteTextView loginEmail;
        private AutoCompleteTextView loginPass;
        private ValueEventListener valueEventListener;
        // private FirebaseDatabaseService firebaseDatabaseService;
        FirestoreService firestoreService;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mAuth = FirebaseAuth.getInstance();

            progressBar = findViewById(R.id.login_progress);
            loginEmail = findViewById(R.id.login_email_et);
            loginPass = findViewById(R.id.login_pass_et);
            loginEmailParent = findViewById(R.id.login_email);
            loginPassParent = findViewById(R.id.login_pass);
            signinButtonGoogle = findViewById(R.id.login_button_google);
            signinButtonMail = findViewById(R.id.login_button_mail);
            loginButtonSignUp = findViewById(R.id.login_button_register);

            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_client_id))
                    .requestEmail()
                    .build();


            signinButtonGoogle.setOnClickListener(l -> attemptLoginGoogle(googleSignInOptions));

            signinButtonMail.setOnClickListener(l -> attemptLoginEmail());

            loginButtonSignUp.setOnClickListener(l -> redirectSignUpActivity());

        }

        private void redirectSignUpActivity() {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra(SignUpActivity.EMAIL_PARAM, loginEmail.getText().toString());
            startActivity(intent);
        }

        private void attemptLoginGoogle(GoogleSignInOptions googleSignInOptions) {
            GoogleSignInClient googleSignIn = GoogleSignIn.getClient(this, googleSignInOptions);
            Intent siginIntent = googleSignIn.getSignInIntent();
            Log.d("JD", "RC_SIGN_IN: " + RC_SIGN_IN);
            startActivityForResult(siginIntent, RC_SIGN_IN);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Log.d("JD", "onActivityResult request code: " + requestCode);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> result = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = result.getResult(ApiException.class);
                    assert account != null;
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    Log.d("JD", "mAuth: " + mAuth);
                    if (mAuth == null) {
                        mAuth = FirebaseAuth.getInstance();
                    }
                    if (mAuth != null) {
                        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                            Log.d("JD", "task.isSuccessful(): " + task.isSuccessful());
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                checkUserDatabaseLogin(user);
                            } else {
                                showErrorDialogMail();
                            }
                        });
                    } else {
                        showGooglePlayServicesError();
                    }
                } catch (ApiException e){
                    Log.d("JD","Exception e: " + e);
                    showErrorDialogMail();
                }
            }
        }

        private void attemptLoginEmail() {
            loginEmailParent.setError(null);
            loginPassParent.setError(null);

            if(loginEmail.getText().length() == 0 ) {
                loginEmailParent.setErrorEnabled(true);
                loginEmailParent.setError(getString(R.string.login_email_error_1));
            } else if (loginPass.getText().length() == 0) {
                loginPassParent.setErrorEnabled(true);
                loginPassParent.setError(getString(R.string.login_mail_error_2));
            } else {
                signInEmail();
            }
        }

        private void signInEmail() {
            if (mAuth == null) {
                mAuth = FirebaseAuth.getInstance();
            }

            if (mAuth != null) {
                mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPass.getText().toString()).addOnCompleteListener(this, task -> {
                    if(!task.isSuccessful() || task.getResult().getUser() == null) {
                        showErrorDialogMail();
                    } else if (!task.getResult().getUser().isEmailVerified()) {
                        showErrorEmailVerified(task.getResult().getUser());
                    } else {
                        FirebaseUser user = task.getResult().getUser();
                        checkUserDatabaseLogin(user);
                    }
                });
            } else {
                showGooglePlayServicesError();
            }
        }

        private void showGooglePlayServicesError() {
            Snackbar.make(loginButtonSignUp, R.string.login_google_play_services_error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.login_download_google_play_services, view -> {
                        try {
                            startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.google_play_services_download_url))));
                        }catch (Exception ex) {
                            startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_download_url))));
                        }
                    }).show();
        }

        @SuppressLint("StringFormatInvalid")
        private void checkUserDatabaseLogin(FirebaseUser user) {

            Toast.makeText(this, String.format(getString(R.string.login_completed), user.getEmail()), Toast.LENGTH_SHORT).show();

            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading Trips...");
            progress.setMessage("Wait while loading all your trips...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();

            firestoreService = FirestoreService.getServiceInstance();
            firestoreService.getTravels(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            Trip travel = documentSnapshot.toObject(Trip.class);
                            Constantes.trips.add(travel);
                            Log.d("JD", "firestore lectura: " + travel.toString());
                        }
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            // startActivity(new Intent(this, MainActivity.class));

            progress.dismiss();
            LoginActivity.this.finish();

//            FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
//
//            firebaseDatabaseService.saveTravel(new Travel("Sevilla", "Londres","Lluvioso", 7.8f),
//                    new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                            if(databaseError == null) {
//                                Log.d("JD", "Travel insertado: ");
//                            } else {
//                                Log.d("JD", "Error al insertar el Travel: " + databaseError.getMessage());
//                            }
//                        }
//                    });
//
//            // solo se ejecuta una vez cuando lo llamemos
//            firebaseDatabaseService.getTravelId("1").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
//                        Travel travel = dataSnapshot.getValue(Travel.class);
//                        Log.d("JD", "Valor modificado: " + travel.toString());
//                        Toast.makeText(MainActivity.this, travel.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            // se llama cuando se modifica el valor del rate
//            firebaseDatabaseService.getTravelRate("2").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
//                        Float rate = dataSnapshot.getValue(Float.class);
//                        Log.d("JD", "Valor rate modificado individualmente: " + rate);
//                        Toast.makeText(MainActivity.this, rate.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            // monitorizar un solo elemento, como el usuario logado
//            if (valueEventListener == null) { // solo lo inicializamos 1 vez, sino se ejecutara tantas veces como inicialicemos
//                valueEventListener =  firebaseDatabaseService.getTravelId("2").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
//                            Travel travel = dataSnapshot.getValue(Travel.class);
//                            Log.d("JD", "Elemento modificado individualmente: " + travel.toString());
//                            Toast.makeText(MainActivity.this, travel.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//
//            // firebaseDatabaseService.getTravelId("2").removeEventListener(valueEventListener);
//
//            // monitorizar una coleccion de elementos
//            ChildEventListener childEventListener =  firebaseDatabaseService.getTravel().addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
//                        Travel travel = dataSnapshot.getValue(Travel.class);
//                        // Toast.makeText(MainActivity.this, "Añadido elemento: " + travel.toString(), Toast.LENGTH_SHORT).show();
//                        Log.d("JD", "Añadido elemento: " + travel.toString());
//                    }
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    Travel travel = dataSnapshot.getValue(Travel.class);
//                    Log.d("JD", "Modificado elemento: " + travel.toString());
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                    Travel travel = dataSnapshot.getValue(Travel.class);
//                    // Toast.makeText(MainActivity.this, "Añadido elemento: " + travel.toString(), Toast.LENGTH_SHORT).show();
//                    Log.d("JD", "Eliminado elemento: " + travel.toString());
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    Travel travel = dataSnapshot.getValue(Travel.class);
//                    // Toast.makeText(MainActivity.this, "Añadido elemento: " + travel.toString(), Toast.LENGTH_SHORT).show();
//                    Log.d("JD", "Añadido movido: " + travel.toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            // metodo firecloud
//
//            FirestoreService firestoreService = FirestoreService.getServiceInstance();
//            firestoreService.saveTravel(new Travel("malaga", "sevilla", "viaje corto", 9.6f), new OnCompleteListener<DocumentReference>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentReference> task) {
//                    if (task.isSuccessful()) {
//                        DocumentReference documentReference = task.getResult();
//                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()){
//                                    DocumentSnapshot document = task.getResult();
//                                    Travel travel = document.toObject(Travel.class);
//                                    Log.d("JD", "firestore almacenamiento feedback: " + travel.toString());
//                                }
//                            }
//                        });
//                        Log.d("JD", "firestore almacenamiento completado: " + task.getResult().getId());
//                    } else {
//                        Log.d("JD", "firestore almacenamiento ha fallado ");
//                    }
//                }
//            });
//
//            firestoreService.getTravels(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()) {
//                        List<Travel> travels = new ArrayList<>();
//                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
//                            Travel travel = documentSnapshot.toObject(Travel.class);
//                            travels.add(travel);
//                            Log.d("JD", "firestore lectura: " + travels.toString());
//                        }
//                    }
//                }
//            });
//
//            firestoreService.getTravelsFiltered(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()) {
//                        List<Travel> travels = new ArrayList<>();
//                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
//                            Travel travel = documentSnapshot.toObject(Travel.class);
//                            travels.add(travel);
//                            Log.d("JD", "firestore lectura filtered: " + travels.toString());
//                        }
//                    }
//                }
//            });
//
//            firestoreService.getTravel("1", new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                    if (documentSnapshot != null && documentSnapshot.exists()) {
//                        Travel travel = documentSnapshot.toObject(Travel.class);
//                        Log.d("JD", "firestore lectura individual: " + travel.toString());
//                        // loginEmail.setText(travel.toString());
//                    }
//                }
//            });
//
//            // startActivity(new Intent(this, TravelList.class));
//            startActivity(new Intent(this, FirebaseStorage.class));

        }

//        @Override
//        protected void onPause() {
//            super.onPause();
//            if(firebaseDatabaseService != null && valueEventListener != null) {
//                firebaseDatabaseService.getTravelId("2").removeEventListener(valueEventListener);
//            }
//        }

        private void showErrorEmailVerified(FirebaseUser user) {
            hideLoginButton(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.login_verified_mail_error)
                    .setPositiveButton(R.string.login_verified_error_ok, ((dialog, which) -> {
                        user.sendEmailVerification().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Snackbar.make(loginEmail, R.string.login_verified_email_error_send, Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(loginEmail, R.string.login_verified_email_error_no_send, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    })).setNegativeButton(R.string.login_verified_error_cancel, (dialog, which) -> {
            }).show();
        }

        private void hideLoginButton(boolean hide) {

            TransitionSet transitionSet = new TransitionSet();
            Transition layoutFade = new AutoTransition();
            layoutFade.setDuration(1000);
            transitionSet.addTransition(layoutFade);

            if (hide) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
                progressBar.setVisibility(View.VISIBLE);
                signinButtonMail.setVisibility(View.GONE);
                signinButtonGoogle.setVisibility(View.GONE);
                loginButtonSignUp.setVisibility(View.GONE);
                loginEmailParent.setEnabled(false);
                loginPassParent.setEnabled(false);
            } else {
                TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
                progressBar.setVisibility(View.GONE);
                signinButtonMail.setVisibility(View.VISIBLE);
                signinButtonGoogle.setVisibility(View.VISIBLE);
                loginButtonSignUp.setVisibility(View.VISIBLE);
                loginEmailParent.setEnabled(true);
                loginPassParent.setEnabled(true);
            }
        }

        private void showErrorDialogMail() {
            hideLoginButton(false);
            Snackbar.make(signinButtonMail, getString(R.string.login_mail_access_error), Snackbar.LENGTH_SHORT).show();
        }

    }
