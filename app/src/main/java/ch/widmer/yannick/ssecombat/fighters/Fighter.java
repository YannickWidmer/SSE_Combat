package ch.widmer.yannick.ssecombat.fighters;


/**
 * Created by Yannick on 01.04.2017.
 */

public abstract class  Fighter {
    boolean  mIsIdle;
    private Long id;
    private String mName;
    private int mMaxLife, mLife, mMaxStamina,mStamina,mTick;

    public Fighter(Long id,String name, int maxLife, int life, int maxStamina, int stamina, int tick) {
        mIsIdle = false;
        this.id = id;
        mName = name;
        mMaxLife = maxLife;
        mLife = life;
        mMaxStamina = maxStamina;
        mStamina = stamina;
        mTick = tick;
    }

    public void setIdle(boolean idle){
        mIsIdle = idle;
    }

    public boolean isIdle(){
        return mIsIdle;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){return id;}

    public abstract boolean isFoe();

    public String getName() {
        return mName;
    }

    public int getMaxLife() {
        return mMaxLife;
    }

    public int getLife() {
        return mLife;
    }

    public int getStaminaMax() {
        return mMaxStamina;
    }

    public int getStamina() {
        return mStamina;
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

    protected void set(Long id, String name, int maxLife, int life, int maxStamina, int stamina, int tick) {
        mName = name;
        this.id = id;
        mMaxLife = maxLife;
        mLife = life;
        mMaxStamina = maxStamina;
        mStamina = stamina;
        mTick = tick;
    }

    @Override
    public String toString(){
        return mName;
    }

    public void useStamina(int stamina) {
        mStamina -= stamina;
    }

    public void useLife(int life) {
        mLife -= life;
    }
}
