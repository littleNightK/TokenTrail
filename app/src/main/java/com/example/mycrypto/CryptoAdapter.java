package com.example.mycrypto;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> implements Filterable {
    private List<CryptoItem> cryptoItemList;
    private List<CryptoItem> filteredCryptoItemList; // Create a copy of the original list for filtering

    public CryptoAdapter(List<CryptoItem> cryptoItemList) {
        this.cryptoItemList = cryptoItemList;
        this.filteredCryptoItemList = new ArrayList<>(cryptoItemList); // Initialize filtered list with all items
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the money_type_item.xml layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_type_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CryptoItem cryptoItem = filteredCryptoItemList.get(position);

        // Bind data to the views in money_type_item.xml
        holder.moneyTypeSymbol.setText(cryptoItem.getSymbol());
        holder.moneyTypeName.setText(cryptoItem.getName());
        holder.moneyTypePrice.setText(cryptoItem.getPrice());
        holder.moneyTypeChange.setText(cryptoItem.getChange());

        // Set the cryptocurrency icon based on the icon resource ID
        holder.moneyTypeIcon.setImageResource(cryptoItem.getIconResId());

        // Set the text color based on whether the change is up or down
        if (cryptoItem.getChange().contains("+")) {
            // Change is up
            holder.moneyTypeChange.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.changeup));
        } else if (cryptoItem.getChange().contains("-")) {
            // Change is down
            holder.moneyTypeChange.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.changedown));
        } else {
            // Fallback color when there's no change indicator
            holder.moneyTypeChange.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.notchange));
        }

        // Set an OnClickListener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to open the CryptoDetailActivity
                Intent intent = new Intent(view.getContext(), CryptoDetailActivity.class);

                // Pass cryptocurrency data as extras to the intent
                intent.putExtra("cryptoSymbol", cryptoItem.getSymbol());
                intent.putExtra("cryptoName", cryptoItem.getName());
                intent.putExtra("cryptoPrice", cryptoItem.getPrice());
                intent.putExtra("cryptoChange", cryptoItem.getChange());
                intent.putExtra("cryptoIconResId", cryptoItem.getIconResId());

                // Start the CryptoDetailActivity
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredCryptoItemList.size(); // Return the size of the filtered list
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView moneyTypeSymbol;
        TextView moneyTypeName;
        TextView moneyTypePrice;
        TextView moneyTypeChange;
        ImageView moneyTypeIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            moneyTypeSymbol = itemView.findViewById(R.id.moneyTypeSymbol);
            moneyTypeName = itemView.findViewById(R.id.moneyTypeName);
            moneyTypePrice = itemView.findViewById(R.id.moneyTypePrice);
            moneyTypeChange = itemView.findViewById(R.id.moneyTypeChange);
            moneyTypeIcon = itemView.findViewById(R.id.moneyTypeIcon);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                List<CryptoItem> filteredList = new ArrayList<>();

                if (TextUtils.isEmpty(charSequence)) {
                    // If the search query is empty, return the original list
                    filteredList.addAll(cryptoItemList);
                } else {
                    String query = charSequence.toString().toLowerCase().trim();

                    // Filter the items based on the query
                    for (CryptoItem cryptoItem : cryptoItemList) {
                        if (cryptoItem.getName().toLowerCase().contains(query) ||
                                cryptoItem.getSymbol().toLowerCase().contains(query)) {
                            filteredList.add(cryptoItem);
                        }
                    }
                }

                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCryptoItemList.clear();
                filteredCryptoItemList.addAll((List<CryptoItem>) filterResults.values);
                notifyDataSetChanged(); // Notify adapter that data has changed
            }
        };
    }
}
