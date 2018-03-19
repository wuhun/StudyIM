package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.StudyShearchActivity;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.FriendGetAddList;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemLongClickListener;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

public class FriendGetAddListActivity extends BaseActivity {

    @BindView(R.id.rv_new_friend_list)
    WuhunRecyclerView rvNewFriendList;
    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;
    @BindView(R.id.ll_hint)
    LinearLayout ll_hint;

    private Context context;
    private List<FriendGetAddList> mData;
    private LQRAdapterForRecyclerView adapter;

    public static final int REQUEST_FAIL = 0x01;
    public static final int REQUEST_ERROR = 0x02;
    public static final int GET_ADD_FRIEND_LIST_SUCCESS = 0x03;
    public static final int ASKADDMSG_A = 0x04;//同意

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_get_add_list);
        ButterKnife.bind(this);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        bodyImgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendGetAddListActivity.this.finish();
            }
        });
        bodySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(StudyShearchActivity.class);
                FriendGetAddListActivity.this.finish();
            }
        });
    }

    private void initView() {
        context = this;
        mData = new ArrayList<>();
        headBg.setImageResource(R.mipmap.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText(R.string.new_friends);
        bodySearch.setVisibility(View.VISIBLE);
        bodySearch.setImageResource(R.drawable.searchl);

        adapter = new LQRAdapterForRecyclerView<FriendGetAddList>(context, mData, R.layout.activity_friend_get_add_list_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, FriendGetAddList item, final int position) {
                if (mData.size() <= 0) {
                    ll_hint.setVisibility(View.VISIBLE);
                } else {
                    ll_hint.setVisibility(View.GONE);
                }
                final FriendGetAddList friendAddList = mData.get(position);
                helper.setText(R.id.tv_user_name, friendAddList.getNICKNAME());
                helper.setText(R.id.tv_user_remark_msg, friendAddList.getREMARKNAME());
                ImageView imgAvatar = helper.getView(R.id.img_avater_item);
                UserAvatarUtil.showAvatar(context, item, Constant.HOME_URL, imgAvatar);

                TextView tvShowAgree = helper.getView(R.id.tv_show_agree);
                Button btnAskaddmsgOk = helper.getView(R.id.btn_askaddmsg_ok);
                if (friendAddList.getBUDDYSTS().equals("A")) {
                    btnAskaddmsgOk.setVisibility(View.GONE);
                    tvShowAgree.setVisibility(View.VISIBLE);
                }else if(friendAddList.getBUDDYSTS().equals("P")) {
                    btnAskaddmsgOk.setVisibility(View.VISIBLE);
                    tvShowAgree.setVisibility(View.GONE);
                }
                btnAskaddmsgOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发送同意请求 - 成功后更新adapte
                        HttpUtil.friendAskaddmsg(friendAddList.getUSERID() + "", "A", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                handler.sendEmptyMessage(REQUEST_ERROR);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String json = response.body().string();
                                    ResultModel model = getGson().fromJson(json, ResultModel.class);
//                                    WuhunDebug.debug("发送确认信息==>" + json);
                                    Message msg = handler.obtainMessage(ASKADDMSG_A, model);
                                    handler.sendMessage(msg);
                                    initData();
                                } else {
                                    handler.sendEmptyMessage(REQUEST_FAIL);
                                }
                            }
                        });
                    }
                });
            }
        };
        adapter.setOnItemClickListener(mOnItemClickListener);
        adapter.setOnItemLongClickListener(mOnItemLongClickListener);
        rvNewFriendList.setAdapter(adapter);
    }

    private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
            // TODO: 2017/12/7 长按显示菜单
//            adapter.removeItem(position);
            return false;
        }
    };

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
            // TODO: 2017/12/7 查看详细信息
            FriendGetAddList getFriendItem = mData.get(position);
            String userid = getFriendItem.getUSERID() + "";
            if(!WuhunDataTool.isNullString(userid)) {
//                WuhunDebug.debug("查看详细信息" + getFriendItem.toString());
                Intent intent = new Intent(context, FriendGetinfoActivity.class);
                intent.putExtra(FriendGetinfoActivity.GET_USERID, userid);
                jumpToActivity(intent);
            }
        }
    };

    private void initData() {
        HttpUtil.friend_GetAddList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(REQUEST_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
//                WuhunDebug.debug("FriendGetAddListActivity==>" + json);
                if (response.isSuccessful()) {
                    BaseModel<FriendGetAddList> model =
                            getGson().fromJson(json, new TypeToken<BaseModel<FriendGetAddList>>() {}.getType());
                    Message msg = handler.obtainMessage(GET_ADD_FRIEND_LIST_SUCCESS, model);
                    handler.sendMessage(msg);
                } else {
                    handler.sendEmptyMessage(REQUEST_FAIL);
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == GET_ADD_FRIEND_LIST_SUCCESS) {
                BaseModel<FriendGetAddList> model = ((BaseModel<FriendGetAddList>) msg.obj);
                if (model.getCode() == 1) {
                    mData = model.getResult();
                    adapter.setData(mData);
                } else {
                    resultFailToast(model.getMsg());
                }
            }else if (msg.what == REQUEST_ERROR) {
                WuhunToast.error(getResources().getString(R.string.request_error)).show();
            } else if (msg.what == REQUEST_FAIL) {
                WuhunToast.warning(getResources().getString(R.string.request_fail)).show();
            }else if(msg.what == ASKADDMSG_A) {
                //同意
                ResultModel model = ((ResultModel)msg.obj);
                if (model.getCode() == 1) {
                    WuhunToast.normal(R.string.add_success).show();
                    BroadcastManager.getInstance(context).sendBroadcast(Constant.UPDATE_CONSTACT_LIST);
                } else {
                    if (!WuhunDataTool.isNullString(model.getMsg())) {
                        WuhunToast.normal(model.getMsg()).show();
                    } else {
                        WuhunToast.normal(R.string.add_fail).show();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };
}
