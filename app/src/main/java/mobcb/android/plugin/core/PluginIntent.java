
package mobcb.android.plugin.core;

import android.content.Intent;
import android.os.Parcelable;
import java.io.Serializable;

public class PluginIntent extends Intent {

    private String packageName;
    private String className;

    public PluginIntent(String packageName, String className) {
        super();
        this.packageName = packageName;
        this.className = className;
    }

    public String getPluginPackage() {
        return packageName;
    }

    public String getPluginClass() {
        return className;
    }


    @Override
    public Intent putExtra(String name, Parcelable value) {
        setupExtraClassLoader(value);
        return super.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, Serializable value) {
        setupExtraClassLoader(value);
        return super.putExtra(name, value);
    }

    private void setupExtraClassLoader(Object value) {
        ClassLoader pluginLoader = value.getClass().getClassLoader();
        setExtrasClassLoader(pluginLoader);
    }

}
