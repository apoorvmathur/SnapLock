package tk.apoorvmathur.multithreadtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class firstLaunchMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch_menu);
        Button button = (Button) findViewById(R.id.buttonSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buttonClicked();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void buttonClicked() throws NoSuchAlgorithmException {
        TextView textView = (TextView) findViewById(R.id.textView5);
        EditText password = (EditText) findViewById(R.id.signupPassword);
        EditText confPassword = (EditText) findViewById(R.id.confPassword);
        String pass = password.getText().toString();
        String confPass = confPassword.getText().toString();
        password.setText("");
        confPassword.setText("");
        if(pass.equals(confPass)) {
            MessageDigest encoder = MessageDigest.getInstance("SHA1");
            byte[] passDigest = encoder.digest(pass.getBytes());
            String encodedPass = Base64.encodeToString(passDigest, Base64.DEFAULT);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.key_file), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.password_hash), encodedPass);
            editor.commit();
            this.finish();
        } else {
            textView.setVisibility(TextView.VISIBLE);
        }
    }
}
