package studyim.cn.edu.cafa.studyim.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：悟魂 ————2017/11/18.
 * 版本：1.0
 * 说明：
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter{

    protected List<T> datas;
    private int layoutRes;

    public BaseRecycleAdapter(@LayoutRes int layoutRes, List<T> datas) {
        this.datas = datas;
        this.layoutRes = layoutRes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent,false);
//        return new BaseViewHolder(view);
        RecyclerView.ViewHolder holder = setViweHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


//    @Override
//    public void onBindViewHolder(BaseRecycleAdapter.BaseViewHolder holder, final int position) {
//        bindData(holder,position);
//    }

    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<T> datas){
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     * @param datas
     */
    public void addData(List<T> datas){
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    /**
     *  绑定数据
     * @param holder  具体的viewHolder
     * @param position  对应的索引
     */
    protected abstract void bindData(RecyclerView.ViewHolder holder, int position);


    /** 添加viewholder */
    protected abstract RecyclerView.ViewHolder setViweHolder(View view);

}
