package shays.myrecipes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
The object that holds the data from spoonacular api after making an http request,
using the service and GSON Converter
*/
public class RecipeThumbnailsResponse {
    @SerializedName("results")
    private List<RecipeThumbnail> recipeThumbnails;

    public RecipeThumbnailsResponse() { }

    public List<RecipeThumbnail> getRecipeThumbnails() {
        return recipeThumbnails;
    }

    public void setRecipeThumbnails(List<RecipeThumbnail> recipeThumbnails) {
        this.recipeThumbnails = recipeThumbnails;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipes=" + recipeThumbnails +
                '}';
    }
}
