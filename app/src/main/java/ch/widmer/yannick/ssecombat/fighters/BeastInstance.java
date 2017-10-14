package ch.widmer.yannick.ssecombat.fighters;

import ch.widmer.yannick.ssecombat.xmlparsing.Beast;

/**
 * Created by yanni on 10.10.2017.
 */

public class BeastInstance extends Fighter{

    private Beast mTemplate;

    public BeastInstance(Long id,String name, Beast template) {
        super(id,name, template.getLiveMax(), template.getLiveMax(), template.getStaminaMax(), template.getStaminaMax(), 0);
        mTemplate = template;
    }

    public BeastInstance(Long id,String name, Beast template, boolean isIdle, int life, int stamina, int ticks) {
        super(id,name, template.getLiveMax(), life, template.getStaminaMax(), stamina, ticks);
        setIdle(isIdle);
        mTemplate = template;
    }

    public void set(Long id, String name, int life, int stamina, int tick) {
        super.set(id,name,getMaxLife(),life,getStaminaMax(),stamina,tick);
    }


    public Beast getTemplate() {
        return mTemplate;
    }

    @Override
    public boolean isFoe(){
        return true;
    }
}
