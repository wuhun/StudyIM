package studyim.cn.edu.cafa.studyim.fragment.study;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import tools.com.lvliangliang.wuhuntools.util.WuhunDateTool;
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
                WuhunDebug.debug("返回" + conversations.size());
                for (Conversation con : conversations) {
                    WuhunDebug.debug("类型:" + con.getConversationType() +
                            "\n对方id" + con.getTargetId() +
                            "\n时间：" + WuhunDateTool.getMMddHHmm(con.getReceivedTime())+
                            "\n是否置顶" + con.isTop() +
                            "\n未读消息数" + con.getUnreadMessageCount() +
                            "\n文字消息草稿" + con.getDraft() +
                            "\n最后消息类型是否为图片" + (con.getLatestMessage() instanceof ImageMessage));
                }
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
//        adapter = new LQRAdapterForRecyclerView<Conversation>(mActivity,  dataList, R.layout.study_conversation_item) {
//            @Override
//            public void convert(LQRViewHolderForRecyclerView helper, Conversation item, int position) {
//                helper.setText(R.id.tvDisplayName, dataList.get(position).getSenderUserName());
//                byte[] encode = dataList.get(position).getLatestMessage().encode();
//                helper.setText(R.id.tvContent, new String(encode));
//                long receivedTime = dataList.get(position).getReceivedTime();
//                helper.setText(R.id.tvTime, WuhunDateTool.getMMddHHmm(receivedTime));
//            }
//        };
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<Conversation>(mActivity, mData, R.layout.study_conversation_item) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Conversation item, int position) {
                    if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
                        ImageView ivHeader = helper.getView(R.id.ivHeader);
//                        UserInfo userInfo = DBManager.getInstance().getUserInfo(item.getTargetId());
//                        if (userInfo != null) {
//                            Glide.with(mContext).load(userInfo.getPortraitUri()).centerCrop().into(ivHeader);
//                            helper.setText(R.id.tvDisplayName, userInfo.getName())
//                                    .setText(R.id.tvTime, TimeUtils.getMsgFormatTime(item.getReceivedTime()))
//                                    .setViewVisibility(R.id.ngiv, View.GONE)
//                                    .setViewVisibility(R.id.ivHeader, View.VISIBLE);
//                        }
                    } else {
//                        Groups groups = DBManager.getInstance().getGroupsById(item.getTargetId());
//                        //九宫格头像
//                        LQRNineGridImageView ngiv = helper.getView(R.id.ngiv);
//                        ngiv.setAdapter(mNgivAdapter);
//                        ngiv.setImagesData(DBManager.getInstance().getGroupMembers(item.getTargetId()));
//                        //群昵称
//                        helper.setText(R.id.tvDisplayName, groups == null ? "" : groups.getName())
//                                .setText(R.id.tvTime, TimeUtils.getMsgFormatTime(item.getReceivedTime()))
//                                .setViewVisibility(R.id.ngiv, View.VISIBLE)
//                                .setViewVisibility(R.id.ivHeader, View.GONE);
                    }

//                    helper.setBackgroundColor(R.id.flRoot, item.isTop() ? UIUtils.getColor(R.color.gray7) : UIUtils.getColor(android.R.color.white))
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
//                        try {
//                            UserInfo curUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
//                            GroupNotificationMessageData data = JsonMananger.jsonToBean(groupNotificationMessage.getData(), GroupNotificationMessageData.class);
//                            String operation = groupNotificationMessage.getOperation();
//                            String notification = "";
//                            String operatorName = data.getOperatorNickname().equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : data.getOperatorNickname();
//                            String targetUserDisplayNames = "";
//                            List<String> targetUserDisplayNameList = data.getTargetUserDisplayNames();
//                            for (String name : targetUserDisplayNameList) {
//                                targetUserDisplayNames += name.equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : name;
//                            }
//                            if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_CREATE)) {
//                                notification = UIUtils.getString(R.string.created_group, operatorName);
//                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_DISMISS)) {
//                                notification = operatorName + UIUtils.getString(R.string.dismiss_groups);
//                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_KICKED)) {
//                                if (operatorName.contains(UIUtils.getString(R.string.you))) {
//                                    notification = UIUtils.getString(R.string.remove_group_member, operatorName, targetUserDisplayNames);
//                                } else {
//                                    notification = UIUtils.getString(R.string.remove_self, targetUserDisplayNames, operatorName);
//                                }
//                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_ADD)) {
//                                notification = UIUtils.getString(R.string.invitation, operatorName, targetUserDisplayNames);
//                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_QUIT)) {
//                                notification = operatorName + UIUtils.getString(R.string.quit_groups);
//                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_RENAME)) {
//                                notification = UIUtils.getString(R.string.change_group_name, operatorName, data.getTargetGroupName());
//                            }
//                            tvContent.setText(notification);
//                        } catch (HttpException e) {
//                            e.printStackTrace();
//                        }
                    }
//                    else if (item.getLatestMessage() instanceof RedPacketMessage) {
//                        RedPacketMessage redPacketMessage = (RedPacketMessage) item.getLatestMessage();
//                        tvContent.setText("[" + UIUtils.getString(R.string.wx_red_pack) + "]" + redPacketMessage.getContent());
//                    }
                }
            };
//            mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
//                Intent intent = new Intent(mActivity, SessionActivity.class);
//                Conversation item = mData.get(position);
//                intent.putExtra("sessionId", item.getTargetId());
//                if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
//                    intent.putExtra("sessionType", SessionActivity.SESSION_TYPE_PRIVATE);
//                } else {
//                    intent.putExtra("sessionType", SessionActivity.SESSION_TYPE_GROUP);
//                }
//                jumpToActivity(intent);
//            });
//            mAdapter.setOnItemLongClickListener((helper, parent, itemView, position) -> {
//                Conversation item = mData.get(position);
//                View conversationMenuView = View.inflate(mActivity, R.layout.dialog_conversation_menu, null);
//                mConversationMenuDialog = new CustomDialog(mContext, conversationMenuView, R.style.MyDialog);
//                TextView tvSetConversationToTop = (TextView) conversationMenuView.findViewById(R.id.tvSetConversationToTop);
//                tvSetConversationToTop.setText(item.isTop() ? UIUtils.getString(R.string.cancel_conversation_to_top) : UIUtils.getString(R.string.set_conversation_to_top));
//                conversationMenuView.findViewById(R.id.tvSetConversationToTop).setOnClickListener(v ->
//                        RongIMClient.getInstance().setConversationToTop(item.getConversationType(), item.getTargetId(), !item.isTop(), new RongIMClient.ResultCallback<Boolean>() {
//                            @Override
//                            public void onSuccess(Boolean aBoolean) {
//                                loadData();
//                                mConversationMenuDialog.dismiss();
//                                mConversationMenuDialog = null;
//                            }
//
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                            }
//                        }));
//                conversationMenuView.findViewById(R.id.tvDeleteConversation).setOnClickListener(v -> {
//                    RongIMClient.getInstance().removeConversation(item.getConversationType(), item.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
//                        @Override
//                        public void onSuccess(Boolean aBoolean) {
//                            loadData();
//                            mConversationMenuDialog.dismiss();
//                            mConversationMenuDialog = null;
//                        }
//
//                        @Override
//                        public void onError(RongIMClient.ErrorCode errorCode) {
//
//                        }
//                    });
//                });
//                mConversationMenuDialog.show();
//                return true;
//            });
//            getView().getRvRecentMessage().setAdapter(mAdapter);
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
