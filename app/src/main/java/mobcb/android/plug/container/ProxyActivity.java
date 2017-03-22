
package mobcb.android.plug.container;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

import mobcb.android.plug.component.PluginActivity;
import mobcb.android.plug.core.Plugin;
import mobcb.android.plug.core.PluginLauncher;
import mobcb.android.plug.core.PluginLoader;
import mobcb.android.plug.core.PluginUtils;

public class ProxyActivity extends Activity {

    private String pluginPackage;
    private String pluginClass;
    private Plugin plugin;
    protected PluginActivity pluginActivity;
    private Theme pluginTheme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setPluginTheme();
        pluginActivity.onCreate(savedInstanceState);
    }

    private void initData() {
        Intent i = getIntent();
        pluginPackage = i.getStringExtra(PluginLauncher.KEY_PLUGIN_PACKAGE);
        pluginClass = i.getStringExtra(PluginLauncher.KEY_PLUGIN_CLASS);
        plugin = PluginLoader.getInstance(getApplicationContext()).findPlugin(pluginPackage);
        try {
            pluginActivity = (PluginActivity) PluginUtils.instantiationClass(plugin.classLoader, pluginClass);
            pluginActivity.bindProxy(plugin,this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPluginTheme() {
        int defaultTheme = plugin.packageInfo.applicationInfo.theme;

        ActivityInfo pluginActivityInfo=null;

        for (ActivityInfo info : plugin.packageInfo.activities) {
            if (info.name.equals(pluginClass)) {
                pluginActivityInfo=info;
                break;
            }
        }

        if(pluginActivityInfo==null){
            return;
        }

        if (pluginActivityInfo.theme == 0) {
            if (defaultTheme != 0) {
                pluginActivityInfo.theme = defaultTheme;
            } else {
                if (Build.VERSION.SDK_INT >= 14) {
                    pluginActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                } else {
                    pluginActivityInfo.theme = android.R.style.Theme;
                }
            }
        }

        if (pluginActivityInfo.theme > 0) {
            this.setTheme(pluginActivityInfo.theme);
        }

        pluginTheme = plugin.resources.newTheme();
        pluginTheme.setTo(this.getTheme());
        try {
            pluginTheme.applyStyle(pluginActivityInfo.theme, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public ClassLoader getClassLoader() {
        if(plugin==null){
            return super.getClassLoader();
        }
        return plugin.classLoader;
    }

    @Override
    public AssetManager getAssets() {
        if(plugin==null){
            return super.getAssets();
        }
        return plugin.assetManager;
    }

    @Override
    public Resources getResources() {
        if(plugin==null){
            return super.getResources();
        }
        return plugin.resources;
    }

    @Override
    public Theme getTheme() {
        if(plugin==null){
            return super.getTheme();
        }
        return pluginTheme;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pluginActivity.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        pluginActivity.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        pluginActivity.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        pluginActivity.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        pluginActivity.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        pluginActivity.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        pluginActivity.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        pluginActivity.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        pluginActivity.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        pluginActivity.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        pluginActivity.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return pluginActivity.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return pluginActivity.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        pluginActivity.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        pluginActivity.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        pluginActivity.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        pluginActivity.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}
