package ch.widmer.yannick.ssecombat.xmlparsing;

/**
 * Created by Yannick on 08.04.2017.
 */

public class Action {

    public int tick, bonus;
    public String attributes,name, damage, difficulty;
    public boolean difficult,charge,shot,twohands;
    public boolean defense;

    @Override
    public String toString(){
        return name;
    }

}
