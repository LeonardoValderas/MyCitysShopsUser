package com.valdroide.mycitysshopsuser.main.place.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.place.State;

import java.util.List;

public class AdapterSpinnerState extends ArrayAdapter<State> {
    private Activity context;
    private List<State> states = null;
    private int resource;

    public AdapterSpinnerState(Activity context, int resource, List<State> states) {
        super(context, resource, states);
        this.context = context;
        this.resource = resource;
        this.states = states;
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
        State state = states.get(position);

        if (state != null) {
            TextView textViewCountry = (TextView) row.findViewById(R.id.textViewGeneral);
            if (textViewCountry != null)
                textViewCountry.setText(state.getSTATE());
        }
        return row;
    }

    public void setStates(List<State> states) {
        this.states = states;
        notifyDataSetChanged();
    }
}

