package com.codepath.instagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotosActivity extends AppCompatActivity {

    private UserPostAdapter mAdapter;
    @Bind(R.id.lvPicture) ListView lvPosts;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRefreshLayout();
        fetchAndDisplayInstagramPosts(true);
    }

    private void setupRefreshLayout() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchAndDisplayInstagramPosts(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void fetchAndDisplayInstagramPosts(boolean showProgress) {
        if (showProgress) {
            showLoadingSpinner();
        }
        hideListView();
        InstagramPostProvider provider = new InstagramPostProvider();
        provider.fetchPosts(new InstagramPostProvider.IOnPhotoFetched() {
            @Override
            public void onFetchedSuccess(final ArrayList<InstagramPost> posts) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleFetch(posts, null);
                    }
                });
            }

            @Override
            public void onFetchedFailed(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleFetch(null, error);
                    }
                });
            }
        });
    }

    private void showLoadingSpinner() {
        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner() {
        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(View.GONE);
    }

    private void hideListView() {
        ListView lv = (ListView) findViewById(R.id.lvPicture);
        lv.setVisibility(View.GONE);
    }

    private void showListView() {
        ListView lv = (ListView) findViewById(R.id.lvPicture);
        lv.setVisibility(View.VISIBLE);
    }

    private void hideStatusTextView() {
        TextView tv = (TextView) findViewById(R.id.tvStatus);
        tv.setVisibility(View.GONE);
    }

    private void showStatusTextView() {
        TextView tv = (TextView) findViewById(R.id.tvStatus);
        tv.setVisibility(View.VISIBLE);
    }

    private void loadPosts(ArrayList<InstagramPost> posts) {
        hideLoadingSpinner();
        if (mAdapter == null) {
            mAdapter = new UserPostAdapter(this, posts);
            lvPosts.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(posts);
        }
    }

    private void handleFetch(ArrayList<InstagramPost> posts, String errorDescription) {
        if (posts.size() > 0 && errorDescription == null) {
            hideStatusTextView();
            showListView();
            loadPosts(posts);
        } else {
            hideLoadingSpinner();
            hideListView();
            showStatusTextView();
        }
        swipeContainer.setRefreshing(false);
    }
}
