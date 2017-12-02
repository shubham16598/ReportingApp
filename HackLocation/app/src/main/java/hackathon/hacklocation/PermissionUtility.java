package hackathon.hacklocation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtility {

    public static boolean checkLocationPermission(Context context) {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }
}
