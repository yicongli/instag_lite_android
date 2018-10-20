package com.unimelb.net;

/*
* Interface of I Response handler, can be failure or success
* */
public interface IResponseHandler {
    void onFailure(int statusCode, String errJson);

    void onSuccess(String json);
}
