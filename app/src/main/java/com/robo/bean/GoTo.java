package com.robo.bean;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class GoTo {
    public String target;

    public GoTo() {

    }
    public GoTo(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("target", target);

        return result;
    }
}
