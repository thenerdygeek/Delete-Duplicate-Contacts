package com.example.android.deleteduplicatecontacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.name;
import static android.R.attr.phoneNumber;
import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }

                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        } finally {
            cur.close();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContact();
            }
        });
    }

    void getContact() {

        ArrayList<User> list = new ArrayList<>();
        ArrayList<User> duplicateList = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor people = getContentResolver().query(uri, null, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String name = new String(), number = new String();
        if (people.moveToFirst()) {
            do {
                name = people.getString(indexName);
                number = people.getString(indexNumber);
                number = number.replaceAll("\\D+", "");

                //Log.d("Main Activity", "Name =  " + name + "\nPhone Number: " + number);

                //Log.d("Main Activity", "XXXXXXXXXXX");// Do work...


                int flag = 0;
                if (list.size() == 0) {
                    list.add(new User(name, number));
                    continue;
                }
//                for (int i = 0; i < list.size(); i++) {
//                    Log.v("List Details", "Name: " + list.get(i).getName() + "\nnumber: " + list.get(i).getNumber());
//                }
                for (int i = 0; i < list.size(); i++) {


                    //Log.v("Somewhat", "name   :   " + name + "\nnum  :  " + number + "\nflag: " + flag + " \nis equal: " + list.get(i).getNumber().equals(number));
                    if (!list.get(i).getNumber().trim().equals(number)) {

                        flag = 1;

                    } else {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    list.add(new User(name, number));
                } else {
                    duplicateList.add(new User(name, number));
                }
            } while (people.moveToNext());
        }




/*
        String str = "";
        while (cursor.moveToNext()) {
            str += cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.)) + ", ";
        }*/
        people.close();
        //Log.d("MainActivity", duplicateList.toString());
        for (User user : duplicateList) {
            //Log.d("MainActivity", "name: " + user.getName() + "\nNumber: " + user.getNumber());
            deleteContact(this,user.getNumber(),user.getName());
        }
        Toast.makeText(MainActivity.this, duplicateList.size() + " Contact(s) Deleted" ,Toast.LENGTH_LONG).show();;

    }

}
