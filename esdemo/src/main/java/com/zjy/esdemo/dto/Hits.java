package com.zjy.esdemo.dto;

import com.alibaba.fastjson.JSONArray;

public class Hits {
    private JSONArray hits;

    public JSONArray getHits() {
        return hits;
    }

    public void setHits(JSONArray hits) {
        this.hits = hits;
    }
}
