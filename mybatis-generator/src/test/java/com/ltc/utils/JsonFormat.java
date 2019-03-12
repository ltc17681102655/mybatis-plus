package com.ltc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonFormat<T> {
    //编写方法
    public String object(String jsonString) {
        JSONObject object = JSONObject.parseObject(jsonString);
        jsonString = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return jsonString;
    }

//    public String array(String jsonString, Class<T> cla) {
//        List<T> ts = JSONObject.parseArray(jsonString, cla);
//        jsonString = JSON.toJSONString(ts, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
//        return jsonString;
//    }

    public String Array(List<T> list) {
        String json = list.stream().map(c -> {
            return this.object(JSONObject.toJSONString(c));
        }).collect(Collectors.joining("/r/n"));
        return json;
    }

}
