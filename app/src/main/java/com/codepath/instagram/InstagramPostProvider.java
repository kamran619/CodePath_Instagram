package com.codepath.instagram;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kpirwani on 2/2/16.
 */
public class InstagramPostProvider {
    final private String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    final private String BASE_URL = "https://api.instagram.com/v1/media/popular?client_id=";
    private String URL;

    private OkHttpClient httpClient;

    public interface IOnPhotoFetched {
        void onFetchedSuccess(ArrayList<InstagramPost> posts);
        void onFetchedFailed(String error);

    }
    public InstagramPostProvider() {
        URL = BASE_URL + CLIENT_ID;
        httpClient = new OkHttpClient();
    }

    public void fetchPosts(final IOnPhotoFetched callback) {
        Request request = new Request.Builder()
                .url(URL)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFetchedFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseString = response.body().string();
                    ArrayList<InstagramPost> posts = InstagramPostResponseParser.parseResponse(responseString);
                    callback.onFetchedSuccess(posts);
                } catch (IOException e) {
                    callback.onFetchedFailed(e.getMessage());
                } catch (JSONException f) {
                    callback.onFetchedFailed(f.getMessage());
                }
            }
        });

    }
}
