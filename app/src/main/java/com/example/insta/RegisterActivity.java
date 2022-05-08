package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY=99;

    EditText userNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    Spinner spinner;
    RadioGroup radioGroup;

    private SharedPreferences preferences;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bundle bundle = getIntent().getExtras();
        // int secret_key = bundle.getInt("SECRET_KEY");
        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordAgainEditText);
        spinner = findViewById(R.id.munkaSpinner);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.ferfi);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userNameEditText.setText(userName);
        passwordEditText.setText(password);
        passwordConfirmEditText.setText(password);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.munka_tipus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        myAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            return;
        }
        String munkaTipus = spinner.getSelectedItem().toString();

        int checkedID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(checkedID);
        String acconType= radioButton.getText().toString();



        Log.i(LOG_TAG, "Regisztrált: " + userName + ", e-mail: " + email);
        // TODO: A regisztrációs funkcionalitást meg kellene valósítani egyszer.
        myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    startScrolling();;
                } else {
                    Log.d(LOG_TAG, "User was't created successfully:", task.getException());
                    Toast.makeText(RegisterActivity.this, "User was't created successfully:", Toast.LENGTH_LONG).show();
                }
            }
        });
        startScrolling();
    }

    public void cancel(View view) {
        finish();
    }

    private void startScrolling(/*registerd user data*/){
        Intent intent = new Intent(this,FooldalActivity.class);
        //intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sekectedItem = adapterView.getItemAtPosition(i).toString();
        Log.i(LOG_TAG,sekectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //TODO
    }
}