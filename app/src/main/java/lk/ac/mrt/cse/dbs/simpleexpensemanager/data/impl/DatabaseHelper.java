package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "190331A.db", null,  1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String accountTableQuery = "create table accounts(accountNo text primary key, bankName text, accountHolderName text, balance real);";
        db.execSQL(accountTableQuery);

        String transactionTableQuery = "create table transactions(transactionId integer primary key autoincrement, date text, accountNo text, expenseType text, amount real, FOREIGN KEY (accountNo) REFERENCES accounts (accountNo) );";
        db.execSQL(transactionTableQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}