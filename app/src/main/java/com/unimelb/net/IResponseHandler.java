package com.unimelb.net;

public interface IResponseHandler {
    void onFailure(int statusCode, String errJson);

    void onSuccess(String json);
}
