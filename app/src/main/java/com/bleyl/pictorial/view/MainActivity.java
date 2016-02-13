package com.bleyl.pictorial.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ViewerService.class);
        intent.putExtra("URL", getIntent().getDataString());
        startService(intent);
        finish();
    }
}