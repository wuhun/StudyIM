package studyim.cn.edu.cafa.studyim.activity.other;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.fragment.other.DetailImgFragment;

public class DetailImgActivity extends AppCompatActivity {

    // 全局变量
    private TextView tv_page;       // 页码
    private TextView tv_title;      // 图标标题
    private ViewPager mViewPager;   // 碎片容器

    private List<String> imgLists;   // 图片集合
    private Integer iIndex = 0;     // 图片集合中的下标

    public static final String ICON = "ICON";
    public static final String PAGE = "PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().hide();// 隐藏标题栏
        setContentView(R.layout.activity_detail_img);

        initData();  // 初始化数据
        initView();  // 初始化视图
        initListener();     // 设置监听
    }

    /** 初始化数据_获取图片url地址集合 */
    private void initData() {
        imgLists = new ArrayList<String>();
        // 接收参数
        String[] imgurls = getIntent().getStringArrayExtra(ICON);
        iIndex = getIntent().getIntExtra(PAGE,0);

        // 数组 --> List集合
        for(String url:imgurls){
            imgLists.add(url);
        }
    }

    /** 初始化视图 */
    private void initView() {
        tv_page = (TextView) findViewById(R.id.tv_page);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mViewPager=(ViewPager)findViewById(R.id.vp_img_prev_container);

        PictureSlidePagerAdapter adapter = new PictureSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        // 设置当前视图
        mViewPager.setCurrentItem(iIndex);  // 重要
        initPage();
    }
    // 初始化页码，标题
    private void initPage() {

        if (imgLists.size()<=1){    //只有一张图片，或者没有图片，不创建
            tv_page.setVisibility(View.GONE);
            tv_title.setVisibility(View.GONE);
            return;
        }
        tv_page.setText((iIndex+1) + "/" + imgLists.size());
        String title = imgLists.get(iIndex).substring(imgLists.get(iIndex).lastIndexOf("/")+1);
        tv_title.setText(title);
    }

    /** 初始化监听 */
    private void initListener() {
        // 碎片切换监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) { }

            @Override
            public void onPageSelected(int i) {setCurrentPage(i); } // 切换页码

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

    }

    /** 设置当前页码  */
    private void setCurrentPage(int position) {

        if (position < 0 || position > imgLists.size() - 1 ||
                imgLists.size()<=1 || iIndex == position) {
            return;
        }
        tv_page.setText((position+1) + "/" + imgLists.size());
        String title = imgLists.get(position).substring(imgLists.get(position).lastIndexOf("/")+1);
        tv_title.setText(title);

        iIndex = position;
    }


    /** ———————————— 内部类FragmentAdapter ————————————*/
    public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int i) {
            return DetailImgFragment.newInstance(imgLists.get(i));
        }

        @Override
        public int getCount() {
            return imgLists.size();
        }

    }
}
