package mobcb.android.plugin.core;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;

public class Plugin {

    public String packageName;
    public String defaultActivity;
    public DexClassLoader classLoader;
    public AssetManager assetManager;
    public Resources resources;
    public PackageInfo packageInfo;

    public Plugin(DexClassLoader loader, Resources resources,
                  PackageInfo packageInfo) {

        this.packageName = packageInfo.packageName;
        this.classLoader = loader;
        this.resources = resources;
        this.assetManager = resources.getAssets();
        this.packageInfo = packageInfo;
        this.defaultActivity = getDefaultActivity(packageInfo);
    }

    private String getDefaultActivity(PackageInfo packageInfo) {

        if (packageInfo!=null&&packageInfo.activities != null && packageInfo.activities.length > 0) {
            return packageInfo.activities[0].name;
        }
        return null;
    }
}
