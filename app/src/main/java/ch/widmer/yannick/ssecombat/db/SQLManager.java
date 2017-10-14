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

import ch.widmer.yannick.ssecombat.fighters.BeastInstance;
import ch.widmer.yannick.ssecombat.fighters.Fighter;
import ch.widmer.yannick.ssecombat.fighters.PlayerCharacter;
import ch.widmer.yannick.ssecombat.xmlparsing.Beast;


public class SQLManager extends SQLiteOpenHelper {

    private static final String LOG="SQLManager";

    // All Static variables related to db
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "MAIN";

    // table names
    public static final String TABLE_PC = "PCS", TABLE_BEAST = "BEAST";

    //Table columns names
    public static final  String KEY_ID = "ID", KEY_NAME = "NAME", KEY_TEMPLATE ="TEMPLATE",
        KEY_ISFOE = "ISFOE", KEY_IDLE = "IDLE",
            KEY_MAXLIFE = "MAXLIFE", KEY_LIFE = "LIFE",
        KEY_MAXSTAMINA="MAXSTAMINA", KEY_STAMINA = "STAMINA", KEY_ACUITY = "ACUITY",
        KEY_TICK = "TICK";


    private SQLiteDatabase db;

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
        String CREATE_PC_TABLE = "CREATE TABLE " + TABLE_PC + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ISFOE + " BOOLEAN, " + KEY_IDLE + " BOOLEAN, "
                + KEY_ACUITY + " INTEGER, " + KEY_MAXLIFE + " INTEGER, "
                + KEY_LIFE + " INTEGER, " + KEY_MAXSTAMINA + " INTEGER, "
                + KEY_STAMINA + " INTEGER," + KEY_TICK + " INTEGER"
                + ");";

        String CREATE_BEAST_TABLE = "CREATE TABLE "+ TABLE_BEAST + "("
                + KEY_ID +" INTEGER PRIMARY KEY, " + KEY_NAME +" TEXT, "
                + KEY_TEMPLATE +" TEXT, " + KEY_IDLE +" BOOLEAN, "
                + KEY_LIFE + " INTEGER, " + KEY_STAMINA + " INTEGER,"
                + KEY_TICK + " INTEGER"
                + ");";

        db.execSQL(CREATE_PC_TABLE);
        db.execSQL(CREATE_BEAST_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEAST);
        // Create tables again
        onCreate(db);
    }


    public void delete(Fighter f){
        if(f instanceof PlayerCharacter)
            deleteEntry(TABLE_PC,f.getId());
        else
            deleteEntry(TABLE_BEAST,f.getId());
    }


    //////////// PUSH  //////////////////////////////////

    public synchronized Long pushPc(PlayerCharacter pc){ // add or update army

        ContentValues c = new ContentValues();
        c.put(KEY_NAME, pc.getName());
        c.put(KEY_ID, pc.getId());
        c.put(KEY_ISFOE,pc.isFoe()?"true":"false");
        c.put(KEY_IDLE,pc.isIdle()?"true":"false");
        c.put(KEY_MAXLIFE, pc.getMaxLife());
        c.put(KEY_LIFE, pc.getLife());
        c.put(KEY_ACUITY,pc.getAcuity());
        c.put(KEY_MAXSTAMINA, pc.getStaminaMax());
        c.put(KEY_STAMINA, pc.getStamina());
        c.put(KEY_TICK, pc.getTick());
        pc.setId(pushData(TABLE_PC,pc.getId(),c));
        return pc.getId();
    }

    public synchronized Long pushBeast(BeastInstance beast){ // add or update army
        ContentValues c = new ContentValues();
        c.put(KEY_ID,beast.getId());
        c.put(KEY_NAME, beast.getName());
        c.put(KEY_TEMPLATE, beast.getTemplate().toString());
        c.put(KEY_IDLE,beast.isIdle()?"true":"false");
        c.put(KEY_LIFE, beast.getLife());
        c.put(KEY_STAMINA, beast.getStamina());
        c.put(KEY_TICK, beast.getTick());
        beast.setId(pushData(TABLE_BEAST,beast.getId(),c));
        return beast.getId();
    }



    //////////// GET  /////////////////////////////////

    public synchronized ArrayList<PlayerCharacter> getPCs(){
        ArrayList<PlayerCharacter> fighters = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.query(TABLE_PC,new String[]{KEY_NAME,KEY_ID,KEY_ACUITY,KEY_ISFOE,KEY_IDLE,KEY_LIFE,KEY_MAXLIFE,KEY_MAXSTAMINA,KEY_STAMINA,KEY_TICK},null,null,null,null,null);
        if(c.moveToFirst()){
            do{
                fighters.add(new PlayerCharacter(getLong(c,KEY_ID),getBoolean(c,KEY_ISFOE),getBoolean(c,KEY_IDLE),
                        getString(c,KEY_NAME), getInt(c,KEY_MAXLIFE), getInt(c,KEY_LIFE),
                        getInt(c,KEY_MAXSTAMINA), getInt(c,KEY_STAMINA), getInt(c,KEY_TICK),getInt(c,KEY_ACUITY)));
            }while(c.moveToNext());
        }
        db.close();
        c.close();
        return fighters;
    }


    public synchronized ArrayList<BeastInstance> getBeasts(){
        ArrayList<BeastInstance> beasts = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.query(TABLE_BEAST,new String[]{KEY_NAME,KEY_ID,KEY_TEMPLATE,KEY_IDLE,KEY_LIFE,KEY_STAMINA,KEY_TICK},null,null,null,null,null);
        if(c.moveToFirst()){
            do{
                //     public BeastInstance(String name, Beast template, boolean isIdle, int life, int stamina, int ticks) {

                beasts.add(new BeastInstance(getLong(c,KEY_ID),getString(c,KEY_NAME), Beast.get(getString(c,KEY_TEMPLATE)),
                        getBoolean(c,KEY_IDLE), getInt(c,KEY_LIFE), getInt(c,KEY_STAMINA), getInt(c,KEY_TICK)));

            }while(c.moveToNext());
        }
        db.close();
        c.close();
        return beasts;
    }






    // Help methods


    private int getInt(Cursor c, String key){
        return c.getInt(c.getColumnIndexOrThrow(key));
    }

    private Long getLong(Cursor c, String key){
        return c.getLong(c.getColumnIndexOrThrow(key));
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
