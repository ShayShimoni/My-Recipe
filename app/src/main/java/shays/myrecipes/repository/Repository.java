package shays.myrecipes.repository;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.model.QuickAnswerResponse;
import shays.myrecipes.model.RecipeThumbnailsResponse;

public class Repository {
    public static final String API_KEY = "23492ec0598e4da3b49a1ff04b0be99b";
    private static Repository repo;
    private final RecipeService service;
    private RecipeDataBaseManager recipeDataBaseManager;
    private boolean isRecipeDBInitialized;

    private Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RecipeService.class);
    }

    public static Repository getRepo() {
        if (repo == null)
            repo = new Repository();
        return repo;
    }

    public void initRecipeDBManager(Context context) {
        if (!isRecipeDBInitialized) {
            recipeDataBaseManager = new RecipeDataBaseManager(context);
            isRecipeDBInitialized = true;
        }
    }

    public RecipeDataBaseManager getRecipeDBManager() {
        if (isRecipeDBInitialized)
            return recipeDataBaseManager;
        else
            throw new RuntimeException("You must initialize the database manager before accessing it.");
    }

    public Call<RecipeThumbnailsResponse> searchRecipesOnline(String query) {
        return service.search(query);
    }

    public Call<OnlineRecipe> getRecipeInfo(int id) {
        return service.getRecipeInfo(id);
    }

    public Call<QuickAnswerResponse> quickAnswer(String query){
        return service.quickAnswer(query);
    }

}