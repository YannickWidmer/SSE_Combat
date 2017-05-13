package ch.widmer.yannick.ssecombat.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import ch.widmer.yannick.ssecombat.R;

public class DialogNew extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_new);
    }

    public void cancel(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        super.finish();
    }

    public void confirmed(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        returnIntent.putExtra("name",
                ((EditText)findViewById(R.id.name)).getText().toString());
        returnIntent.putExtra("tick",
                parseInt((EditText)findViewById(R.id.tick)));
        returnIntent.putExtra("stamina max",
                parseInt((EditText)findViewById(R.id.stamina_max)));
        returnIntent.putExtra("stamina",
                parseInt((EditText)findViewById(R.id.stamina)));
        returnIntent.putExtra("life max",
                parseInt((EditText)findViewById(R.id.life_max)));
        returnIntent.putExtra("life",
                parseInt((EditText)findViewById(R.id.life)));
        returnIntent.putExtra("foe",((CheckBox)findViewById(R.id.foe)).isChecked());
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
