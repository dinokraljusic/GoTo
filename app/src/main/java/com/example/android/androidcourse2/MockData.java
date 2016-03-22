package com.example.android.androidcourse2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jackblack on 3/22/16.
 */
public class MockData {

  public static  List<Paket> createListOfPackages(){
        //Mockup of Packages
        List<Paket> paketi = new ArrayList<>();
        Paket p1 = new Paket();
        p1.ID = (int) (Math.random() * 1000);
        p1.Fragile = false;
        p1.AirTransport = true;
        p1.pickupDate = new Date();
        p1.Destination = "Paris";

        Paket p2 = new Paket();
        p2.ID = (int) (Math.random() * 1000);
        p2.Fragile = true;
        p2.Hazardous = true;
        p2.pickupDate = new Date();
        p2.Destination = "London";

        Paket p3 = new Paket();
        p3.ID = (int) (Math.random() * 1000);
        p3.Fragile = true;
        p3.Heavy = true;
        p3.pickupDate = new Date();
        p3.Destination = "Sarajevo";
        paketi.add(p1);
        paketi.add(p2);
        paketi.add(p3);

        return paketi;

    }

}
