package ch.widmer.yannick.ssecombat;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import ch.widmer.yannick.ssecombat.db.SQLManager;

/**
 * Created by Yannick on 04.04.2017.
 */

public class TickManager {
    private ArrayList<Fighter> mList;
    private static Random random = new Random();
    private SQLManager sqlManager;
    private Comparator<Fighter> comparator = new Comparator<Fighter>() {
        @Override
        public int compare(Fighter o1, Fighter o2) {
            return 2*o1.getTick() - 2*o2.getTick() + (o1.isFoe()?1:0) -(o2.isFoe()?1:0) ;
        }
    };

    public TickManager(ArrayList<Fighter> list, Activity context) {
        sqlManager = new SQLManager(context);
        mList = list;
        mList.addAll(sqlManager.getFighters());
    }

    public void add(boolean isFoe,String name, int maxLife, int life, int maxStamina, int stamina, int tick, int acuity){
        mList.add(new Fighter(isFoe,name,maxLife,life,maxStamina,stamina,tick,acuity));
        orderListByTick();
    }

    public void next(){
        Fighter mNext = mList.get(0);
        int tickPassing = mNext.getTick();
        for(Fighter f:mList){
            f.passTicks(tickPassing);
        }
    }

    public boolean action(int id, int ticks, int stamina, int life){
        Fighter f = findById(id);
        f.addTicks(ticks);
        f.useStamina(stamina);
        f.useLife(life);
        orderListByTick();
        return true;
    }

    private void orderListByTick(){
        Collections.sort(mList, comparator);
        save();
    }

    public Fighter findById(int id) {
        for(Fighter f:mList){
            if(f.getId() == id) return f;
        }
        return null;
    }

    public void errase(Fighter f) {
        mList.remove(f); save();
    }

    public Fighter get(int position) {
        return mList.get(position);
    }

    private void save(){
        sqlManager.save(mList);
    }

    public ArrayList<String> acuity_test() {
        ArrayList<String> res = new ArrayList<>();
        for(Fighter f:mList)
            res.add((random.nextInt(f.getActuity())+1) +" "+f.getName());
        Collections.sort(res);
        Collections.reverse(res);
        return res;
    }
}
