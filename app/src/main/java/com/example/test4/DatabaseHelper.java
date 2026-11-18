package com.example.test4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.test4.model.Appointment;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "appointments.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_APPOINTMENTS = "appointments";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_APPOINTMENTS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "location TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "time TEXT NOT NULL, "
                + "reminder INTEGER NOT NULL)");

        insertUser(db, "admin", "123456");
        insertAppointment(db, new Appointment(
                "Họp nhóm đồ án", "Phòng B203", "2026-10-25", "09:00", true));
        insertAppointment(db, new Appointment(
                "Khám răng", "Nha khoa Minh Tâm", "2026-10-26", "14:30", true));
        insertAppointment(db, new Appointment(
                "Đi cà phê với bạn", "Highland Coffee", "2026-10-27", "19:00", false));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{"id"},
                "username = ? AND password = ?",
                new String[]{username, password},
                null,
                null,
                null);
        boolean matched = cursor.moveToFirst();
        cursor.close();
        return matched;
    }

    public long insertUser(String username, String password) {
        return insertUser(getWritableDatabase(), username, password);
    }

    private long insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return db.insert(TABLE_USERS, null, values);
    }

    public long insertAppointment(Appointment appointment) {
        return insertAppointment(getWritableDatabase(), appointment);
    }

    private long insertAppointment(SQLiteDatabase db, Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put("title", appointment.getTitle());
        values.put("location", appointment.getLocation());
        values.put("date", appointment.getDate());
        values.put("time", appointment.getTime());
        values.put("reminder", appointment.isReminder() ? 1 : 0);
        return db.insert(TABLE_APPOINTMENTS, null, values);
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_APPOINTMENTS,
                null,
                null,
                null,
                null,
                null,
                "date ASC, time ASC");
        while (cursor.moveToNext()) {
            list.add(readAppointment(cursor));
        }
        cursor.close();
        return list;
    }

    public Appointment getAppointmentById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_APPOINTMENTS,
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        Appointment appointment = null;
        if (cursor.moveToFirst()) {
            appointment = readAppointment(cursor);
        }
        cursor.close();
        return appointment;
    }

    public int updateReminder(int id, boolean on) {
        ContentValues values = new ContentValues();
        values.put("reminder", on ? 1 : 0);
        return getWritableDatabase().update(
                TABLE_APPOINTMENTS,
                values,
                "id = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteAppointment(int id) {
        return getWritableDatabase().delete(
                TABLE_APPOINTMENTS,
                "id = ?",
                new String[]{String.valueOf(id)});
    }

    public int countAll() {
        return count("SELECT COUNT(*) FROM " + TABLE_APPOINTMENTS, null);
    }

    public int countReminders() {
        return count(
                "SELECT COUNT(*) FROM " + TABLE_APPOINTMENTS + " WHERE reminder = ?",
                new String[]{"1"});
    }

    private int count(String sql, String[] args) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    private Appointment readAppointment(Cursor cursor) {
        Appointment appointment = new Appointment();
        appointment.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        appointment.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        appointment.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
        appointment.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        appointment.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        appointment.setReminder(cursor.getInt(cursor.getColumnIndexOrThrow("reminder")) == 1);
        return appointment;
    }
}
