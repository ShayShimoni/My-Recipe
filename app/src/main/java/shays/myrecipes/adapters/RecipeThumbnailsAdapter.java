package shays.myrecipes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shays.myrecipes.databinding.ListItemRecipeThumbnailBinding;
import shays.myrecipes.model.RecipeThumbnail;


public class RecipeThumbnailsAdapter extends RecyclerView.Adapter<RecipeThumbnailsAdapter.RecipeHolder> {
    private List<RecipeThumbnail> recipeThumbnailList;
    private MutableLiveData<RecipeThumbnail> recipeClicked;

    public RecipeThumbnailsAdapter(List<RecipeThumbnail> recipeThumbnailList, MutableLiveData<RecipeThumbnail> recipeClicked) {
        this.recipeThumbnailList = recipeThumbnailList;
        this.recipeClicked = recipeClicked;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemRecipeThumbnailBinding binding = ListItemRecipeThumbnailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        RecipeThumbnail recipeThumbnail = recipeThumbnailList.get(position);
        holder.binding.tvTitle.setText(recipeThumbnail.getTitle());
        Picasso.get().load(recipeThumbnail.getImage()).into(holder.binding.ivImage);
        holder.itemView.setOnClickListener(v -> recipeClicked.setValue(recipeThumbnail));
    }

    @Override
    public int getItemCount() {
        return recipeThumbnailList.size();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder {
        ListItemRecipeThumbnailBinding binding;

        public RecipeHolder(@NonNull ListItemRecipeThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
