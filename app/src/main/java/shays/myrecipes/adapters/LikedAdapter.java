package shays.myrecipes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import shays.myrecipes.R;
import shays.myrecipes.databinding.ListItemRecipeBinding;
import shays.myrecipes.model.OnlineRecipe;
import shays.myrecipes.repository.Repository;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.LikedRecipeHolder> {
    private List<OnlineRecipe> recipes;
    private MutableLiveData<OnlineRecipe> recipeClicked;

    public LikedAdapter(List<OnlineRecipe> recipes, MutableLiveData<OnlineRecipe> recipeClicked) {
        this.recipes = recipes;
        this.recipeClicked = recipeClicked;
    }

    @NonNull
    @Override
    public LikedRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemRecipeBinding binding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LikedRecipeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedRecipeHolder holder, int position) {
        OnlineRecipe recipe = recipes.get(position);
        holder.binding.tvRecipeItemTitle.setText(recipe.getTitle());
        Picasso.get().load(recipe.getImageUrl()).into(holder.binding.ivRecipeItemImage);
        holder.itemView.setOnClickListener(v -> recipeClicked.postValue(recipe));
        holder.binding.ibRecipeItemDelete.setOnClickListener(v -> {
            Snackbar make = Snackbar.make(v, R.string.snackbar_remove_recipe, Snackbar.LENGTH_LONG);
            make.setAction(R.string.snackbar_action_yes, v1 -> {
                Repository.getRepo().getRecipeDBManager().deleteOnlineRecipe(recipe);
                recipes.remove(recipe);
                notifyDataSetChanged();
            });
            make.show();
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    static class LikedRecipeHolder extends RecyclerView.ViewHolder {
        ListItemRecipeBinding binding;

        public LikedRecipeHolder(@NonNull ListItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
