package com.amarispay.mobilemoneyservice.utilities;
import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OkHttpUtility {
    private static Response response;
    private static OkHttpClient client =
            new OkHttpClient();
    public static Response getCallRequest(String url, HashMap<String, String> parameters)
            throws IOException {
        HttpUrl.Builder urlBuilder =HttpUrl
                .parse(url)
                .newBuilder();
        Iterator it = parameters.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            urlBuilder.addQueryParameter(pair.getKey().toString(), pair.getValue().toString());
        }
        String builtUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(builtUrl)
                .build();
        return client.newCall(request).execute();
    }

    public static Response postCallRequest(String url, HashMap<String, String> headerParameters,
                                           RequestBody requestBody)
        throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        Iterator it = headerParameters.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            requestBuilder.addHeader(pair.getKey().toString(),pair.getValue().toString());
        }
        Request request = requestBuilder.url(url).post(requestBody).build();
        return client.newCall(request).execute();
    }
}
