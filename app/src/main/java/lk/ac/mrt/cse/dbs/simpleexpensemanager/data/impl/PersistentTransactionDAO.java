package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DatabaseHelper implements TransactionDAO {
    private List<Transaction> transactions;

    public PersistentTransactionDAO(Context context) {
        super(context);
        this.transactions = new ArrayList<Transaction>();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy");

        cv.put("date",simpleDateFormat.format(date));
        cv.put("accountNo",accountNo);
        cv.put("expenseType",expenseType.toString());
        cv.put("amount",amount);

        db.insert("transactions",null,cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        this.transactions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from transactions",null);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy");
        if(cursor.moveToFirst()){
            do{

                try {
                    Date date = simpleDateFormat.parse(cursor.getString(1));
                    String accountNo = cursor.getString(2);
                    ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(3).toUpperCase());
                    double amount = cursor.getDouble(4);

                    Transaction newTransaction = new Transaction(date,accountNo,expenseType,amount);
                    transactions.add(newTransaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        this.transactions = getAllTransactionLogs();
        int size = transactions.size();
        if( size > limit){
            return transactions.subList(size - limit, size);
        }else{
            return transactions;
        }
    }
}