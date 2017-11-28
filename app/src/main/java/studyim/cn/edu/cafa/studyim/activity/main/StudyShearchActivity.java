package studyim.cn.edu.cafa.studyim.activity.main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

public class StudyShearchActivity extends BaseActivity {

    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.ll_default_content)
    RelativeLayout llDefaultContent;
    @BindView(R.id.et_search_content)
    EditText etSearchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_shearch);
        ButterKnife.bind(this);

        initListener();
    }

    private void initListener() {
        btnSearch.setOnClickListener(mOnClick);
    }

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    WuhunToast.info("您搜索的内容为："+etSearchContent.getText().toString()).show();
                    break;
            }
        }
    };
}
