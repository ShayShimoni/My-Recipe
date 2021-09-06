package shays.myrecipes.ui.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import shays.myrecipes.adapters.ListsAdapter;
import shays.myrecipes.databinding.FragmentMyRecipeListsBinding;
import shays.myrecipes.ui.lists.liked.LikedFragment;
import shays.myrecipes.ui.lists.myown.MyOwnFragment;


public class MyRecipeListsFragment extends Fragment {
    private FragmentMyRecipeListsBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyRecipeListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListsAdapter listsAdapter = new ListsAdapter(this);
        binding.viewPager.setAdapter(listsAdapter);
        ArrayList<Fragment> fragmentList = listsAdapter.getFragmentList();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());

                //Resets list item delete options and the menu item title, when changing the current viewed list.
                Fragment fragment = fragmentList.get(tab.getPosition());
                if (fragment.getClass() == LikedFragment.class) {
                    LikedFragment likedFragment = (LikedFragment) fragment;
                    if (likedFragment.getRvLikedRecipes() != null)
                        likedFragment.reset();
                } else if (fragment.getClass() == MyOwnFragment.class) {
                    MyOwnFragment myOwnFragment = (MyOwnFragment) fragment;
                    if (myOwnFragment.getRvMyOwn() != null)
                        myOwnFragment.reset();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

    }

}