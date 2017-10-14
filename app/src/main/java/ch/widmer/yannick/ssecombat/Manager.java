package ch.widmer.yannick.ssecombat;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import ch.widmer.yannick.ssecombat.db.SQLManager;
import ch.widmer.yannick.ssecombat.fighters.BeastInstance;
import ch.widmer.yannick.ssecombat.fighters.Fighter;
import ch.widmer.yannick.ssecombat.fighters.PlayerCharacter;
import ch.widmer.yannick.ssecombat.xmlparsing.Beast;

/**
 * Created by Yannick on 04.04.2017.
 */

public class Manager {
    private ArrayList<Fighter> mActiveList = new ArrayList<>(), mIdlePCList = new ArrayList<>(),
            mIdleFoeList = new ArrayList<>(), mFoes = new ArrayList<>();
    private ArrayList<PlayerCharacter> mPlayers = new ArrayList<>();
    private static Random random = new Random();
    private SQLManager sqlManager;
    private Comparator<Fighter> comparator = new Comparator<Fighter>() {
        @Override
        public int compare(Fighter o1, Fighter o2) {
            return 2*o1.getTick() - 2*o2.getTick() + (o1.isFoe()?1:0) -(o2.isFoe()?1:0) ;
        }
    };

    // Instead of making the managers lists available we expect to have them given at start and we keep a reference in MainActivity
    public Manager(ArrayList<Fighter> activeList, ArrayList<Fighter> idleFoes, ArrayList<Fighter> idlePc, Activity context) {
        sqlManager = new SQLManager(context);
        mActiveList = activeList;
        mIdleFoeList = idleFoes;
        mIdlePCList = idlePc;
        // The manager will be instantiated asyncronously hence we can make heavy work here
        for(PlayerCharacter f:sqlManager.getPCs()){
            if(f.isIdle()){
                if(f.isFoe()) {
                    mFoes.add(f);
                    mIdleFoeList.add(f);
                }else{
                    mPlayers.add(f);
                    mIdlePCList.add(f);
                }
            }else{
                mActiveList.add(f);
                if(f.isFoe()) mFoes.add(f);
                else mPlayers.add(f);
            }
        }

        for(BeastInstance beast:sqlManager.getBeasts()){
            mFoes.add(beast);
            if(beast.isIdle()) mIdleFoeList.add(beast);
            else mActiveList.add(beast);
        }
        orderListByTick();
    }

    public void next(){
        Fighter mNext = mActiveList.get(0);
        int tickPassing = mNext.getTick();
        for(Fighter f:mActiveList){
            f.passTicks(tickPassing);
        }
        for(Fighter f:mIdlePCList){
            f.passTicks(tickPassing);
        }
        for(Fighter f:mIdleFoeList){
            f.passTicks(tickPassing);
        }

        updateDb();
    }

    public boolean action(Long id,boolean isBeast, int ticks, int stamina, int life){
        Fighter f = isBeast?findBeastById(id):findPCById(id);
        f.addTicks(ticks);
        f.useStamina(stamina);
        f.useLife(life);
        updateFighter(f);
        orderListByTick();
        return true;
    }


    private void orderListByTick(){
        Collections.sort(mActiveList, comparator);
    }

    public PlayerCharacter findPCById(Long id) {
        for(PlayerCharacter f:mPlayers){
            if(f.getId() == id) return f;
        }
        for(Fighter f:mFoes)
            if(f.getId() == id && f instanceof PlayerCharacter) return (PlayerCharacter) f;
        return null;
    }

    public BeastInstance findBeastById(Long id){
        for(Fighter f:mFoes){
            if(f.getId() == id && f instanceof BeastInstance) return (BeastInstance) f;
        }
        return null;
    }

    public void addPC(final boolean isFoe, String name, int maxLife, int life, int maxStamina, int stamina, int tick, int acuity){
        final PlayerCharacter pc = new PlayerCharacter(null,isFoe,false,name,maxLife,life,maxStamina,stamina,tick,acuity);
        sqlManager.pushPc(pc);
        if(isFoe) mFoes.add(pc);
        else mPlayers.add(pc);
        mActiveList.add(pc);
        orderListByTick();
    }

    public void addBeast(String name, Beast template){
        final BeastInstance beast = new BeastInstance(null,name,template);
        sqlManager.pushBeast(beast);
        mActiveList.add(beast);
        mFoes.add(beast);
        orderListByTick();
    }

    public void errase(final Fighter f) {
        mActiveList.remove(f);
        if(f.isFoe()){
            mFoes.remove(f);
            mIdleFoeList.remove(f);
        }else{
            mPlayers.remove(f);
            mIdlePCList.remove(f);
        }
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                sqlManager.delete(f);
                return null;
            }
        }.execute();
    }


    public ArrayList<String> acuity_test() {
        ArrayList<String> res = new ArrayList<>();
        for(PlayerCharacter f:mPlayers)
            res.add((f.getAcuity()>0?(random.nextInt(f.getAcuity())+1):1) +" "+f.getName());
        Collections.sort(res);
        Collections.reverse(res);
        return res;
    }

    public void makeIdle(Fighter f) {
        f.setIdle(true);
        mActiveList.remove(f);
        if(f.isFoe())
            mIdleFoeList.add(f);
        else
            mIdlePCList.add(f);

        updateFighter(f);
    }

    public void unIdle(Fighter f){
        f.setIdle(false);
        mActiveList.add(f);
        mIdlePCList.remove(f);
        mIdleFoeList.remove(f);
        updateFighter(f);
        orderListByTick();
    }

    public void updateFighter(final Fighter f){
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (f instanceof PlayerCharacter)
                    sqlManager.pushPc((PlayerCharacter) f);
                else
                    sqlManager.pushBeast((BeastInstance) f);
                return null;
            }
        }.execute();
    }

    private void updateDb(){
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (PlayerCharacter f : mPlayers)
                    sqlManager.pushPc(f);

                for (Fighter f : mFoes) {
                    if (f instanceof PlayerCharacter) sqlManager.pushPc((PlayerCharacter) f);
                    else sqlManager.pushBeast((BeastInstance) f);
                }
                return null;
            }
        }.execute();

    }
}
