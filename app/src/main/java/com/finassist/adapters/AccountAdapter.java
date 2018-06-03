package com.finassist.adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.data.Account;

import java.util.List;


public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private final List<Account> accountList;
    private final AccountAdapterOnClickHandler clickHandler;

    public interface AccountAdapterOnClickHandler {
        void onClick(Account account);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public AccountAdapter(List<Account> dataset, AccountAdapterOnClickHandler clickHandler) {
        accountList = dataset;
        this.clickHandler = clickHandler;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent,
												int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_list_item, parent, false);

        return new AccountViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Account account = accountList.get(position);

		holder.tvName.setText(account.getName());
		holder.tvDescription.setText(account.getDescription());

		if(holder.tvDescription.getText() == null
				|| holder.tvDescription.getText().toString().trim().equals("")) {
			holder.tvDescription.setVisibility(View.GONE);
		}

		holder.tvBalance.setText(String.format("%.2f", account.getBalance()) + " kn");
		if(account.getBalance() > 0) {
			holder.tvBalance.setTextColor(
					Resources.getSystem().getColor(android.R.color.holo_green_dark));
		}
		else {
			holder.tvBalance.setTextColor(
					Resources.getSystem().getColor(android.R.color.holo_red_dark));
		}
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return accountList.size();
    }


	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	class AccountViewHolder extends RecyclerView.ViewHolder {

    	TextView tvName, tvDescription, tvBalance;

		@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
		AccountViewHolder(View itemView) {
			super(itemView);
			itemView.setClickable(true);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Account account = accountList.get(getAdapterPosition());
					clickHandler.onClick(account);
				}
			});

			tvBalance = itemView.findViewById(R.id.tvBalance);
			tvName = itemView.findViewById(R.id.tvName);
			tvDescription = itemView.findViewById(R.id.tvDescription);
		}

	}
}
