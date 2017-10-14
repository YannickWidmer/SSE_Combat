package ch.widmer.yannick.ssecombat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.widmer.yannick.ssecombat.fighters.Fighter;
import ch.widmer.yannick.ssecombat.fighters.PlayerCharacter;

/**
 * Created by Yannick on 01.04.2017.
 */

public class IdleAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Fighter> mIdlePlayers;
    private List<Fighter> mIdleFoes;

    private Comparator<Fighter> comparator = new Comparator<Fighter>() {
        @Override
        public int compare(Fighter o1, Fighter o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(mIdlePlayers, comparator);
        Collections.sort(mIdleFoes, comparator);
        super.notifyDataSetChanged();
    }

    public IdleAdapter(Context context, List<Fighter> players, List<Fighter> foes) {
        mIdlePlayers = players;
        mIdleFoes = foes;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mIdlePlayers.size() + 1)
            return 0;
        return 1;
    }

    @Override
    public int getCount() {
        return 2 + mIdleFoes.size() + mIdlePlayers.size();
    }

    @Override
    public Fighter getItem(int position) {
        int playerSize = mIdlePlayers.size();
        if (position == 0 || position == playerSize + 1)
            return null;
        if (position <= playerSize)
            return mIdlePlayers.get(position - 1);
        else
            return mIdleFoes.get(position - 2 - playerSize);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        boolean isTitle = getItemViewType(position) == 0;

        if (view == null)
            view = mInflater.inflate(isTitle ? R.layout.list_title : R.layout.list_text_item, null);

        if (isTitle) ((TextView) view).setText(position == 0 ? R.string.players : R.string.foes);
        else ((TextView) view).setText(getItem(position).getName());

        return view;
    }
}