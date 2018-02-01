package studyim.cn.edu.cafa.studyim.activity.rong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.fragment.SubConversationListFragment;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;

public class SubConversationListActivtiy extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);
        ButterKnife.bind(this);

        initView();
        getIntentData();
        initListener();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");
        if (type == null) return;

        if (type.equals("group")) {
            setTitle("群组");
        } else if (type.equals("private")) {
            setTitle("我的私人会话");
        } else if (type.equals("discussion")) {
            setTitle("我的讨论组");
        } else if (type.equals("system")) {
            setTitle("系统消息");
        } else {
            setTitle("聊天");
        }
    }

    private void initListener() {
        bodyImgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubConversationListActivtiy.this.finish();
            }
        });
    }

    private void initView() {
        headBg.setImageResource(R.drawable.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodySearch.setVisibility(View.GONE);

        SubConversationListFragment fragment = new SubConversationListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.sub_conversation_list, fragment);
        transaction.commit();
    }

    private void setTitle(String title){
        bodyTvTitle.setText(title);
    }
}
