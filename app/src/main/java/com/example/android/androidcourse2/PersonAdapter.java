package com.example.android.androidcourse2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ase on 22.03.2016..
 */
public class PersonAdapter extends BaseAdapter {

    List<Person> list = null;
    Context context = null;

    PersonAdapter(Context c, List<Person> l)

    {
        context = c;
        list = l;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.person, parent, false);
        }

        Person p = list.get(position);

        TextView id = (TextView) convertView.findViewById(R.id.personID);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView lastname= (TextView) convertView.findViewById(R.id.lastname);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView email = (TextView) convertView.findViewById(R.id.email);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        // id.setText(p.ID);
        name.setText(p.name);
        lastname.setText(p.lastname);
        //phone.setText(p.phone);
        email.setText(p.email);
        address.setText(p.address);
        return convertView;


    }
}
