package com.example.cs4084_finalproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class SavedMemesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SavedMemesAdapter savedMemesAdapter;
    private ArrayList<String> savedMemesList;

    public SavedMemesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_memes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewSavedMemes);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        savedMemesList = loadSavedMemes();
        savedMemesAdapter = new SavedMemesAdapter(savedMemesList, this);
        recyclerView.setAdapter(savedMemesAdapter);
    }

    private ArrayList<String> loadSavedMemes() {
        DBHandler dbHandler = new DBHandler(requireContext());
        ArrayList<String> memes = dbHandler.readMemes();
        return memes != null ? memes : new ArrayList<>();
    }
}
