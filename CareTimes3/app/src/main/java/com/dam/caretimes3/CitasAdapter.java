package com.dam.caretimes3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CitasAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> citas;

    public CitasAdapter(Context context, List<String> citas) {
        super(context, 0, citas);
        this.context = context;
        this.citas = citas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cita, parent, false);
        }

        TextView citaTextView = convertView.findViewById(R.id.citaTextView);
        citaTextView.setText(citas.get(position));

        return convertView;
    }
}
