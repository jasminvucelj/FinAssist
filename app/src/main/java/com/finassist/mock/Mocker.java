package com.finassist.mock;

import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.finassist.helpers.FirebaseDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.finassist.helpers.FirebaseDatabaseHelper.dummyAccount;
import static com.finassist.helpers.FirebaseDatabaseHelper.transactionCategories;


public class Mocker {

    public static void generateMockUserData(String currentUserId) {

        Account account1 = new AccountWithBalance("Zaba giro account", Account.TYPE_GIRO_ACCOUNT ,7000);
        Account account2 = new AccountWithBalance("Zaba current account", Account.TYPE_CURRENT_ACCOUNT, 2500);
        Account account3 = new CashAccount("Cash");

		List<Transaction> transactionList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd.MM.yyyy. HH:mm:ss",
                Locale.getDefault());

        List<Date> dates1 = new ArrayList<>();
        try {
        	// 5.

            dates1.add(sdf.parse("01.05.2018. 12:52:55")); // 0
            dates1.add(sdf.parse("01.05.2018. 17:23:28"));
			dates1.add(sdf.parse("02.05.2018. 09:45:03"));
			dates1.add(sdf.parse("02.05.2018. 10:01:49"));
			dates1.add(sdf.parse("03.05.2018. 09:12:09"));

			dates1.add(sdf.parse("04.05.2018. 08:21:33")); // 5
			dates1.add(sdf.parse("04.05.2018. 15:40:43"));
			dates1.add(sdf.parse("05.05.2018. 18:07:34"));
			dates1.add(sdf.parse("06.05.2018. 10:39:29"));
			dates1.add(sdf.parse("06.05.2018. 11:12:13"));

			dates1.add(sdf.parse("06.05.2018. 19:44:56")); // 10
			dates1.add(sdf.parse("08.05.2018. 11:59:41"));
			dates1.add(sdf.parse("08.05.2018. 13:25:12"));
			dates1.add(sdf.parse("10.05.2018. 09:00:29"));
			dates1.add(sdf.parse("11.05.2018. 16:57:49"));

			dates1.add(sdf.parse("11.05.2018. 16:59:14")); // 15
			dates1.add(sdf.parse("11.05.2018. 17:00:36"));
			dates1.add(sdf.parse("11.05.2018. 17:01:58"));
			dates1.add(sdf.parse("11.05.2018. 17:02:40"));
			dates1.add(sdf.parse("13.05.2018. 16:16:16"));

			dates1.add(sdf.parse("13.05.2018. 20:26:44")); // 20
			dates1.add(sdf.parse("15.05.2018. 12:03:24"));
			dates1.add(sdf.parse("18.05.2018. 08:09:48"));
			dates1.add(sdf.parse("21.05.2018. 11:18:53"));
			dates1.add(sdf.parse("21.05.2018. 12:18:35"));

			dates1.add(sdf.parse("23.05.2018. 07:43:15")); // 25
			dates1.add(sdf.parse("24.05.2018. 07:55:27"));
			dates1.add(sdf.parse("24.05.2018. 07:56:56"));
			dates1.add(sdf.parse("24.05.2018. 18:05:06"));
			dates1.add(sdf.parse("25.05.2018. 13:12:40"));

			dates1.add(sdf.parse("26.05.2018. 14:42:21")); // 30
			dates1.add(sdf.parse("26.05.2018. 15:53:31"));
			dates1.add(sdf.parse("28.05.2018. 08:23:53"));
			dates1.add(sdf.parse("28.05.2018. 08:51:37"));
			dates1.add(sdf.parse("28.05.2018. 13:01:28"));

			dates1.add(sdf.parse("30.05.2018. 07:24:08")); // 35
			dates1.add(sdf.parse("30.05.2018. 20:42:00"));
			dates1.add(sdf.parse("30.05.2018. 21:04:18"));
			dates1.add(sdf.parse("31.05.2018. 18:17:16"));
			dates1.add(sdf.parse("31.05.2018. 20:02:20"));


			// 6.


		} catch (ParseException e) {
            e.printStackTrace();
        }

		FirebaseDatabaseHelper.saveAccount(account1, currentUserId);
		FirebaseDatabaseHelper.saveAccount(account2, currentUserId);
		FirebaseDatabaseHelper.saveAccount(account3, currentUserId);

		// 5.

        transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(0), 175,
				transactionCategories.get(39), "Lunch at work (whole week)")); // 0
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(1), 1200,
				transactionCategories.get(90), "Car repairs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(2), 180,
				transactionCategories.get(21), "Tuition for kids"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account2, account1, dates1.get(3), 500,
				transactionCategories.get(0), "Transfer"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates1.get(4), 800,
				transactionCategories.get(4), "Bonus at work"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(5), 120,
				transactionCategories.get(19), "Teambuilding trip costs")); // 5
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(6), 150,
				transactionCategories.get(52), "Plumbing repairs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(7), 80,
				transactionCategories.get(38), "Drink after work"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account1, account3, dates1.get(8), 800,
				transactionCategories.get(1), "Withdrawal"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(9), 220,
				transactionCategories.get(63), "Zoo with family"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(10), 300,
				transactionCategories.get(36), "Dinner with family")); // 10
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(11), 160,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(12), 60,
				transactionCategories.get(57), "Buying a new book"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates1.get(13), 8500,
				transactionCategories.get(3), "Salary"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(14), 280,
				transactionCategories.get(80), "Power bill"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(15), 135,
				transactionCategories.get(78), "Water bill")); // 15
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(16), 240,
				transactionCategories.get(83), "Phone & mobile phone monthly subscription"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(17), 250,
				transactionCategories.get(84), "Internet bill"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(18), 115,
				transactionCategories.get(85), "Garbage/recycling bill"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(19), 45,
				transactionCategories.get(47), "Painkillers"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(20), 75,
				transactionCategories.get(61), "Watching football game")); // 20
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(21), 175,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(22), 420,
				transactionCategories.get(36), "Monthly groceries"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates1.get(23), 500,
				transactionCategories.get(10), "Repaid debt from friend"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(24), 200,
				transactionCategories.get(39), "Lunch at work (whole week)"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(25), 185,
				transactionCategories.get(23), "School supplies for kids")); // 25
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account1, account3, dates1.get(26), 600,
				transactionCategories.get(1),"Withdrawal"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account2, account1, dates1.get(27), 2000,
				transactionCategories.get(0),"Transfer"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(28), 320,
				transactionCategories.get(89), "Car refuel"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(29), 250,
				transactionCategories.get(93), "Charity"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(30), 125,
				transactionCategories.get(67), "Dog food")); // 30
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(31), 150,
				transactionCategories.get(69), "Taking dog to the vet"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates1.get(32), 180,
				transactionCategories.get(43), "Health insurance"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(33), 450,
				transactionCategories.get(19), "Monthly travelling costs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(34), 140,
				transactionCategories.get(39), "Lunch at work (whole week)"));

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(35), 90,
				transactionCategories.get(33), "Buying some food"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(36), 275,
				transactionCategories.get(51), "Dinner with relatives"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account1, dates1.get(37), 1000,
				transactionCategories.get(6), "Gift from relatives"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates1.get(38), 1350,
				transactionCategories.get(53), "Buying new sofa"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates1.get(39), 100,
				transactionCategories.get(38), "Drink with friend"));

		// 6.



		for (Transaction t : transactionList) {
			FirebaseDatabaseHelper.saveTransaction(t, currentUserId);
		}

    }

}
