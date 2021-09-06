package shays.myrecipes.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import shays.myrecipes.ui.lists.liked.LikedFragment;
import shays.myrecipes.ui.lists.myown.MyOwnFragment;

public class ListsAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentList;

    public ListsAdapter(@NonNull Fragment fragment) {
        super(fragment);
        fragmentList = new ArrayList<>();
        fragmentList.add(new LikedFragment());
        fragmentList.add(new MyOwnFragment());
    }

    public ArrayList<Fragment> getFragmentList() {
        return fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
