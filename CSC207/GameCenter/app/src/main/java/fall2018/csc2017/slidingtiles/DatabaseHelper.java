package fall2018.csc2017.slidingtiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fall2018.csc2017.slidingtiles.Factory;
import fall2018.csc2017.slidingtiles.InterfaceAdapter;
import fall2018.csc2017.slidingtiles.ScoreStrategy;
import fall2018.csc2017.slidingtiles.UserAccount;
import fall2018.csc2017.slidingtiles.UserAccountManager;

/**
 * A helper class to database
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "User.db";
    private static final String TABLE_NAME = "user_table";
    private static final String KEY_NAME = "USERNAME";
    private static final String KEY_USER = "USERACCOUNT";
    private final String USER_ACCOUNT_MANAGER = "UserAccountManager";

    /**
     * Construct a new DatabaseHelper.
     *
     * @param context activity context.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Create a table in the database.
     *
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME + " (USERNAME TEXT PRIMARY KEY, " +
                "USERACCOUNT OBJECT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    /**
     * Check if a UserAccountManager exists.
     *
     * @return whether there exists a UserAccountManager.
     */
    boolean ifAccountManagerExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select count(*) from " + TABLE_NAME + " where " + KEY_NAME + " = '" + USER_ACCOUNT_MANAGER + "'";
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res.getInt(0) == 1;
    }

    /**
     * Create a UserAccountManager.
     *
     * @return whether a UserAccountManager has been successfully created.
     */
    boolean createUserAccountManager() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Factory f = new Factory();
        contentValues.put(KEY_USER, convertToJson(f.createUserManager()));
        contentValues.put(KEY_NAME, USER_ACCOUNT_MANAGER);
        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;

    }

    /**
     * Convert the Object argument to Json.
     *
     * @param o the Objected to be converted to Json.
     * @return whether the operation is done successfully
     */
    private String convertToJson(Object o) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ScoreStrategy.class, new InterfaceAdapter<ScoreStrategy>())
                .create();
        return gson.toJson(o);
    }

    /**
     * Insert a new username and userAccount into the database.
     *
     * @param username    the username to be inserted as the key
     * @param userAccount the userAccount to be inserted as the content.
     * @return whether the username and userAccount has been successfully inserted into the database.
     * @throws SQLException
     */
    boolean createAndInsertNew(String username, UserAccount userAccount) throws SQLException {
        boolean found;
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, username);
        contentValues.put(KEY_USER, convertToJson(userAccount)); //change default value of age = 0 and email = "" in regist activity
        try {
            result = db.insertOrThrow(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }
        found = result != -1;
        return found;
    }

    /**
     * Extract the UserAccountManager.
     *
     * @return the UserAccountManager
     */
    public UserAccountManager selectAccountManager() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select " + KEY_USER + " from " + TABLE_NAME + " where " + KEY_NAME
                + " = '" + USER_ACCOUNT_MANAGER + "'";
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return convertToAccountManager(res.getString(0));
    }

    /**
     * Extract the UserAccount with the username.
     *
     * @param username the username of the UserAccount wanted.
     * @return the UserAccount wanted.
     */
    public UserAccount selectUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select " + KEY_USER + " from " + TABLE_NAME + " where " + KEY_NAME + " = '" + username + "'";
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return convertToUserAccount(res.getString(0));
    }

    /**
     * Update the username and UserAccount information in the database.
     *
     * @param username    the username of the UserAccount to be updated.
     * @param userAccount the UserAccount to be updated.
     */
    public void updateUser(String username, UserAccount userAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, username);
        contentValues.put(KEY_USER, convertToJson(userAccount));
        db.update(TABLE_NAME, contentValues, "USERNAME = ?", new String[]{username});

    }

    /**
     * Delete a UserAccount with the old username and insert a UserAccount and the new username as the key.
     *
     * @param oldUserName the username of the UserAccount to be deleted.
     * @param newUserName the username of the New UserAccount to be inserted.
     * @param userAccount the UserAccount to be inserted.
     * @return whether the operation has been done successfully.
     */
    public boolean deleteAndInsertUser(String oldUserName, String newUserName, UserAccount userAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        boolean found;
        db.delete(TABLE_NAME, "USERNAME = ?", new String[]{oldUserName});
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, newUserName);
        contentValues.put(KEY_USER, convertToJson(userAccount));
        try {
            result = db.insertOrThrow(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }
        found = result != -1;
        return found;
    }

    /**
     * Update the database with the UserAccountManager given.
     *
     * @param userAccountManager the UserAccountManager to be inserted.
     */
    public void updateAccountManager(UserAccountManager userAccountManager) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, USER_ACCOUNT_MANAGER);
        contentValues.put(KEY_USER, convertToJson(userAccountManager));
        db.update(TABLE_NAME, contentValues, "USERNAME = ?", new String[]{USER_ACCOUNT_MANAGER});

    }

    /**
     * Convert the jsonString to the corresponding UserAccount.
     *
     * @param jsonString the jsonString of the UserAccount wanted.
     * @return the UserAccount wanted.
     */
    private UserAccount convertToUserAccount(String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ScoreStrategy.class, new InterfaceAdapter<ScoreStrategy>())
                .create();
        return gson.fromJson(jsonString, UserAccount.class);
    }

    /**
     * Convert the jsonString to the corresponding UserAccountManager.
     *
     * @param jsonString the jsonString of the UserAccountManager wanted.
     * @return the UserAccountManager wanted.
     */
    private UserAccountManager convertToAccountManager(String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ScoreStrategy.class, new InterfaceAdapter<ScoreStrategy>())
                .create();
        return gson.fromJson(jsonString, UserAccountManager.class);
    }

    /**
     * Check whether the database has the UserAccount with the username.
     *
     * @param username the username of the UserAccount being searched for.
     * @return the UserAccount wanted.
     */
    boolean hasUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select count(*) from " + TABLE_NAME + " where " + KEY_NAME + " = '" + username + "'";
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res.getInt(0) == 1;
    }
}
