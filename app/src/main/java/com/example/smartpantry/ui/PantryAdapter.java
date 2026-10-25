package com.example.smartpantry.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.ExpiryUtil;
import com.example.smartpantry.util.Units;

import java.util.ArrayList;
import java.util.List;

/** Custom RecyclerView adapter for the pantry list. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean highlightExpiring = true;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems, boolean highlightExpiring) {
        items.clear();
        items.addAll(newItems);
        this.highlightExpiring = highlightExpiring;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        final PantryItem item = items.get(position);
        h.name.setText(item.getName());
        h.qty.setText(Units.formatQty(item.getQuantity()) + " " + Units.normalize(item.getUnit()));

        String exp = item.getExpiryDate();
        if (exp == null || exp.isEmpty()) {
            h.expiry.setText(R.string.no_expiry);
            h.expiry.setTextColor(Color.parseColor("#757575"));
        } else {
            Integer days = ExpiryUtil.daysUntil(exp);
            String label = h.itemView.getContext().getString(R.string.expires_on, exp);
            if (days != null && days < 0) label += " (expired)";
            else if (days != null && days <= ExpiryUtil.SOON_DAYS) label += " (soon)";
            h.expiry.setText(label);
            boolean warn = highlightExpiring && ExpiryUtil.isExpiringSoon(exp);
            h.expiry.setTextColor(warn ? Color.parseColor("#C62828") : Color.parseColor("#757575"));
        }

        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name, qty, expiry;
        final ImageButton btnEdit, btnDelete;

        VH(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.tvName);
            qty = v.findViewById(R.id.tvQty);
            expiry = v.findViewById(R.id.tvExpiry);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
