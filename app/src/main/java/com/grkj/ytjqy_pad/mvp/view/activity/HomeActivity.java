package com.grkj.ytjqy_pad.mvp.view.activity;

import static com.grkj.ytjqy_pad.gather.Constants.USER_NAME;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.HomeTotalBean;
import com.grkj.ytjqy_pad.mvp.model.JyxwUserInfoBean;
import com.grkj.ytjqy_pad.mvp.model.MenuBean;
import com.grkj.ytjqy_pad.mvp.model.NoticeBean;
import com.grkj.ytjqy_pad.mvp.presenter.HomePresenter;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.BluetoothPermissionHelper;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.GrRecyclerView;
import com.grkj.ytjqy_pad.utils.view.NoticeDialog;
import com.grkj.ytjqy_pad.utils.view.RefreshUtils;
import com.grkj.ytjqy_pad.utils.view.listdialog.WarmingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.xujiaji.happybubble.BubbleDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by yusam on 2022/3/20.
 * 菜单页
 */

public class HomeActivity extends BaseActivity<HomePresenter> implements HomePresenter.View {


    @BindView(R.id.mVersion)
    TextView mVersion;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.iv_left)
    ImageView iv_left;

    @BindView(R.id.iv_right)
    ImageView iv_right;

    @BindView(R.id.btnRv)
    GrRecyclerView btnRv;

    SmartRefreshLayout refresh_layout;

    private String menuListStr;

    private boolean isSjdw = false;

    private int lastMenuIndex = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initEventAndData() {
        mNeedBackGesture = false;
        tv_right.setText(DateTimeUtil.getYMDW());
        mVersion.setText("当前版本号:V" + AppUtils.getVersionName(mContext));
        menuListStr = getString();
        mMenuList = GsonUtil.JsonToList(menuListStr, MenuBean.class);

//        CrashReport.testJavaCrash();

        if (mMenuList == null || mMenuList.size() == 0) {
            ToastUtil.showToast(mContext, "该用户未绑定菜单，请联系管理员！");
            finish();
            return;
        }

        lastMenuIndex = (int) SPUtils.get(mContext, Constants.LAST_MENU_INDEX, 0);
        if (lastMenuIndex < 0 || lastMenuIndex >= mMenuList.size()) {
            lastMenuIndex = 0;
        }

        //默认展示第一菜单
        mMenuList.get(lastMenuIndex).SELECT = true;
        isSjdw = menuListStr.contains("SjInputListActivity") || menuListStr.contains("SjOutputListActivity");


        getHnDwList();
        mPresenter.getLastedNotice();
        initBottom();
    }

    private String getString() {
        String menuListStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        return menuListStr;
    }



    /**
     * 单位列表
     */
    private void getHnDwList() {
        boolean isHn = false;
        for (int i = 0; i < mMenuList.size(); i++) {//判断是不是湖南环境
            if ((mMenuList.get(i).href.contains(".hn."))) {
                isHn = true;
            }
        }
        if (isHn) {
            mPresenter.getDwList(new HashMap<>());
        }
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initBtnRecycleView(isLandscape);
//        initGridView();
    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initBtnRecycleView(isLandscape);
//        getConnectedBltInfo();
        TextView tv_top2 = findViewById(R.id.tv_top2);
        String username = SPUtils.get(mContext, USER_NAME, "").toString();
        tv_top2.setText(getString(R.string.common_welcome, username));

        ((TextView) findViewById(R.id.tv_tips)).setText(getString(R.string.str_current_tips, DateTimeUtil.getCurrentTime()));


//        initRecycleView();

        refresh_layout = findViewById(R.id.refresh_layout);
        RefreshUtils.setRefreshConfig(mContext, refresh_layout)
                .setEnableLoadmore(false)
                .setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isSjdw) {
                                    mPresenter.getTotalInfoSjdw();
                                } else {
                                    mPresenter.getTotalInfo();
                                }

                            }
                        }, 200);
                    }
                });


        bluetoothPermissionHelper = new BluetoothPermissionHelper(this, new BluetoothPermissionHelper.Callback() {
            @Override
            public void onPermissionsGranted(int requestCode) {
                // 权限已获取，静默匹配蓝牙设备（后台扫描，不弹全屏loading遮挡页面）
                matchDevice();
                // 例如：开始扫描、连接等
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> deniedPermissions) {
                // 权限被拒绝，可根据需求提示用户或降级处理
                PermissionUtils.showSettingDialog(mContext, "蓝牙");
                // 可以检查是否勾选“不再询问”，并引导用户去设置页面
            }
        });
        bluetoothPermissionHelper.requestPermissions(REQUEST_BLUETOOTH_PERMS);


    }


    public static void setDrawableWithName(Context context, ImageView imageView, String resName) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(resName, "mipmap", context.getPackageName());
        imageView.setImageResource(resId);
    }

    public static void setBgWithName(Context context, LinearLayout bg_layout, String resName) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(resName, "mipmap", context.getPackageName());
        bg_layout.setBackgroundResource(resId);
    }


    private List<MenuBean> mMenuList;//菜单列表
    private CommonAdapter<MenuBean> adapter;//菜单列表适配器
    private static final int MAX_COUNT = 8;


    GridLayoutManager mLinearLayoutManager;
    CommonAdapter<MenuBean> btnAdapter;

    /**
     * 按钮列表初始化
     */
    private void initBtnRecycleView(boolean isLandscape) {
        mLinearLayoutManager = new GridLayoutManager(mContext, 6);
        btnRv.setLayoutManager(mLinearLayoutManager);
        btnAdapter = new CommonAdapter<MenuBean>(mContext, R.layout.item_select_btn, mMenuList) {
            @Override
            protected void convert(ViewHolder holder, final MenuBean bean, final int position) {
                if (bean != null) {
                    TextView tv_value = holder.getView(R.id.tv_value);
                    tv_value.setText(bean.name);
                    tv_value.setSelected(bean.SELECT);
                }
            }
        };

        btnAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                for (int j = 0; j < mMenuList.size(); j++) {
                    mMenuList.get(j).SELECT = false;
                }
                mMenuList.get(i).SELECT = true;
                btnAdapter.notifyDataSetChanged();
                if (isLandscape) {
                    initGridView(mMenuList.get(i).childList);
                } else {
                    initRecycleView(mMenuList.get(i).childList);
                }

                if (i != mMenuList.size() - 1) { //除了设置系统设置菜单，其他菜单点击保存最后点击的菜单
                    SPUtils.put(mContext, Constants.LAST_MENU_INDEX, i);
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });


        btnRv.setAdapter(btnAdapter);

        //默认展示选中菜单的子菜单（原先依赖 convert 回调触发，绑定时机不可控，导致首屏子菜单加载延迟）
        if (isLandscape) {
            initGridView(mMenuList.get(lastMenuIndex).childList);
        } else {
            initRecycleView(mMenuList.get(lastMenuIndex).childList);
        }
    }

    /**
     * 竖版菜单列表初始化
     */
    private void initRecycleView(List<MenuBean> childList) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CommonAdapter<MenuBean>(mContext, R.layout.item_home_menu, childList) {
            @Override
            protected void convert(ViewHolder holder, final MenuBean bean, final int position) {
                if (bean != null) {
                    ImageView iv_logo = holder.getView(R.id.iv_logo);
                    setDrawableWithName(mContext, iv_logo, bean.icon);
                    holder.setText(R.id.tv_name, bean.name);

                    TextView tv_name = holder.getView(R.id.tv_name);
                    tv_name.setEllipsize(TextUtils.TruncateAt.START);
                    if (bean.name.length() > 6) {
                        tv_name.setTextSize(10f);
                    }
                    if (bean.name.length() > 8) {
                        tv_name.setTextSize(8.5f);
                    }

                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                onClickItem(childList, i);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });


        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 横板菜单列表初始化
     */
    private void initGridView(List<MenuBean> mMenuList) {

        int count = 1;
        if (mMenuList.size() > MAX_COUNT) {
            count = 4;
            iv_right.setVisibility(View.VISIBLE);
//            iv_left.setVisibility(View.VISIBLE);
        } else if (mMenuList.size() > MAX_COUNT - 2) {
            count = 4;
        } else if (mMenuList.size() > MAX_COUNT - 4) {
            count = 3;
        } else if (mMenuList.size() > MAX_COUNT - 6) {
            count = 2;
        }
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = (wm.getDefaultDisplay().getWidth() - 300) / count;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return true;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mMenuList.size() > MAX_COUNT) {//超过8个时
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {//滑动结束
                        boolean isShowLeft = true;
                        boolean isShowRight = true;
                        if (!mRecyclerView.canScrollHorizontally(1)) {//到达最右侧
                            isShowRight = false;
                        } else if (!mRecyclerView.canScrollHorizontally(-1)) {//到达最左侧
                            isShowLeft = false;
                        }
                        //左右箭头显示处理
                        iv_left.setVisibility(isShowLeft ? View.VISIBLE : View.GONE);
                        iv_right.setVisibility(isShowRight ? View.VISIBLE : View.GONE);
                    }
                }

            }
        });

        adapter = new CommonAdapter<MenuBean>(mContext, R.layout.item_home_menu, mMenuList) {
            @Override
            protected void convert(ViewHolder holder, final MenuBean bean, final int position) {
                if (bean != null) {
                    LinearLayout view = holder.getView(R.id.mMenu);
                    GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    params.width = width;
                    view.setLayoutParams(params);

                    LinearLayout bg_layout = holder.getView(R.id.bg_layout);
                    setBgWithName(mContext, bg_layout, bean.iconUrl);

                    ImageView iv_logo = holder.getView(R.id.iv_logo);
                    setDrawableWithName(mContext, iv_logo, bean.icon);

                    holder.setText(R.id.tv_name, bean.name);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                onClickItem(mMenuList, i);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });


        mRecyclerView.setAdapter(adapter);
    }


    private void onClickItem(List<MenuBean> mMenuList, int i) {
        if (TextUtils.isEmpty(mMenuList.get(i).href)) {
            ToastUtil.showToast(mContext, "该菜单未配置跳转路由，请联系管理员！");
        } else {
            if (mMenuList.get(i).href.contains(".hn.")) {
                //社会源收集 经营出入库登记
                if (!mMenuList.get(i).href.contains("Collect") && !mMenuList.get(i).href.contains("Waste")) {
                    if (popUserList.size() == 0) {
                        ToastUtil.showToast(mContext, "该菜单不可用，请联系管理员！");
                        return;
                    }

                    WarmingDialog warmingDialog = new WarmingDialog(mContext, popUserList);
                    warmingDialog.setConfirmListener(new WarmingDialog.ConfirmListener() {
                        @Override
                        public void onConfirmClick(String id) {
                            warmingDialog.dismiss();
                            SPUtils.put(mContext, Constants.SELECT_DWID, id);
                            try {
                                Class mActivity = Class.forName(mMenuList.get(i).href);
                                startActivity(new Intent(mContext, mActivity));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                    warmingDialog.show();
                } else {
                    try {
                        SPUtils.put(mContext, Constants.SELECT_DWID, "");
                        Class mActivity = Class.forName(mMenuList.get(i).href);
                        startActivity(new Intent(mContext, mActivity));

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
//                goToActivity(new Intent(mContext, WasteOutputListActivity.class));
                try {
                    SPUtils.put(mContext, Constants.SELECT_DWID, "");
                    Class mActivity = Class.forName(mMenuList.get(i).href);
                    startActivity(new Intent(mContext, mActivity).putExtra(Constants.MENU_TYPE, mMenuList.get(i).type));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    //记录用户首次点击返回键的时间
    private long mFirstTime = 0;

    @Override
    //键盘按键松开
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - mFirstTime > 2000) {
                    ToastUtil.showToast(mContext, "再按一次退出程序");
                    mFirstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void showError(String msg) {
        if (!isLandscape) {
            refresh_layout.finishRefresh();
        }
        ToastUtil.showToast(mContext, msg);
    }

    @Override
    public void getTotalInfoSuccess(Object bean) {

        if (!isLandscape) {
            refresh_layout.finishRefresh();
        }

        HomeTotalBean homeTotalBean = GsonUtil.objectToBean(bean, HomeTotalBean.class);

        TextView tv_cflj = findViewById(R.id.tv_cflj);
        TextView tv_rklj = findViewById(R.id.tv_rklj);
        TextView tv_cklj = findViewById(R.id.tv_cklj);


        if (isSjdw) {
            LinearLayout ll_cflj = findViewById(R.id.ll_cflj);
            ll_cflj.setVisibility(View.GONE);
            tv_rklj.setText(homeTotalBean.rkl);
            tv_cklj.setText(homeTotalBean.ckl);
        } else {
            tv_cflj.setText(homeTotalBean.csl);
            tv_rklj.setText(homeTotalBean.rkl);
            tv_cklj.setText(homeTotalBean.ckl);
        }


        tv_cflj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(tv_cflj);
            }
        });

        tv_rklj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(tv_rklj);
            }
        });

        tv_cklj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(tv_cklj);
            }
        });
    }

    private List<JyxwUserInfoBean> userList = new ArrayList<>();

    private List<SelectPopupWindowBean> popUserList = new ArrayList<>();


    @Override
    public void getDwListSuccess(Object rows) {
        userList = GsonUtil.JsonToList(GsonUtil.GsonToString(rows), JyxwUserInfoBean.class);
        for (int i = 0; i < userList.size(); i++) {
            SelectPopupWindowBean bean = new SelectPopupWindowBean(userList.get(i).id, userList.get(i).entername);
            popUserList.add(bean);
        }
    }

    @Override
    public void getLastedNoticeSuccess(NoticeBean noticeBean) {
//        SPUtils.remove(mContext, "noticeId");
        String noticeId = SPUtils.get(mContext, "noticeId", "").toString();
        boolean no_cache = TextUtils.isEmpty(noticeId) || !TextUtils.equals(noticeBean.content.id, noticeId);//无公告缓存判断
        if (TextUtils.equals(noticeBean.isHasNewNotice, "1") && no_cache) {//有最新公告且无公告缓存
            NoticeDialog dialog = new NoticeDialog(mContext, noticeBean);
            dialog.setConfirmListener(new NoticeDialog.ConfirmListener() {
                @Override
                public void onConfirmClick() {
                    dialog.dismiss();
                }

                @Override
                public void onCancelClick() {
                    SPUtils.put(mContext, "noticeId", noticeBean.content.id);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    //显示详情气泡弹窗
    private void showTips(TextView tv) {
        View tips_view = LayoutInflater.from(this).inflate(R.layout.dialog_tips, null);
        TextView tv_title = tips_view.findViewById(R.id.tv_title);
        tv_title.setText(tv.getText().toString());

        BubbleDialog mBubbleDialog = new BubbleDialog(this)
                .addContentView(tips_view)
                .setClickedView(tv)
//                    .setPosition(mPosition)
                .setOffsetY(8);
        mBubbleDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLandscape) {
            refresh_layout.autoRefresh();
        }
        //置空选中的dwid
        SPUtils.put(mContext, Constants.SELECT_DWID, "");
        SPUtils.put(mContext, Constants.IS_HN_MENU, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private static final int REQUEST_BLUETOOTH_PERMS = 100;
    private BluetoothPermissionHelper bluetoothPermissionHelper;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果交给 Helper 处理
        bluetoothPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 根据蓝牙名称和mac匹配对应设备
     */
    @SuppressLint("MissingPermission")
    private void matchDevice() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BleManager.getInstance().scan(new BleScanCallback() {
                    @Override
                    public void onScanFinished(List<BleDevice> scanResultList) {
                        if (scanResultList.size() == 1) {
                            BleDevice device = scanResultList.get(0);
                            SPUtils.put(mContext, Constants.PRINTER_MAC, device.getMac());
                            SPUtils.put(mContext, Constants.PRINTER_NAME, device.getName());
                        }
                    }

                    @Override
                    public void onScanStarted(boolean success) {
                    }

                    @Override
                    public void onScanning(BleDevice bleDevice) {
                    }
                });

                //获取已连接蓝牙设备
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
                final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for (BluetoothDevice device : bondedDevices) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (Constants.checkBluetoothInList(device)) {
                        map.put("DeviceName", device.getName());
                        map.put("BDAddress", device.getAddress());
                        map.put("BondState", device.getBondState() + "");
                        list.add(map);
                    }
                }

                if (list.size() == 1) {
                    SPUtils.put(mContext, Constants.PRINTER_MAC, list.get(0).get("BDAddress"));
                    SPUtils.put(mContext, Constants.PRINTER_NAME, list.get(0).get("DeviceName"));
                }
            }
        });
    }


}
