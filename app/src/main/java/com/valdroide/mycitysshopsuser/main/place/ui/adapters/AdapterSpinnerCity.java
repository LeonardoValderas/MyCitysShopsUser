package com.valdroide.mycitysshopsuser.main.place.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.place.City;

import java.util.List;

public class AdapterSpinnerCity extends ArrayAdapter<City> {
    private Activity context;
    private List<City> cities = null;
    private int resource;

    public AdapterSpinnerCity(Activity context, int resource, List<City> cities) {
        super(context, resource, cities);
        this.context = context;
        this.resource = resource;
        this.cities = cities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        City city = cities.get(position);

        if (city != null) {
            TextView textViewCountry = (TextView) row.findViewById(R.id.textViewGeneral);
            if (textViewCountry != null)
                textViewCountry.setText(city.getCITY());
        }
        return row;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }
}

