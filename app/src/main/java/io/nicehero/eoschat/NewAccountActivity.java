package io.nicehero.eoschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xdandroid.hellodaemon.IntentWrapper;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaccount);
        Button b = (Button)findViewById(R.id.submitButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText typePassword = (EditText)NewAccountActivity.this.findViewById(R.id.typePassword);
                EditText typePassword2 = (EditText)NewAccountActivity.this.findViewById(R.id.typePassword2);
                if (!typePassword.getText().toString().equals(typePassword2.getText().toString())) {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.password_not_same),
                            false);
                    return;
                }
            }
        });
    }

    public void onBackPressed() {
        if (App.getMyAccount() == null) {
            try {
                Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
                launcherIntent.addCategory(Intent.CATEGORY_HOME);
                startActivity(launcherIntent);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return;
        }
        IntentWrapper.onBackPressed(this);
    }

}
