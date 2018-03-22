package studyim.cn.edu.cafa.studyim.fragment.study;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileMediaTool;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

/**
 * 作者：悟魂 ————2017/11/18.
 * 版本：1.0
 * 说明：
 */

public class StudyAllFragment extends BaseFragment {

    @BindView(R.id.rv_conversation)
    WuhunRecyclerView rvConverstation;

    LQRAdapterForRecyclerView<Conversation> mAdapter;
    List<Conversation> mData;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
//                WuhunDebug.debug("返回" + conversations.size());
//                for (Conversation con : conversations) {
//                    WuhunDebug.debug("类型:" + con.getConversationType() +
//                            "\n对方id" + con.getTargetId() +
//                            "\n时间：" + WuhunDateTool.getDateFormat(con.getReceivedTime(), WuhunDateTool.DATE1) +
//                            "\n是否置顶" + con.isTop() +
//                            "\n未读消息数" + con.getUnreadMessageCount() +
//                            "\n文字消息草稿" + con.getDraft() +
//                            "\n最后消息类型是否为图片" + (con.getLatestMessage() instanceof ImageMessage));
//                }
                mData = conversations;
                mAdapter.setData(mData);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                WuhunDebug.debug("getConversationList error");
            }
        });
    }

    @Override
    protected void initView() {
        mData = new ArrayList<>();

        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<Conversation>(mActivity, mData, R.layout.study_conversation_item) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Conversation item, int position) {

                    helper.setBackgroundColor(R.id.flRoot, item.isTop() ? R.color.gray8 : android.R.color.white)
                            .setText(R.id.tvCount, item.getUnreadMessageCount() + "")
                            .setViewVisibility(R.id.tvCount, item.getUnreadMessageCount() > 0 ? View.VISIBLE : View.GONE);

                    TextView tvContent = helper.getView(R.id.tvContent);
                    if (!TextUtils.isEmpty(item.getDraft())) {
//                        MoonUtils.identifyFaceExpression(mActivity, tvContent, item.getDraft(), ImageSpan.ALIGN_BOTTOM);
                        helper.setViewVisibility(R.id.tvDraft, View.VISIBLE);
                        return;
                    } else {
                        helper.setViewVisibility(R.id.tvDraft, View.GONE);
                    }


                    if (item.getLatestMessage() instanceof TextMessage) {
//                        MoonUtils.identifyFaceExpression(mContext, tvContent, ((TextMessage) item.getLatestMessage()).getContent(), ImageSpan.ALIGN_BOTTOM);
                    } else if (item.getLatestMessage() instanceof ImageMessage) {
                        tvContent.setText("[图片]");
                    } else if (item.getLatestMessage() instanceof VoiceMessage) {
                        tvContent.setText("[语音]");
                    } else if (item.getLatestMessage() instanceof FileMessage) {
                        FileMessage fileMessage = (FileMessage) item.getLatestMessage();
                        if (WuhunFileMediaTool.isImageFileType(fileMessage.getName())) {
                            tvContent.setText("[贴图]");
                        } else if (WuhunFileMediaTool.isVideoFileType(fileMessage.getName())) {
                            tvContent.setText("[视频]");
                        }
                    } else if (item.getLatestMessage() instanceof LocationMessage) {
                        tvContent.setText("[位置]");
                    } else if (item.getLatestMessage() instanceof GroupNotificationMessage) {
                        GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) item.getLatestMessage();
                    }
                }
            };
            rvConverstation.setAdapter(mAdapter);
        }
    }

    @Override
    protected void getIntentData(Bundle arguments) {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.study_all_fragment;
    }
}
