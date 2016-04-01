
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @创建者 ：yqlee
 * @时间 ：2016/3/23  15:35
 * @描述 ：模板方法模式再抽取BaseAdapter
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;

    public CommonAdapter(Context context) {
        this.mContext = context;
    }

    public CommonAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    /**
     * @param datas
     */
    public void setData(List<T> datas) {
//		if (mDatas != null) {
//			mDatas.clear();
//		}
        if (datas == null) return;
        if (mDatas != null) mDatas.clear();
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setmDataNull(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setDataNoClear(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void addData(List<T> datas) {
//        if (datas == null) return;
        if (mDatas != null) {
            mDatas.addAll(datas);
        } else {
            mDatas = datas;
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mDatas != null) {
            mDatas.clear();
            mDatas = null;
        }
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        if (t != null && mDatas != null) {
            mDatas.add(t);
            notifyDataSetChanged();
        }
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return
     */
    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(mContext,
                position, convertView, parent, getLayoutId());
        bindData(position, holder);
        return holder.getConvertView();
    }

    public abstract void bindData(int position, ViewHolder holder);

    public abstract int getLayoutId();
}
