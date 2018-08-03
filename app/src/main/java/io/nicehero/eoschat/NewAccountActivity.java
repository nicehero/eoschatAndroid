package io.nicehero.eoschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
        setTitle(getString(R.string.title_bind_account));

        final EditText typeEncryptPrivateKey = NewAccountActivity.this.findViewById(R.id.typeEncryptPrivateKey);
        final EditText typeAccountName = NewAccountActivity.this.findViewById(R.id.typeAccountName);
        final EditText typePrivateKey = NewAccountActivity.this.findViewById(R.id.typePrivateKey);
        final EditText typeNodeAddress = NewAccountActivity.this.findViewById(R.id.typeNodeAddress);
        final EditText typeContractAccount = NewAccountActivity.this.findViewById(R.id.typeContractAccount);
        final EditText typePassword = NewAccountActivity.this.findViewById(R.id.typePassword);
        final EditText typePassword2 = NewAccountActivity.this.findViewById(R.id.typePassword2);
        if (App.getMyAccount() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            typePrivateKey.setText(App.getMyAccount().privateKey);
            typeAccountName.setText(App.getMyAccount().accountName);
            typeEncryptPrivateKey.setText(App.getMyAccount().encryptPrivateKey);
            typeNodeAddress.setText(App.getMyAccount().nodeAddress);
            typeContractAccount.setText(App.getMyAccount().contractAccount);
            typePassword.setText(App.getMyAccount().password);
        }
        else
        {
            if (App.isDebug(this))
            {
                typePrivateKey.setText("5JamVnrEocpLxf4fBfLGqHYCnvs2ACivDND8JtQ5pb6tiRfUTSj");
                typeAccountName.setText("eoschattest");
                typeEncryptPrivateKey.setText("5fd6732fa52e0711403282597b58e095907e7ea7d8797b6e4988f9abf7eafd7e");
            }
        }
        Button b = findViewById(R.id.submitButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!typePassword.getText().toString().equals(typePassword2.getText().toString())) {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.password_not_same),
                            false);
                    return;
                }

                if (typeAccountName.getText().toString().isEmpty())
                {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.no_account_name),
                            false);
                    return;
                }
                if (typePrivateKey.getText().toString().isEmpty())
                {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.no_private_key),
                            false);
                    return;
                }
                if (typeContractAccount.getText().toString().isEmpty())
                {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.no_private_key),
                            false);
                    return;
                }
                if (typeNodeAddress.getText().toString().isEmpty())
                {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.no_node_address),
                            false);
                    return;
                }
                String regex="^[A-Fa-f0-9]+$";
                if (typeEncryptPrivateKey.getText().toString().isEmpty()
                        || !typeEncryptPrivateKey.getText().toString().matches(regex))
                {
                    AppUtils.showToast(
                            NewAccountActivity.this,
                            getString(R.string.no_encrypt_privatekey),
                            false);
                    return;
                }
                App.Account account = new App.Account();
                account.accountName = typeAccountName.getText().toString();
                account.nodeAddress = typeContractAccount.getText().toString();
                account.contractAccount = typeContractAccount.getText().toString();
                account.encryptPrivateKey = typeEncryptPrivateKey.getText().toString();
                account.password = typePassword.getText().toString();
                account.privateKey = typePrivateKey.getText().toString();
                App.setMyAccount(account);
                NewAccountActivity.this.onBackPressed();
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
        super.onBackPressed();
    }

}
