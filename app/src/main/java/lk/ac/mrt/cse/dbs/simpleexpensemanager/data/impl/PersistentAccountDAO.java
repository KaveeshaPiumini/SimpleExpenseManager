package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DatabaseHelper implements AccountDAO {
    private List<String> accountNos;
    private List<Account> accounts;

    public PersistentAccountDAO(@Nullable Context context) {

        super(context);
        this.accountNos = new ArrayList<String>();
        this.accounts = new ArrayList<Account>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        this.accountNos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select accountNo from accounts",null);

        if(cursor.moveToFirst()){
            do{
                accountNos.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        return accountNos;
    }

    @Override
    public List<Account> getAccountsList() {
        this.accounts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from accounts",null);

        if(cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(1);
                String bankName = cursor.getString(2);
                String accountHolderName = cursor.getString(3);
                double balance = cursor.getDouble(4);

                Account newAccount = new Account(accountNo,bankName,accountHolderName,balance);
                accounts.add(newAccount);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from accounts where accountNo= '"+ accountNo + "' ;",null);


        String bankName = cursor.getString(2);
        String accountHolderName = cursor.getString(3);
        double balance = cursor.getDouble(4);

        Account account = new Account(accountNo,bankName,accountHolderName,balance);

        cursor.close();
        db.close();

        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        this.accountNos = getAccountNumbersList();

        if(!accountNos.contains(account.getAccountNo())) {

            cv.put("accountNo", account.getAccountNo());
            cv.put("bankName", account.getBankName());
            cv.put("accountHolderName", account.getAccountHolderName());
            cv.put("balance", account.getBalance());

            db.insert("accounts", null, cv);
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from accounts where accountNo = '"+ accountNo +"' ;");

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select balance from accounts where accountNo= '"+ accountNo + "' ;",null);

        cursor.moveToFirst();
        double balance = cursor.getDouble(0);

        switch (expenseType){
            case INCOME:
                balance += amount;
                break;
            case EXPENSE:
                balance -= amount;
                break;
            default:
                break;
        }

        db.execSQL("update accounts set balance = '"+ balance +"' where accountNo= '"+ accountNo + "' ;");

        cursor.close();
        db.close();

    }
}
