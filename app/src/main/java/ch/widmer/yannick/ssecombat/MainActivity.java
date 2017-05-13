package ch.widmer.yannick.ssecombat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.steamcrafted.materialiconlib.MaterialMenuInflater;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

import ch.widmer.yannick.ssecombat.dialogs.DialogAction;
import ch.widmer.yannick.ssecombat.dialogs.DialogModif;
import ch.widmer.yannick.ssecombat.dialogs.DialogNew;
import ch.widmer.yannick.ssecombat.xmlparsing.Action;
import ch.widmer.yannick.ssecombat.xmlparsing.MyXmlParser;
import ch.widmer.yannick.ssecombat.xmlparsing.Weapon;

import static ch.widmer.yannick.ssecombat.R.id.fab;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    private TickManager mManager;
    private ListAdapter mAdapter;
    private ArrayAdapter<Weapon> weaponArrayAdapter;
    private ArrayAdapter<Action> actionAdapter;
    private ArrayAdapter<Fighter> fighterAdapter;
    private Weapon chosenWeapon;


    private final static int NEW =0 , MODIF = 1, ACT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((FloatingActionButton) findViewById(fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DialogNew.class);
                startActivityForResult(intent, NEW);
            }
        });




        //Do Asyncronously what can be done asyncronously
        new AsyncInit().execute();

        // Do what is displayed imediately in this thread
        ArrayList<Fighter> list = new ArrayList<>();
        mManager = new TickManager(list,this);
        mAdapter = new ListAdapter(this,list);

        ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(mAdapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTouchFighter(mAdapter.getItem(position));
            }
        });
        lView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), DialogModif.class);
                Fighter f = mManager.get(position);
                intent.putExtra("id",f.getId());
                intent.putExtra("name",f.getName());
                intent.putExtra("tick",f.getTick());
                intent.putExtra("stamina max",f.getStaminaMax());
                intent.putExtra("stamina",f.getStamina());
                intent.putExtra("life max",f.getMaxLife());
                intent.putExtra("life",f.getLife());
                startActivityForResult(intent, MODIF);
                return true;
            }
        });

    }

    private void onTouchFighter(final Fighter fighter){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.use_weapon, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog.Builder builderWeapons  = new AlertDialog.Builder(MainActivity.this);
                builderWeapons.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builderWeapons.setAdapter(weaponArrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenWeapon = weaponArrayAdapter.getItem(which);
                        actionAdapter.clear();
                        actionAdapter.addAll(chosenWeapon.actions());
                        AlertDialog.Builder buildeActions  = new AlertDialog.Builder(MainActivity.this);
                        buildeActions.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        buildeActions.setAdapter(actionAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(LOG,"which :"+which);
                                act(fighter,chosenWeapon,actionAdapter.getItem(which));
                            }});
                        buildeActions.create().show();

                    }
                });
                builderWeapons.create().show();
            }
        });

        builder.setNeutralButton(R.string.free, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplication(), DialogAction.class);
                intent.putExtra("text","");
                intent.putExtra("id",fighter.getId());
                intent.putExtra("ticks",0);
                intent.putExtra("rawTicks",true);
                intent.putExtra("defense",false);
                intent.putExtra("staminaNow",fighter.getStamina());
                intent.putExtra("staminaMax",fighter.getStaminaMax());
                intent.putExtra("lifeNow",fighter.getLife());
                intent.putExtra("lifeMax",fighter.getMaxLife());
                startActivityForResult(intent,ACT);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void act(Fighter fighter, Weapon chosenWeapon, Action action) {
        Intent intent = new Intent(getApplication(), DialogAction.class);
        String text = chosenWeapon.toString()+": "+action.name + " "+action.attributes+" "+action.bonus
                +"\n difficulty:"+(action.difficult?"+":"")+action.difficulty
                +"\n ticks "+action.tick;
        if(!action.damage.equals("")) text +="  damage "+action.damage;
        intent.putExtra("text",text);
        intent.putExtra("id",fighter.getId());
        intent.putExtra("ticks",action.tick);
        intent.putExtra("defense",action.defense);
        intent.putExtra("staminaNow",fighter.getStamina());
        intent.putExtra("staminaMax",fighter.getStaminaMax());
        intent.putExtra("lifeNow",fighter.getLife());
        intent.putExtra("lifeMax",fighter.getMaxLife());
        startActivityForResult(intent,ACT);
    }


    // Result from Dialogs
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case MODIF:
                    Fighter f = mManager.findById(data.getIntExtra("id",0));
                    if(f!= null){
                        if(data.getBooleanExtra("errase",false)){
                            mManager.errase(f);
                        }else{
                            f.set(data.getStringExtra("name"),
                                    data.getIntExtra("life max",0),
                                    data.getIntExtra("life",0),
                                    data.getIntExtra("stamina max",0),
                                    data.getIntExtra("stamina",0),
                                    data.getIntExtra("tick",0)
                            );
                        }
                    }
                    break;
                case NEW:
                    mManager.add(data.getBooleanExtra("foe",false),
                            data.getStringExtra("name"),
                            data.getIntExtra("life max",0),
                            data.getIntExtra("life",0),
                            data.getIntExtra("stamina max",0),
                            data.getIntExtra("stamina",0),
                            data.getIntExtra("tick",0)
                            );
                    break;
                case ACT:
                    mManager.action(data.getIntExtra("id",0),data.getIntExtra("ticks",0),data.getIntExtra("stamina",0),data.getIntExtra("life",0));
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    }

            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MaterialMenuInflater
                    .with(this) // Provide the activity context
                    // Set the fall-back color for all the icons. Colors set inside the XML will always have higher priority
                    .setDefaultColor(Color.BLUE)
                    // Inflate the menu
                    .inflate(R.menu.menu_main, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_next:
                mManager.next();
                break;
        }
        mAdapter.notifyDataSetChanged();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }




    private class AsyncInit extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                int[] ids = new int[]{R.raw.weapons};
                for (int i : ids) {
                    MyXmlParser myParser = new MyXmlParser(getResources().openRawResource(i), parser);
                    switch (myParser.main.name) {
                        case "weapons":
                            Weapon.parse(myParser);
                            Log.d(LOG, "weapons now " + Weapon.values());
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            weaponArrayAdapter = new ArrayAdapter<Weapon>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,Weapon.values());
            actionAdapter = new ArrayAdapter<Action>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item);
            fighterAdapter = new ArrayAdapter<Fighter>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item);
            return null;
        }
    }

}
