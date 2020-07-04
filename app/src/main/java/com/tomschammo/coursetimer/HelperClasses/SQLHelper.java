package com.tomschammo.coursetimer.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.tomschammo.coursetimer.UserEvents.Session;

import java.util.ArrayList;


/**
 *
 * Handles everything related to the database.
 *
 */

public class SQLHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "timeStopper";
    private static final int DB_VERSION = 4;
    private static final String DATE = "date";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String TOTAL_TIME = "totalTime";
    private static final String CLIENT_NAME = "clientName";
    private static final String MONEY_EARNED = "moneyEarned";
    private static final String CURRENCY = "currency";
    private static final String PAY = "pay";




    public SQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates all the necessary tables if they don't already exist. <br>
     *
     * Initializes the pay to a standard pay of 10 and the currency to dollars ('$').
     *
     * @param db A instance of the {@link SQLiteDatabase} class to perform all the changes to the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate");
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS sessions ( id INTEGER PRIMARY KEY AUTOINCREMENT , %s VARCHAR(10) NOT NULL, %s BIGINT(8) NOT NULL , %s BIGINT(8) NOT NULL , %s BIGINT(8) NOT NULL, %s TEXT, %s FLOAT NOT NULL);", DATE, START_TIME, END_TIME, TOTAL_TIME, CLIENT_NAME, MONEY_EARNED));
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS settings ( %s VARCHAR(1) NOT NULL, %s FLOAT NOT NULL);", CURRENCY, PAY));
        db.execSQL("INSERT INTO settings (currency, pay) VALUES ('$', 10)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println(String.format("onUpgrade | %s | %s", oldVersion, newVersion));
        db.execSQL("DROP TABLE sessions");
        db.execSQL("DROP TABLE settings");
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS sessions ( id INTEGER PRIMARY KEY AUTOINCREMENT , %s VARCHAR(10) NOT NULL, %s BIGINT(8) NOT NULL , %s BIGINT(8) NOT NULL , %s BIGINT(8) NOT NULL, %s TEXT NOT NULL, %s FLOAT NOT NULL);", DATE, START_TIME, END_TIME, TOTAL_TIME, CLIENT_NAME, MONEY_EARNED));
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS settings (%s VARCHAR(1) NOT NULL, %s FLOAT NOT NULL);", CURRENCY, PAY));
        db.execSQL("INSERT INTO settings (currency, pay) VALUES ('$', 10)");
    }

    /**
     * Adds a new session to the database.
     *
     * @param date          The date of the session.
     * @param startTime     The time of the start of the session.
     * @param endTime       The time of the end of the session.
     * @param totalTime     The time the session lasted in format 'hh:mm:ss'.
     * @param client        The name of the client.
     * @param money         The money eared during that session.
     *
     * @return <code>true</code> if session was successfully added, <code>false</code> otherwise.
     */
    public boolean addSession(String date, String startTime, String endTime, String totalTime, String client, double money) {

        try (SQLiteDatabase database = this.getWritableDatabase()) {

            ContentValues values = new ContentValues();

            values.put(DATE, date);
            values.put(START_TIME, startTime);
            values.put(END_TIME, endTime);
            values.put(TOTAL_TIME, totalTime);
            values.put(CLIENT_NAME, client);
            values.put(MONEY_EARNED, money);

            return database.insertOrThrow("sessions", null, values) != -1;
        }

    }

    /**
     * Changes the pay received per hour. <br>
     *
     * @param newPay    Value of the new pay.
     */
    public void changePay(double newPay) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("UPDATE settings SET " + PAY + " =  '" + newPay + "' WHERE _rowid_ = 1");
    }


    /**
     * Changes the currency that is displayed. <br>
     *
     * Does not convert older values from old currency to new one. <br>
     *
     * Visual effect only.
     *
     * @param currency  New currency.
     */
    public void changeCurrency(String currency) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("UPDATE settings SET " + CURRENCY + " = '" + currency + "' WHERE _rowid_ = 1");
    }

    /**
     * Returns current pay or  null if none is set. <br>
     *
     * The pay shouldn't be null, though. <br>
     *
     * The default pay is 10 (unit) per hour.
     *
     * @return Double pay or null.
     */
    public Double getPay() {

        SQLiteDatabase database = this.getReadableDatabase();


        try (Cursor cursor = database.rawQuery("SELECT pay FROM settings", null)){
            if(cursor.moveToNext())
                return cursor.getDouble(0);

            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    /**
     * Returns the current currency (for example '$') or null if no currency is set. <br>
     *
     * The pay shouldn't be null though. <br>
     *
     * The default unit is '$'.
     *
     * @return String currency or null.
     */
    public String getCurrency() {

        SQLiteDatabase database = this.getReadableDatabase();

        try (Cursor cursor = database.rawQuery("SELECT currency FROM settings", null)){

            if (cursor.moveToNext())
                return cursor.getString(0);

            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Returns the time of all the sessions added together in minutes.
     *
     * @return  double totalTime (in minutes).
     */
    public double getTotalTime() {

        try (Cursor cursor = this.getReadableDatabase().rawQuery("SELECT SUM(totalTime) FROM sessions", null)) {
            if (cursor.moveToNext())
                return cursor.getDouble(0);
        }
        
        return 0;
    }

    /**
     * Used to get all sessions stored in the database.
     *
     * @return ArrayList with Session objects.
     */
    public ArrayList<Session> getSessions() {

        ArrayList<Session> sessions = new ArrayList<>();

        try (Cursor cursor = this.getReadableDatabase().rawQuery("SELECT id, date, startTime, endTime, totalTime, clientName, moneyEarned FROM sessions", null)) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String timeStart = cursor.getString(2);
                String timeStop = cursor.getString(3);
                double timeTotal = cursor.getDouble(4);
                String client = cursor.getString(5);
                double earnings = cursor.getDouble(6);

                Session session = new Session(id, date, timeStart, timeStop, client, timeTotal, earnings);

                sessions.add(session);
            }
        }

        return sessions;
    }

    /**
     * Deletes a session from the database.
     *
     * @param session The Session which should be deleted.
     *
     * @return The result of the delete function as an int.
     */
    public int deleteSession(@NonNull Session session) {
        return this.getWritableDatabase().delete("sessions", "id = ?", new String[]{String.valueOf(session.getId())});
    }

}