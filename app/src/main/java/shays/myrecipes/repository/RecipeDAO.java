package shays.myrecipes.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import shays.myrecipes.model.MyRecipe;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.model.RecipeWithIngredients;

@Dao
public interface RecipeDAO {

    //--------------- Online Recipe ---------------//
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addAllRecipes(List<OnlineRecipe> recipes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addOnlineRecipe(OnlineRecipe recipe);

    @Delete
    void deleteOnlineRecipe(OnlineRecipe recipe);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addAllIngredients(List<OnlineRecipe.Ingredient> ingredients);

    @Delete
    void deleteIngredient(OnlineRecipe.Ingredient ingredient);

    @Transaction
    @Query("SELECT * FROM online_recipe")
    List<RecipeWithIngredients> getRecipeWithIngredients();

    //---------------- My Recipe ------------------//
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMyRecipe(MyRecipe myRecipe);

    @Delete
    void deleteMyRecipe(MyRecipe myRecipe);

    @Query("SELECT * FROM my_recipe")
    List<MyRecipe>  getMyRecipes();
}
