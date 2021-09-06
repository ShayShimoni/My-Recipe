package shays.myrecipes.ui.search;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import shays.myrecipes.MyInternetReceiver;
import shays.myrecipes.R;
import shays.myrecipes.adapters.RecipeThumbnailsAdapter;
import shays.myrecipes.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {
    public static final String BUNDLE_RECIPE_KEY = "recipe";
    public static final String BUNDLE_IS_RECIPE_FROM_DB = "isRecipeFromDB";
    private final MyInternetReceiver internetReceiver = new MyInternetReceiver();
    private SearchViewModel mSearchViewModel;
    private FragmentSearchBinding binding;
    private String query;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        requireContext().registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        MyInternetReceiver.connectivityReceiverListener = isConnected -> {
          if (!isConnected){
              Toast toast = Toast.makeText(getContext(), R.string.no_internet_connection_error, Toast.LENGTH_LONG);
              toast.setGravity(Gravity.CENTER, 0, 0);
              toast.show();
          }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        requireContext().unregisterReceiver(internetReceiver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        mSearchViewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), throwable -> {
            System.out.println(throwable.getMessage());
        });

        //Gets the results from user's search query,  and set the RecyclerView's adapter.
        mSearchViewModel.getRecipeThumbnailLiveData().observe(getViewLifecycleOwner(), recipes -> {
            if (!recipes.isEmpty()) {
                binding.rvRecipeThumbnails.setAdapter(new RecipeThumbnailsAdapter(recipes, mSearchViewModel.getRecipeClicked()));
                binding.rvRecipeThumbnails.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            } else {
                Toast toast = Toast.makeText(getContext(), R.string.toast_recipe_not_found_error, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            binding.progressBar.setVisibility(View.INVISIBLE);
        });

        mSearchViewModel.getRecipeClicked().observe(getViewLifecycleOwner(), recipeThumbnail -> {
            if (recipeThumbnail == null)
                return;
            binding.progressBar.setVisibility(View.VISIBLE);
            mSearchViewModel.getRecipeInfo(recipeThumbnail.getId());

        });

        //Gets the info of the clicked recipe, and navigates to the recipe fragment.
        mSearchViewModel.getRecipeLiveData().observe(getViewLifecycleOwner(), recipe -> {
            if (mSearchViewModel.getRecipeClicked().getValue() == null)
                return;
            TransitionManager.beginDelayedTransition(binding.getRoot());
            NavController navController = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle(); //Contains the recipe info.
            bundle.putParcelable(getString(R.string.bundle_recipe), recipe);
            bundle.putBoolean(getString(R.string.bundle_is_recipe_from_db), false);
            navController.navigate(R.id.onlineRecipeFragment, bundle);
            mSearchViewModel.getRecipeClicked().postValue(null);
            binding.progressBar.setVisibility(View.INVISIBLE);
        });

        //Gets the user's query from the EditText, and search the spoonacular database for recipes.
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                boolean connected = MyInternetReceiver.isConnected(requireContext());// Checks internet connection.
                if (!connected) {
                    Toast.makeText(getContext(), R.string.no_internet_connection_error, Toast.LENGTH_SHORT).show();
                    return false;
                }

                //Validates the query
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty() && !query.equals(this.query)) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    mSearchViewModel.searchRecipesOnline(query);
                    this.query = query; //Prevents using the same request.
                }
                binding.etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
            return false;
        });
    }
}