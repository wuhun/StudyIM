package studyim.cn.edu.cafa.studyim.fragment.other;

import android.content.Intent;
import android.os.Bundle;
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
import studyim.cn.edu.cafa.studyim.activity.other.WebViewActivity;
import studyim.cn.edu.cafa.studyim.activity.rong.ConversationListActivity;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.model.GroupActiveModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunDateTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunImgTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/11 0011
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupActiveFragment extends BaseFragment {

    @BindView(R.id.rv_active_list)
    WuhunRecyclerView rv_active_list;
    @BindView(R.id.tv_enpty_hint)
    TextView tv_enpty_hint;

    List<GroupActiveModel.ResultBean> dataList;
    LQRAdapterForRecyclerView<GroupActiveModel.ResultBean> adapter;

    String beforeUri;

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                GroupActiveModel.ResultBean resultBean = dataList.get(position);
                if (resultBean != null) {
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.REQUEST_URI, resultBean.getCONTENT_URL());
                    jumpToActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        ConversationListActivity conversation = (ConversationListActivity) this.mActivity;
        TestLog.i("班级fragment： id" + conversation.mTargetId +
                " - title:" + conversation.mTitle +
                " - groupId:" + conversation.mGroupId +
        " - friendId:" + conversation.mFriendId);

        if (conversation.mGroupId != null) {
            HttpUtil.getActivityList(conversation.mGroupId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WuhunToast.normal(R.string.request_error_net).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if (response.isSuccessful() && !WuhunDataTool.isNullString(result)) {
                        GroupActiveModel resultModel = MyApplication.getGson().fromJson(result, GroupActiveModel.class);
//                        TestLog.i("==> " + resultModel.toString());

                        if (resultModel != null && resultModel.getCode() == 1) {
                            beforeUri = resultModel.getBefore();
                            final List<GroupActiveModel.ResultBean> activeList = resultModel.getResult();
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataList.clear();
                                    dataList = activeList;
                                    if (dataList != null && dataList.size() > 0) {
                                        tv_enpty_hint.setVisibility(View.GONE);
                                    } else {
                                        tv_enpty_hint.setVisibility(View.VISIBLE);
                                    }
                                    adapter.setData(dataList);
                                }
                            });
                        }
                    } else {
                        WuhunToast.info(R.string.server_connection_error).show();
                    }
                }
            });

        }
    }

    @Override
    protected void initView() {
        dataList = new ArrayList<>();

        adapter = new LQRAdapterForRecyclerView<GroupActiveModel.ResultBean>(mActivity, dataList, R.layout.group_active_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, GroupActiveModel.ResultBean item, int position) {
                GroupActiveModel.ResultBean resultBean = dataList.get(position);
                if (resultBean != null) {
                    TextView tv_notice_title = helper.getView(R.id.tv_notice_title);
                    TextView tv_notice_content = helper.getView(R.id.tv_notice_content);
                    ImageView image = helper.getView(R.id.img_picture);
                    TextView tv_author = helper.getView(R.id.tv_author);
                    if (resultBean.getTITLE() != null)
                        helper.setText(R.id.tv_notice_title, resultBean.getTITLE());
                    if (resultBean.getSUMMARY() != null) {
                        tv_notice_content.setText("\t\t" + resultBean.getSUMMARY());
                    } else {
                        tv_notice_content.setVisibility(View.GONE);
                    }
                    String resultUri = resultBean.getIMAGE();
                    if (resultUri != null && WuhunImgTool.isImage(resultUri) && WuhunImgTool.isImage(resultUri)) {
                        String uri = UserAvatarUtil.initUri(beforeUri, resultUri);
                        UserAvatarUtil.showImage(GroupActiveFragment.this, uri, image);
                    } else {
                        image.setVisibility(View.GONE);
                    }
                    if (resultBean.getSOURCE() != null) {
                        tv_author.setText(resultBean.getSOURCE());
                    }
                    long create_date = resultBean.getCREATE_DATE();
                    helper.setText(R.id.tv_time, WuhunDateTool.getDateFormat(create_date, WuhunDateTool.DATE4));
                }
            }
        };
        rv_active_list.setAdapter(adapter);
    }

    @Override
    protected void getIntentData(Bundle arguments) {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_group_active;
    }
}
