package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.Conversation;
import studyim.cn.edu.cafa.studyim.R;
import tools.com.lvliangliang.wuhuntools.ui.ClearWriteEditText;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

public class GroupHistoryActivity extends AppCompatActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;
    @BindView(R.id.body_ok)
    TextView bodyOk;

    @BindView(R.id.edit_search_content)
    ClearWriteEditText editSearchContent;
    @BindView(R.id.rc_history)
    WuhunRecyclerView rcHistory;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Conversation.ConversationType mConversationType;//会话类型
    public static final String RCTOKEN = "rc_token";
    private String GroupRcid;

    public static final String GROUP_ID = "group_id";
    public static final String GROUP_MASTER_ID = "group_master_id";
    public static final String GROUP_MANAGER_ID = "group_manager_id";
    public static final String CONVERSTATION_TYPE = "converstation_type";
    private String converstationType= "";
    private String groupId = "";
    private String groupMasterId = "";
    private String groupManagerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_hisotory);
        ButterKnife.bind(this);

        initGetIntentDate();
        initData();
        initListener();
    }

    private void initGetIntentDate() {
        Intent intent = getIntent();
        GroupRcid = intent.getStringExtra(RCTOKEN);
    }

    private void initListener() {
        bodyImgMenu.setOnClickListener(mClick);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.body_img_menu:
                    GroupHistoryActivity.this.finish();
                    break;
            }
        }
    };

    private void initData() {
        headBg.setImageResource(R.drawable.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("查看历史消息");
        bodySearch.setVisibility(View.GONE);
        bodyOk.setVisibility(View.GONE);

//        RongIM.getInstance().getHistoryMessages();
    }

}
