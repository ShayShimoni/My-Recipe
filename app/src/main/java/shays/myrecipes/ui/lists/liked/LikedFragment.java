package shays.myrecipes.ui.lists.liked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import shays.myrecipes.R;
import shays.myrecipes.adapters.LikedAdapter;
import shays.myrecipes.databinding.FragmentLikedBinding;


public class LikedFragment extends Fragment {
    public String MENU_ITEM_TITLE = "Edit";
    private FragmentLikedBinding binding;
    private LikedViewModel mLikedViewModel;
    private MenuItem menuItem;
    private RecyclerView rvLikedRecipes;
    public boolean flag;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentLikedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        reset();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        menuItem = menu.add(0, R.id.action_edit, 101, MENU_ITEM_TITLE);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {

            if (!flag)
                item.setTitle("Done");
            else
                item.setTitle("Edit");

            toggleDeleteViewConstraints();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLikedViewModel = new ViewModelProvider(this).get(LikedViewModel.class);
        rvLikedRecipes = binding.rvLikedRecipes;

        mLikedViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), recipes -> {
            binding.rvLikedRecipes.setAdapter(new LikedAdapter(recipes, mLikedViewModel.getRecipeClicked()));
            binding.rvLikedRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        mLikedViewModel.getRecipeClicked().observe(getViewLifecycleOwner(), recipe -> {
            if (recipe == null)
                return;
            NavController navController = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.bundle_recipe), recipe);
            bundle.putBoolean(getString(R.string.bundle_is_recipe_from_db), true);
            navController.navigate(R.id.onlineRecipeFragment, bundle);
            mLikedViewModel.getRecipeClicked().postValue(null);
        });
    }

    public RecyclerView getRvLikedRecipes() {
        return rvLikedRecipes;
    }

    //Toggle between delete item list state to regular state
    private void toggleDeleteViewConstraints() {
        int childCount = binding.rvLikedRecipes.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View flDelete = binding.rvLikedRecipes.getChildAt(i).findViewById(R.id.fl_recipe_item_delete);
            View gv80 = binding.rvLikedRecipes.getChildAt(i).findViewById(R.id.gv80);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) flDelete.getLayoutParams();

            if (!flag)
                layoutParams.startToEnd = gv80.getId();
            else
                layoutParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;

            flDelete.setLayoutParams(layoutParams);
        }
        flag = !flag;
    }

    //Restore list item state to regular
    public void reset() {
        int childCount = binding.rvLikedRecipes.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View flDelete = binding.rvLikedRecipes.getChildAt(i).findViewById(R.id.fl_recipe_item_delete);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) flDelete.getLayoutParams();

            layoutParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            flDelete.setLayoutParams(layoutParams);
        }
        flag = false;
       MENU_ITEM_TITLE = "Edit";
    }

}