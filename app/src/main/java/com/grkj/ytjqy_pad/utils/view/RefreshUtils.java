package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;

import com.grkj.ytjqy_pad.R;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;


/**
 * 头部刷新设置
 */

public class RefreshUtils {

    public static SmartRefreshLayout setRefreshConfig(Context context, SmartRefreshLayout refreshLayout) {
        //设置 Header 为 Material样式
        refreshLayout.setRefreshHeader(new MaterialHeader(context).setShowBezierWave(true));
        refreshLayout.setPrimaryColorsId(android.R.color.transparent);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //设置是否在加载更多完成之后滚动内容显示新数据
        refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new ClassicsFooter(context).setAccentColor(context.getResources().getColor(R.color.color_font_blue)));
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000);
//            }
//        });
        refreshLayout.setEnableLoadmore(true);
        return refreshLayout;
    }

}
