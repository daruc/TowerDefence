package com.daruc.towerdefence;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darek on 07.04.18.
 */

public class MapAdapter extends BaseAdapter {

    private List<Map> maps;

    public MapAdapter(Context context) {
        maps = new ArrayList<>(2);
        maps.add(new Map("Map 1", R.raw.map_1));
        maps.add(new Map("Map 2", R.raw.map_2));
    }

    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int position) {
        return maps.get(position).resourceId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text = new TextView(parent.getContext());
        text.setText(maps.get(position).name);
        text.setHeight(200);
        return text;
    }

    public static class Map {
        public Map(String name, int resourceId) {
            this.name = name;
            this.resourceId = resourceId;
        }

        public String name;
        public int resourceId;
    }
}
