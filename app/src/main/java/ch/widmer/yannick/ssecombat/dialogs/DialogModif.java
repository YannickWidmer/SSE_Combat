package ch.widmer.yannick.ssecombat.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ch.widmer.yannick.ssecombat.R;

import static ch.widmer.yannick.ssecombat.R.id.tick;

public class DialogModif extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_modif);

        ((EditText)findViewById(R.id.name)).setText(getIntent().getStringExtra("name"));
        ((EditText)findViewById(tick)).setText(""+getIntent().getIntExtra("tick",0));
        ((EditText)findViewById(R.id.stamina_max)).setText(""+getIntent().getIntExtra("stamina max",0));
        ((EditText)findViewById(R.id.stamina)).setText(""+getIntent().getIntExtra("stamina",0));
        ((EditText)findViewById(R.id.life_max)).setText(""+getIntent().getIntExtra("life max",0));
        ((EditText)findViewById(R.id.life)).setText(""+getIntent().getIntExtra("life",0));
        ((EditText)findViewById(R.id.acuity)).setText(""+getIntent().getIntExtra("acuity",0));
    }

    public void cancel(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        super.finish();
    }

    public void errase(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        returnIntent.putExtra("id",getIntent().getIntExtra("id",0));
        returnIntent.putExtra("errase",true);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }

    public void idle(View v){
        answer(true);
    }

    public void confirmed(View v) {
        answer(false);
    }

    private void answer(boolean idle){
        Intent returnIntent;
        returnIntent = new Intent();
        returnIntent.putExtra("id",getIntent().getIntExtra("id",0));
        returnIntent.putExtra("errase",false);
        returnIntent.putExtra("name",
                ((EditText)findViewById(R.id.name)).getText().toString());
        returnIntent.putExtra("tick",
                parseInt((EditText)findViewById(tick)));
        returnIntent.putExtra("stamina max",
                parseInt((EditText)findViewById(R.id.stamina_max)));
        returnIntent.putExtra("stamina",
                parseInt((EditText)findViewById(R.id.stamina)));
        returnIntent.putExtra("life max",
                parseInt((EditText)findViewById(R.id.life_max)));
        returnIntent.putExtra("life",
                parseInt((EditText)findViewById(R.id.life)));
        returnIntent.putExtra("acuity",
                parseInt((EditText)findViewById(R.id.acuity)));
        returnIntent.putExtra("idle",idle);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }

    private int parseInt(EditText view){
        try {
            return  Integer.parseInt(view.getText().toString());
        }catch (Exception e){
            return 0;
        }
    }
}
