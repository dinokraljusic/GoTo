package com.example.android.androidcourse2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.List;

/**
 * Created by jackblack on 3/22/16.
 */
public class PaketAdapter extends BaseAdapter {

        List<Paket> list = null;
        Context context = null;

        PaketAdapter(Context c, List<Paket> l){
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
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.paket2, parent, false);
            }

            final Paket p = list.get(position);

            //TextView id = (TextView) convertView.findViewById(R.id.packetID);//
            TextView pickupDate = (TextView) convertView.findViewById(R.id.pickupDate);
            TextView destination = (TextView) convertView.findViewById(R.id.destination);
            ImageView Fragile = (ImageView) convertView.findViewById(R.id.fragile);
            ImageView Liquid = (ImageView) convertView.findViewById(R.id.liquid);
            ImageView Heavy = (ImageView) convertView.findViewById(R.id.heavy);
            ImageView Perishable = (ImageView) convertView.findViewById(R.id.perishable);


            if (p.Photo!=null && p.Photo!="") {
                ImageView image = (ImageView)convertView.findViewById(R.id.paketimage);
                image.setImageURI(Uri.parse(p.Photo+"thumb"));
                if(image.getImageAlpha() == 0)
                    image.setImageURI(Uri.parse(p.Photo));//EMULATOR
                //imageLoader.DisplayImage(p.Photo, image);
            }

            //id.setText(String.valueOf(p.getId()));
            pickupDate.setText(String.format("%tF", p.pickupDate));
            destination.setText(p.Destination);

            if (!p.Fragile) Fragile.setVisibility(View.GONE);
            if (!p.Liquid) Liquid.setVisibility(View.GONE);
            if (!p.Heavy) Heavy.setVisibility(View.GONE);
            if (!p.Perishable) Perishable.setVisibility(View.GONE);

            Fragile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Fragile           Broken", Toast.LENGTH_LONG);
                }
            });

            final double lon = p.pickupLon, lat = p.pickupLat;
            ImageView map = (ImageView) convertView.findViewById(R.id.mapicon);
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent i = new Intent(PaketAdapter.this, Map.class);
                    Intent i = new Intent(context, Map.class);
                    i.putExtra("lon", lon);
                    i.putExtra("lat", lat);
                    context.startActivity(i);
                }
            });

            final CheckBox check = (CheckBox)convertView.findViewById(R.id.checkbox);
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (check.isChecked()) {
                        ListPackages.checkedIndices.add(new Long(p.getId()));
                    } else {
                        ListPackages.checkedIndices.remove(p.getId());
                    }
                }
            });

            if (p.ReceiverID == 999)
            {
                convertView.setBackgroundColor(0x00FF00);
                convertView.setAlpha(0.5F);
                check.setVisibility(View.INVISIBLE);
            }
            if(p.pickupLat!=0){
                map.setVisibility(View.VISIBLE);
            }
            else
            {
                map.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }


}
