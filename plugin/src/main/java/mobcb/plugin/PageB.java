package mobcb.plugin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mobcb.android.plugin.app.BasePluginActivity;
import mobcb.android.plugin.core.PluginIntent;

public class PageB extends BasePluginActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_b);

        Button nextBtn= (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginIntent i=new PluginIntent(getPackageName(),PageC.class.getName());
                startActivity(i);
            }
        });

    }
}
