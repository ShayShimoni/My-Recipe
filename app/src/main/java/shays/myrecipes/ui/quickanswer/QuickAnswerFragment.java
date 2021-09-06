package shays.myrecipes.ui.quickanswer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import shays.myrecipes.databinding.FragmentQuickAnswerBinding;

public class QuickAnswerFragment extends Fragment {

    private QuickAnswerViewModel mViewModel;
    private FragmentQuickAnswerBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuickAnswerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuickAnswerViewModel.class);

        //Gets the query from the user and sends an http request..
        binding.etQuickAnswerQuestion.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etQuickAnswerQuestion.getText().toString().trim();
                if (query.isEmpty())
                    return false;
                mViewModel.quickAnswer(query);
                binding.tvQuickAnswerAnswer.setText("");
                binding.progressBar2.setVisibility(View.VISIBLE);
            }
            return false;
        });

        mViewModel.getAnswerLiveData().observe(getViewLifecycleOwner(), answerResponse -> {
            binding.progressBar2.setVisibility(View.INVISIBLE);
            binding.tvQuickAnswerAnswer.setText(answerResponse.getAnswer());
        });
    }
}