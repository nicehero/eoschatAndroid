package io.nicehero.eoschat;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.xdandroid.hellodaemon.DaemonEnv;
import com.xdandroid.hellodaemon.IntentWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>
{
    private DialogsList mDialogsList;
    protected ImageLoader imageLoader;
    private LinearLayout mSettingLayout;
    protected DialogsListAdapter<Dialog> dialogsAdapter;

    private BottomNavigationView navigation;

    private static ChatJS chatJS;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    mDialogsList.setVisibility(View.VISIBLE);
                    mSettingLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_setting:
                    mDialogsList.setVisibility(View.GONE);
                    mSettingLayout.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Picasso.with(MainActivity.this).setLoggingEnabled(true);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Log.i("ImageLoader.loadImage",url);
                Picasso.with(MainActivity.this).load(url).into(imageView);
            }
        };

        mDialogsList = findViewById(R.id.dialogsList);
        mSettingLayout = findViewById(R.id.settingLayout);
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);

        dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);

        mDialogsList.setBackgroundColor(Color.parseColor("#ffffff"));
        mDialogsList.setAdapter(dialogsAdapter);

        Button setNotifyButton = findViewById(R.id.notifyButton);
        setNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentWrapper.whiteListMatters(MainActivity.this, "轨迹跟踪服务的持续运行");
            }
        });

        if (App.getMyAccount() == null) {
            startActivity(new Intent(this, NewAccountActivity.class));
        }
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        AppUtils.showToast(
                this,
                dialog.getDialogName(),
                false);
    }
    @Override
    public void onDialogClick(Dialog dialog) {
        MessageActivity.open(this);
    }
    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }

}
