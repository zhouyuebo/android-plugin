package mobcb.android.plugin.core;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import mobcb.android.plugin.app.BasePluginActivity;
import mobcb.android.plugin.app.BasePluginFragmentActivity;
import mobcb.android.plugin.proxy.ProxyActivity;
import mobcb.android.plugin.proxy.ProxyFragmentActivity;

public class PluginLauncher {
    public static final String KEY_PLUGIN_PACKAGE = "plugin_package";
    public static final String KEY_PLUGIN_CLASS = "plugin_class";


    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_NOT_INSTALLED = 2;
    public static final int RESULT_NO_CLASS = 3;
    public static final int RESULT_UNKNOWN_PLUGIN_TYPE = 4;


    public static void launcherPluginActivity(final Context context, String apkPath, String packageName, String activityClass) {

        final PluginIntent intent = new PluginIntent(packageName, activityClass);
        PluginLoader loader = PluginLoader.getInstance(context);

        if (loader.hasPlugin(packageName)) {
            startActivity(context, intent);
            return;
        }

        loader.loadPluginAsync(apkPath, new Handler(), new PluginLoader.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(Plugin plugin) {
                startActivity(context, intent);
            }
        });

    }

    public static int startActivity(Context context, PluginIntent intent) {
        return startActivityForResult(context, intent, -1);
    }

    public static int startActivityForResult(Context context, PluginIntent intent, int requestCode) {

        String packageName = intent.getPluginPackage();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("Plugin packageName is null.");
        }

        Plugin plugin = PluginLoader.getInstance(context).findPlugin(packageName);
        if (plugin == null) {
            return RESULT_NOT_INSTALLED;
        }

        final String className = getPluginActivityFullName(intent, plugin);

        Class<?> clazz = null;
        try {
            clazz = PluginUtils.loadClass(plugin.classLoader, className);
        } catch (ClassNotFoundException e) {
            return RESULT_NO_CLASS;
        }

        Class<? extends Activity> activityClass = getProxyActivityClass(clazz);
        if (activityClass == null) {
            return RESULT_UNKNOWN_PLUGIN_TYPE;
        }

        intent.putExtra(KEY_PLUGIN_CLASS, className);
        intent.putExtra(KEY_PLUGIN_PACKAGE, packageName);
        intent.setClass(context, activityClass);

        performStartActivityForResult(context, intent, requestCode);

        return RESULT_SUCCESS;
    }

    private static Class<? extends Activity> getProxyActivityClass(Class<?> clazz) {
        Class<? extends Activity> activityClass = null;
        if (BasePluginActivity.class.isAssignableFrom(clazz)) {
            activityClass = ProxyActivity.class;
        } else if (BasePluginFragmentActivity.class.isAssignableFrom(clazz)) {
            activityClass = ProxyFragmentActivity.class;
        }

        return activityClass;
    }

    private static String getPluginActivityFullName(PluginIntent intent, Plugin plugin) {
        String className = intent.getPluginClass();
        className = (className == null ? plugin.defaultActivity : className);
        if (className.startsWith(".")) {
            className = intent.getPluginPackage() + className;
        }
        return className;
    }


    private static void performStartActivityForResult(Context context, PluginIntent intent, int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
