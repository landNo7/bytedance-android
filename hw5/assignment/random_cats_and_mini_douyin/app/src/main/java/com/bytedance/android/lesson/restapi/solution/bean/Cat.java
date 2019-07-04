package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.17 18:08
 */
public class Cat {

    // TODO-C1 (1) Implement your Cat Bean here according to the response json

    private List<Object> breeds;
    private String id;
    private String url;
    private int width;
    private int height;

    public String getUrl(){
        return this.url;
    }

    @Override
    public String toString() {
        return "id = " + this.id +
                "\nurl = " + this.url +
                "\nwidth = " + this.width +
                "\nheight = " + this.height;
    }
}
