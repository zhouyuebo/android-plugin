package mobcb.android.plugin.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class PluginLoader {

    private static PluginLoader sInstance;

    public static PluginLoader getInstance(Context context) {

        if (sInstance == null) {
            synchronized (PluginLoader.class) {
                if (sInstance == null) {
                    sInstance = new PluginLoader(context);
                }
            }
        }

        return sInstance;
    }

    private static final String PLUGIN_LOCATION = "plugins";
    private static final String PLUGIN_LIB_LOCATION = "lib";

    public static interface OnLoadCompleteListener {
        public void onLoadComplete(Plugin plugin);
    }

    private final Context context;
    private final HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();

    private PluginLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public synchronized void clearPlugins() {
        plugins.clear();
    }

    public Plugin findPlugin(String packageName) {
        return plugins.get(packageName);
    }

    public boolean hasPlugin(String packageName) {
        return plugins.containsKey(packageName);
    }

    public void loadPluginAsync(final String apkPath, final Handler handler,final OnLoadCompleteListener listener) {
        Thread loadThread=new Thread(new Runnable() {
            @Override
            public void run() {
                final Plugin plugin=loadPlugin(apkPath, true);
                if(handler!=null&&listener!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLoadComplete(plugin);
                        }
                    });
                }
            }
        });

        loadThread.setPriority(Thread.MIN_PRIORITY);
        loadThread.start();
    }

    public Plugin loadPlugin(String apkPath) {
        return loadPlugin(apkPath, true);
    }

    public synchronized  Plugin loadPlugin(String apkPath, boolean hasSoLib) {

        PackageInfo packageInfo = PluginUtils.getPackageArchiveInfo(context, apkPath);
        if (packageInfo == null) {
            return null;
        }

        Plugin plugin = plugins.get(packageInfo.packageName);
        if (plugin != null) {
            return plugin;
        }

        deletePluginDir(packageInfo);

        if (hasSoLib) {
            LibCopyUtils.copyPluginSoLib(context, apkPath, getPluginLibDir(packageInfo));
        }

        DexClassLoader dexClassLoader = PluginUtils.createDexClassLoader(context, apkPath, getPluginRootDir(packageInfo), getPluginLibDir(packageInfo));
        AssetManager assetManager = PluginUtils.createAssetManager(apkPath);
        Resources resources = PluginUtils.createResources(context, assetManager);

        plugin = new Plugin(dexClassLoader, resources, packageInfo);
        plugins.put(packageInfo.packageName, plugin);

        return plugin;
    }

    private String getPluginRootDir(PackageInfo packageInfo) {
        File pluginLocation = context.getDir(PLUGIN_LOCATION, Context.MODE_PRIVATE);

        File rootDir = new File(pluginLocation, packageInfo.packageName);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        return rootDir.getAbsolutePath();
    }

    private String getPluginLibDir(PackageInfo packageInfo) {
        File pluginLocation = context.getDir(PLUGIN_LOCATION, Context.MODE_PRIVATE);

        File libDir = new File(pluginLocation, packageInfo.packageName + "/" + PLUGIN_LIB_LOCATION);
        if (!libDir.exists()) {
            libDir.mkdirs();
        }

        return libDir.getAbsolutePath();
    }

    private void deletePluginDir(PackageInfo packageInfo) {
        String rootDir = getPluginRootDir(packageInfo);

        try {
            PluginUtils.deleteFile(new File(rootDir));
        } catch (IOException e) {
            e.printStackTrace();
            //this is not important.
        }

    }
}
