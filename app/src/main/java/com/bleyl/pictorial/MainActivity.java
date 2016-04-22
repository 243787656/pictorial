package com.bleyl.pictorial;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

public class MainActivity extends Activity implements DialogInterface.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && !Settings.canDrawOverlays(this)) {
            setTheme(R.style.AppTheme_Dialog);
            showPermissionDialog();
        } else {
            Intent intent = new Intent(this, ViewerService.class);
            intent.putExtra("URL", getIntent().getDataString());
            startService(intent);
            finish();
        }
    }

    public void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.missing_permission_title)
                .setMessage(R.string.missing_permission_message)
                .setNegativeButton(android.R.string.cancel, this)
                .setPositiveButton(android.R.string.ok, this)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                finish();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
                break;
        }
    }
}