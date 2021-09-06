package shays.myrecipes.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/*
This class is made for describing the relation in the room database between 2 tables
using a foreign key, which will be used to retrieve data from both tables.
*/
public class RecipeWithIngredients {

    @Embedded
    public OnlineRecipe recipe;
    @Relation(
            parentColumn = "id", //Primary Key
            entityColumn = "recipe_id" //Foreign Key
    )
    public List<OnlineRecipe.Ingredient> ingredients;

}
