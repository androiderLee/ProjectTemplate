
/**
 * @创建者 ：yqlee
 * @时间 ：2015/11/13 11:10
 * @描述 ：封装ultra-下拉刷新头，ListView的分页显示，GridView同理
 */
public abstract class ABasePtrListActivity<T> extends BaseActivity implements PtrHandler, AbsListView.OnScrollListener {

    private boolean mLastItemVisible;
    private boolean mFirstItemVisible;
    protected FrameLayout mFooterView;
    private TextView mFooterText;
    protected ListView mListView;
    protected CommonAdapter<T> mAdapter;
    public static int PAGE_SIZE = 20;//ListView一页加载的数量
    private View mProgressWheel;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        afterViewCreated();
    }

    protected void initListView(PtrClassicFrameLayout ptrFrameLayout, ListView listView, CommonAdapter<T> adapter) {
        mListView = listView;
        mAdapter = adapter;
        mListView.setOnScrollListener(this);
        initFooterView();
        mListView.setAdapter(adapter);
        if (ptrFrameLayout != null) {
            MaterialHeader header = new MaterialHeader(this);//Material风格的下拉刷新头
            header.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
            header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
            header.setPadding(0, 30, 0, 20);
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
            ptrFrameLayout.setPtrHandler(this);
        }
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled
     * If the view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     *
     * @param arg0
     * @param state
     */
    @Override
    public void onScrollStateChanged(AbsListView arg0, int state) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisible) {
            if (!checkIsEnd()) {
                updateFooterTipText(BaseApp.getGlobleContext().getString(R.string.txt_loading));
                onLoadMore();
            } else {
                removeFooterViewWhenEnd();
            }
        }
        //上拉刷新
        else if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mFirstItemVisible) {
            removeFooterViewWhenEnd();
        }
    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 2);
        mFirstItemVisible = (totalItemCount > 0) && (firstVisibleItem <= 1);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        onRefresh(ptrFrameLayout);
    }

    protected void initFooterView() {
        mFooterView = (FrameLayout) View.inflate(this, R.layout.footer_loading_view, null);
        mFooterView.setVisibility(View.GONE);
        mFooterText = (TextView) mFooterView.findViewById(R.id.footer_text);
        mProgressWheel = mFooterView.findViewById(R.id.pull_to_refresh_progress);
        if (mListView != null)
            mListView.addFooterView(mFooterView, null, false);
    }

    private void updateFooterTipText(String text) {
        if (mFooterView.getVisibility() != View.VISIBLE) mFooterView.setVisibility(View.VISIBLE);
        if (mFooterText != null && text != null) {
            mFooterText.setText(text);
        }
    }

    protected void removeFootView() {
        if (mListView != null && mListView.getFooterViewsCount() > 0) {
            mListView.removeFooterView(mFooterView);
        }
    }

    private boolean checkIsEnd() {
        int count = mAdapter.getCount();
        //count<PAGE_SIZE
        if (count < PAGE_SIZE) {
            mListView.removeFooterView(mFooterView);
            return true;
        }//count>=PAGE_SIZE
        else {
            //模不等于0，说明最后一次加载的数据不是不是20，那么久说明数据已经加载完了
            if (count % PAGE_SIZE != 0) {
                return true;
            }
        }
        return false;
    }

    private void removeFooterViewWhenEnd() {
        mListView.removeFooterView(mFooterView);
    }

    public abstract void afterViewCreated();

    public abstract void onRefresh(PtrFrameLayout ptrFrameLayout);

    public abstract void onLoadMore();

}
