package ch.widmer.yannick.ssecombat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Yannick on 01.04.2017.
 */

public class ListAdapter extends ArrayAdapter<Fighter> {
        private LayoutInflater mInflater;
    private List<Fighter> mItems;

    private Comparator<Fighter> comparator = new Comparator<Fighter>() {
        @Override
        public int compare(Fighter o1, Fighter o2) {
            return 2*o1.getTick() - 2*o2.getTick() + (o1.isFoe()?1:0) -(o2.isFoe()?1:0) ;
        }
    };

    @Override
    public void notifyDataSetChanged(){
        Collections.sort(mItems, comparator);
        super.notifyDataSetChanged();
    }

        public ListAdapter(Context context, List<Fighter> items) {
            super(context, 0, items);
            mItems = items;
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

            if(fighter.isFoe()) {
                view.setBackgroundResource(R.drawable.layout_bckgrnd_foe);
            }else{
                view.setBackgroundResource(R.drawable.layout_bckgrnd);
            }
            view.invalidate();


            return view;
        }
    }
