package com.bleyl.pictorial.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.bleyl.pictorial.R;
import com.bleyl.pictorial.utils.PermissionUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && !PermissionUtil.hasSystemAlertPermission(this)) {
            setTheme(R.style.AppTheme_Dialog);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(getString(R.string.missing_permission_title));
            alertDialog.setMessage(getString(R.string.missing_permission_message));
            alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.create().show();
        } else {
            Intent intent = new Intent(this, ViewerService.class);
            intent.putExtra("URL", getIntent().getDataString());
            startService(intent);
            finish();
        }
    }
}