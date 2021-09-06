package shays.myrecipes.ui.myrecipe;

import android.app.Application;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import shays.myrecipes.R;
import shays.myrecipes.model.MyRecipe;


public class MyRecipeViewModel extends AndroidViewModel {


    public MyRecipeViewModel(@NonNull Application application) {
        super(application);
    }

    public void setRecipeImage(MyRecipe myRecipe){
        try {
            FileInputStream fileInputStream = getApplication().openFileInput(myRecipe.getId() + getApplication().getString(R.string.extension_jpg));
            myRecipe.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}