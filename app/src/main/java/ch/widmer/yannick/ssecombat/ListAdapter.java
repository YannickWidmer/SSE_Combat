package ch.widmer.yannick.ssecombat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yannick on 01.04.2017.
 */

public class ListAdapter extends ArrayAdapter<Fighter> {
        private LayoutInflater mInflater;

        public ListAdapter(Context context, List<Fighter> items) {
            super(context, 0, items);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }



    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view =  mInflater.inflate(R.layout.list_item, null);
            } else {
                view =  convertView;
            }
            Fighter fighter = getItem(position);

            ((TextView)view.findViewById(R.id.name)).setText(fighter.getName());
            ((JaugeView)view.findViewById(R.id.vie)).setValues(fighter.getMaxLife(),fighter.getLife());
            ((JaugeView)view.findViewById(R.id.stamina)).setValues(fighter.getStaminaMax(),fighter.getStamina());
            ((TextView)view.findViewById(R.id.tick)).setText(""+fighter.getTick());
            view.set
            if(fighter.isFoe()) {
                view.setBackgroundResource(R.color.grey);
            }else{
                view.setBackgroundResource(R.color.white);
            }
            view.invalidate();


            return view;
        }
    }
