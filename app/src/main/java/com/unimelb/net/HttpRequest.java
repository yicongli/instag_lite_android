package com.unimelb.net;

import com.unimelb.constants.CommonConstants;
import com.unimelb.utils.TokenHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Encapsulate the OkHttp library
 * <p>
 * usage example:
 * HttpRequest.getInstance().doGetRequestAsync("http://192.168.1.12:8080/api/v1/test", null, new IResponseHandler(){
 *
 * @Override public void onFailure(int statusCode, String errMsg) {
 * Log.d("TAG", statusCode + "");
 * Log.d("TAG", errMsg);
 * }
 * @Override public void onSuccess(String json) {
 * Log.d("TAG", json);
 * }
 * });
 */
public class HttpRequest {

    /**
     * JSON media type
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Create an instance of http client
     */
    private static OkHttpClient client;

    /**
     * Http request instance
     */
    private static HttpRequest instance;

    /**
     * Constructor method
     */
    public HttpRequest() {
        client = new OkHttpClient();
    }

    /**
     * Instance
     *
     * @return
     */
    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }

        return instance;
    }

    /**
     * Asynchronous get request
     *
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
     * Asynchronous json post request
     *
     * @param url
     * @param jsonBody
     * @param responseHandler
     */
    public void doPostRequestAsync(String url, String jsonBody, final IResponseHandler responseHandler) {
        RequestBody body = RequestBody.create(JSON, jsonBody == null ? "" : jsonBody);
        Request request = new Request.Builder().url(url + "?access_token=" + CommonConstants.token).post(body).build();
        newCall(request, responseHandler);
    }


    /**
     * Asynchronous form post request
     *
     * @param url
     * @param paramsMap
     * @param responseHandler
     */
    public void doFilePostRequestAsync(String url, Map<String, Object> paramsMap, final IResponseHandler responseHandler) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, object.toString());
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url + "?access_token=" + CommonConstants.token).post(body).build();
        newCall(request, responseHandler);
    }

    /**
     * Asynchronous json put request
     *
     * @param url
     * @param jsonBody
     * @param responseHandler
     */
    public void doPutRequestAsync(String url, String jsonBody, final IResponseHandler responseHandler) {
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder().url(url + "?access_token=" + CommonConstants.token).put(body).build();
        newCall(request, responseHandler);
    }

    /**
     * Asynchronous delete request
     *
     * @param url
     * @param paramsMap
     * @param responseHandler
     */
    public void doDeleteRequestAsync(String url, Map<String, String> paramsMap, final IResponseHandler responseHandler) {
        String requestUrl = handleGetParams(url, paramsMap);
        Request request = new Request.Builder().url(requestUrl).delete().build();

        newCall(request, responseHandler);
    }


    /**
     * universal client call
     *
     * @param request
     * @param responseHandler
     */
    private void newCall(Request request, final IResponseHandler responseHandler) {
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
     *
     * @param url
     * @param paramsMap
     * @return
     */
    private String handleGetParams(String url, Map<String, String> paramsMap) {
        String requestUrl = url + "?access_token=" + CommonConstants.token;

        if (paramsMap != null && paramsMap.size() > 0) {
            StringBuilder tempParams = new StringBuilder();
            for (String key : paramsMap.keySet()) {
                tempParams.append("&");
                //handle URLEncoder for the params
                try {
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            //get fully request url
            requestUrl = String.format("%s%s", url, tempParams.toString());
            System.out.println(requestUrl);
        }
        return requestUrl;
    }
}
