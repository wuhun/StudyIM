package studyim.cn.edu.cafa.studyim.activity.rong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import studyim.cn.edu.cafa.studyim.R;

public class ConversationListActivity extends AppCompatActivity {


    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView backActivity;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView detailUser;
//    @BindView(R.id.body_ok)
//    TextView bodyOk;
//    @BindView(R.id.rl_head)
//    RelativeLayout rlHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        ButterKnife.bind(this);
//        setContentView(R.layout.conversationlist);
        getIntentDate();
        initView();
        initListener();
    }

    private void initListener() {
        backActivity.setOnClickListener(mClick);
        detailUser.setOnClickListener(mClick);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.body_img_menu) { //返回
                ConversationListActivity.this.finish();
            }else if(v.getId() == R.id.body_search) { //聊天设置
                // TODO: 2017/12/21 设置当前用户资料
            }
        }
    };

    private void initView() {
        headBg.setImageResource(R.drawable.main_bg);
        backActivity.setImageResource(R.drawable.icon_back);
        detailUser.setImageResource(R.drawable.default_user);
    }

    private String mTitle;
    private String mTargetId;
    /** 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId */
    private String mTargetIds;
    private Conversation.ConversationType mConversationType;//会话类型

    public void getIntentDate() {
        Intent intent = getIntent();
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTitle = intent.getData().getQueryParameter("title");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
        bodyTvTitle.setText(mTitle);
    }

    /**
     * 加载会话页面 ConversationFragment
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }
}
