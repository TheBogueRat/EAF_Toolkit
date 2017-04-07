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
        Project project = realm.createObject(Project.class,0);
        project.setProjName("EAF Museum Sample");
        project.setProjLoc("Pensacola, FL");
        project.setSoilType(2);
        project.setSoilInfo("Loose, sandy soil");
        project.setProjInfo("Randomly generated data for fictional project location.");
        project.setDateCreated(new Date());

        Point point1 = realm.createObject(Point.class,0);
        RealmList<Reading> readingList1 = new RealmList<>();
        for (int i = 0; i < 20; i++) {
            Reading reading = realm.createObject(Reading.class, i);
            reading.setBlows((int)(Math.random() * 10 + 1));
            reading.setReadingNum(i);
            reading.setHammer(1);
            reading.setDepth(50);
            reading.setSoilType(2);
            reading.calcCbr();
            reading.setTotalDepth((i+1) * 50);
            reading.setPoint(point1);
            readingList1.add(reading);
        }
//        point1.setId(0);  // Already set Id.
        point1.setPointNum("Back Wall");
        point1.setSoilType(2);
        point1.setCbr(12.84);
        point1.setProject(project);
        point1.setDate(new Date());
        point1.setReadings(readingList1);

        RealmList<Point> pointList = new RealmList<>();
        pointList.add(point1);

        Point point2 = realm.createObject(Point.class,1);
        RealmList<Reading> readingList2 = new RealmList<>();
        for (int i = 0; i < 20; i++) {
            Reading reading = realm.createObject(Reading.class, i+30);
            reading.setBlows((int)(Math.random() * 10 + 1));
            reading.setReadingNum(i);
            reading.setHammer(1);
            reading.setDepth(50);
            reading.setSoilType(2);
            reading.calcCbr();
            reading.setTotalDepth((i+1) * 50);
            reading.setPoint(point2);
            readingList2.add(reading);
        }
        point2.setPointNum("Center of site");
        point2.setSoilType(2);
        point2.setCbr(17.82);
        point2.setProject(project);
        point2.setDate(new Date());
        point2.setReadings(readingList2);
        pointList.add(point2);

        Point point3 = realm.createObject(Point.class,2);
        RealmList<Reading> readingList3 = new RealmList<>();
        for (int i = 0; i < 20; i++) {
            Reading reading = realm.createObject(Reading.class, i+60);
            reading.setBlows((int)(Math.random() * 10 + 1));
            reading.setReadingNum(i);
            reading.setHammer(1);
            reading.setDepth(50);
            reading.setSoilType(2);
            reading.calcCbr();
            reading.setTotalDepth((i+1) * 50);
            reading.setPoint(point3);
            readingList3.add(reading);
        }
        point3.setPointNum("Front Left");
        point3.setSoilType(2);
        point3.setCbr(32.12);
        point3.setProject(project);
        point3.setDate(new Date());
        point3.setReadings(readingList3);
        pointList.add(point3);

        project.setPoints(pointList);
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