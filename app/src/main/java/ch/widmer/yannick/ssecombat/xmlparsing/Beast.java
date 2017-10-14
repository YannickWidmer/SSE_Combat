package ch.widmer.yannick.ssecombat.xmlparsing;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Yannick on 05.04.2017.
 */

public class Beast {
    private static final String LOG = "Beast";
    private String name;
    private int mLiveMax, mStaminaMax;
    private ArrayList<BeastAction> actions = new ArrayList<>();
    private static Map<String,Beast> values = new HashMap<>();
    private static final String NAME = "name", LIVE = "live", STAMINA = "stamina";
    private static final String[] fields = new String[]{NAME,LIVE,STAMINA};

    private Beast(){}

    public static void parse(MyXmlParser myParser) {
        Beast beast;
        boolean abort;
        for(MyXmlParser.Entry entry: myParser.main.entrys) {
            abort = false;
            if (entry.name.equals("beast")) {

                for(String field:fields)
                    if (!entry.hasAttribute(field)) {
                        Log.w(LOG, "there is a beast without a "+field+"!");
                        abort = true;
                    }
                if(abort) continue;

                beast = new Beast();
                beast.name = entry.getAttribute(NAME);
                beast.mLiveMax = entry.getInt(LIVE);
                beast.mStaminaMax = entry.getInt(STAMINA);
                values.put(beast.name,beast);
                Log.d(LOG,"Beast "+beast.name);

                for (MyXmlParser.Entry actionEntry : entry.entrys) {
                    try {
                        beast.actions.add(new BeastAction(actionEntry));
                    }catch (Exception e){
                        Log.w(LOG,beast.name +" "+e.toString());
                    }

                }
            }
        }
    }

    public static ArrayList<Beast> values() {
        return new ArrayList<>(values.values());
    }

    public static Beast get(String name){
        return values.get(name);
    }

    @Override
    public String toString(){
        return name;
    }

    public int getLiveMax() {
        return mLiveMax;
    }

    public int getStaminaMax() {
        return mStaminaMax;
    }

    public ArrayList<BeastAction> actions() {
        return actions;
    }
}


