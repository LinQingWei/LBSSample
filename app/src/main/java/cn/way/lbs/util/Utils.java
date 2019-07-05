package cn.way.lbs.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;
import com.tencent.map.lib.basemap.data.GeoPoint;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;

public final class Utils {
    private static Application sApplication;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application</p>
     *
     * @param context
     */
    public static void init(@NonNull final Context context) {
        sApplication = (Application) context.getApplicationContext();
    }

    /**
     * Return the context of Application object.
     *
     * @return
     */
    public static Application getApplication() {
        if (sApplication != null) {
            return sApplication;
        }

        throw new NullPointerException("u should init first...");
    }

    /**
     * 返回坐标系名称
     */
    public static String getCoordinateTypeString(int coordinateType) {
        if (coordinateType == TencentLocationManager.COORDINATE_TYPE_GCJ02) {
            return "国测局坐标(火星坐标)";
        } else if (coordinateType == TencentLocationManager.COORDINATE_TYPE_WGS84) {
            return "WGS84坐标(GPS坐标, 地球坐标)";
        } else {
            return "非法坐标";
        }
    }

    public static String getLocationString(TencentLocation location, int level) {
        StringBuilder sb = new StringBuilder();

        sb.append("经度=").append(location.getLongitude()).append(",")
                .append("纬度=").append(location.getLatitude()).append(",")
                .append("海拔=").append(location.getAltitude()).append(",")
                .append("精度=").append(location.getAccuracy()).append(",")
                .append("来源=").append(location.getProvider()).append(",");


        switch (level) {
            case TencentLocationRequest.REQUEST_LEVEL_GEO:
                break;
            case TencentLocationRequest.REQUEST_LEVEL_NAME:
                sb.append("名称=").append(location.getName()).append(",");
                sb.append("地址=").append(location.getAddress()).append(",");
                break;
            case TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA:
            case TencentLocationRequest.REQUEST_LEVEL_POI:
            case 7:
                sb.append("国家=").append(location.getNation()).append(",");
                sb.append("省=").append(location.getProvince()).append(",");
                sb.append("市=").append(location.getCity()).append(",");
                sb.append("区=").append(location.getDistrict()).append(",");
                sb.append("镇=").append(location.getTown()).append(",");
                sb.append("村=").append(location.getVillage()).append(",");
                sb.append("街道=").append(location.getStreet()).append(",");
                sb.append("门号=").append(location.getStreetNo()).append(",");

                if (level == TencentLocationRequest.REQUEST_LEVEL_POI) {
                    List<TencentPoi> poiList = location.getPoiList();
                    int size = poiList.size();
                    for (int i = 0, limit = 3; i < limit && i < size; i++) {
                        sb.append("\n");
                        sb.append("poi[" + i + "]=")
                                .append(getPoiString(poiList.get(i))).append(",");
                    }
                }
                break;
            default:
                break;
        }

        return sb.toString();
    }

    public static String getPoiString(TencentPoi poi) {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(poi.getName()).append(",");
        sb.append("address=").append(poi.getAddress()).append(",");
        sb.append("catalog=").append(poi.getCatalog()).append(",");
        sb.append("distance=").append(poi.getDistance()).append(",");
        sb.append("latitude=").append(poi.getLatitude()).append(",");
        sb.append("longitude=").append(poi.getLongitude()).append(",");
        return sb.toString();
    }

    public static GeoPoint of(TencentLocation location) {
        return new GeoPoint((int) (location.getLatitude() * 1E6),
                (int) (location.getLongitude() * 1E6));
    }

    /**
     * 返回 manifest 中的 key
     */
    public static String getKey(Context context) {
        String key = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (metaData != null) {
                key = metaData.getString("TencentMapSDK");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TencentLocation",
                    "Location Manager: no key found in manifest file");
            key = "";
        }
        return key;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
