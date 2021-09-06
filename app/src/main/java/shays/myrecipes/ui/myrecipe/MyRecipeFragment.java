package shays.myrecipes.ui.myrecipe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import shays.myrecipes.R;
import shays.myrecipes.databinding.FragmentMyRecipeBinding;
import shays.myrecipes.model.MyRecipe;


public class MyRecipeFragment extends Fragment {

    public static final String FILE_PROVIDER_AUTHORITY = "shays.myrecipes.ui.create.fileprovider";
    public static final String BUNDLE_KEY_MY_RECIPE = "myRecipe";
    private FragmentMyRecipeBinding binding;
    private MyRecipeViewModel mRecipeViewModel;
    private MyRecipe myRecipe;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRecipeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, R.id.action_share, 101, "Share")
                .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_share_24, null))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("*/*");
            File file = new File(requireContext().getFilesDir(), myRecipe.getId() + getString(R.string.extension_jpg));
            Uri uriForFile = FileProvider.getUriForFile(requireContext(), FILE_PROVIDER_AUTHORITY, file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this recipe i made!\n\n" + concatRecipe());
            startActivity(Intent.createChooser(shareIntent, null));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecipeViewModel = new ViewModelProvider(this).get(MyRecipeViewModel.class);

        myRecipe = requireArguments().getParcelable(BUNDLE_KEY_MY_RECIPE);
        myRecipe.setIngredients(myRecipe.fromStringToList());
        mRecipeViewModel.setRecipeImage(myRecipe);

        binding.tvMyRecipeTitle.setText(myRecipe.getTitle());
        binding.tvMyRecipeServingsValue.setText(String.valueOf(myRecipe.getServings()));
        binding.tvMyRecipeReadyInMinutesValue.setText(String.valueOf(myRecipe.getReadyInMinutes()));
        binding.clMyRecipeInfo.setBackground(new BitmapDrawable(getResources(), myRecipe.getImageBitmap()));

        setUpIngredientsAndInstructionsViews();
    }

    private void setUpIngredientsAndInstructionsViews(){
        TextView textView;
        boolean toggleBackgroundColorFlag = true;
        for (String ingredient : myRecipe.getIngredients()) {
            textView = new TextView(requireContext());
            textView.setText(ingredient);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            textView.setLayoutParams(layoutParams);
            if (toggleBackgroundColorFlag)
                textView.setBackgroundColor(Color.LTGRAY);
            toggleBackgroundColorFlag = !toggleBackgroundColorFlag;

            textView.setTextAppearance(R.style.IngredientTextView);
            textView.setPadding(16, 16, 16, 16);
            binding.llIngredients.addView(textView);
        }

        textView = new TextView(requireContext());
        textView.setText(myRecipe.getInstructions());
        textView.setTextAppearance(R.style.IngredientTextView);
        textView.setPadding(16, 16, 16, 16);
        binding.llInstructions.addView(textView);
    }

    public String concatRecipe() {
        StringBuilder sb = new StringBuilder();
        sb.append(myRecipe.getTitle()).append("\n");
        sb.append("Ready in minutes: ").append(myRecipe.getReadyInMinutes()).append(" | ")
                .append("Servings: ").append(myRecipe.getServings()).append("\n\n");
        sb.append("Ingredients:\n");
        for (String ingredient : myRecipe.getIngredients()) {
            sb.append(ingredient).append("\n");
        }
        sb.append("\nInstructions:\n");
        sb.append(myRecipe.getInstructions());
        return sb.toString();
    }
}