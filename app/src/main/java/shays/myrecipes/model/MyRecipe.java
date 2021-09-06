package shays.myrecipes.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "my_recipe")
public class MyRecipe implements Parcelable {

    @NonNull
    @PrimaryKey
    private String id;

    private String title;

    private int servings;

    @ColumnInfo(name = "ready_in_minutes")
    private int readyInMinutes;

    @Ignore
    private Bitmap imageBitmap;

    @Ignore
    private List<String> ingredients;

    @ColumnInfo(name = "ingredients")
    private String ingredientsStr;

    private String instructions;

    public MyRecipe() { }

    public MyRecipe(String title, int servings, int readyInMinutes, String ingredients, String instructions, Bitmap imageBitmap) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.ingredientsStr = ingredients;
        this.instructions = instructions;
        this.imageBitmap = imageBitmap;
    }


    public MyRecipe(@NotNull String absolutePath, String title, int servings, int readyInMinutes, String ingredients, String instructions) {
        this.id = absolutePath;
        this.title = title;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.ingredientsStr = ingredients;
        this.instructions = instructions;
    }

    protected MyRecipe(Parcel in) {
        id = in.readString();
        title = in.readString();
        servings = in.readInt();
        readyInMinutes = in.readInt();
        ingredients = in.createStringArrayList();
        ingredientsStr = in.readString();
        instructions = in.readString();
    }

    public static final Creator<MyRecipe> CREATOR = new Creator<MyRecipe>() {
        @Override
        public MyRecipe createFromParcel(Parcel in) {
            return new MyRecipe(in);
        }

        @Override
        public MyRecipe[] newArray(int size) {
            return new MyRecipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getServings() {
        return servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getIngredientsStr() {
        return ingredientsStr;
    }

    public String getInstructions() {
        return instructions;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setIngredientsStr(String ingredientsStr) {
        this.ingredientsStr = ingredientsStr;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "\nMyRecipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", servings=" + servings +
                ", readyInMinutes=" + readyInMinutes +
                ", imageBitmap=" + imageBitmap +
                ", ingredients=" + ingredients +
                ", ingredientsStr='" + ingredientsStr + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }

    public List<String> fromStringToList() {
        String[] split = ingredientsStr.split("\\|");
        return Arrays.asList(split);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeInt(servings);
        dest.writeInt(readyInMinutes);
        dest.writeStringList(ingredients);
        dest.writeString(ingredientsStr);
        dest.writeString(instructions);
    }
}
