package com.example.android.androidcourse2;

import android.location.Location;
import android.net.Uri;
import java.util.Date;
import android.location.Location;
import android.net.Uri;

import com.orm.SugarRecord;

import java.util.Date;

public class Paket extends SugarRecord {
    /*Paket(){

    }*/
    static enum Status {PickedUp, InTransport, Delivered, Distributed, Destroyed, Lost , Stolen }
    static enum Type {Food, Water, Medical, Tools, Shelter, Clothing, Baby,FirstAid }
    transient int ID; //unique id
    long senderId;
    long receiverId;
    long transpoterId;
    String Photo;
    double pickupLon;
    double pickupLat;
    Date pickupDate;
    Date deliveryDate;
    String Destination;

    float WeightKG;
    boolean Perishable;
    boolean Fragile;
    boolean Heavy;
    boolean Liquid;
    boolean Hazardous;
    boolean AirTransport;
    String HandlingInstructions;
    String Comments;

    int packetTypeId;
    int packetStatusId;
}
