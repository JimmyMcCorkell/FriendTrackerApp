package com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimmy.tyler.friendtrackerapp.R;

public abstract class AbstractFragment extends Fragment {

    private String fragmentName;
    private int fabDrawableId, stringId;

    /**
     * primary constructor for instantiation
     *
     * @param fragment      the fragment
     * @param fragmentName  the name for the fragment
     * @param fabDrawableId the icon for the fab
     * @param stringId      id of the fragment
     * @return returns the abstract fragment
     */
    public static AbstractFragment newInstance(AbstractFragment fragment, String fragmentName, int fabDrawableId, int stringId) {
        fragment.fragmentName = fragmentName;
        fragment.fabDrawableId = fabDrawableId;
        fragment.stringId = stringId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /**
     * Abstract function for the fabaction implement fab action
     *
     * @param view
     */
    public abstract void fabAction(View view);

    /**
     * Gets the name of the current fragment
     *
     * @return The fragment name
     */
    public String getFragmentName() {
        return fragmentName;
    }

    /**
     * Gets the fab id. The icon id
     *
     * @return the fab id
     */
    public int getFabDrawableId() {
        return fabDrawableId;
    }

    /**
     * Gets the fragments id
     *
     * @return fragment id
     */

    public int getStringId() {
        return stringId;
    }

}