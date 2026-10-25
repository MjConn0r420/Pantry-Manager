package com.example.smartpantry.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.util.RecipeMatcher.MatchResult;

import java.util.ArrayList;
import java.util.List;

/** Adapter for both the strict-suggestion list and the "Almost There" list. */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {

    public interface Listener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<MatchResult> data = new ArrayList<>();
    private final boolean almostThere;
    private final Listener listener;

    public RecipeAdapter(boolean almostThere, Listener listener) {
        this.almostThere = almostThere;
        this.listener = listener;
    }

    public void setData(List<MatchResult> results) {
        data.clear();
        data.addAll(results);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        final MatchResult m = data.get(position);
        h.name.setText(m.recipe.getName());
        if (almostThere) {
            h.sub.setText(m.missingText());
        } else {
            h.sub.setText(h.itemView.getContext().getString(
                    R.string.ready_to_cook, m.recipe.getIngredients().size()));
        }
        h.itemView.setOnClickListener(v -> listener.onRecipeClick(m.recipe));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name, sub;

        VH(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.tvRecipeName);
            sub = v.findViewById(R.id.tvRecipeSub);
        }
    }
}
