package ch.widmer.yannick.ssecombat.xmlparsing;

import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Yannick on 05.04.2017.
 */

public class Weapon {
    private static final String LOG = "Weapon";
    private String name;
    private ArrayList<Action> actions = new ArrayList<>();
    private static ArrayList<Weapon> values = new ArrayList<Weapon>();

    public static void parse(MyXmlParser myParser) {
        Weapon weapon;
        Action action;
        for(MyXmlParser.Entry entry: myParser.main.entrys) {
            if (entry.name.equals("weapon")) {
                if (!entry.hasAttribute("name")) {
                    Log.w(LOG, "there is a weapon without a name!");
                    continue;
                }
                weapon = new Weapon();
                values.add(weapon);
                weapon.name = entry.getAttribute("name");
                Log.d(LOG,"Weapon "+weapon.name);
                for (MyXmlParser.Entry actionEntry : entry.entrys) {
                    action = new Action();
                    action.tick =
                            actionEntry.hasAttribute("ticks") ?
                                    actionEntry.getInt("ticks") : 0;
                    action.bonus =
                            actionEntry.hasAttribute("bonus") ?
                                    actionEntry.getInt("bonus") : 0;
                    action.name =
                            actionEntry.hasAttribute("name") ?
                                    actionEntry.getAttribute("name") : "";
                    action.attributes =
                            actionEntry.hasAttribute("attributes") ?
                                    actionEntry.getAttribute("attributes") : "";
                    action.damage =
                            actionEntry.hasAttribute("damage") ?
                                    actionEntry.getAttribute("damage") : "";
                    action.difficulty =
                            actionEntry.hasAttribute("difficulty") ?
                                    actionEntry.getAttribute("difficulty") : "";
                    action.difficult = actionEntry.getBool("difficult");
                    action.charge = actionEntry.getBool("charge");
                    action.shot = actionEntry.getBool("shot");
                    action.twohands = actionEntry.getBool("twohands");
                    action.attack = actionEntry.getBool("attack");
                    weapon.actions.add(action);
                }
            }
        }
    }

    @Override
    public String toString(){
        return name;
    }

    public static ArrayList<Weapon> values() {
                return values;
    }

    public ArrayList<Action> actions() {
        return this.actions;
    }
}


