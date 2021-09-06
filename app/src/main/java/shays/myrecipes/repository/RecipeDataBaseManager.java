package shays.myrecipes.repository;

import android.content.Context;

import androidx.room.Room;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import shays.myrecipes.model.MyRecipe;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.model.RecipeWithIngredients;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RecipeDataBaseManager {
    public static final String SUFFIX_FILE_JPG = ".jpg";
    public static final String MY_RECIPES_DB_NAME = "recipes";
    private final ExecutorService EXECUTOR;
    private final RecipeDataBase db;
    private final File filesDir;

    public RecipeDataBaseManager(Context context) {
        db = Room.databaseBuilder(context, RecipeDataBase.class, MY_RECIPES_DB_NAME).build();
        EXECUTOR = Executors.newSingleThreadExecutor();
        filesDir = context.getFilesDir();
    }

    public void addOnlineRecipe(OnlineRecipe recipe) {
        EXECUTOR.execute(() -> {
            db.getRecipeDAO().addOnlineRecipe(recipe);
        });
    }

    public void deleteOnlineRecipe(OnlineRecipe onlineRecipe) {
        EXECUTOR.execute(() -> {
            db.getRecipeDAO().deleteOnlineRecipe(onlineRecipe);
            new File(filesDir, onlineRecipe.getId() + SUFFIX_FILE_JPG).delete();
        });
    }

    public void addAllIngredients(List<OnlineRecipe.Ingredient> ingredients) {
        EXECUTOR.execute(() -> {
            db.getRecipeDAO().addAllIngredients(ingredients);
        });
    }

    public void deleteIngredient(OnlineRecipe.Ingredient ingredient) {
        EXECUTOR.execute(() -> {
            db.getRecipeDAO().deleteIngredient(ingredient);
        });
    }

    public void getRecipeWithIngredients(RecipeWithIngredientCallBack listener) {
        EXECUTOR.execute(() -> {
            listener.recipesArrived(db.getRecipeDAO().getRecipeWithIngredients());
        });
    }

    public void addMyRecipe(MyRecipe myRecipe) {
        EXECUTOR.execute(() -> {
            db.getRecipeDAO().addMyRecipe(myRecipe);
        });
    }

    public void deleteMyRecipe(MyRecipe myRecipe) {
        EXECUTOR.execute(() -> {
            db.getRecipeDAO().deleteMyRecipe(myRecipe);
            new File(filesDir, myRecipe.getId() + SUFFIX_FILE_JPG).delete();
        });
    }

    public void getMyRecipes(MyRecipeCallBack listener) {
        EXECUTOR.execute(() -> {
            listener.recipesArrived(db.getRecipeDAO().getMyRecipes());
        });
    }

    public interface RecipeWithIngredientCallBack {
        void recipesArrived(List<RecipeWithIngredients> recipesWithIngredients);
    }

    public interface MyRecipeCallBack {
        void recipesArrived(List<MyRecipe> myRecipes);
    }

}
