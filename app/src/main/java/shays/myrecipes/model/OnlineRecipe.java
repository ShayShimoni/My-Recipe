package shays.myrecipes.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "online_recipe")
public class OnlineRecipe implements Parcelable {

    @PrimaryKey
    private int id;

    private String title;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    private String imageUrl;

    private int servings;

    @ColumnInfo(name = "ready_in_minutes")
    private int readyInMinutes;

    @ColumnInfo(name = "source_name")
    private String sourceName;

    @ColumnInfo(name = "source_url")
    private String sourceUrl;

    @ColumnInfo(name = "health_score")
    private double healthScore;

    @ColumnInfo(name = "dairy_free")
    private boolean dairyFree;

    @ColumnInfo(name = "gluten_free")
    private boolean glutenFree;

    private boolean vegan;

    @ColumnInfo(name = "vegetarian")
    private boolean vegetarian;

    @Ignore
    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;

    private String summary;

    @Ignore
    private Bitmap bitmap;

    public OnlineRecipe() {
    }

    protected OnlineRecipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imageUrl = in.readString();
        servings = in.readInt();
        readyInMinutes = in.readInt();
        sourceName = in.readString();
        sourceUrl = in.readString();
        healthScore = in.readDouble();
        dairyFree = in.readByte() != 0;
        glutenFree = in.readByte() != 0;
        vegan = in.readByte() != 0;
        vegetarian = in.readByte() != 0;
        summary = in.readString();
    }

    public static final Creator<OnlineRecipe> CREATOR = new Creator<OnlineRecipe>() {
        @Override
        public OnlineRecipe createFromParcel(Parcel in) {
            return new OnlineRecipe(in);
        }

        @Override
        public OnlineRecipe[] newArray(int size) {
            return new OnlineRecipe[size];
        }
    };

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "\nRecipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", servings=" + servings +
                ", readyInMinutes=" + readyInMinutes +
                ", sourceName='" + sourceName + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", healthScore=" + healthScore +
                ", dairyFree=" + dairyFree +
                ", glutenFree=" + glutenFree +
                ", vegan=" + vegan +
                ", vegetarian=" + vegetarian +
                ", ingredients=" + ingredients +
                ", summary='" + summary + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeInt(servings);
        dest.writeInt(readyInMinutes);
        dest.writeString(sourceName);
        dest.writeString(sourceUrl);
        dest.writeDouble(healthScore);
        dest.writeByte((byte) (dairyFree ? 1 : 0));
        dest.writeByte((byte) (glutenFree ? 1 : 0));
        dest.writeByte((byte) (vegan ? 1 : 0));
        dest.writeByte((byte) (vegetarian ? 1 : 0));
        dest.writeString(summary);
    }

    @Entity
    public static class Ingredient {
        @PrimaryKey
        private int id;

        @ColumnInfo(name = "image_url")
        @SerializedName("image")
        private String imageUrl;

        @ColumnInfo(name = "summary")
        @SerializedName("original")
        private String summary;

        @ColumnInfo(name = "recipe_id")
        private int recipeID;

        public Ingredient() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public int getRecipeID() {
            return recipeID;
        }

        public void setRecipeID(int recipeID) {
            this.recipeID = recipeID;
        }

        @Override
        public String toString() {
            return "\nIngredient{" +
                    "id=" + id +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", summary='" + summary + '\'' +
                    ", recipeID=" + recipeID +
                    '}';
        }
    }
}
