package com.bleyl.pictorial.api.gfycat.responses;

import com.bleyl.pictorial.models.GfyItem;
import com.google.gson.annotations.SerializedName;

public class MetadataResponse {

    @SerializedName("gfyItem")
    private GfyItem gfyItem;

    public GfyItem getGfyItem() {
        return gfyItem;
    }
}

