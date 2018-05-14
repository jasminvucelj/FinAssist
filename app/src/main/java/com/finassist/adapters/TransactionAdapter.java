package com.finassist.adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.data.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final List<Transaction> transactionList;
    private final TransactionAdapterOnClickHandler clickHandler;

    public interface TransactionAdapterOnClickHandler {
        void onClick(Transaction transaction);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(List<Transaction> dataset, TransactionAdapterOnClickHandler clickHandler) {
        transactionList = dataset;
        this.clickHandler = clickHandler;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent,
													int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_item, parent, false);

        return new TransactionViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Transaction transaction = transactionList.get(position);

        holder.tvAmount.setText(String.format("%.2f", transaction.getAmount()) + " kn");
        holder.tvDescription.setText(transaction.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        holder.tvDate.setText(sdf.format(transaction.getDateTime()));
        holder.tvCategory.setText(transaction.getCategory().toString());

        if (transaction.getType() == Transaction.TYPE_INCOME) {
			holder.tvAccount.setText(transaction.getToAcc().getName());
		}
		else if(transaction.getType() == Transaction.TYPE_EXPENDITURE) {
			holder.tvAccount.setText(transaction.getFromAcc().getName());
		}

		// TODO transfer

        // holder.ivType.setImageDrawable(); TODO

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transactionList.size();
    }


	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public class TransactionViewHolder extends RecyclerView.ViewHolder
			/* implements View.OnClickListener */ {

    	TextView tvAmount, tvDescription, tvDate, tvCategory, tvAccount;
		ImageView ivType;

		@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
		public TransactionViewHolder(View itemView) {
			super(itemView);
			itemView.setClickable(true);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Transaction transaction = transactionList.get(getAdapterPosition());
					clickHandler.onClick(transaction);
				}
			});

			tvAmount = itemView.findViewById(R.id.tv_amount);
			tvDescription = itemView.findViewById(R.id.tv_description);
			tvDate = itemView.findViewById(R.id.tv_date);
			tvCategory = itemView.findViewById(R.id.tv_category);
			tvAccount = itemView.findViewById(R.id.tv_account);
			ivType = itemView.findViewById(R.id.iv_type);

		}

		/*
		@Override
		public void onClick(View v) {
			Transaction transaction = transactionList.get(getAdapterPosition());
			clickHandler.onClick(transaction);
		}
		*/
	}
}
