package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.model.GroupFile;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

public class GroupFilesActivity extends AppCompatActivity {

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
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.ll_sub_title)
    LinearLayout llSubTitle;
    @BindView(R.id.rv_group_file)
    WuhunRecyclerView rvGroupFile;
    @BindView(R.id.rl_tab_photo)
    RelativeLayout rlTabPhoto;
    @BindView(R.id.rl_tab_video)
    RelativeLayout rlTabVideo;
    @BindView(R.id.rl_tab_document)
    RelativeLayout rlTabDocument;
    @BindView(R.id.rl_tab_other)
    RelativeLayout rlTabOther;

    private Context mContext;
    public static final String GROUP_ID = "groupId";
    private String groupid;

    private String[] fileTypes = {"photo", "video", "document", "other"};
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_files);
        ButterKnife.bind(this);

        initView();
        getIntentData();
        initListener();

    }

    private void initListener() {
        rlTabPhoto.setOnClickListener(mClickListener);
        rlTabVideo.setOnClickListener(mClickListener);
        rlTabDocument.setOnClickListener(mClickListener);
        rlTabOther.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_tab_photo:
                    position = 0;
                    test();
                    break;
                case R.id.rl_tab_video:
                    position = 1;
                    test();
                    break;
                case R.id.rl_tab_document:
                    position = 2;
                    test();
                    break;
                case R.id.rl_tab_other:
                    position = 3;
                    test();
                    break;
            }
        }
    };

    private void test() {
        if (groupid != null) {
            HttpUtil.getGroupFiles(groupid, fileTypes[position], new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    TestLog.i("群文件: " + result);
                }
            });
        }
    }

    private void getIntentData() {
        groupid = getIntent().getStringExtra(GROUP_ID);
    }

    LQRAdapterForRecyclerView<GroupFile> adapter;
    List<GroupFile> mdata = new ArrayList<>();

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("群文件列表");
        bodySearch.setVisibility(View.GONE);
        bodyOk.setVisibility(View.GONE);

        adapter = new LQRAdapterForRecyclerView<GroupFile>(mContext, mdata, R.layout.group_file_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, GroupFile item, int position) {

            }
        };
    }


}
