package studyim.cn.edu.cafa.studyim.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendListModel;
import studyim.cn.edu.cafa.studyim.ui.QuickIndexBar;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.SortUtils;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRHeaderAndFooterAdapter;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

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
    @BindView(R.id.img_add_friend)
    ImageView imgAddFriend;

    @BindView(R.id.head_bg)
    ImageView headBg;//背景颜色

    private LQRHeaderAndFooterAdapter mAdapter;
    private List<Friend> mData;
    private TextView footerView;

    private static String HOME_URL;

//    @OnClick({R.id.img_add_friend})
//    public void onClick(View view){
//        if(view.getId() == R.id.img_add_friend) {
////            jumpToActivity(AddFriend);
//            WuhunToast.normal("查找添加好友").show();
//        }
//    }

    /** 适配快速导航条 */
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
    }

    @Override
    protected void initData() {
        setAdapter();
        loadData();
    }

    private void loadData() {
        HttpUtil.getFriendList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(REQUEST_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    FriendListModel friendList = MyApplication.getGson().fromJson(result, FriendListModel.class);
                    if (null == friendList || null == friendList.getResult()) {
                        handler.sendEmptyMessage(REQUEST_FAIL);
                        return;
                    } else {
                        Message msg = handler.obtainMessage(FRIEND_LIST_SUCCESS, friendList);
                        handler.sendMessage(msg);
                    }
                } else {
                    handler.sendEmptyMessage(REQUEST_FAIL);
                }
            }
        });
//        List<Friend> friends = new ArrayList<>();
//        friends.add(new Friend("1","http://tupian.qqjay.com/tou2/2017/0524/d0991c6179bf6ebe68f443859f27254a.jpg","囚徒","囚徒","囚徒"));
//        friends.add(new Friend("2","http://tupian.qqjay.com/tou2/2017/0524/595255a4d8c66da4e6a861d748c0899c.jpg","冷酷谋杀犯","冷酷谋杀犯","冷酷谋杀犯"));
//        friends.add(new Friend("3","http://tupian.qqjay.com/tou2/2017/0701/7db9580e389dd4f2121b93d716cc0387.jpg","冷酷谋杀犯","冷酷谋杀犯","冷酷谋杀犯"));
//        friends.add(new Friend("4","http://tupian.qqjay.com/tou2/2017/0701/ef41afc6dc7b543ec509f94328810423.jpg","记得笑","记得笑","记得笑"));
//        friends.add(new Friend("5","http://tupian.qqjay.com/tou2/2017/0701/b5e38a8bf690cbacd17b36c1b988f6f1.jpg","孤神","孤神","孤神"));
//        friends.add(new Friend("6","http://tupian.qqjay.com/tou2/2017/0701/99ebdcc1ec2dc7c6a2448144f0674669.png","无极","无极","无极"));
//        friends.add(new Friend("7","http://tupian.qqjay.com/tou2/2017/0701/c9eb2682b9c0a6f9a89520a8b5ab3818.jpg","入言梦","入言梦","入言梦"));
//        friends.add(new Friend("8","http://tupian.qqjay.com/tou2/2017/0701/149ea01cb1e26f8b25b69f9b4bd67700.jpg","怎深拥你i","怎深拥你i","怎深拥你i"));
//        friends.add(new Friend("9","http://tupian.qqjay.com/tou3/2016/1221/33ddf51ce1e3dd5a56b20c28ea50717b.jpg","失心疯","失心疯","失心疯"));
//        friends.add(new Friend("10","http://tupian.qqjay.com/tou3/2016/1221/719adfb657ec7e7958d194122f7bc023.jpg","翻跟斗","翻跟斗","翻跟斗"));
//        friends.add(new Friend("11","http://tupian.qqjay.com/tou3/2016/1221/719adfb657ec7e7958d194122f7bc023.jpg","翻跟斗","翻跟斗","翻跟斗"));
//        friends.add(new Friend("12","http://tupian.qqjay.com/tou3/2016/1221/0a561ab522b8c14b534fc849e030fa12.jpg","好姑凉前途无量","好姑凉前途无量","好姑凉前途无量"));
//        friends.add(new Friend("13","http://tupian.qqjay.com/tou2/2017/0524/d0991c6179bf6ebe68f443859f27254a.jpg","45","45","45"));
//        friends.add(new Friend("14","http://tupian.qqjay.com/tou2/2017/0524/d0991c6179bf6ebe68f443859f27254a.jpg","434","434","434"));
//        friends.add(new Friend("15","http://tupian.qqjay.com/tou2/2017/0524/d0991c6179bf6ebe68f443859f27254a.jpg","464","464","464"));
//        friends.add(new Friend("15","http://tupian.qqjay.com/tou2/2017/0524/d0991c6179bf6ebe68f443859f27254a.jpg","!@#$","!@#$","!@#$"));
//        friends.add(new Friend("15","http://tupian.qqjay.com/tou2/2017/0524/d0991c6179bf6ebe68f443859f27254a.jpg","@#$","@#$","@#$"));
//        friends.add(new Friend("16","http://tupian.qqjay.com/tou3/2016/1221/4d7f0d1cd71545086ceaac05504c7e4a.jpg","admin","admin","admin"));
//        friends.add(new Friend("17","http://tupian.qqjay.com/tou3/2016/1221/4d7f0d1cd71545086ceaac05504c7e4a.jpg","呆到深处自然萌","呆到深处自然萌","呆到深处自然萌"));
//        friends.add(new Friend("18", null,"张三","张三","张三"));

//        updateBottom(friends);
    }

    public static final int FRIEND_LIST_SUCCESS = 0x01;
    public static final int REQUEST_FAIL = 0x02;
    public static final int REQUEST_ERROR = 0x03;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REQUEST_ERROR) {
                WuhunToast.error(getResources().getString(R.string.request_error)).show();
            }else if(msg.what == REQUEST_FAIL) {
                WuhunToast.warning(getResources().getString(R.string.request_fail)).show();
            }else if(msg.what == FRIEND_LIST_SUCCESS) {
                FriendListModel model = (FriendListModel)msg.obj;
                HOME_URL = model.getBefore();

                WuhunDebug.debug("=success-获取好友列表=>" + model.getResult());
                if (model.getCode() == 1) {
                    List<Friend> friends = new ArrayList<>();
                    friends.addAll(model.getResult());
                    updateBottom(friends);
//                    WuhunDebug.debug("===>" + model.getResult());
                } else {
                    if (WuhunDataTool.isNullString(model.getMsg())) {
                        WuhunToast.normal(model.getMsg()).show();
                    } else {
                        WuhunToast.normal("获取失败").show();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    private void updateBottom(List<Friend> friends) {
        if (friends != null && friends.size() > 0) {
            mData.clear();
            mData.addAll(friends);

            if (mData.size() == 0) {
//                footerView.setVisibility(View.GONE);
                footerView.setText("您还没有联系人，去添加吧！");
            } else {
                footerView.setVisibility(View.VISIBLE);
                footerView.setText("联系人："+ mData.size());
            }
            //整理排序
            SortUtils.sortContacts(mData);
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initView() {
        mData = new ArrayList<>();
    }

    @Override
    protected void getIntentData(Bundle arguments) { }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_contact_fragment;
    }

    private void setAdapter() {
        if (mAdapter == null) {
            LQRAdapterForRecyclerView adapter = new LQRAdapterForRecyclerView<Friend>(mActivity, mData, R.layout.item_contact) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Friend item, int position) {
                    helper.setText(R.id.tvName, item.getREMARKNAME());

                    ImageView imgAvatar = helper.getView(R.id.img_avater_item);
                    UserAvatarUtil.showAvatar(mActivity, item, HOME_URL, imgAvatar);//头像

                    String str = "";
                    //得到当前字母
//                    String currentLetter = item.getDisplayNameSpelling().charAt(0) + "";
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
                WuhunToast.info("点击了：" + (position)).show(); //有头部 -1：没有：position
            }
        });
    }

    public View getFooterView() {
        footerView = new TextView(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        footerView.setGravity(Gravity.CENTER);
        footerView.setPadding(0,10,0,10);
//        footerView.setBackgroundResource(R.color.gray5);
        return footerView;
    }

}
