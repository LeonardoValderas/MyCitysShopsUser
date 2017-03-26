package com.valdroide.mycitysshopsuser.main.place.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.place.Country;

import java.util.List;

public class AdapterSpinnerCountry extends ArrayAdapter<Country> {
    private Activity context;
    private List<Country> countries = null;
    private int resource;

    public AdapterSpinnerCountry(Activity context, int resource, List<Country> countries) {
        super(context, resource, countries);
        this.context = context;
        this.resource = resource;
        this.countries = countries;
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
        Country country = countries.get(position);

        if (country != null) {
            TextView textViewCountry = (TextView) row.findViewById(R.id.textViewGeneral);
            if (textViewCountry != null)
                textViewCountry.setText(country.getCOUNTRY());
        }
        return row;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }
}

