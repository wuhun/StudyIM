package studyim.cn.edu.cafa.studyim.fragment.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.other.FriendGetinfoActivity;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendListModel;
import studyim.cn.edu.cafa.studyim.ui.MorePopWindow;
import studyim.cn.edu.cafa.studyim.ui.QuickIndexBar;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.SortUtils;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRHeaderAndFooterAdapter;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/10 0010
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MainContactFragment extends BaseFragment {


    @BindView(R.id.rv_contacts)
    WuhunRecyclerView rvContacts;
    @BindView(R.id.qib)
    QuickIndexBar mQib;
    @BindView(R.id.tvLetter)
    TextView mTvLetter;
    @BindView(R.id.img_more_pop)
    ImageView imgMorePop;
    @BindView(R.id.img_add_friend_dot)
    ImageView imgAddFriendDot;

    @BindView(R.id.rc_status_bar)
    LinearLayout rcStatusBar;
    @BindView(R.id.rc_status_bar_image)
    ImageView rcStatusBarImage;
    @BindView(R.id.rc_status_bar_text)
    TextView rcStatusBarText;

    @BindView(R.id.head_bg)
    ImageView headBg;//背景颜色
    @BindView(R.id.ll_all_body)
    LinearLayout llAllBody;

    private LQRHeaderAndFooterAdapter mAdapter;
    private List<Friend> mData;
    private TextView footerView;

    private static String HOME_URL;

    /**
     * 适配快速导航条
     */
    private QuickIndexBar.OnLetterUpdateListener mOnLetterUpdateListener = new QuickIndexBar.OnLetterUpdateListener() {
        @Override
        public void onLetterUpdate(String letter) {
            //显示对话框
            showLetter(letter);
            //滑动到第一个对应字母开头的联系人
            if ("↑".equalsIgnoreCase(letter)) {
                rvContacts.moveToPosition(0);//集合中移除
            } else if ("☆".equalsIgnoreCase(letter)) {
                rvContacts.moveToPosition(0);//集合中移除
            } else {
                List<Friend> data = ((LQRAdapterForRecyclerView) ((LQRHeaderAndFooterAdapter) rvContacts.getAdapter()).getInnerAdapter()).getData();
                for (int i = 0; i < data.size(); i++) {
                    Friend friend = data.get(i);
                    String noteName = WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getREMARKNAME()) + "";
                    if (WuhunDataTool.isNullString(noteName)) {
                        noteName = WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getNAME()) + "";
                    }
                    if (noteName.equalsIgnoreCase(letter)) {
                        rvContacts.moveToPosition(i);//移动到制定位置
                        break;
                    }
                }
            }
        }

        @Override
        public void onLetterCancel() {
            hideLetter();
        }
    };

    private void showLetter(String letter) {
        mTvLetter.setVisibility(View.VISIBLE);
        mTvLetter.setText(letter);
    }

    private void hideLetter() {
        mTvLetter.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mQib.setOnLetterUpdateListener(mOnLetterUpdateListener);
        imgMorePop.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_more_pop) {
//                int screenWidth = WuhunDeviceTool.getScreenWidth(mActivity);
                MorePopWindow morePopWindow = new MorePopWindow(mActivity);
//                llAllBody.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                int y = llAllBody.getHeight();
                morePopWindow.showPopupWindow(imgMorePop, 0, 0);
//                TestLog.i("剩余宽度==>" + (screenWidth - ) + " - " + WuhunSizeTool.dp2px(5));

//                // TODO: 2017/12/6 查看请求列表
//                jumpToActivity(FriendGetAddListActivity.class);
                imgAddFriendDot.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void initData() {
        registerBR();
        loadData();
    }

    private void loadData() {
        /** 获取好友列表 */
        final List<Friend> friends = DBManager.getmInstance().getFriends();
        if (WuhunNetTools.isAvailable(mActivity)) {
            HttpUtil.getFriendList(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(REQUEST_ERROR);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String result = response.body().string();
                    TestLog.i("好友列表===>" + result);

                    if (response.isSuccessful()) {
                        final FriendListModel friendList = getGson().fromJson(result, FriendListModel.class);
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (friendList != null && friendList.getCode() == 1) {
                                    HOME_URL = friendList.getBefore();
                                    if (friendList.getResult() != null) {
                                        updateBottom(friendList.getResult());
                                    } else {
                                        WuhunToast.info(R.string.get_contact_fail).show();
                                    }
                                } else {
                                    updateBottom(null);
                                    if (!WuhunDataTool.isNullString(friendList.getMsg())) {
                                        WuhunToast.normal(friendList.getMsg()).show();
                                    } else {
                                        WuhunToast.normal(R.string.get_fail).show();
                                    }
                                }
                            }
                        });
                    } else {
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WuhunToast.info(R.string.server_connection_error).show();
                            }
                        });
                    }
                }
            });
        } else {
//            TestLog.i("==> 本地数据库： " + friends.toString());
            if (friends != null)
                updateBottom(friends);
        }
    }

    private void unregisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(Constant.ADD_FRIEND_RED_DOT);
        BroadcastManager.getInstance(getActivity()).unregister(Constant.UPDATE_CONSTACT_LIST);
    }

    /**
     * 广播更新列表
     */
    private void registerBR() {
        BroadcastManager.getInstance(getActivity()).register(Constant.ADD_FRIEND_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // getActivity 设置父activity的tag提示
                imgAddFriendDot.setVisibility(View.VISIBLE);
            }
        });
        BroadcastManager.getInstance(getActivity()).register(Constant.UPDATE_CONSTACT_LIST, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                TestLog.i("收到广播更新列表………………");
                loadData(); //更新列表
            }
        });
    }

    public static final int REQUEST_ERROR = 0x03;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REQUEST_ERROR) {
                if(MainContactFragment.this.isAdded())
                    WuhunToast.error(getResources().getString(R.string.request_error_net)).show();
            }
            super.handleMessage(msg);
        }
    };


    private void updateBottom(List<Friend> friends) {
        if (friends != null && friends.size() > 0) {
            DBManager.getmInstance().setAllUserInfo(friends, HOME_URL);
            mData.clear();
            mData.addAll(friends);

            footerView.setVisibility(View.VISIBLE);
            footerView.setText(String.format(getString(R.string.constact), mData.size()));
            //整理排序
            mData = SortUtils.sortContacts(mData);
        } else {
            footerView.setVisibility(View.VISIBLE);
            footerView.setText(R.string.no_contact);
            mData.clear();
        }
        if (mAdapter != null)
            adapter.setData(mData);
//        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initView() {
        mData = new ArrayList<>();
        setAdapter();
    }

    @Override
    protected void getIntentData(Bundle arguments) {
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_contact_fragment;
    }


    LQRAdapterForRecyclerView adapter;
    private void setAdapter() {
        if (mAdapter == null) {
            adapter = new LQRAdapterForRecyclerView<Friend>(mActivity, mData, R.layout.item_contact) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Friend item, int position) {
                    helper.setText(R.id.tvName, item.getREMARKNAME());

                    ImageView imgAvatar = helper.getView(R.id.img_contact_avater_item);

                    String uri = UserAvatarUtil.initUri(HOME_URL, item.getAVATAR());
                    final String avatarUri = UserAvatarUtil.getAvatarUri(
                            item.getUSERBUDDYID() + "", item.getREMARKNAME(), uri);
                    UserAvatarUtil.showImage(MainContactFragment.this, avatarUri, imgAvatar);


                    String str = "";
                    //得到当前字母
                    String currentLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(item.getREMARKNAME()) + "";
                    if (position == 0) {
                        str = currentLetter;
                    } else {
                        //得到上一个字母
                        String preLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(mData.get(position - 1).getREMARKNAME()) + "";
                        //如果和上一个字母的首字母不同则显示字母栏
                        if (!preLetter.equalsIgnoreCase(currentLetter)) {
                            str = currentLetter;
                        }
                    }
                    int nextIndex = position + 1;
                    if (nextIndex < mData.size() - 1) {
                        //得到下一个字母
                        String nextLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(mData.get(nextIndex).getREMARKNAME()) + "";
                        //如果和下一个字母的首字母不同则隐藏下划线
                        if (!nextLetter.equalsIgnoreCase(currentLetter)) {
                            helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                        } else {
                            helper.setViewVisibility(R.id.vLine, View.VISIBLE);
                        }
                    } else {
                        helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                    }
                    if (position == mData.size() - 1) {
                        helper.setViewVisibility(R.id.vLine, View.GONE);
                    }

                    //根据str是否为空决定字母栏是否显示
                    if (TextUtils.isEmpty(str)) {
                        helper.setViewVisibility(R.id.tvIndex, View.GONE);
                    } else {
                        helper.setViewVisibility(R.id.tvIndex, View.VISIBLE);
                        helper.setText(R.id.tvIndex, str);
                    }
                }
            };
//            adapter.addHeaderView(getFooterView());
            adapter.addFooterView(getFooterView());
            mAdapter = adapter.getHeaderAndFooterAdapter();
            rvContacts.setAdapter(mAdapter);
        }
        ((LQRAdapterForRecyclerView)
                mAdapter.getInnerAdapter()).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
//                WuhunToast.info("点击了：" + (position)).show(); //有头部 -1：没有：position
                Friend friend = mData.get(position);
                String userid = friend.getUSERBUDDYID();
                Intent intent = new Intent(mActivity, FriendGetinfoActivity.class);
                intent.putExtra(FriendGetinfoActivity.GET_USERID, userid);
//                intent.putExtra(FriendGetinfoActivity.GET_USERID, userid);
                jumpToActivity(intent);
            }
        });
    }

    public View getFooterView() {
        footerView = new TextView(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        footerView.setGravity(Gravity.CENTER);
        footerView.setPadding(0, 10, 0, 10);
        footerView.setTextColor(Color.parseColor("#FBFBFB"));
//        footerView.setBackgroundResource(R.color.gray5);
        return footerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setNotificationBarVisibility(RongIM.getInstance().getCurrentConnectionStatus());
    }

    /**
     * 提示
     */
    private void setNotificationBarVisibility(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        if (this.getResources().getBoolean(io.rong.imkit.R.bool.rc_is_show_warning_notification)) {
            String content = null;
            if (status.equals(ConnectionStatus.NETWORK_UNAVAILABLE)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable);
            } else if (status.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick);
            } else if (status.equals(ConnectionStatus.CONNECTED)) {
                rcStatusBar.setVisibility(View.GONE);
            } else if (status.equals(ConnectionStatus.DISCONNECTED)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_disconnect);
            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting);
            }

            if (content != null && this.rcStatusBar != null) {
                final String statusText = content;
                if (rcStatusBar.getVisibility() == View.GONE) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                                rcStatusBar.setVisibility(View.VISIBLE);
                                rcStatusBarText.setText(statusText);
                                if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                                    rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_connecting_animated);
                                } else {
                                    rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_network_available);
                                }
                            }

                        }
                    }, 4000L);
                } else {
                    rcStatusBarText.setText(content);
                    if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTING)) {
                        rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_connecting_animated);
                    } else {
                        rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_network_available);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBR();
        handler.removeCallbacksAndMessages(null);
    }

}
