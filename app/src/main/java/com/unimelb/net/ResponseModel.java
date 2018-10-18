package com.unimelb.net;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parse json string to object, the json string is from the server response
 */
public class ResponseModel {

    /** Server responses code */
    private int code;

    /** Server responses message info*/
    private String msg;

    /** Server responses data */
    private JSONObject data;

    /**
     * Constructor function
     * @param json
     */
    public ResponseModel(String json){
        // Parse the json
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(json);
            data = (JSONObject) obj.get("data");
            code = Integer.parseInt(obj.get("code").toString());
            msg = (String) obj.get("msg");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /********** Getter & Setter  *******/

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
