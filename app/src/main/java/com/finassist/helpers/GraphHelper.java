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
	public static void graphSpotlightTransactions(LineChart chart, List<Transaction> transactionList) {
		List<Entry> entriesIncome = new ArrayList<>();
		List<Entry> entriesExpenditure = new ArrayList<>();

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		double maxYValue = Double.MIN_VALUE;

		for (Transaction transaction : transactionList) {
			if (transaction.getAmount() > maxYValue) {
				maxYValue = transaction.getAmount();
			}

			if (transaction.getType() == Transaction.TYPE_INCOME) {
				entriesIncome.add(new Entry(
						(float) transaction.getDateTime().getTime(),
						(float) transaction.getAmount()));
			} else if (transaction.getType() == Transaction.TYPE_EXPENDITURE) {
				entriesExpenditure.add(new Entry(
						(float) transaction.getDateTime().getTime(),
						(float) transaction.getAmount()));
			}
		}

		if (entriesIncome.size() > 0) {
			LineDataSet dataSetIncome = new LineDataSet(
					entriesIncome, "Income");
			dataSetIncome.setValueTextSize(Utils.convertDpToPixel(4));
			dataSetIncome.setLineWidth(Utils.convertDpToPixel(2));
			dataSetIncome.setColor(Resources.getSystem().getColor(android.R.color.holo_green_dark));
			dataSetIncome.setCircleColor(Resources.getSystem().getColor(android.R.color.holo_green_dark));
			dataSets.add(dataSetIncome);
		}

		if (entriesExpenditure.size() > 0) {
			LineDataSet dataSetExpenditure = new LineDataSet(
					entriesExpenditure, "Expenditure");
			dataSetExpenditure.setValueTextSize(Utils.convertDpToPixel(4));
			dataSetExpenditure.setLineWidth(Utils.convertDpToPixel(2));
			dataSetExpenditure.setColor(Resources.getSystem().getColor(android.R.color.holo_red_dark));
			dataSetExpenditure.setCircleColor(Resources.getSystem().getColor(android.R.color.holo_red_dark));
			dataSets.add(dataSetExpenditure);
		}

		LineData lineData = new LineData(dataSets);

		chart.setData(lineData);

		chart.getXAxis().setDrawLabels(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.getXAxis().setDrawGridLines(false);

		chart.getAxisLeft().setTextSize(Utils.convertDpToPixel(5));
		chart.getAxisLeft().setAxisMaximum((float) maxYValue * 1.5f);
		chart.getAxisLeft().setDrawGridLines(false);

		chart.getAxisRight().setEnabled(false);

		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(Utils.convertDpToPixel(5));

		chart.invalidate();
	}

	public static void statisticsGraph1(LineChart chart, List<Transaction> transactionList) {
		List<Entry> entries = new ArrayList<>();

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		entries.add(new Entry(2f, 230f));
		entries.add(new Entry(4f, 30f));
		entries.add(new Entry(9f, 450f));
		entries.add(new Entry(14f, 320f));
		entries.add(new Entry(18f, 140f));
		entries.add(new Entry(22f, 1600f));
		entries.add(new Entry(24f, 1350f));
		entries.add(new Entry(26f, 1110f));
		entries.add(new Entry(27f, 1000f));
		entries.add(new Entry(30f, 1200f));

		LineDataSet dataSet = new LineDataSet(
				entries, "Account balance");
		dataSet.setValueTextSize(Utils.convertDpToPixel(4));
		dataSet.setLineWidth(Utils.convertDpToPixel(2));
		dataSet.setColor(Resources.getSystem().getColor(android.R.color.holo_orange_dark));
		dataSet.setCircleColor(Resources.getSystem().getColor(android.R.color.holo_orange_dark));
		dataSets.add(dataSet);

		LineData lineData = new LineData(dataSets);

		chart.setData(lineData);

		chart.getXAxis().setDrawLabels(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.getXAxis().setDrawGridLines(false);

		chart.getAxisLeft().setTextSize(Utils.convertDpToPixel(5));
		// chart.getAxisLeft().setAxisMaximum((float)maxYValue*1.5f);
		chart.getAxisLeft().setDrawGridLines(false);

		chart.getAxisRight().setEnabled(false);

		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(Utils.convertDpToPixel(5));

		chart.invalidate();
	}

	public static void statisticsGraph2(LineChart chart, List<Transaction> transactionList) {
		List<Entry> entries = new ArrayList<>();

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		entries.add(new Entry(1f, 1230f));
		entries.add(new Entry(4f, 1300f));
		entries.add(new Entry(6f, 940f));
		entries.add(new Entry(11f, 800f));
		entries.add(new Entry(15f, 350f));
		entries.add(new Entry(20f, 650f));
		entries.add(new Entry(21f, 750f));
		entries.add(new Entry(24f, 900f));
		entries.add(new Entry(28f, 500f));
		entries.add(new Entry(30f, 440f));

		LineDataSet dataSet = new LineDataSet(
				entries, "Account balance");
		dataSet.setValueTextSize(Utils.convertDpToPixel(4));
		dataSet.setLineWidth(Utils.convertDpToPixel(2));
		dataSet.setColor(Resources.getSystem().getColor(android.R.color.holo_blue_dark));
		dataSet.setCircleColor(Resources.getSystem().getColor(android.R.color.holo_blue_dark));
		dataSets.add(dataSet);

		LineData lineData = new LineData(dataSets);

		chart.setData(lineData);

		chart.getXAxis().setDrawLabels(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.getXAxis().setDrawGridLines(false);

		chart.getAxisLeft().setTextSize(Utils.convertDpToPixel(5));
		// chart.getAxisLeft().setAxisMaximum((float)maxYValue*1.5f);
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
		// TODO chart

	}
}

