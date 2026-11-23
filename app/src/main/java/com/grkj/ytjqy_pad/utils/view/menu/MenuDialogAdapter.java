package com.grkj.ytjqy_pad.utils.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grkj.ytjqy_pad.R;

import java.util.List;

/**
 * Created by yusam on 2018/9/26.
 */

public class MenuDialogAdapter extends BaseAdapter {
    private List<MenuDialogBean> data;

    private Context context;

    public MenuDialogAdapter(Context context, List<MenuDialogBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<MenuDialogBean> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.isEmpty() || data.size() == 0 ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_menu, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) view.findViewById(R.id.mName);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.mIcon);
            view.setTag(viewHolder);//讲ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重获取viewHolder
        }

        viewHolder.mName.setText(data.get(position).name);
        Glide.with(context).load(data.get(position).icon).into(viewHolder.mIcon);

        return view;
    }

    //内部类
    class ViewHolder {
        TextView mName;
        ImageView mIcon;
    }

}
