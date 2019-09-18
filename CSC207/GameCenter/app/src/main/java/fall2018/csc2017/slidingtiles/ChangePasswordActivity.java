package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

/**
 * Activity for changing password extending AppCompatActivity.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    /**
     * For user to input username.
     */
    TextView username;

    /**
     * For user to enter their old password or email.
     */
    TextView passwordOrEmail;

    /**
     * For user to enter new password.
     */
    TextView newPw;

    /**
     * For user to re-enter new password.
     */
    TextView confirmPw;

    /**
     * A button that update all the information when click.
     */
    Button update;

    /**
     * For user to enter new password.
     */
    TextView message;

    /**
     * The user involved in the activity.
     */
    UserAccount user;

    /**
     * All users.
     */
    UserAccountManager users;

    /**
     * Databae helper.
     */
    private DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllComponents();
        getAllUser();
        addUpdateButton();
    }

    /**
     * Add button that allows user to update information.
     */
    private void addUpdateButton() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateButtonPushed()) {
                    Intent intent = new Intent(ChangePasswordActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Link all the fields with the corresponding element in the view.
     */
    private void getAllComponents() {
        setContentView(R.layout.activity_change_password);
        username = findViewById(R.id.username);
        passwordOrEmail = findViewById(R.id.psOrEmail);
        newPw = findViewById(R.id.password);
        confirmPw = findViewById(R.id.confirmpw);
        update = findViewById(R.id.update);
        message = findViewById(R.id.message);
    }

    /**
     * Get the user list and the current user information from the database.
     */
    private void getAllUser() {
        myDB = new DatabaseHelper(this);
        this.users = myDB.selectAccountManager();
    }

    /**
     * Update information of UserAccount.
     * @return if information is updated
     */
    boolean updateButtonPushed() {
        String usernameS = username.getText().toString();
        if (!users.getUserList().contains(usernameS)) {
            Toasty.error(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        user = myDB.selectUser(usernameS);
        String passwordOrEmailS = this.passwordOrEmail.getText().toString();
        String newPwS = this.newPw.getText().toString();
        String confirmS = confirmPw.getText().toString();
        if (!(user.getEmail().equals(passwordOrEmailS) || user.getPassword().equals(passwordOrEmailS))) {
            Toasty.error(getApplicationContext(), "The current password or email is not correct.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (!(newPwS.equals(confirmS))) {
            Toasty.error(getApplicationContext(), "The new password are not the same with the confirmed password.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (!(validateInfo(newPw.getText().toString(), "^[a-z0-9]{1,9}$"))) {
            Toasty.error(getApplicationContext(), "The format of new password is not correct.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        user.setPassword(newPwS);
        myDB.updateUser(username.getText().toString(), user);
        Toasty.success(getApplicationContext(), "Your password is successfully updated.", Toast.LENGTH_SHORT, true).show();
        return true;

    }
    /**
     * Check if the String argument fulfills the regex argument
     *
     * @param info  the information to be checked
     * @param regex the regex to be fulfilled.
     * @return whether the regex is fulfilled by the info.
     */
    private boolean validateInfo(String info, String regex) {
        String infoRegex = regex;
        Pattern regexP = Pattern.compile(infoRegex);
        Matcher matcher = regexP.matcher(info);
        return matcher.matches();
    }

}
