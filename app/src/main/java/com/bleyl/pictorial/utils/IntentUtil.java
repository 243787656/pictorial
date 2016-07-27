package com.bleyl.pictorial.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class IntentUtil {

    public static Intent getDefaultBrowser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent queryIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"));
        ActivityInfo activityInfo = queryIntent.resolveActivityInfo(packageManager, 0);
        ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setComponent(name);
        return intent;
    }
}
