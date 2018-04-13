package com.daruc.towerdefence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

public class InfinityModeMenuActivity extends Activity {
    public static final String MAP_RESOURCE_ID = "mapResourceId";
    private MapAdapter mapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_infinity_mode_menu);

        ListView listView = findViewById(R.id.mapList);
        mapAdapter = new MapAdapter(this);
        listView.setAdapter(mapAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(MAP_RESOURCE_ID, (Integer) mapAdapter.getItem(position));
                startActivity(intent);
                startActivity(intent);
            }
        });
    }
}
