package com.example.android.androidcourse2;

import com.orm.SugarRecord;

/**
 * Created by CIKBIH on 22.03.2016..
 */
public class Person extends SugarRecord {
    int ID;
    String name;
    String lastName;
    String phone;
    String email;
    String address;
    String photoFileName;
    enum Type {Sender, Receiver, Transporter}
    Type type;
}


