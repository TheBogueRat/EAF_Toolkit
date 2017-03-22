package com.bogueratcreations.eaftoolkit;

import android.app.Application;
import android.util.Log;

import com.bogueratcreations.eaftoolkit.DCP.PrimaryKeyFactory;
import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by jodyroth on 10/24/16.
 * Created to have an application instance of the Realm ORM
 */

public class EAFToolkitApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("EAFToolkit", "Preparing to initialize Realm...");
        Realm.init(this);
        Log.d("EAFToolkit", "Realm initialized, initializing configuration...");
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("EAFToolkit.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .initialData(new seedRealmDb())
                .build();

        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Log.d("EAFToolkit", "settingDefaultConfiguration...");
        Realm.setDefaultConfiguration(realmConfig);

        // Get the max id numbers for each model.class
        Log.d("EAFToolkit", "Getting defaultInstance for Realm...");
        Realm realm = Realm.getDefaultInstance();
        // Get location of Realm Db
        Log.d("", "path: " + realm.getPath());
        Log.d("EAFToolkit_Debug", "Just got realm default instance and now trying to initialize PrimaryKeyFactory");
        PrimaryKeyFactory.getInstance().initialize(realm);
        realm.close();
    }

// NOTES: To pull the Db from simulator, use Terminal in Android Studio with the following commands..
// Start this process in the target directory on the local drive.
// $ adb shell
// $ su
// $ cp data/data/com.bogueratcreations.eafToolkit/files/EAFToolkit.realm /sdcard/EAFToolkit.realm
// $ exit
// $ exit
// $ adb pull /sdcard/EAFToolkit.realm

    // OR THIS SCRIPT

    // pullRealm.sh located in the user root.

    // To view the DB on an actual device, try Stetho-Realm - not working with 2.4...
}

class seedRealmDb implements Realm.Transaction {

    @Override
    public void execute(Realm realm) {
        Log.d("EAFToolkit", "Beginning seedRealmDb transaction...");
        // For one-to-many relationship, need to assign objects to each other in both models!!!
        final Project realmProject = new Project();
        final Point realmPoint1 = new Point();
        final Point realmPoint2 = new Point();
        final Point realmPoint3 = new Point();
        final Reading realmReading1 = new Reading();
        final Reading realmReading2 = new Reading();
        final Reading realmReading3 = new Reading();
        final Reading realmReading4 = new Reading();
        final Reading realmReading5 = new Reading();
        final Reading realmReading6 = new Reading();

        realmProject.setId(0);
        realmProject.setProjName("Test Project 1" + (int)(Math.random()*50+1));
        realmProject.setProjLoc("Test data for testing purposes.");
        realmProject.setProjInfo("Test data for EAF Toolkit.");
        realmProject.setDateCreated(new Date());
        realmReading1.setId(0);
        realmReading1.setBlows(99);
        realmReading1.setReadingNum(0);
        realmReading1.setHammer(1);
        realmReading1.setDepth(25);
        realmReading1.setSoilType(2);
        realmReading1.setCbr(2.1384);
        realmReading1.setPoint(realmPoint1);

        realmReading2.setId(1);
        realmReading2.setBlows(99);
        realmReading2.setReadingNum(1);
        realmReading2.setHammer(1);
        realmReading2.setDepth(25);
        realmReading2.setSoilType(2);
        realmReading2.setCbr(5.834);
        realmReading2.setPoint(realmPoint1);

        RealmList<Reading> readings1 = new RealmList<>(realmReading1,realmReading2);

        realmReading3.setId(2);
        realmReading3.setBlows(15);
        realmReading3.setReadingNum(0);
        realmReading3.setHammer(1);
        realmReading3.setDepth(25);
        realmReading3.setSoilType(2);
        realmReading3.setCbr(28.874);
        realmReading3.setPoint(realmPoint2);

        realmReading4.setId(3);
        realmReading4.setBlows(15);
        realmReading4.setReadingNum(1);
        realmReading4.setHammer(1);
        realmReading4.setDepth(25);
        realmReading4.setSoilType(2);
        realmReading4.setCbr(39.23);
        realmReading4.setPoint(realmPoint2);

        RealmList<Reading> readings2 = new RealmList<>(realmReading3,realmReading4);

        realmReading5.setId(4);
        realmReading5.setBlows(5);
        realmReading5.setReadingNum(0);
        realmReading5.setHammer(1);
        realmReading5.setDepth(25);
        realmReading5.setSoilType(2);
        realmReading5.setCbr(18.343);
        realmReading5.setPoint(realmPoint3);

        realmReading6.setId(5);
        realmReading6.setBlows(5);
        realmReading6.setReadingNum(1);
        realmReading6.setHammer(1);
        realmReading6.setDepth(25);
        realmReading6.setSoilType(2);
        realmReading6.setCbr(15.233);
        realmReading6.setPoint(realmPoint3);

        RealmList<Reading> readings3 = new RealmList<>(realmReading5,realmReading6);

        realmPoint1.setId(0);
        realmPoint1.setPointNum("Test Back Leg 1");
        realmPoint1.setSoilType(2);
        realmPoint1.setCbr(2.1384);
        realmPoint1.setProject(realmProject);
        realmPoint1.setReadings(readings1);
        realmPoint1.setDate(new Date());
        realmPoint2.setId(1);
        realmPoint2.setPointNum("Test Back Leg 2");
        realmPoint2.setSoilType(2);
        realmPoint2.setDate(new Date());
        realmPoint2.setCbr(28.874);
        realmPoint2.setProject(realmProject);
        realmPoint2.setReadings(readings2);
        realmPoint3.setId(2);
        realmPoint3.setPointNum("Test Front Leg 3");
        realmPoint3.setSoilType(2);
        realmPoint3.setDate(new Date());
        realmPoint3.setCbr(15.233);
        realmPoint3.setProject(realmProject);
        realmPoint3.setReadings(readings3);

        RealmList<Point> points = new RealmList<>(realmPoint1,realmPoint2,realmPoint3);
        realmProject.setPoints(points);

        realm.copyToRealmOrUpdate(realmProject);
        realm.copyToRealmOrUpdate(realmPoint1);
        realm.copyToRealmOrUpdate(realmPoint2);
        realm.copyToRealmOrUpdate(realmPoint3);
        realm.copyToRealmOrUpdate(realmReading1);
        realm.copyToRealmOrUpdate(realmReading2);
        realm.copyToRealmOrUpdate(realmReading3);
        realm.copyToRealmOrUpdate(realmReading4);
        realm.copyToRealmOrUpdate(realmReading5);
        realm.copyToRealmOrUpdate(realmReading6);
        Log.d("EAFToolkit", "Completed seedRealmDb transaction.");
    }

    @Override
    public int hashCode() {
        return seedRealmDb.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof seedRealmDb;
    }
}