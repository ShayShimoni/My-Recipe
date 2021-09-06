package shays.myrecipes.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import shays.myrecipes.model.MyRecipe;
import shays.myrecipes.model.OnlineRecipe;

@Database(entities = {OnlineRecipe.class, OnlineRecipe.Ingredient.class, MyRecipe.class}, version = 1)
public abstract class RecipeDataBase extends RoomDatabase {
    public abstract RecipeDAO getRecipeDAO();
}
