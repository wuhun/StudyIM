package studyim.cn.edu.cafa.studyim.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/10 0010
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class BaseFragment extends Fragment {

    protected FragmentActivity mActivity;
    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(setLayoutResouceId(), container, false);
            ButterKnife.bind(this, mRootView);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if(parent != null) {
                parent.removeView(mRootView);
            }
        }
        getIntentData(getArguments());//数据初始化:从activity传递过来的参数，适配器初始化，集合初始化等
        initView();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        initData();
//        initListener();
//    }

    /////////////////////////////////////////////////////////////////////////////////
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (null != mRootView) {
//            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
//        }
//    }
    /////////////////////////////////////////////////////////////////////////////////


    protected abstract void initListener();

    protected abstract void initData();

    protected abstract void initView();//获取控件

    protected abstract void getIntentData(Bundle arguments);

    protected abstract int setLayoutResouceId();



    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(mActivity, activity);
        startActivity(intent);
    }
}
