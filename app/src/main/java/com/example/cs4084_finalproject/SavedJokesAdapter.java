package com.example.cs4084_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SavedJokesAdapter extends RecyclerView.Adapter<SavedJokesAdapter.JokeViewHolder> {

    private final List<String> jokes;

    public SavedJokesAdapter(List<String> jokes) {
        this.jokes = jokes;
    }

    @NonNull
    @Override
    public JokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new JokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JokeViewHolder holder, int position) {
        String joke = jokes.get(position);
        holder.textView.setText(joke);

        // 👉 Set up click to open JokeDetailFragment
        holder.itemView.setOnClickListener(v -> {
            JokeDetailFragment jokeDetailFragment = JokeDetailFragment.newInstance(joke);
            ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, jokeDetailFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        });
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    static class JokeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public JokeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
