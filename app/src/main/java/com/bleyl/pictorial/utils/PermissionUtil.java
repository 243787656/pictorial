package com.bleyl.pictorial.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Robert on 13.02.2016.
 */
public class PermissionUtil {
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasSystemAlertPermission(Context ctx) {
        return Settings.canDrawOverlays(ctx); //PackageManager.PERMISSION_GRANTED == ctx.checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW);
    }
}
