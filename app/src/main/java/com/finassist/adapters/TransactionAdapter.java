package com.finassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.classes.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvAmount, tvDescription, tvDate, tvCategory, tvAccount;
        public ImageView ivType;

        public ViewHolder(View v) {
            super(v);

            tvAmount = v.findViewById(R.id.tv_amount);
            tvDescription = v.findViewById(R.id.tv_description);
            tvDate = v.findViewById(R.id.tv_date);
            tvCategory = v.findViewById(R.id.tv_category);
            tvAccount = v.findViewById(R.id.tv_account);
            ivType = v.findViewById(R.id.iv_type);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(List<Transaction> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.mTextView.setText(mDataset[position]);

        Transaction transaction = mDataset.get(position);

        holder.tvAmount.setText(String.valueOf(transaction.getAmount()));
        holder.tvDescription.setText(transaction.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        holder.tvDate.setText(sdf.format(transaction.getDateTime()));
        holder.tvCategory.setText(transaction.getCategory().toString());

        // holder.tvAccount.setText(transaction.getFromAcc().getName()); TODO


        // holder.ivType.setImageDrawable(); TODO


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
