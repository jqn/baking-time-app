package com.brunogtavares.bakingtime;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brunogtavares.bakingtime.model.Recipe;
import com.brunogtavares.bakingtime.webservice.BakingTimeAPIClient;
import com.brunogtavares.bakingtime.webservice.BakingTimeRepository;
import com.brunogtavares.bakingtime.webservice.BakingTimeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {



    private RecipeAdapter mRecipeAdapter;
    private MainViewModel mViewModel;

    @BindView(R.id.rv_recipe_list) RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // This will help to ache the viewholders and improve scrolling performance
        mRecyclerView.setItemViewCacheSize(8);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // Create a new adapter that takes an empty list of movies as input
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeAdapter.setContext(getApplicationContext());

        mRecyclerView.setAdapter(mRecipeAdapter);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        boolean isConnected = checkForNetworkStatus();
        if (isConnected) {
            initViewModel();
        }


    }

    @Override
    public void onClick(Recipe recipe) {
        // TODO impelment Intent to detail recipe
    }

    private void initViewModel() {

        mViewModel.getAllRecipes().observe(this, new android.arch.lifecycle.Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

                recipes.size();

                mRecipeAdapter.setRecipeList(recipes);

                // TODO display error message and or empty state
            }
        });

    }

    private boolean checkForNetworkStatus() {

        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
}
