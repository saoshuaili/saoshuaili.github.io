package com.coachhe.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/3/19
 * Time: 21:58
 * Description:
 */
public class JSONUtil {
    public static boolean isJsonValidate(String log) {
        try {
            JSONObject.parseObject(log);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
