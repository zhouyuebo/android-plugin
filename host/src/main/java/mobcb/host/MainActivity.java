package mobcb.host;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import mobcb.android.plugin.core.PluginIntent;
import mobcb.android.plugin.core.PluginLauncher;
import mobcb.android.plugin.core.PluginLoader;

public class MainActivity extends AppCompatActivity {
    private Button startPluginBtn;

    private String pluginPackage="mobcb.plugin";
    private String pluginClass="mobcb.plugin.MainActivity";
    private String pluginPath="/sdcard/plugin.apk";

    private PluginLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loader=PluginLoader.getInstance(getApplicationContext());
        loader.clearPlugins();

        setContentView(R.layout.activity_main);
        startPluginBtn= (Button) findViewById(R.id.start_plugin_btn);
        startPluginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loader.hasPlugin(pluginPackage)){
                    loader.loadPlugin(pluginPath);
                }
                PluginIntent intent=new PluginIntent(pluginPackage,pluginClass);
                int result=PluginLauncher.startActivity(MainActivity.this,intent);
                Log.i("TAG","result:"+result);
            }
        });
    }
}
