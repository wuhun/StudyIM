package studyim.cn.edu.cafa.studyim.fragment.other;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.rong.ConversationListActivity;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.model.NoticeListModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunDateTool;
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
public class GroupGradeFragment extends BaseFragment {

    @BindView(R.id.rv_notice_list)
    WuhunRecyclerView rv_notice_list;
    @BindView(R.id.tv_enpty_hint)
    TextView tv_enpty_hint;

    LQRAdapterForRecyclerView<NoticeListModel.ResultBean> adapter;
    private List<NoticeListModel.ResultBean> dataList;

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        ConversationListActivity conversation = (ConversationListActivity) this.mActivity;
//        TestLog.i("班级fragment： id" + conversation.mTargetId +
//                " - title:" + conversation.mTitle +
//                " - groupId:" + conversation.mGroupId);
        if (conversation.mGroupId != null)
            HttpUtil.getGroupNoticeList(conversation.mGroupId, new Callback() {
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
                        NoticeListModel resultModel = MyApplication.getGson().fromJson(result, NoticeListModel.class);
//                        TestLog.i("==> " + resultModel.toString());
                        if (resultModel.getCode() == 1) {
                            final List<NoticeListModel.ResultBean> notices = resultModel.getResult();
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataList.clear();
                                    dataList = notices;
                                    if (dataList != null && dataList.size() > 0) {
                                        tv_enpty_hint.setVisibility(View.GONE);
                                    } else {
                                        tv_enpty_hint.setVisibility(View.VISIBLE);
                                    }
                                    adapter.setData(dataList);
                                }
                            });
                        }
                    }
                }
            });
    }

    @Override
    protected void initView() {
        dataList = new ArrayList<>();

        adapter = new LQRAdapterForRecyclerView<NoticeListModel.ResultBean>(mActivity, dataList, R.layout.group_notice_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, NoticeListModel.ResultBean item, int position) {
                tv_enpty_hint.setVisibility(View.GONE);
                NoticeListModel.ResultBean resultBean = dataList.get(position);
                if (resultBean != null) {
                    helper.setText(R.id.tv_notice_title, resultBean.getTITLE());
                    helper.setText(R.id.tv_notice_content, "\t\t" + resultBean.getCONTENT());
                    helper.setText(R.id.tv_author, resultBean.getAUTHOR());
                    long time = resultBean.getRELEASETIME().getTime();
                    if (time != 0) {
                        String dateFormat = WuhunDateTool.getDateFormat(time, WuhunDateTool.DATE4);
                        TestLog.i("time:" + dateFormat);
                        helper.setText(R.id.tv_time, dateFormat);
                    }
                }
            }
        };
        rv_notice_list.setAdapter(adapter);
    }

    @Override
    protected void getIntentData(Bundle arguments) {
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_group_class;
    }
}
