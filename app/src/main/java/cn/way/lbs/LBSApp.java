package cn.way.lbs;

import android.app.Application;

import com.tencent.map.geolocation.TencentGeofence;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;

import java.util.ArrayList;

public class LBSApp extends Application {
    private static ArrayList<Marker> sFenceItems = new ArrayList<>();
    private static ArrayList<TencentGeofence> sFences = new ArrayList<>();
    private static ArrayList<String> sEvents = new ArrayList<>();

    public static ArrayList<Marker> getFenceItems() {
        return sFenceItems;
    }

    public static ArrayList<TencentGeofence> getFences() {
        return sFences;
    }

    public static TencentGeofence getLastFence() {
        if (sFences.isEmpty()) {
            return null;
        }

        return sFences.get(sFences.size() - 1);
    }

    public static ArrayList<String> getEvents() {
        return sEvents;
    }
}
