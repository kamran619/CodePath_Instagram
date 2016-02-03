package com.codepath.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchAndDisplayInstagramPosts();
    }

    private void fetchAndDisplayInstagramPosts() {
        showLoadingSpinner();
        InstagramPostProvider provider = new InstagramPostProvider();
        provider.fetchPosts(new InstagramPostProvider.IOnPhotoFetched() {
            @Override
            public void onFetchedSuccess(ArrayList<InstagramPost> posts) {
                hideLoadingSpinner();
            }

            @Override
            public void onFetchedFailed(String error) {
                hideLoadingSpinner();
            }
        });
    }

    private void showLoadingSpinner() {

    }

    private void hideLoadingSpinner() {

    }
}
