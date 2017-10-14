package ch.widmer.yannick.ssecombat.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import ch.widmer.yannick.ssecombat.R;
import ch.widmer.yannick.ssecombat.xmlparsing.Beast;

public class DialogNewBeast extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_new_beast);

        ListView lv = (ListView)findViewById(R.id.list);
        final ArrayAdapter<Beast> adap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Beast.values());
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name",
                        ((EditText)findViewById(R.id.name)).getText().toString());
                returnIntent.putExtra("beast",adap.getItem(position).toString());
                setResult(RESULT_OK, returnIntent);
                DialogNewBeast.this.finish();
            }
        });
    }

    public void cancel(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        super.finish();
    }

}
