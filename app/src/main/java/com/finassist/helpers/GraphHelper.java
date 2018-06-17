package com.finassist.helpers;

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
	public static void graphSpotlightTransactions(LineChart chart, List<Transaction> transactionList) {
		List<Entry> entriesIncome = new ArrayList<>();
		List<Entry> entriesExpenditure = new ArrayList<>();

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		double maxYValue = Double.MIN_VALUE;

		for(Transaction transaction : transactionList) {
			if(transaction.getAmount() > maxYValue) {
				maxYValue = transaction.getAmount();
			}

			if(transaction.getType() == Transaction.TYPE_INCOME) {
				entriesIncome.add(new Entry(
						(float)transaction.getDateTime().getTime(),
						(float)transaction.getAmount()));
			}
			else if(transaction.getType() == Transaction.TYPE_EXPENDITURE) {
				entriesExpenditure.add(new Entry(
						(float)transaction.getDateTime().getTime(),
						(float)transaction.getAmount()));
			}
		}

		if(entriesIncome.size() > 0) {
			LineDataSet dataSetIncome = new LineDataSet(
					entriesIncome, "Income transactions");
			dataSetIncome.setValueTextSize(Utils.convertDpToPixel(4));
			dataSetIncome.setLineWidth(Utils.convertDpToPixel(1));
			dataSets.add(dataSetIncome);
		}

		if(entriesExpenditure.size() > 0) {
			LineDataSet dataSetExpenditure = new LineDataSet(
					entriesExpenditure, "Expenditure transactions");
			dataSetExpenditure.setValueTextSize(Utils.convertDpToPixel(4));
			dataSetExpenditure.setLineWidth(Utils.convertDpToPixel(1));
			dataSets.add(dataSetExpenditure);
		}

		LineData lineData = new LineData(dataSets);

		chart.setData(lineData);

		chart.getXAxis().setDrawLabels(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.getXAxis().setDrawGridLines(false);

		chart.getAxisLeft().setTextSize(Utils.convertDpToPixel(5));
		chart.getAxisLeft().setAxisMaximum((float)maxYValue*2);
		chart.getAxisLeft().setDrawGridLines(false);

		chart.getAxisRight().setEnabled(false);

		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(Utils.convertDpToPixel(5));

		chart.invalidate();
	}
}
