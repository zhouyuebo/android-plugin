package mobcb.host;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import mobcb.android.plugin.core.PluginLauncher;

public class MainActivity extends AppCompatActivity {
    private Button startPluginBtn;

    private String pluginPackage="mobcb.plugin";
    private String pluginClass="mobcb.plugin.MainActivity";
    private String pluginPath="/sdcard/plugin.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startPluginBtn= (Button) findViewById(R.id.start_plugin_btn);
        startPluginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginLauncher.launcherPluginActivity(getApplicationContext(),pluginPath,pluginPackage,pluginClass);
            }
        });
    }
}
