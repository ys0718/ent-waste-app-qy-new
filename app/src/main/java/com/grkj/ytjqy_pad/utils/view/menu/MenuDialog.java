package com.grkj.ytjqy_pad.utils.view.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.view.CustomDialog;

import java.util.List;

/**
 * Created by chenerte on 2017/12/21.
 */

public class MenuDialog {

    private Context mContext;
    private GridView mGridView;
    private MenuDialogAdapter mAdapter;
    private CustomDialog.Builder mBuilder;

    public MenuDialog(Context mContext, MenuClickItemListenerInterface mClickItemListenerInterface) {
        this.mContext = mContext;
        this.mClickItemListenerInterface = mClickItemListenerInterface;
    }

    public void show(List<MenuDialogBean> mDatas) {
        View viewChoose = LayoutInflater.from(mContext).inflate(R.layout.dialog_menu, null);
        mGridView = (GridView) viewChoose.findViewById(R.id.mGridView);
        mAdapter = new MenuDialogAdapter(mContext, mDatas);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBuilder.closeDialog();
                    }
                },500);
                mClickItemListenerInterface.onDo(i);
            }
        });

        mBuilder = new CustomDialog.Builder(mContext);
        mBuilder.setContentView(viewChoose, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBuilder.setTitle("请选择");
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.create().show();
    }


    private MenuClickItemListenerInterface mClickItemListenerInterface;

    public interface MenuClickItemListenerInterface {
        void onDo(int index);
    }

    public void setClicklistener(MenuClickItemListenerInterface mClickItemListenerInterface) {
        this.mClickItemListenerInterface = mClickItemListenerInterface;
    }

}
