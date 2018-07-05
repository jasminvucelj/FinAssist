package com.finassist.helpers;

import android.content.res.Resources;

import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GraphHelper {
	private static final int[] colors = new int[] {
			Resources.getSystem().getColor(android.R.color.holo_red_dark),
			Resources.getSystem().getColor(android.R.color.holo_blue_dark),
			Resources.getSystem().getColor(android.R.color.holo_green_dark),
			Resources.getSystem().getColor(android.R.color.holo_purple),
			Resources.getSystem().getColor(android.R.color.holo_orange_dark)
	};

	public static void graphSpotlightTransactions(LineChart chart, List<Transaction> transactionList) {
		List<Entry> entries = new ArrayList<>();

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		double maxYValue = Double.MIN_VALUE;

		for (Transaction transaction : transactionList) {
			double amount = transaction.getAmount();
			entries.add(new Entry(
					(float) transaction.getDateTime().getTime(),
					(float) amount));
			if (amount > maxYValue) {
				maxYValue = amount;
			}
		}

		if (entries.size() > 0) {
			LineDataSet dataSet = new LineDataSet(
					entries, "Most recent expenditures");
			dataSet.setValueTextSize(Utils.convertDpToPixel(5));
			dataSet.setLineWidth(Utils.convertDpToPixel(2));
			dataSet.setColor(colors[0]);
			dataSet.setCircleColor(colors[0]);
			dataSets.add(dataSet);
		}

		LineData lineData = new LineData(dataSets);

		chart.setData(lineData);

		chart.getXAxis().setDrawLabels(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.getXAxis().setDrawGridLines(false);
		chart.getXAxis().setTextSize(Utils.convertDpToPixel(4));

		chart.getAxisLeft().setTextSize(Utils.convertDpToPixel(5));
		chart.getAxisLeft().setAxisMaximum((float) maxYValue * 1.75f);
		chart.getAxisLeft().setDrawGridLines(false);

		chart.getAxisRight().setEnabled(false);

		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(Utils.convertDpToPixel(5));

		chart.invalidate();
	}

	public static void statisticsChart(LineChart chart,
									   List<Account> accountList,
									   List<Double[]> startEndValues,
									   List<List<Transaction>> accountTransactionLists) {

		try {
			ArrayList<ILineDataSet> dataSets = new ArrayList<>();

			//double maxYValue = Double.MIN_VALUE;

			if(accountList.size() <= 0 || accountTransactionLists.size() <= 0) {
				chart.invalidate();
				return;
			}

			for (int i = 0; i < accountList.size(); i++) {
				Account account = accountList.get(i);
				List<Entry> entries = new ArrayList<>();

				// add initial state of account
				double lastBalance = startEndValues.get(i)[0];
				//entries.add(new Entry(0f, (float) lastBalance));

				// add new entry for each transaction involving that account
				List<Transaction> currentAccountTransactionList = accountTransactionLists.get(i);
				if(currentAccountTransactionList.size() <= 0) {
					continue;
				}
				for (Transaction transaction : currentAccountTransactionList) {
					double balanceChange = 0;

					// balance increases
					if(transaction.getType() == Transaction.TYPE_INCOME
							|| (transaction.getType() == Transaction.TYPE_TRANSFER
							&& transaction.getToAcc().equals(account))) {
						balanceChange += transaction.getAmount();
					}

					// balance decreases
					if(transaction.getType() == Transaction.TYPE_EXPENDITURE
							|| (transaction.getType() == Transaction.TYPE_TRANSFER
							&& transaction.getFromAcc().equals(account))) {
						balanceChange -= transaction.getAmount();
					}

					lastBalance += balanceChange;
					entries.add(new Entry(
							(float) transaction.getDateTime().getTime(),
							(float) lastBalance));
					//if (lastBalance > maxYValue) {
					//	maxYValue = lastBalance;
					//}
				}

				LineDataSet dataSet = new LineDataSet(
						entries, account.getName());
				dataSet.setLineWidth(Utils.convertDpToPixel(2));
				dataSet.setDrawValues(false);
				dataSet.setColor(colors[i % colors.length]);
				dataSet.setCircleColor(colors[i % colors.length]);
				dataSets.add(dataSet);
			}

			LineData lineData = new LineData(dataSets);

			chart.setData(lineData);

			chart.getXAxis().setDrawLabels(false);
			chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
			chart.getXAxis().setDrawGridLines(false);
			chart.getXAxis().setTextSize(Utils.convertDpToPixel(4));

			chart.getAxisLeft().setTextSize(Utils.convertDpToPixel(5));
			//chart.getAxisLeft().setAxisMaximum((float) maxYValue * 1.25f);
			chart.getAxisLeft().setDrawGridLines(false);

			chart.getAxisRight().setEnabled(false);

			chart.getDescription().setEnabled(false);
			chart.getLegend().setTextSize(Utils.convertDpToPixel(5));

			chart.invalidate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

