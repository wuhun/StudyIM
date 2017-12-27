package studyim.cn.edu.cafa.studyim.activity.rong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studyim.cn.edu.cafa.studyim.R;

public class SubConversationListActivtiy extends AppCompatActivity {

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
        initListener();
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
        bodyTvTitle.setText("消息");
        bodySearch.setVisibility(View.GONE);
    }
}
