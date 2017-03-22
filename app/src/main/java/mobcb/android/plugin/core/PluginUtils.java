package mobcb.android.plugin.core;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginUtils {

    public static final PackageInfo getPackageArchiveInfo(Context context, String apkPath) {
        return context.getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
    }

    public static final DexClassLoader createDexClassLoader(Context context, String apkPath, String optimizedDirectory, String librarySearchPath) {
        return new DexClassLoader(apkPath, optimizedDirectory, librarySearchPath, context.getClassLoader());
    }

    public static final AssetManager createAssetManager(String apkPath) {

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            return assetManager;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static final Resources createResources(Context context, AssetManager assetManager) {
        Resources hostRes = context.getResources();
        Resources resources = new Resources(assetManager, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
        return resources;
    }

    public static final Class<?> loadClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return Class.forName(className, true, classLoader);
    }

    public static final Object instantiationClass(ClassLoader classLoader, String className) throws Exception {

        Class<?> clazz = classLoader.loadClass(className);
        Constructor<?> constructor = clazz.getConstructor(new Class[]{});
        return constructor.newInstance(new Object[]{});

    }
}
