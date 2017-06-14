package tk.apoorvmathur.multithreadtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Base64;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.key_file), MODE_PRIVATE);
        String pWord = sharedPreferences.getString(getString(R.string.password_hash), "");
        if(pWord.equals("")) {
            Intent intent = new Intent(this, firstLaunchMenu.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
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
        EditText password = (EditText) findViewById(R.id.loginPassword);
        String pass = password.getText().toString().trim();
        password.setText("");
        MessageDigest encoder = MessageDigest.getInstance("SHA1");
        byte[] passDigest = encoder.digest(pass.getBytes());
        String encodedPass = Base64.encodeToString(passDigest, Base64.DEFAULT).trim();
        System.out.println(encodedPass);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.key_file), MODE_PRIVATE);
        String pWord = sharedPreferences.getString(getString(R.string.password_hash), "").trim();
        System.out.println(pWord);
        if(encodedPass.equals(pWord)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("password", pass);
            startActivity(intent);
        } else {
            TextView textView = (TextView) findViewById(R.id.textView3);
            textView.setVisibility(TextView.VISIBLE);
        }
    }
}
