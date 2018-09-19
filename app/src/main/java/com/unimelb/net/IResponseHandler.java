package com.unimelb.net;

public interface IResponseHandler {
    void onFailure(int statusCode, String errMsg);

    void onSuccess(String json);
}
