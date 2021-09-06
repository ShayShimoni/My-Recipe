package shays.myrecipes.ui.lists.myown;

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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import shays.myrecipes.R;
import shays.myrecipes.adapters.MyOwnAdapter;
import shays.myrecipes.databinding.FragmentMyOwnBinding;


public class MyOwnFragment extends Fragment {
    public String MENU_ITEM_TITLE = "Edit";
    private FragmentMyOwnBinding binding;
    private MyOwnViewModel myOwnViewModel;
    private MenuItem menuItem;
    private RecyclerView rvMyOwn;
    public boolean flag;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyOwnBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
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
        myOwnViewModel = new ViewModelProvider(this).get(MyOwnViewModel.class);
        rvMyOwn = binding.rvMyOwn;

        myOwnViewModel.getListLiveData().observe(getViewLifecycleOwner(), myRecipes -> {
            binding.rvMyOwn.setAdapter(new MyOwnAdapter(myRecipes, myOwnViewModel.getMyRecipeClicked()));
            binding.rvMyOwn.setLayoutManager(new LinearLayoutManager(requireContext()));
        });

        myOwnViewModel.getMyRecipeClicked().observe(getViewLifecycleOwner(), myRecipe -> {
            if (myRecipe == null)
                return;
            Bundle bundle = new Bundle();
            bundle.putParcelable("myRecipe", myRecipe);
            NavHostFragment.findNavController(this).navigate(R.id.myRecipeFragment, bundle);
            myOwnViewModel.getMyRecipeClicked().postValue(null);
        });
    }

    public RecyclerView getRvMyOwn() {
        return rvMyOwn;
    }

    //Toggle between delete item list state to regular state
    private void toggleDeleteViewConstraints() {
        int childCount = binding.rvMyOwn.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View flDelete = binding.rvMyOwn.getChildAt(i).findViewById(R.id.fl_recipe_item_delete);
            View gv80 = binding.rvMyOwn.getChildAt(i).findViewById(R.id.gv80);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) flDelete.getLayoutParams();
            if (!flag) {
                layoutParams.startToEnd = gv80.getId();
            } else {
                layoutParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            }
            flDelete.setLayoutParams(layoutParams);
        }
        flag = !flag;
    }

    //Restore list item state to regular
    public void reset() {
        int childCount = binding.rvMyOwn.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View flDelete = binding.rvMyOwn.getChildAt(i).findViewById(R.id.fl_recipe_item_delete);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) flDelete.getLayoutParams();

            layoutParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            flDelete.setLayoutParams(layoutParams);
        }
        flag = false;
        MENU_ITEM_TITLE = "Edit";
    }

    @Override
    public void onResume() {
        super.onResume();
        reset();
    }

}