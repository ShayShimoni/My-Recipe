package shays.myrecipes.ui.create;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Executors;

import shays.myrecipes.R;
import shays.myrecipes.model.MyRecipe;
import shays.myrecipes.repository.Repository;

public class CreateNewRecipeViewModel extends AndroidViewModel {

    public CreateNewRecipeViewModel(@NonNull Application application) {
        super(application);
    }

    public void saveNewRecipe(MyRecipe myRecipe, boolean didCaptureImage) {
        Repository.getRepo().getRecipeDBManager().addMyRecipe(myRecipe);
        try {
            //Checks if the image came from gallery, if so save it locally.
            if (!didCaptureImage){
                FileOutputStream imageStream = getApplication().openFileOutput(myRecipe.getId() + ".jpg", Context.MODE_PRIVATE);
                Executors.newSingleThreadExecutor().execute(() ->
                        myRecipe.getImageBitmap().compress(Bitmap.CompressFormat.JPEG, 100, imageStream));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplication(), R.string.toast_recipe_saved_success, Toast.LENGTH_SHORT).show();
    }

}
