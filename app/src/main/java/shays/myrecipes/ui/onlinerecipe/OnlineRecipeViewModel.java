package shays.myrecipes.ui.onlinerecipe;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Executors;

import shays.myrecipes.R;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.repository.Repository;

public class OnlineRecipeViewModel extends AndroidViewModel {


    public OnlineRecipeViewModel(@NonNull Application application) {
        super(application);
    }

    //Saves the recipe in Room database
    public void addRecipeToList(OnlineRecipe onlineRecipe) {
        Repository repo = Repository.getRepo();
        repo.getRecipeDBManager().addOnlineRecipe(onlineRecipe);
        repo.getRecipeDBManager().addAllIngredients(onlineRecipe.getIngredients());
        try {
            //
            FileOutputStream fileOutputStream = getApplication().openFileOutput(onlineRecipe.getId() + getApplication().getString(R.string.extension_jpg), Context.MODE_PRIVATE);
            Executors.newSingleThreadExecutor().execute(() ->
                    onlineRecipe.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
