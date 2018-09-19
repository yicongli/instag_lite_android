package com.unimelb.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Encapsulate the OkHttp library
 */
public class HttpRequest {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;
    private static HttpRequest instance;

    public HttpRequest() {
        client = new OkHttpClient();
    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }

        return instance;
    }

    /**
     * Asynchronous get request
     * @param url
     * @param paramsMap
     * @param responseHandler
     */
    public void doGetRequestAsync(String url, Map<String, String> paramsMap, final IResponseHandler responseHandler) {
        String requestUrl = handleGetParams(url, paramsMap);
        Request request = new Request.Builder().url(requestUrl).build();

        newCall(request, responseHandler);
    }

    /**
     * Asynchronous post request
     * @param url
     * @param paramsMap
     * @param responseHandler
     */
    public void doPostRequestAsync(String url, Map<String, String> paramsMap, final IResponseHandler responseHandler){
        FormBody.Builder builder = new FormBody.Builder();

        if(paramsMap != null && paramsMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder().url(url).post(builder.build()).build();
        newCall(request, responseHandler);
    }


    private void newCall(Request request, final IResponseHandler responseHandler){
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseHandler.onFailure(-1, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    responseHandler.onSuccess(response.body().string());
                } else {
                    responseHandler.onFailure(response.code(), response.body().string());
                }
            }
        });
    }


    /**
     * compose get request parameters
     * @param url
     * @param paramsMap
     * @return
     */
    private String handleGetParams(String url, Map<String, String> paramsMap){
        String requestUrl = url;

        if (paramsMap != null && paramsMap.size() > 0) {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //handle URLEncoder for the params
                try {
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pos++;
            }

            //get fully request url
            requestUrl = String.format("%s?%s", url, tempParams.toString());
        }
        return requestUrl;
    }
}
