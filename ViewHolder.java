
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ViewHolder，缓存
 */
public class ViewHolder {

    private int mPostion;
    private SparseArray<View> mViews;//Android提供的一个数据结构，取代HashMap,效率更高
    private View mContentView;

    /**
     * 构建ViewHolder,并且给contentView打上tag:ViewHolder
     *
     * @param context
     * @param position
     * @param parent
     * @param layoutId layoutId
     */
    public ViewHolder(Context context, int position, ViewGroup parent,
                      int layoutId) {
        mContentView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mContentView.setTag(this);
        this.mPostion = position;
        this.mViews = new SparseArray<View>();
    }

    /**
     * 获取ViewHolder,静态方法
     *
     * @param context
     * @param position
     * @param convertView 可复用的convertView,如果convertView不为null，那么服用
     * @param parent
     * @param layoutId
     * @return
     */
    public static ViewHolder getViewHolder(Context context, int position,
                                           View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new ViewHolder(context, position, parent, layoutId);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPostion = position;
            return holder;
        }
    }

    public int getPostion() {
        return mPostion;
    }

    /**
     * 获取contentView
     *
     * @return
     */
    public View getConvertView() {
        return mContentView;
    }

    public ViewHolder setText(int viewId, String str) {
        ((TextView) getView(viewId)).setText(str == null ? "" : str);
        return this;
    }

    /**
     * 获取contentView的childView
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int id) {
        View view = mViews.get(id);
        if (view != null) {
            return (T) view;
        } else {
            View v = mContentView.findViewById(id);
            mViews.put(id, v);
            return (T) v;
        }
    }
}
