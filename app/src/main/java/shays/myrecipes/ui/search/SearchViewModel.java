package shays.myrecipes.ui.search;

import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.model.RecipeThumbnail;
import shays.myrecipes.model.RecipeThumbnailsResponse;
import shays.myrecipes.repository.Repository;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<RecipeThumbnail>> recipeThumbnailLiveData;
    private final MutableLiveData<OnlineRecipe> recipeLiveData;
    private final MutableLiveData<Throwable> exceptionLiveData;
    private final MutableLiveData<RecipeThumbnail> recipeClicked;

    public SearchViewModel() {
        recipeThumbnailLiveData = new MutableLiveData<>();
        exceptionLiveData = new MutableLiveData<>();
        recipeLiveData = new MutableLiveData<>();
        recipeClicked = new MutableLiveData<>();
    }

    public LiveData<List<RecipeThumbnail>> getRecipeThumbnailLiveData() {
        return recipeThumbnailLiveData;
    }

    public LiveData<Throwable> getExceptionLiveData() {
        return exceptionLiveData;
    }

    public MutableLiveData<RecipeThumbnail> getRecipeClicked() {
        return recipeClicked;
    }

    public MutableLiveData<OnlineRecipe> getRecipeLiveData() {
        return recipeLiveData;
    }

    public void searchRecipesOnline(String query) {
        Repository.getRepo().searchRecipesOnline(query).enqueue(new Callback<RecipeThumbnailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeThumbnailsResponse> call, @NonNull Response<RecipeThumbnailsResponse> response) {
                RecipeThumbnailsResponse recipeThumbnailsResponse = response.body();
                if (recipeThumbnailsResponse == null)
                    return;
                recipeThumbnailLiveData.postValue(recipeThumbnailsResponse.getRecipeThumbnails());
            }

            @Override
            public void onFailure(@NonNull Call<RecipeThumbnailsResponse> call, @NonNull Throwable t) {
                exceptionLiveData.postValue(t);
            }
        });
    }

    public void getRecipeInfo(int id) {
        Repository.getRepo().getRecipeInfo(id).enqueue(new Callback<OnlineRecipe>() {
            @Override
            public void onResponse(@NonNull Call<OnlineRecipe> call, @NonNull Response<OnlineRecipe> response) {
                OnlineRecipe recipe = response.body();
                if (recipe == null)
                    return;

                //Init the image of the current recipe
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        URL url = new URL(recipe.getImageUrl());
                        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                        InputStream inputStream = httpsURLConnection.getInputStream();
                        recipe.setBitmap(BitmapFactory.decodeStream(inputStream));
                        recipeLiveData.postValue(recipe);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }

            @Override
            public void onFailure(@NonNull Call<OnlineRecipe> call, @NonNull Throwable t) {
                exceptionLiveData.postValue(t);
            }
        });
    }
}