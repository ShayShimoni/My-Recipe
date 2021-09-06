package shays.myrecipes.model;

public class RecipeThumbnail {
    private int id;
    private String title;
    private String image;

    public RecipeThumbnail() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "\nRecipeListItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
