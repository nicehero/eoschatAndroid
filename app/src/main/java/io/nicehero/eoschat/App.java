package io.nicehero.eoschat;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.math.BigInteger;

import com.xdandroid.hellodaemon.DaemonEnv;

public class App extends Application {

    private static final String mHomeUrl = "file:///android_asset/index.html";
    private static ChatJS chatJS;
    private static Account myAccount;
    @Override
    public void onCreate() {
        super.onCreate();
        //需要在 Application 的 onCreate() 中调用一次 DaemonEnv.initialize()
        DaemonEnv.initialize(this, Push.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        Push.sShouldStopService = false;
        DaemonEnv.startServiceMayBind(Push.class);

        chatJS = new ChatJS(this);
        chatJS.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        chatJS.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }
        });
        WebSettings webSetting = chatJS.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();

        chatJS.getSettings().setAllowFileAccessFromFileURLs(true);
        chatJS.loadUrl(mHomeUrl);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

    }

    public static boolean setMyAccount(Account account) {
        String s = "myecdh.set_private_key(\"" + account.encryptPrivateKey + "\")\n";
        s = s + "codeAccount = \"" + account.contractAccount + "\";\n";
        s = s + "myAccount = \"" + account.accountName + "\";\n";
        s = s + "rpc = new eosjs2_jsonrpc.JsonRpc('" + account.nodeAddress + "');\n";
        s = s + "signatureProvider = new eosjs2_jssig.default(['" + account.privateKey + "']);\n";
        s = s + "api = new eosjs2.Api({ rpc, signatureProvider });\n";
        s = s + "myecdh.get_public_key();\n";

        chatJS.evaluateJavascript(s,new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.i("chatJS.evaluateJavascript return",value);
            }
        });
        myAccount = account;
        return true;
    }
    public static Account getMyAccount() {
        return myAccount;
    }

    public static class Account
    {
        public String accountName;
        public String privateKey;
        public String nodeAddress;
        public String contractAccount;
        public String encryptPrivateKey;
        public String password;
    }

    public static boolean isDebug(Context context){
        boolean isDebug = context.getApplicationInfo()!=null&&
                (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        return isDebug;
    }

}
