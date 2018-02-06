package studyim.cn.edu.cafa.studyim.fragment.other;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.polites.android.GestureImageView;

import studyim.cn.edu.cafa.studyim.R;
import tools.com.lvliangliang.wuhuntools.util.WuhunDeviceTool;


/**
 * Created by Administrator on 2017/4/16 0016.
 */
public class DetailImgFragment extends Fragment {

    private final static String IMG_URL = "IMG_URL";

    private int stateHeight = 0;//状态栏高度
    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度

    GestureImageView imageView; // 自定义imageview视图
    private String imgResourceURL;//图片资源url

    /**
     * 获取url地址，屏幕宽高
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imgResourceURL = getArguments().getString(IMG_URL);     // 取参图片url地址
        }
        //获取屏幕宽度
        screenWidth = WuhunDeviceTool.getScreenWidth(this.getActivity());
        //获取屏幕高度
        screenHeight = WuhunDeviceTool.getScreenHeight(this.getActivity());
        //获取状态栏高度
        if (null == savedInstanceState) {   // 保存的临时数据
            Rect frame = new Rect();
            DetailImgFragment.this
                    .getActivity().getWindow().getDecorView()
                    .getWindowVisibleDisplayFrame(frame);
            stateHeight = frame.top;
        } else {
            stateHeight = savedInstanceState.getInt("stateHeight");
        }
        //计算可用屏幕高度
        screenHeight = screenHeight - stateHeight;

    }

    /**
     * 获取自定义imageview视图,设置宽高,
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = (View) inflater.inflate(R.layout.fragment_img_detail, container, false);
        View view = inflater.inflate(R.layout.fragment_img_detail, null, false);

        imageView = (GestureImageView) view.findViewById(R.id.iv_big_img);  // 自定义imageView

//        imageView.setScreen_H(screenHeight);
//        imageView.setScreen_W(screenWidth);
        return view;
    }

    /**
     * 加载并且,显示图片
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null != imgResourceURL || !"".equals(imgResourceURL)) {
            imageView.setVisibility(View.VISIBLE);
            //ImageLoader.getInstance().displayImage(imgResourceURL, imageView);
//            UserAvatarUtil.showImage(get, imgResourceURL, imageView);
            Glide.with(getActivity()).load(imgResourceURL)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.default_user)
                    .into(imageView);
        }
    }

    /**
     * 返回当前碎片视图,留给外边的接口传输图片url地址,并返回碎片视图
     */
    public static DetailImgFragment newInstance(String imgResourceURL) {
        DetailImgFragment fragment = new DetailImgFragment(); // 创建新的碎片

        Bundle arg = new Bundle();// 传参给新碎片url地址
        arg.putString(IMG_URL, imgResourceURL);
        fragment.setArguments(arg);   // create方法中获取参数

        return fragment;
    }

    /**
     * 临时保存bundle的数据
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("stateHeight", stateHeight); // 状态栏高度
        super.onSaveInstanceState(outState);
    }

    /**
     * 退出应用执行：清空碎片内容
     */
    @Override
    public void onDetach() {
        if (imageView != null) {
            imageView.setImageDrawable(null);
        }
        super.onDetach();
    }
}
