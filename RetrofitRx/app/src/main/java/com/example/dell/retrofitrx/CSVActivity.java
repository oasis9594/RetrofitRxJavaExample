package com.example.dell.retrofitrx;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CSVActivity extends AppCompatActivity {

    Button csvbutton;
    String filename=null;
    String path_to_csv=null;
    RecyclerView csvlist;
    CSVAdapter csvAdapter;
    CoordinatorLayout coordinatorLayout;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csv_list);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        csvbutton=(Button)findViewById(R.id.convertToCSV);
        csvlist=(RecyclerView)findViewById(R.id.csvlist);
        requestPermission();
    }
    public void doTask()
    {
        getContact();
        csvbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContactObservable().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Contact>() {
                            @Override
                            public void onError(Throwable e) {
                                Log.e("rxjava","error");
                            }

                            @Override
                            public void onComplete() {
                                try {
                                    zip(filename+".csv", filename+".zip");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "File stored at: "+path_to_csv, Snackbar.LENGTH_LONG);

                                // Changing message text color
                                //snackbar.setActionTextColor(Color.RED);
                                snackbar.show();
                            }

                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(Contact contact) {

                                File myFile;
                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                                String TimeStampDB = sdf.format(cal.getTime());
                                path_to_csv=getExternalFilesDir(null).getAbsolutePath();
                                Log.e("path",""+path_to_csv);
                                filename=getExternalFilesDir(null).getAbsolutePath()+"/Export_"+TimeStampDB+ SystemClock.currentThreadTimeMillis();
                                myFile = new File(filename+".csv");
                                FileOutputStream fOut = null;
                                try {
                                    fOut = new FileOutputStream(myFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                assert fOut != null;
                                OutputStreamWriter myOutWriter= new OutputStreamWriter(fOut);

                                try {
                                    myFile.createNewFile();
                                    myOutWriter.append("Name;Number");
                                    myOutWriter.append("\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                for(int l=0;l<contact.getItems().size();l++){

                                    try {
                                        item p=contact.getItems().get(l);
                                        myOutWriter.append(p.name).append(";").append(p.number);
                                        myOutWriter.append("\n");
                                    }catch (IOException e){}

                                }
                            }
                        });
            }
        });

    }
    public static void zip(String file, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[100000];

            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, 100000);
            try {
                ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, 100000)) != -1) {
                    out.write(data, 0, count);
                }
            } finally {
                origin.close();
            }
        } finally {
            out.close();
        }
    }
    public void requestPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else {
            doTask();
        }
    }
    private Contact getContact(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        //String[] adobe_products =new String[phones.getCount()];
        Contact contacts=new Contact();
        int i=0;
        ArrayList<item> items=new ArrayList<>();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            item person=new item();
            person.setName(name);
            person.setNumber(phoneNumber);
            items.add(person);
            //adobe_products[i++]=name+"----"+phoneNumber;
            Log.e("phone",""+name+phones.getCount());
        }
        phones.close();
        contacts.setItems(items);
        /*ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_act,adobe_products);
        ListView listView = (ListView)findViewById(R.id.list_con);
        listView.setAdapter(adapter);*/
        csvlist.setLayoutManager(new LinearLayoutManager(this));
        csvAdapter = new CSVAdapter(items);
        csvlist.setAdapter(csvAdapter);
        return contacts;
    }
    public Observable<Contact> getContactObservable(){
        return Observable.just(getContact());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doTask();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to access conntacts", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

