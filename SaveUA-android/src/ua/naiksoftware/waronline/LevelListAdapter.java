package ua.naiksoftware.waronline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.badlogic.gdx.utils.Array;
import ua.naiksoftware.waronline.map.MapEntry;

public class LevelListAdapter extends BaseAdapter {

    private Array<MapEntry> list = new Array<MapEntry>();
    private Context context;
    private LayoutInflater li;

    public LevelListAdapter(Context context, Array<MapEntry> arr) {
        if (arr != null) {
            list = arr;
        }
        this.context = context;
        li = LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size;
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = li.inflate(R.layout.level_list_row, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.level_row_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MapEntry entry = list.get(position);

        holder.name.setText(entry.getName());
        return view;
    }

    private static class ViewHolder {

        TextView name;
    }
}
