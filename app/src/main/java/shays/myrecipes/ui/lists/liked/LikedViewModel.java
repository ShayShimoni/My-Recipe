package shays.myrecipes.ui.lists.liked;

import android.app.Application;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import shays.myrecipes.R;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.model.RecipeWithIngredients;
import shays.myrecipes.repository.Repository;


public class LikedViewModel extends AndroidViewModel {

    private MutableLiveData<List<OnlineRecipe>> recipesLiveData;
    private MutableLiveData<OnlineRecipe> recipeClicked;

    public LikedViewModel(@NonNull Application application) {
        super(application);
        recipesLiveData = new MutableLiveData<>();
        recipeClicked = new MutableLiveData<>();
        getOnlineRecipe();
    }

    //Gets the data from OnlineRecipe table with the corresponding Ingredient's list table, and merge them together.
    private void getOnlineRecipe(){
        Repository.getRepo().getRecipeDBManager().getRecipeWithIngredients(recipesWithIngredients -> {
            ArrayList<OnlineRecipe> recipes = new ArrayList<>();
            for (RecipeWithIngredients recipeWithIngredient : recipesWithIngredients) {
                recipeWithIngredient.recipe.setIngredients(recipeWithIngredient.ingredients);
                recipes.add(recipeWithIngredient.recipe);
            }

            //Sets images for each recipe, from local directory by id.
            for (OnlineRecipe recipe : recipes) {
                try {
                    FileInputStream imageIS = getApplication().openFileInput(recipe.getId() + getApplication().getString(R.string.extension_jpg));
                    recipe.setBitmap(BitmapFactory.decodeStream(imageIS));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            recipesLiveData.postValue(recipes);
        });
    }

    public LiveData<List<OnlineRecipe>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public MutableLiveData<OnlineRecipe> getRecipeClicked() {
        return recipeClicked;
    }
}
