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
import ch.widmer.yannick.ssecombat.dialogs.DialogModifBeast;
import ch.widmer.yannick.ssecombat.dialogs.DialogModifPc;
import ch.widmer.yannick.ssecombat.dialogs.DialogNewBeast;
import ch.widmer.yannick.ssecombat.dialogs.DialogNewPC;
import ch.widmer.yannick.ssecombat.fighters.BeastInstance;
import ch.widmer.yannick.ssecombat.fighters.Fighter;
import ch.widmer.yannick.ssecombat.fighters.PlayerCharacter;
import ch.widmer.yannick.ssecombat.xmlparsing.Action;
import ch.widmer.yannick.ssecombat.xmlparsing.Beast;
import ch.widmer.yannick.ssecombat.xmlparsing.BeastAction;
import ch.widmer.yannick.ssecombat.xmlparsing.MyXmlParser;
import ch.widmer.yannick.ssecombat.xmlparsing.Weapon;

import static ch.widmer.yannick.ssecombat.R.id.fab;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    private Manager mManager;
    private ListAdapter mActiveAdapter;
    private IdleAdapter mIdleAdapter;


    private Weapon chosenWeapon;
    private ArrayAdapter<Weapon>  weaponArrayAdapter;
    private ArrayAdapter<Action>  actionAdapter;
    private ArrayAdapter<BeastAction> beastActionAdapter;


    private final static int NEWPC =0 ,NEWBEAST = 1, MODIFPC = 2, MODIBEAST = 3, ACTPC = 4, ACTBEAST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new AsyncInit().execute();
    }

    private void onTouchPC(final PlayerCharacter pc){
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
                                act(pc,chosenWeapon,actionAdapter.getItem(which));
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
                intent.putExtra("id",pc.getId());
                intent.putExtra("ticks",0);
                intent.putExtra("rawTicks",true);
                intent.putExtra("attack",false);
                intent.putExtra("staminaNow",pc.getStamina());
                intent.putExtra("staminaMax",pc.getStaminaMax());
                intent.putExtra("lifeNow",pc.getLife());
                intent.putExtra("lifeMax",pc.getMaxLife());
                startActivityForResult(intent, ACTPC);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }


    private void onTouchBeast(final BeastInstance beast){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog.Builder builderWeapons  = new AlertDialog.Builder(MainActivity.this);
                builderWeapons.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                beastActionAdapter.clear();
                beastActionAdapter.addAll(beast.getTemplate().actions());
                builderWeapons.setAdapter(beastActionAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        act(beast,beastActionAdapter.getItem(which));
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
                intent.putExtra("id",beast.getId());
                intent.putExtra("ticks",0);
                intent.putExtra("rawTicks",true);
                intent.putExtra("attack",false);
                intent.putExtra("staminaNow",beast.getStamina());
                intent.putExtra("staminaMax",beast.getStaminaMax());
                intent.putExtra("lifeNow",beast.getLife());
                intent.putExtra("lifeMax",beast.getMaxLife());
                startActivityForResult(intent, ACTBEAST);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }


    private void act(PlayerCharacter pc, Weapon chosenWeapon, Action action) {
        Intent intent = new Intent(getApplication(), DialogAction.class);
        String text = chosenWeapon.toString()+": "+action.name + " "+action.attributes+" "+action.bonus
                +"\n difficulty:"+(action.difficult?"+":"")+action.difficulty
                +"\n ticks "+action.tick;
        if(!action.damage.equals("")) text +="  damage "+action.damage;
        intent.putExtra("text",text);
        intent.putExtra("id",pc.getId());
        intent.putExtra("ticks",action.tick);
        intent.putExtra("attack",action.attack);
        intent.putExtra("staminaNow",pc.getStamina());
        intent.putExtra("staminaMax",pc.getStaminaMax());
        intent.putExtra("lifeNow",pc.getLife());
        intent.putExtra("lifeMax",pc.getMaxLife());
        startActivityForResult(intent, ACTPC);
    }

    private void act(BeastInstance beast, BeastAction action) {
        Intent intent = new Intent(getApplication(), DialogAction.class);
        String text = action.getName()+": "+action.getDescription()
                +"\n ticks "+action.getTicks();
        intent.putExtra("text",text);
        intent.putExtra("id",beast.getId());
        intent.putExtra("ticks",action.getTicks());
        intent.putExtra("staminaUse",action.getStamina());
        intent.putExtra("staminaNow",beast.getStamina());
        intent.putExtra("staminaMax",beast.getStaminaMax());
        intent.putExtra("lifeNow",beast.getLife());
        intent.putExtra("attack",action.isAttack());
        intent.putExtra("lifeMax",beast.getMaxLife());
        startActivityForResult(intent, ACTBEAST);
    }


    // Result from Dialogs
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case MODIFPC:
                    PlayerCharacter pc = mManager.findPCById(data.getLongExtra("id",0));
                    if(pc!= null){
                        if(data.getBooleanExtra("errase",false)) mManager.errase(pc);
                        else{
                            pc.set(data.hasExtra("id")?data.getLongExtra("id",0):null,data.getStringExtra("name"),
                                    data.getIntExtra("life max",0),
                                    data.getIntExtra("life",0),
                                    data.getIntExtra("stamina max",0),
                                    data.getIntExtra("stamina",0),
                                    data.getIntExtra("tick",0),
                                    data.getIntExtra("acuity",0)
                            );
                            if(data.getBooleanExtra("idle",false)) mManager.makeIdle(pc);
                            mManager.updateFighter(pc);
                        }
                    }
                    break;
                case MODIBEAST:
                    BeastInstance f = mManager.findBeastById(data.getLongExtra("id",0));
                    if(f!= null) {
                        if (data.getBooleanExtra("errase", false)) mManager.errase(f);
                        else {

                            f.set(f.getId(),data.getStringExtra("name"),
                                    data.getIntExtra("life", 0),
                                    data.getIntExtra("stamina", 0),
                                    data.getIntExtra("tick", 0)
                            );
                            if (data.getBooleanExtra("idle", false)) mManager.makeIdle(f);
                            mManager.updateFighter(f);
                        }
                    }
                    break;
                case NEWPC:
                    mManager.addPC(data.getBooleanExtra("foe",false),
                            data.getStringExtra("name"),
                            data.getIntExtra("life max",0),
                            data.getIntExtra("life",0),
                            data.getIntExtra("stamina max",0),
                            data.getIntExtra("stamina",0),
                            data.getIntExtra("tick",0),
                            data.getIntExtra("acuity",0)
                            );
                    break;
                case NEWBEAST:
                    mManager.addBeast(data.getStringExtra("name"), Beast.get(data.getStringExtra("beast")));
                    break;
                case ACTPC:
                    mManager.action(data.getLongExtra("id",0),false,data.getIntExtra("ticks",0),data.getIntExtra("stamina",0),data.getIntExtra("life",0));
                    break;
                case ACTBEAST:
                    mManager.action(data.getLongExtra("id",0),true,data.getIntExtra("ticks",0),data.getIntExtra("stamina",0),data.getIntExtra("life",0));
                    break;
            }

            mActiveAdapter.notifyDataSetChanged();
            mIdleAdapter.notifyDataSetChanged();
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
            case R.id.action_acuity:
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
                builderSingle.setTitle("acuity test:");
                builderSingle.setSingleChoiceItems(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mManager.acuity_test()),0,null);
                builderSingle.show();
                break;
        }
        mActiveAdapter.notifyDataSetChanged();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    private class AsyncInit extends AsyncTask<Void,Void,Long> {

        @Override
        protected Long doInBackground(Void... params) {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                int[] ids = new int[]{R.raw.weapons, R.raw.beasts};
                for (int i : ids) {
                    MyXmlParser myParser = new MyXmlParser(getResources().openRawResource(i), parser);
                    switch (myParser.main.name) {
                        case "weapons":
                            Weapon.parse(myParser);
                            Log.d(LOG, "weapons now " + Weapon.values());
                            break;
                        case "beasts":
                            Beast.parse(myParser);
                            Log.d(LOG, "beasts now " + Beast.values());
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            weaponArrayAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,Weapon.values());
            actionAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item);
            beastActionAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item);


            ArrayList<Fighter> activeList = new ArrayList<>(), idleFoes = new ArrayList<>(), idlePcs = new ArrayList<>();
            // The Manager has to be instantiated after the Xml files are parsed since it will use the beasts.
            mManager = new Manager(activeList,idleFoes,idlePcs, MainActivity.this);
            mActiveAdapter = new ListAdapter(MainActivity.this,activeList);



            // Drawer stuff
            mIdleAdapter = new IdleAdapter(MainActivity.this,idlePcs,idleFoes);


            return null;
        }

        @Override
        protected void onPostExecute(Long nothing){
            ListView lView = (ListView) findViewById(R.id.list);
            lView.setAdapter(mActiveAdapter);
            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fighter fighter = mActiveAdapter.getItem(position);
                    if(fighter instanceof PlayerCharacter)
                        onTouchPC((PlayerCharacter) fighter);
                    else
                        onTouchBeast((BeastInstance)fighter);
                }
            });
            lView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    Fighter f = mActiveAdapter.getItem(position);
                    if(f instanceof PlayerCharacter) {
                        intent = new Intent(getApplication(), DialogModifPc.class);
                        intent.putExtra("id", f.getId());
                        intent.putExtra("name", f.getName());
                        intent.putExtra("tick", f.getTick());
                        intent.putExtra("acuity", ((PlayerCharacter) f).getAcuity());
                        intent.putExtra("stamina max", f.getStaminaMax());
                        intent.putExtra("stamina", f.getStamina());
                        intent.putExtra("life max", f.getMaxLife());
                        intent.putExtra("life", f.getLife());
                        startActivityForResult(intent, MODIFPC);
                    }else{
                        intent = new Intent(getApplication(), DialogModifBeast.class);
                        intent.putExtra("id", f.getId());
                        intent.putExtra("name", f.getName());
                        intent.putExtra("tick", f.getTick());
                        intent.putExtra("stamina max", f.getStaminaMax());
                        intent.putExtra("stamina", f.getStamina());
                        intent.putExtra("life max", f.getMaxLife());
                        intent.putExtra("life", f.getLife());
                        startActivityForResult(intent, MODIBEAST);
                    }
                    return true;
                }
            });

            lView = (ListView) findViewById(R.id.right_drawer);
            lView.setAdapter(mIdleAdapter);
            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fighter f = mIdleAdapter.getItem(position);
                    if(f != null) {
                        mManager.unIdle(f);
                        mActiveAdapter.notifyDataSetChanged();
                        mIdleAdapter.notifyDataSetChanged();
                    }
                }
            });

            ((FloatingActionButton) findViewById(fab)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(R.string.new_pc, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplication(), DialogNewPC.class);
                            startActivityForResult(intent, NEWPC);
                        }
                    });

                    builder.setNeutralButton(R.string.new_beast, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplication(), DialogNewBeast.class);
                            startActivityForResult(intent, NEWBEAST);
                        }
                    });
                    builder.create().show();

                }
            });
        }
    }

}
