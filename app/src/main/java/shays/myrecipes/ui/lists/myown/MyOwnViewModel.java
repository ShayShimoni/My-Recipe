package shays.myrecipes.ui.lists.myown;

import android.app.Application;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import shays.myrecipes.R;
import shays.myrecipes.model.MyRecipe;
import shays.myrecipes.repository.Repository;

public class MyOwnViewModel extends AndroidViewModel {
    private MutableLiveData<List<MyRecipe>> listMutableLiveData;
    private MutableLiveData<MyRecipe> myRecipeClicked;

    public MyOwnViewModel(@NonNull Application application) {
        super(application);
        listMutableLiveData = new MutableLiveData<>();
        myRecipeClicked = new MutableLiveData<>();
        getMyRecipes();
    }

    public LiveData<List<MyRecipe>> getListLiveData() {
        return listMutableLiveData;
    }

    public MutableLiveData<MyRecipe> getMyRecipeClicked() {
        return myRecipeClicked;
    }

    //Gets the data from MyRecipe table.
    private void getMyRecipes(){
        Repository.getRepo().getRecipeDBManager().getMyRecipes(myRecipes -> {

            //Sets images for each recipe, from local directory by id.
            for (MyRecipe myRecipe : myRecipes) {
                try {
                    FileInputStream imageInputStream = getApplication().openFileInput(myRecipe.getId() + getApplication().getString(R.string.extension_jpg));
                    myRecipe.setImageBitmap(BitmapFactory.decodeStream(imageInputStream));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            listMutableLiveData.postValue(myRecipes);
        });
    }
}
