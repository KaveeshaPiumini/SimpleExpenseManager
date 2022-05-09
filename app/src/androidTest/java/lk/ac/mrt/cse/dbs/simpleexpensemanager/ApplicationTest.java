/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentDemoExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{

    private static ExpenseManager expenseManager;

    @BeforeClass
    public static void setupSimpleExpenseManager(){

        try {
            expenseManager = new PersistentDemoExpenseManager(ApplicationProvider.getApplicationContext());
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addAccountTest (){
        expenseManager.addAccount("11111","NSB","PK",9000.0);
        assertTrue(expenseManager.getAccountNumbersList().contains("11111"));
    }

    @Test
    public void addTransactionTest(){
        int noOfTransactions = expenseManager.getTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("78945Z", 1, 1, 2000, ExpenseType.EXPENSE, "1000.0");
            assertEquals(noOfTransactions +1,expenseManager.getTransactionLogs().size());
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }

}