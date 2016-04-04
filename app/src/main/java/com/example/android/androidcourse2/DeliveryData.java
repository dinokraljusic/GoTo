package com.example.android.androidcourse2;

import android.location.Location;
import android.net.Uri;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by nermina on 03/25/16.
 */
public class DeliveryData extends SugarRecord{

    Uri Photo;
    String Comment;
    Date deliveryDate;
    Location deliveryLocation;
    int deliveryId;
}
