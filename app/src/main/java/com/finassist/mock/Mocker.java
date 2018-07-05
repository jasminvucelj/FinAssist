package com.finassist.mock;

import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.data.Transaction;
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

        List<Date> dates = new ArrayList<>();
        try {
        	// region dates5-1

            dates.add(sdf.parse("01.05.2018. 12:52:55")); // 0
            dates.add(sdf.parse("01.05.2018. 17:23:28"));
			dates.add(sdf.parse("02.05.2018. 09:45:03"));
			dates.add(sdf.parse("02.05.2018. 10:01:49"));
			dates.add(sdf.parse("03.05.2018. 09:12:09"));

			// endregion

			// region dates5-2

			dates.add(sdf.parse("04.05.2018. 08:21:33")); // 5
			dates.add(sdf.parse("04.05.2018. 15:40:43"));
			dates.add(sdf.parse("05.05.2018. 18:07:34"));
			dates.add(sdf.parse("06.05.2018. 10:39:29"));
			dates.add(sdf.parse("06.05.2018. 11:12:13"));

			// endregion

			// region dates5-3

			dates.add(sdf.parse("06.05.2018. 19:44:56")); // 10
			dates.add(sdf.parse("08.05.2018. 11:59:41"));
			dates.add(sdf.parse("08.05.2018. 13:25:12"));
			dates.add(sdf.parse("10.05.2018. 09:00:29"));
			dates.add(sdf.parse("11.05.2018. 16:57:49"));

			// endregion

			// region dates5-4

			dates.add(sdf.parse("11.05.2018. 16:59:14")); // 15
			dates.add(sdf.parse("11.05.2018. 17:00:36"));
			dates.add(sdf.parse("11.05.2018. 17:01:58"));
			dates.add(sdf.parse("11.05.2018. 17:02:40"));
			dates.add(sdf.parse("13.05.2018. 16:16:16"));

			// endregion

			// region dates5-5

			dates.add(sdf.parse("13.05.2018. 20:26:44")); // 20
			dates.add(sdf.parse("15.05.2018. 12:03:24"));
			dates.add(sdf.parse("18.05.2018. 08:09:48"));
			dates.add(sdf.parse("21.05.2018. 11:18:53"));
			dates.add(sdf.parse("21.05.2018. 12:18:35"));

			// endregion

			// region dates5-6

			dates.add(sdf.parse("23.05.2018. 07:43:15")); // 25
			dates.add(sdf.parse("24.05.2018. 07:55:27"));
			dates.add(sdf.parse("24.05.2018. 07:56:56"));
			dates.add(sdf.parse("24.05.2018. 18:05:06"));
			dates.add(sdf.parse("25.05.2018. 13:12:40"));

			// endregion

			// region dates5-7

			dates.add(sdf.parse("26.05.2018. 14:42:21")); // 30
			dates.add(sdf.parse("26.05.2018. 15:53:31"));
			dates.add(sdf.parse("28.05.2018. 08:23:53"));
			dates.add(sdf.parse("28.05.2018. 08:51:37"));
			dates.add(sdf.parse("28.05.2018. 13:01:28"));

			// endregion

			// region dates5-8

			dates.add(sdf.parse("30.05.2018. 07:24:08")); // 35
			dates.add(sdf.parse("30.05.2018. 20:42:00"));
			dates.add(sdf.parse("30.05.2018. 21:04:18"));
			dates.add(sdf.parse("31.05.2018. 18:17:16"));
			dates.add(sdf.parse("31.05.2018. 20:02:20"));

			// endregion

			// region dates6-1

			dates.add(sdf.parse("01.06.2018. 11:52:44")); // 40
			dates.add(sdf.parse("01.06.2018. 17:23:28"));
			dates.add(sdf.parse("01.06.2018. 17:45:17"));
			dates.add(sdf.parse("02.06.2018. 10:01:49"));
			dates.add(sdf.parse("02.06.2018. 18:12:09"));

			// endregion

			// region dates6-2

			dates.add(sdf.parse("03.06.2018. 11:57:52")); // 45
			dates.add(sdf.parse("04.06.2018. 14:33:28"));
			dates.add(sdf.parse("05.06.2018. 17:45:55"));
			dates.add(sdf.parse("07.06.2018. 10:01:49"));
			dates.add(sdf.parse("07.06.2018. 11:12:09"));

			// endregion

			// region dates6-3

			dates.add(sdf.parse("08.06.2018. 19:00:29")); // 50
			dates.add(sdf.parse("09.06.2018. 11:59:41"));
			dates.add(sdf.parse("10.06.2018. 16:57:49"));
			dates.add(sdf.parse("12.06.2018. 07:57:49"));
			dates.add(sdf.parse("12.06.2018. 08:11:12"));

			// endregion

			// region dates6-4

			dates.add(sdf.parse("13.06.2018. 20:02:22")); // 55
			dates.add(sdf.parse("16.06.2018. 07:49:59"));
			dates.add(sdf.parse("16.06.2018. 08:01:37"));
			dates.add(sdf.parse("16.06.2018. 11:54:10"));
			dates.add(sdf.parse("17.06.2018. 12:14:39"));

			// endregion

			// region dates6-5

			dates.add(sdf.parse("18.06.2018. 09:06:03")); // 60
			dates.add(sdf.parse("18.06.2018. 13:45:11"));
			dates.add(sdf.parse("18.06.2018. 16:01:37"));
			dates.add(sdf.parse("18.06.2018. 17:54:10"));
			dates.add(sdf.parse("20.06.2018. 20:14:25"));

			// endregion

			// region dates6-6
			dates.add(sdf.parse("23.06.2018. 12:06:21")); // 65
			dates.add(sdf.parse("23.06.2018. 16:18:20"));
			dates.add(sdf.parse("24.06.2018. 10:11:01"));
			dates.add(sdf.parse("25.06.2018. 07:21:19"));
			dates.add(sdf.parse("26.06.2018. 09:44:31"));

			// endregion

			// region dates6-7
			dates.add(sdf.parse("27.06.2018. 08:24:51")); // 70
			dates.add(sdf.parse("28.06.2018. 10:29:18"));
			dates.add(sdf.parse("28.06.2018. 13:13:56"));
			dates.add(sdf.parse("30.06.2018. 11:49:30"));
			dates.add(sdf.parse("30.06.2018. 17:56:56"));

			// endregion


		} catch (ParseException e) {
            e.printStackTrace();
        }

		String oId1 = FirebaseDatabaseHelper.saveAccount(account1, currentUserId);
        account1.setId(oId1);
		String oId2 = FirebaseDatabaseHelper.saveAccount(account2, currentUserId);
		account2.setId(oId2);
		String oId3 = FirebaseDatabaseHelper.saveAccount(account3, currentUserId);
		account3.setId(oId3);

		// region transactions5-1

        transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(0), 175,
				transactionCategories.get(39), "Lunch at work (whole week)")); // 0
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(1), 1200,
				transactionCategories.get(90), "Car repairs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(2), 180,
				transactionCategories.get(21), "Tuition for kids"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account2, account1, dates.get(3), 500,
				transactionCategories.get(0), "Transfer"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates.get(4), 800,
				transactionCategories.get(4), "Bonus at work"));

		// endregion

		// region transactions5-2

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(5), 120,
				transactionCategories.get(19), "Teambuilding trip costs")); // 5
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(6), 150,
				transactionCategories.get(52), "Plumbing repairs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(7), 80,
				transactionCategories.get(38), "Drink after work"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account1, account3, dates.get(8), 800,
				transactionCategories.get(1), "Withdrawal"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(9), 220,
				transactionCategories.get(63), "Zoo with family"));

		// endregion

		// region transactions5-3

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(10), 300,
				transactionCategories.get(36), "Dinner with family")); // 10
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(11), 160,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(12), 60,
				transactionCategories.get(57), "Buying a new book"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates.get(13), 9000,
				transactionCategories.get(3), "Salary"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(14), 280,
				transactionCategories.get(80), "Power bill"));

		// endregion

		// region transactions5-4

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(15), 135,
				transactionCategories.get(78), "Water bill")); // 15
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(16), 240,
				transactionCategories.get(83), "Phone & mobile phone monthly subscription"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(17), 250,
				transactionCategories.get(84), "Internet bill"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(18), 115,
				transactionCategories.get(85), "Garbage/recycling bill"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(19), 45,
				transactionCategories.get(47), "Painkillers"));

		// endregion

		// region transactions5-5

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(20), 75,
				transactionCategories.get(61), "Watching football game")); // 20
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(21), 175,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(22), 420,
				transactionCategories.get(36), "Monthly groceries"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates.get(23), 500,
				transactionCategories.get(10), "Repaid debt from friend"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(24), 200,
				transactionCategories.get(39), "Lunch at work (whole week)"));

		// endregion

		// region transactions5-6

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(25), 185,
				transactionCategories.get(23), "School supplies for kids")); // 25
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account1, account3, dates.get(26), 600,
				transactionCategories.get(1),"Withdrawal"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account2, account1, dates.get(27), 2000,
				transactionCategories.get(0),"Transfer"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(28), 320,
				transactionCategories.get(89), "Car refuel"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(29), 250,
				transactionCategories.get(93), "Charity"));

		// endregion

		// region transactions5-7

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(30), 125,
				transactionCategories.get(67), "Dog food")); // 30
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(31), 150,
				transactionCategories.get(69), "Taking dog to the vet"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(32), 180,
				transactionCategories.get(43), "Health insurance"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(33), 450,
				transactionCategories.get(19), "Monthly travelling costs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(34), 140,
				transactionCategories.get(39), "Lunch at work (whole week)"));

		// endregion

		// region transactions5-8

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(35), 90,
				transactionCategories.get(33), "Buying some food")); // 35
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(36), 275,
				transactionCategories.get(51), "Dinner with relatives"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account1, dates.get(37), 1000,
				transactionCategories.get(6), "Gift from relatives"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(38), 1350,
				transactionCategories.get(53), "Buying new sofa"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(39), 100,
				transactionCategories.get(38), "Drink with friend"));

		//endregion

		// region transactions6-1

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(40), 70,
				transactionCategories.get(52), "New light bulbs")); // 40
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(41), 90,
				transactionCategories.get(52), "Heating repairs"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(42), 80,
				transactionCategories.get(47), "Some medicine for kids"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account1, account3, dates.get(43), 300,
				transactionCategories.get(1),"Withdrawal"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(44), 240,
				transactionCategories.get(61), "Watching football game"));

		// endregion

		// region transactions6-2

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(45), 150,
				transactionCategories.get(39), "Lunch at work (whole week)")); // 45
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(46), 500,
				transactionCategories.get(72), "Retirement fund"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates.get(47), 600,
				transactionCategories.get(5), "Sold old phone"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(48), 180,
				transactionCategories.get(21), "Tuition for kids"));
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account2, account1, dates.get(49), 500,
				transactionCategories.get(0), "Transfer"));

		// endregion

		// region transactions6-3

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(50), 60,
				transactionCategories.get(38), "Drink with friends")); // 50
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(51), 135,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates.get(52), 9000,
				transactionCategories.get(3), "Salary"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(53), 450,
				transactionCategories.get(36), "Monthly groceries"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(54), 280,
				transactionCategories.get(80), "Power bill"));

		// endregion

		// region transactions6-4

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(55), 300,
				transactionCategories.get(36), "Dinner with family")); // 55
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(56), 240,
				transactionCategories.get(83), "Mobile phone monthly subscription"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(57), 250,
				transactionCategories.get(84), "Phone & internet bill"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(58), 170,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(59), 100,
				transactionCategories.get(47), "Painkillers"));

		// endregion

		// region transactions6-5

		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account2, account1, dates.get(60), 2500,
				transactionCategories.get(0), "Transfer")); // 60
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(61), 260,
				transactionCategories.get(89), "Car refuel"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(62), 110,
				transactionCategories.get(67), "Dog food"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(63), 80,
				transactionCategories.get(69), "Taking dog to the vet"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(64), 400,
				transactionCategories.get(36), "Dinner with coworkers"));

		// endregion

		// region transactions6-6

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(65), 195,
				transactionCategories.get(39), "Lunch at work (whole week)")); // 65
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(66), 130,
				transactionCategories.get(52), "Electrical repairs"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account1, dates.get(67), 700,
				transactionCategories.get(6), "Gift from parents"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(68), 65,
				transactionCategories.get(23), "School supplies for kids"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account2, dummyAccount, dates.get(69), 100,
				transactionCategories.get(93), "Charity"));

		// endregion

		// region transactions6-7

		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(70), 145,
				transactionCategories.get(85), "Garbage/recycling bill")); // 70
		transactionList.add(new Transaction(Transaction.TYPE_TRANSFER,
				account1, account3, dates.get(71), 500,
				transactionCategories.get(1), "Withdrawal"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account3, dummyAccount, dates.get(72), 500,
				transactionCategories.get(29), "Brother's wedding"));
		transactionList.add(new Transaction(Transaction.TYPE_EXPENDITURE,
				account1, dummyAccount, dates.get(73), 165,
				transactionCategories.get(39), "Lunch at work (whole week)"));
		transactionList.add(new Transaction(Transaction.TYPE_INCOME,
				dummyAccount, account2, dates.get(74), 300,
				transactionCategories.get(10), "Repaid debt from friend"));

		// endregion

		for (Transaction transaction : transactionList) {
			Account tempFromAcc = transaction.getFromAcc();
			Account tempToAcc = transaction.getToAcc();

			if (transaction.getType() == Transaction.TYPE_INCOME
					|| transaction.getType() == Transaction.TYPE_TRANSFER) {
				tempToAcc.processTransaction(Account.GAIN, transaction.getAmount());
				FirebaseDatabaseHelper.saveAccount(tempToAcc, currentUserId, tempToAcc.getId());
			}
			if (transaction.getType() == Transaction.TYPE_EXPENDITURE
					|| transaction.getType() == Transaction.TYPE_TRANSFER) {
				tempFromAcc.processTransaction(Account.LOSS, transaction.getAmount());
				FirebaseDatabaseHelper.saveAccount(tempFromAcc, currentUserId, tempFromAcc.getId());
			}

			FirebaseDatabaseHelper.saveTransaction(transaction, currentUserId);
		}

    }

}
