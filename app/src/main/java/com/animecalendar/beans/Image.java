package com.animecalendar.beans;

import org.json.JSONException;
import org.json.JSONObject;

public class Image {
    private String original;
    private String preview;
    private String x96;
    private String x48;

    public Image(String image) throws JSONException {
        JSONObject jsonObject = new JSONObject(image);
        this.original = jsonObject.getString("original");
        this.preview = jsonObject.getString("preview");
        this.x96 = jsonObject.getString("x96");
        this.x48 = jsonObject.getString("x48");
    }

    public String getOriginal() {
        return original;
    }

    public String getPreview() {
        return preview;
    }

    public String getX96() {
        return x96;
    }

    public String getX48() {
        return x48;
    }
}
