package ch.widmer.yannick.ssecombat.fighters;

/**
 * Created by yanni on 10.10.2017.
 */

public class PlayerCharacter extends Fighter{
    private int mAcuity;
    private boolean mIsFoe;

    public PlayerCharacter(Long id,boolean isFoe, boolean isIdle,String name, int maxLife, int life, int maxStamina, int stamina, int tick, int acuity) {
        super(id,name, maxLife, life, maxStamina, stamina, tick);
        mIsFoe = isFoe;
        setIdle(isIdle);
        mAcuity = acuity;
    }

    public boolean isFoe(){
        return mIsFoe;
    }

    public void set(Long id, String name, int maxLife, int life, int maxStamina, int stamina, int tick, int acutiy) {
        super.set(id,name,maxLife,life,maxStamina,stamina,tick);
        mAcuity = acutiy;
    }

    public int getAcuity(){
        return mAcuity;
    }
}
