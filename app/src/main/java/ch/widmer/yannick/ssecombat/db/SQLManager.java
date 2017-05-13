package ch.widmer.yannick.ssecombat.db;

/**
 * Created by Yannick on 17.04.2017.
 */


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.widmer.yannick.ssecombat.Fighter;


public class SQLManager extends SQLiteOpenHelper {

    private static final String LOG="SQLManager";

    // All Static variables related to db
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "MAIN";

    // table names
    public static final String TABLE_FIGHTER = "FIGHTER";

    //Table columns names
    public static final  String KEY_ID = "ID", KEY_NAME = "NAME",
        KEY_ISFOE = "ISFOE", KEY_MAXLIFE = "MAXLIFE", KEY_LIFE = "LIFE",
        KEY_MAXSTAMINA="MAXSTAMINA", KEY_STAMINA = "STAMINA",
        KEY_TICK = "TICK";


    private SQLiteDatabase db;

    private List<String> warnings;


    public SQLManager(Activity context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ARMY_TABLE = "CREATE TABLE " + TABLE_FIGHTER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ISFOE + " BOOLEAN, "
                + KEY_MAXLIFE + " INTEGER, " +  KEY_LIFE + " INTEGER, "
                + KEY_MAXSTAMINA + " INTEGER, " + KEY_STAMINA + " INTEGER,"
                + KEY_TICK + " INTEGER"
                + ");";

        db.execSQL(CREATE_ARMY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIGHTER);
        // Create tables again
        onCreate(db);
    }



    public void save(List<Fighter> fighters){
        // This should delete all
        getWritableDatabase().delete(TABLE_FIGHTER,null, null);
        for(Fighter f:fighters)
            pushFighter(f,null);
    }

    //////////// PUSH  //////////////////////////////////

    private synchronized Long pushFighter(Fighter fighter, Long id){ // add or update army

        ContentValues c = new ContentValues();
        c.put(KEY_NAME, fighter.getName());
        c.put(KEY_ISFOE,fighter.isFoe()?"true":"false");
        c.put(KEY_MAXLIFE, fighter.getMaxLife());
        c.put(KEY_LIFE, fighter.getLife());
        c.put(KEY_MAXSTAMINA, fighter.getStaminaMax());
        c.put(KEY_STAMINA, fighter.getStamina());
        c.put(KEY_TICK, fighter.getTick());

        return pushData(TABLE_FIGHTER,id,c);
    }



    //////////// GET  /////////////////////////////////

    public synchronized ArrayList<Fighter> getFighters(){
        ArrayList<Fighter> fighters = new ArrayList<>();
        Fighter fighter;
        db = getReadableDatabase();
        Cursor c = db.query(TABLE_FIGHTER,new String[]{KEY_NAME,KEY_ID,KEY_ISFOE,KEY_LIFE,KEY_MAXLIFE,KEY_MAXSTAMINA,KEY_STAMINA,KEY_TICK},null,null,null,null,null);
        if(c.moveToFirst()){
            do{
                fighters.add(new Fighter(getBoolean(c,KEY_ISFOE),
                        getString(c,KEY_NAME), getInt(c,KEY_MAXLIFE), getInt(c,KEY_LIFE),
                        getInt(c,KEY_MAXSTAMINA), getInt(c,KEY_STAMINA), getInt(c,KEY_TICK)));
            }while(c.moveToNext());
        }
        db.close();
        c.close();
        return fighters;
    }




    // Help methods

    private int get(Cursor c,String key){
        return c.getInt(c.getColumnIndexOrThrow(key));
    }

    private int getInt(Cursor c, String key){
        return c.getInt(c.getColumnIndexOrThrow(key));
    }

    private boolean getBoolean(Cursor c,String key){
        return c.getString(c.getColumnIndexOrThrow(key)).toLowerCase().equals("true");
    }


    private String getString(Cursor c,String key){
        return c.getString(c.getColumnIndexOrThrow(key));
    }


    private long pushData(String table,Long id, ContentValues values){
        db = getWritableDatabase();
        if(id == null){
            Log.d(LOG,"inserting ");
            id=db.insert(table, null,values);
        }else{
            Log.d(LOG,"updating ");
            db.update(table, values, KEY_ID+" = ?", new String[]{String.valueOf(id)});
        }
        db.close();
        return id;
    }

    private void deleteEntry(String table,Long id){
        getWritableDatabase().delete(table,KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
}
