package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    /**
     * EditText view for username
     */
    EditText etUsername;

    /**
     * EditText view for password
     */
    EditText etPassword;

    /**
     * Button for log in
     */
    Button bLogIn;

    /**
     * TextView for register, functions as a button
     */
    TextView registerLink;

    /**
     * TextView for reset password
     */
    TextView resetPassword;

    /**
     * UserAccountManager
     */
    UserAccountManager userAccountManager;

    /**
     * TextView to set messages.
     */
    TextView massage;

    /**
     * Initialize login in activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new DatabaseHelper(this);
        if (!myDB.ifAccountManagerExists()) {
            myDB.createUserAccountManager();
            userAccountManager = myDB.selectAccountManager();
        } else {
            userAccountManager = myDB.selectAccountManager();
        }
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.password);
        bLogIn = findViewById(R.id.bLogin);
        massage = findViewById(R.id.massage);
        registerLink = findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        resetPassword = findViewById(R.id.forgotPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasswordIntent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                LoginActivity.this.startActivity(resetPasswordIntent);
            }
        });

        loginButtonPushed();
    }

    /**
     * Actions after login in button is triggered, jump to game center activity while passing info to
     * game center activity.
     */
    private void loginButtonPushed() {
        bLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (myDB.hasUser(username) && ifPasswordCorrect(username, password)) {
                    Toasty.success(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT, true).show();
                    DataHolder.getInstance().save("current user", username);
                    Intent loginIntent = new Intent(LoginActivity.this, InfoPanelMainActivity.class);
                    LoginActivity.this.startActivity(loginIntent);
                } else {
                    Toasty.error(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }
    // pass user name to global context using application singleton
    // so that all activity knows current user

    /**
     * Check if tempted user has legit id and username
     *
     * @param username the username to check
     * @param password the password to check
     * @return the userAccount if the username and password are correctly matching
     */
    private boolean ifPasswordCorrect(String username, String password) {
        UserAccount account = myDB.selectUser(username);
        return account.getPassword().equals(password);
    }
}

