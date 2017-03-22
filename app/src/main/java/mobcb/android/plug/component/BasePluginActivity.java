

package mobcb.android.plug.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import mobcb.android.plug.core.Plugin;


public class BasePluginActivity extends Activity implements PluginActivity {


    protected Activity proxyActivity;
    protected Plugin plugin;

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void bindProxy(Plugin plugin,Activity proxyActivity) {
        this.plugin=plugin;
        this.proxyActivity=proxyActivity;
    }

    @Override
    public void setContentView(View view) {
        proxyActivity.setContentView(view);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        proxyActivity.setContentView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        proxyActivity.setContentView(layoutResID);
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        proxyActivity.addContentView(view, params);
    }

    @Override
    public View findViewById(int id) {
        return proxyActivity.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        return proxyActivity.getIntent();
    }

    @Override
    public ClassLoader getClassLoader() {
        return proxyActivity.getClassLoader();
    }

    @Override
    public Resources getResources() {
        return proxyActivity.getResources();
    }

    @Override
    public String getPackageName() {
        return plugin.packageName;
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return proxyActivity.getLayoutInflater();
    }

    @Override
    public MenuInflater getMenuInflater() {
        return proxyActivity.getMenuInflater();
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return proxyActivity.getSharedPreferences(name, mode);
    }

    @Override
    public Context getApplicationContext() {
        return proxyActivity.getApplicationContext();
    }

    @Override
    public WindowManager getWindowManager() {
        return proxyActivity.getWindowManager();
    }

    @Override
    public Window getWindow() {
        return proxyActivity.getWindow();
    }

    @Override
    public Object getSystemService(String name) {
        return proxyActivity.getSystemService(name);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }


    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {

    }

    public void onWindowFocusChanged(boolean hasFocus) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
