package mobcb.plugin;

import android.os.Bundle;

import mobcb.android.plug.component.BasePluginActivity;

public class MainActivity extends BasePluginActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
