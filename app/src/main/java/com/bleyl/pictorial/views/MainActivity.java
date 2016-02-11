package com.bleyl.pictorial.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainService.class);
        intent.putExtra("URL", getIntent().getDataString());
        startService(intent);
        finish();
    }
}