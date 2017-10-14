package ch.widmer.yannick.ssecombat.xmlparsing;

/**
 * Created by yanni on 10.10.2017.
 */

public class BeastAction {
    private String name, description;
    private int ticks, stamina;
    private boolean isAttack;
    private static final String NAME = "name", DESCRIPTION = "description", TICKS ="ticks", STAMINA ="stamina", ATTACK ="attack";
    private static final String[] necessaryfields =new String[]{NAME,DESCRIPTION,TICKS,STAMINA};

    public BeastAction(MyXmlParser.Entry actionEntry) throws Exception{
        for(String field: necessaryfields){
            if(!actionEntry.hasAttribute(field))
                throw new Exception("missing "+field);
        }
        name = actionEntry.getAttribute(NAME);
        description = actionEntry.getAttribute(DESCRIPTION);
        ticks = actionEntry.getInt(TICKS);
        stamina = actionEntry.getInt(STAMINA);
        isAttack = actionEntry.getBool(ATTACK);
    }

    public boolean isAttack(){
        return isAttack;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTicks() {
        return ticks;
    }

    public int getStamina() {
        return stamina;
    }
}
