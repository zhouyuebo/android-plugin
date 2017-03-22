package mobcb.android.plug.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
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

    private static final String PLUGIN_LOCATION ="plugins";
    private static final String PLUGIN_LIB_LOCATION ="lib";

    private final Context context;
    private final HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();

    private PluginLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public void clearPlugins(){
        plugins.clear();
    }

    public Plugin findPlugin(String packageName){
        return plugins.get(packageName);
    }

    public boolean hasPlugin(String packageName){
        return plugins.containsKey(packageName);
    }

    public Plugin loadPlugin(String apkPath){
        return loadPlugin(apkPath,true);
    }

    public Plugin loadPlugin(String apkPath, boolean hasSoLib){

        PackageInfo packageInfo= PluginUtils.getPackageArchiveInfo(context,apkPath);
        if(packageInfo==null){
            return null;
        }

        Plugin plugin=plugins.get(packageInfo.packageName);
        if(plugin!=null){
            return plugin;
        }

        if(hasSoLib){
            SoLibCopyUtils.copyPluginSoLib(context,apkPath,getPluginLibDir(packageInfo));
        }

        DexClassLoader dexClassLoader = PluginUtils.createDexClassLoader(context,apkPath,getPluginRootDir(packageInfo),getPluginLibDir(packageInfo));
        AssetManager assetManager = PluginUtils.createAssetManager(apkPath);
        Resources resources = PluginUtils.createResources(context,assetManager);

        plugin=new Plugin(dexClassLoader, resources, packageInfo);
        plugins.put(packageInfo.packageName, plugin);

        return plugin;
    }

    private String getPluginRootDir(PackageInfo packageInfo){
        File fd=context.getFilesDir();

        File rootDir=new File(fd,PLUGIN_LOCATION+"/"+packageInfo.packageName);
        if(!rootDir.exists()){
            rootDir.mkdirs();
        }

        return rootDir.getAbsolutePath();
    }

    private String getPluginLibDir(PackageInfo packageInfo){
        File fd=context.getFilesDir();

        File libDir=new File(fd,PLUGIN_LOCATION+"/"+packageInfo.packageName+"/"+PLUGIN_LIB_LOCATION);
        if(!libDir.exists()){
            libDir.mkdirs();
        }

        return libDir.getAbsolutePath();
    }
}
