package shays.myrecipes.ui.onlinerecipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import shays.myrecipes.R;
import shays.myrecipes.databinding.FragmentOnlineRecipeBinding;
import shays.myrecipes.model.OnlineRecipe;

public class OnlineRecipeFragment extends Fragment {
    private final String PREFIX_INGREDIENT_IMAGE_100 = "https://spoonacular.com/cdn/ingredients_100x100/";
    private OnlineRecipeViewModel mOnlineRecipeViewModel;
    private FragmentOnlineRecipeBinding binding;
    private OnlineRecipe recipe;
    private boolean isRecipeFromDB;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentOnlineRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, R.id.action_share, 101, "Share")
                .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_share_24, null))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (!isRecipeFromDB){
            menu.add(0, R.id.action_add_to_list, 102, "Add to list")
                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_list_add_24, null))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        inflater.inflate(R.menu.menu_main, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/*");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome recipe i found!\n" + recipe.getSourceUrl());
                startActivity(Intent.createChooser(sendIntent, null));
                break;

            case R.id.action_add_to_list:
                mOnlineRecipeViewModel.addRecipeToList(recipe);
                Snackbar.make(requireView(), R.string.snackbar_recipe_saved, Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOnlineRecipeViewModel = new ViewModelProvider(this).get(OnlineRecipeViewModel.class);

        Bundle arguments = getArguments();
        if (getArguments() == null)
            return;
        recipe = (OnlineRecipe) arguments.get(getString(R.string.bundle_recipe));
        isRecipeFromDB = arguments.getBoolean(getString(R.string.bundle_is_recipe_from_db));
        for (OnlineRecipe.Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getId() == 0) {
                recipe.getIngredients().remove(ingredient);
                continue;
            }
            ingredient.setRecipeID(recipe.getId());
        }

       setupViews();
    }

    private void setupViews() {
        binding.clRecipeInfo.setBackground(new BitmapDrawable(getResources(), recipe.getBitmap()));
        binding.tvRecipeTitle.setText(recipe.getTitle());
        binding.tvRecipeHealthScoreValue.setText(Double.toString(recipe.getHealthScore()));
        binding.tvRecipeReadyInMinutesValue.setText(Integer.toString(recipe.getReadyInMinutes()));
        binding.tvRecipeServingsValue.setText(Integer.toString(recipe.getServings()));

        indicateTags(recipe.isVegetarian(), binding.tvRecipeVegetarianTag);
        indicateTags(recipe.isVegan(), binding.tvRecipeVeganTag);
        indicateTags(recipe.isGlutenFree(), binding.tvRecipeGlutenfreeTag);
        indicateTags(recipe.isDairyFree(), binding.tvRecipeDairyfreeTag);

        for (OnlineRecipe.Ingredient recipeIngredient : recipe.getIngredients()) {
            addIngredientsToView(recipeIngredient);
        }

        binding.tvRecipeSourceName.setText(recipe.getSourceName());
        binding.btnGoToRecipe.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceUrl()));
            startActivity(sendIntent);
        });
    }

    private void addIngredientsToView(@NotNull OnlineRecipe.Ingredient ingredient) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        TextView textView = new TextView(getContext());
        ImageView imageView = new ImageView(getContext());

        //LinearLayout params & set attr
        LinearLayout.LayoutParams linearLayoutLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutLayoutParams.topMargin = 8;
        linearLayout.setLayoutParams(linearLayoutLayoutParams);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //TextView params & set attr
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        textLayoutParams.weight = 10;
        textView.setLayoutParams(textLayoutParams);

        textView.setText(ingredient.getSummary());
        textView.setTextAppearance(R.style.IngredientTextView);
        textView.setGravity(Gravity.CENTER);
        textView.setLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(16, 0, 16, 0);


        //ImageView params & set attr
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(200, 200);
        imageLayoutParams.weight = 1;
        imageView.setLayoutParams(imageLayoutParams);


        Picasso.get().load(PREFIX_INGREDIENT_IMAGE_100 + ingredient.getImageUrl()).into(imageView);

        //Add views to this activity's layout
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        binding.llIngredients.addView(linearLayout);
    }

    //Checks the recipe's tags and set TextViews accordingly.
    private void indicateTags(boolean flag, @NonNull TextView textView) {
        if (flag) {
            textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_text_view_tag_true, null));
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_yes_indicator_24, null);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        } else {
            textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_text_view_tag_false, null));
            textView.setTextColor(Color.WHITE);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_no_indicator_24, null);
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, drawable);
        }

    }
}