package ch.widmer.yannick.ssecombat;


/**
 * Created by Yannick on 01.04.2017.
 */

public class Fighter {
    boolean mIsFoe;
    private static int ID=0;
    private final int id = ++ID;
    String mName;
    int mMaxLife, mLife, mMaxStamina,mStamina,mTick,mAcuity;

    public Fighter(boolean isFoe, String name, int maxLife, int life, int maxStamina, int stamina, int tick, int acuity) {
        mIsFoe = isFoe;
        mName = name;
        mMaxLife = maxLife;
        mLife = life;
        mMaxStamina = maxStamina;
        mStamina = stamina;
        mTick = tick;
        mAcuity = acuity;
    }

    public int getId(){return id;}

    public boolean isFoe() {
        return mIsFoe;
    }

    public String getName() {
        return mName;
    }

    public int getMaxLife() {
        return mMaxLife;
    }

    public int getLife() {
        return mLife;
    }

    public void passTicks(int ticks){
        mTick -=ticks;
        if(mTick <0) mTick = 0;
    }

    public void addTicks(int ticks){
        if(ticks < 0) ticks =0;
        mTick += ticks;
    }

    public int getTick() {
        return mTick;
    }

    public void set(String name, int maxLife, int life, int maxStamina, int stamina, int tick, int acutiy) {
        mName = name;
        mMaxLife = maxLife;
        mLife = life;
        mMaxStamina = maxStamina;
        mStamina = stamina;
        mTick = tick;
        mAcuity = acutiy;
    }

    public int getActuity(){
        return mAcuity;
    }

    @Override
    public String toString(){
        return mName;
    }

    public int getStaminaMax() {
        return mMaxStamina;
    }

    public int getStamina() {
        return mStamina;
    }

    public void useStamina(int stamina) {
        mStamina -= stamina;
    }

    public void useLife(int life) {
        mLife -= life;
    }
}
