package com.finassist.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.data.Account;


public class AccountCheckBox extends LinearLayout {
	CheckBox checkBox;
	TextView tvAccountName, tvAccountType, tvBalance;

	Account account;

	public AccountCheckBox(Context context) {
		this(context, null, 0);
	}

	public AccountCheckBox(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AccountCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		LayoutInflater.from(context).inflate(R.layout.account_checkbox, this, true);

		checkBox = findViewById(R.id.checkBox);

		tvAccountName = findViewById(R.id.tvAccountName);
		tvAccountType = findViewById(R.id.tvAccountType);
		tvBalance = findViewById(R.id.tvBalance);
	}

	public void setAccount(Account account) {
		this.account = account;

		tvAccountName.setText(account.getName());
		tvAccountType.setText(Account.accountTypeLabels[account.getType()]);
		tvBalance.setText(account.getBalance() + " kn");

		if(account.getBalance() > 0) {
			tvBalance.setTextColor(
					Resources.getSystem().getColor(android.R.color.holo_green_dark));
		}
		else {
			tvBalance.setTextColor(
					Resources.getSystem().getColor(android.R.color.holo_red_dark));
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
		checkBox.setOnCheckedChangeListener(listener);
	}

	public boolean isChecked() {
		return checkBox.isChecked();
	}

}
