package shays.myrecipes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import shays.myrecipes.R;
import shays.myrecipes.databinding.ListItemRecipeBinding;
import shays.myrecipes.model.MyRecipe;
import shays.myrecipes.repository.Repository;


public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MyRecipeViewHolder> {
    private List<MyRecipe> myRecipes;
    private MutableLiveData<MyRecipe> myRecipeClicked;

    public MyOwnAdapter(List<MyRecipe> myRecipes, MutableLiveData<MyRecipe> myRecipeClicked) {
        this.myRecipes = myRecipes;
        this.myRecipeClicked = myRecipeClicked;
    }

    @NonNull
    @Override
    public MyRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemRecipeBinding binding = ListItemRecipeBinding.inflate(inflater, parent, false);
        return new MyRecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeViewHolder holder, int position) {
        MyRecipe myRecipe = myRecipes.get(position);
        holder.binding.tvRecipeItemTitle.setText(myRecipe.getTitle());
        holder.binding.ivRecipeItemImage.setImageBitmap(myRecipe.getImageBitmap());
        holder.itemView.setOnClickListener(v -> myRecipeClicked.postValue(myRecipe));
        holder.binding.ibRecipeItemDelete.setOnClickListener(v -> {
            Snackbar make = Snackbar.make(v, R.string.snackbar_remove_recipe, Snackbar.LENGTH_LONG);
            make.setAction(R.string.snackbar_action_yes, v1 -> {
                Repository.getRepo().getRecipeDBManager().deleteMyRecipe(myRecipe);
                myRecipes.remove(myRecipe);
                notifyDataSetChanged();
            });
            make.show();
        });
    }

    @Override
    public int getItemCount() {
        return myRecipes.size();
    }

    static class MyRecipeViewHolder extends RecyclerView.ViewHolder {
        ListItemRecipeBinding binding;

        public MyRecipeViewHolder(@NonNull ListItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
