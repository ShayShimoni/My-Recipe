package shays.myrecipes.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import shays.myrecipes.R;
import shays.myrecipes.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cvSearch.setOnClickListener(this);
        binding.cvList.setOnClickListener(this);
        binding.cvCreate.setOnClickListener(this);
        binding.cvQuickAnswer.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        NavController navController = NavHostFragment.findNavController(this);
        switch (v.getId()){
            case R.id.cv_search:
                navController.navigate(R.id.searchFragment);
                break;
            case R.id.cv_list:
                navController.navigate(R.id.myRecipeListsFragment);
                break;
            case R.id.cv_create:
                navController.navigate(R.id.createNewRecipeFragment);
                break;
            case R.id.cv_quick_answer:
                navController.navigate(R.id.quickAnswerFragment);
                break;
        }
    }
}