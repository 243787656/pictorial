package com.bleyl.pictorial.models.gfycat.responses;

import com.bleyl.pictorial.models.gfycat.GfyItem;
import com.google.gson.annotations.SerializedName;

public class MetadataResponse {

    @SerializedName("gfyItem")
    private GfyItem mGfyItem;

    public GfyItem getGfyItem() {
        return mGfyItem;
    }
}

