package ch.widmer.yannick.ssecombat.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.widmer.yannick.ssecombat.R;

public class DialogAction extends Activity {

    int staminaNow, staminaUse = 0, lifeNow , lifeUse=0, ticks;
    boolean rawTicks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_action);
        ((TextView)findViewById(R.id.text)).setText(getIntent().getStringExtra("text"));
        staminaNow = getIntent().getIntExtra("staminaNow",0);
        ((TextView)findViewById(R.id.staminaNow)).setText(""+staminaNow+" - ");
        ((TextView)findViewById(R.id.staminaUsed)).setText("0");
        ((TextView)findViewById(R.id.staminaMax)).setText(""+getIntent().getIntExtra("staminaMax",0));
        lifeNow = getIntent().getIntExtra("lifeNow",0);
        ((TextView)findViewById(R.id.lifeNow)).setText(""+lifeNow+" - ");
        ((TextView)findViewById(R.id.lifeUsed)).setText("0");
        ((TextView)findViewById(R.id.lifeMax)).setText(""+getIntent().getIntExtra("lifeMax",0));

        ((TextView)findViewById(R.id.ticks)).setText("0");
        rawTicks = getIntent().getBooleanExtra("rawTicks",false);
        if(!rawTicks)
            ((LinearLayout)findViewById(R.id.ticksLayout)).setVisibility(View.INVISIBLE);
    }

    public void end(int i){
        Intent returnIntent;
        returnIntent = new Intent();
        returnIntent.putExtra("id",getIntent().getIntExtra("id",-1));
        returnIntent.putExtra("stamina",staminaUse);
        returnIntent.putExtra("life",lifeUse);

        if(!rawTicks) {
            ticks = getIntent().getIntExtra("ticks", 0);
            if (getIntent().getBooleanExtra("defense", false))
                ticks += i;
        }
        returnIntent.putExtra("ticks",ticks);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }

    public void minus(View v){
        if(--staminaUse<0)
            staminaUse = 0;
        ((TextView)findViewById(R.id.staminaUsed)).setText(""+staminaUse);
    }

    public void plus(View v){
        if(++staminaUse>staminaNow)
            staminaUse = staminaNow;
        ((TextView)findViewById(R.id.staminaUsed)).setText(""+staminaUse);
    }

    public void minusTicks(View v){
        if(--ticks<0)
            ticks = 0;
        ((TextView)findViewById(R.id.ticks)).setText(""+ticks);
    }

    public void plusTicks(View v){
        ((TextView)findViewById(R.id.ticks)).setText(""+ ++ticks);
    }

    public void minusLife(View v){
        if(--lifeUse<0)
            lifeUse = 0;
        ((TextView)findViewById(R.id.lifeUsed)).setText(""+lifeUse);
    }

    public void plusLife(View v){
        if(++lifeUse>lifeNow)
            lifeUse = lifeNow;
        ((TextView)findViewById(R.id.lifeUsed)).setText(""+lifeUse);
    }

    public void fmm(View v){
        end(7);
    }

    public void fm(View v){
        end(3);
    }
    public void fail(View v){
        end(0);
    }

    public void success(View v){
        end(0);
    }
    public void sp(View v){
        end(0);
    }
    public void spp(View v){
        end(0);
    }

    public void cancel(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        super.finish();
    }
}
