package shays.myrecipes.repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.model.QuickAnswerResponse;
import shays.myrecipes.model.RecipeThumbnailsResponse;


public interface RecipeService {

    @GET("recipes/complexSearch?number=40&apiKey=" + Repository.API_KEY)
    Call<RecipeThumbnailsResponse> search(@Query("query") String query);

    @GET("recipes/{id}/information?apiKey=" + Repository.API_KEY)
    Call<OnlineRecipe> getRecipeInfo(@Path("id") int id);

    @GET("recipes/quickAnswer?apiKey=" + Repository.API_KEY)
    Call<QuickAnswerResponse> quickAnswer(@Query("q") String q);

}
