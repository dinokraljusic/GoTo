package com.example.android.androidcourse2;

import android.location.Location;
import android.net.Uri;
import java.util.Date;
import android.location.Location;
import android.net.Uri;

import com.orm.SugarRecord;

import java.util.Date;

public class Paket extends SugarRecord {
    Paket(){

    }
    int ID; //unique id
    int SenderID;
    Uri Photo;
    Location PickupLocation; //longitude latitude
    Location DeliveryLocation;
    Date pickupDate;
    Date deliveryDate;
    int ReceiverID;
    String Destination;
    //int Size;

    float WeightKG;
    //Type
    boolean Perishable;
    boolean Fragile;
    boolean Heavy;
    boolean Liquid;
    boolean Hazardous;
    boolean AirTransport;
    String HandlingInstructions;
    String Comments;
    enum Status {PickedUp, InTransport, Delivered, Distributed, Destroyed, Lost , Stolen }
    enum Type {Food, Water, Medical, Tools, Shelter, Clothing, Baby,FirstAid }
    Type type;
    Status status;
}
