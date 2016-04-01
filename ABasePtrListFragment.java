
/**
 * @创建者 ：yqlee
 * @时间 ：2016/3/23  18:04
 * @描述 :封装ultra-下拉刷新头，ListView的分页显示，GridView同理
 */
public abstract class ABasePtrListFragment<T> extends BaseFragment implements PtrHandler, AbsListView.OnScrollListener {

    private boolean mLastItemVisible;
    private boolean mFirstItemVisible;
    private FrameLayout mFooterView;
    private TextView mFooterText;
    protected ListView mListView;
    protected CommonAdapter<T> mAdapter;
    public final static int PAGE_SIZE = 20;
    private int mPageSize = 0;
    private int mMinSizeWhenShowText = 5;
    protected boolean isRemoveFooterWhenEnd;
    private View mProgressWheel;
    protected GridViewWithHeaderAndFooter mGridView;

    @Override
    protected View initRootView(LayoutInflater inflater) {
        return initView(inflater);
    }

    @Override
    protected void afterView(View view, Bundle savedInstanceState) {
        mPageSize = PAGE_SIZE;
        afterView(view);
    }

    public void initListView(PtrClassicFrameLayout mPtrFrameLayout, ListView lv, CommonAdapter<T> adapter) {
        mListView = lv;
        mAdapter = adapter;
        mListView.setOnScrollListener(this);
        initFooterView();
        mListView.setAdapter(adapter);
        if (mPtrFrameLayout != null) {
            MaterialHeader header = getMaterialHeader();
            mPtrFrameLayout.setHeaderView(header);
            mPtrFrameLayout.addPtrUIHandler(header);
            mPtrFrameLayout.setPtrHandler(this);
        }

    }

    public void initGridView(PtrClassicFrameLayout mPtrFrameLayout, GridViewWithHeaderAndFooter gv, CommonAdapter<T> adapter) {
        mGridView = gv;
        mAdapter = adapter;
        mGridView.setOnScrollListener(this);
        initFooterView();
        mGridView.setAdapter(adapter);
        if (mPtrFrameLayout != null) {
            MaterialHeader header = getMaterialHeader();
            mPtrFrameLayout.setHeaderView(header);
            mPtrFrameLayout.addPtrUIHandler(header);
            mPtrFrameLayout.setPtrHandler(this);
        }
    }

    private MaterialHeader getMaterialHeader() {
        MaterialHeader header = new MaterialHeader(mActivity);
        header.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, 30, 0, 20);
        return header;
    }

    public void initFooterView() {
        mFooterView = (FrameLayout) View.inflate(getActivity(), R.layout.footer_loading_view, null);
        mFooterView.setVisibility(View.GONE);
        mFooterText = (TextView) mFooterView.findViewById(R.id.footer_text);
        mProgressWheel = mFooterView.findViewById(R.id.pull_to_refresh_progress);
        if (mListView != null) mListView.addFooterView(mFooterView);
        if (mGridView != null) mGridView.addFooterView(mFooterView);
    }

    public boolean isHasFooterView() {
        if (mListView != null && mListView.getFooterViewsCount() > 0) return true;
        if (mGridView != null && mGridView.getFooterViewCount() > 0) return true;
        return false;
    }

    public void updateFooterTipText(String text) {
        if (mFooterView.getVisibility() != View.VISIBLE) mFooterView.setVisibility(View.VISIBLE);
        if (mFooterText != null && text != null) {
            mFooterText.setText(text);
        }
    }

    public void updateFooterTextAndhideLoad(String text) {
        if (mFooterView.getVisibility() != View.VISIBLE) mFooterView.setVisibility(View.VISIBLE);
        if (mFooterText != null && text != null) {
            mFooterText.setText(text);
            mProgressWheel.setVisibility(View.GONE);
        }
    }

    public boolean checkIsEnd() {
        int count = mAdapter.getCount();
        if (count < mPageSize) {
            return true;
        } else {
            if (count % mPageSize != 0) {
                return true;
            }
        }
        return false;
    }

    public void removeFootView() {
        if (mListView != null && mListView.getFooterViewsCount() > 0) {
            mListView.removeFooterView(mFooterView);
        }
        if (mGridView != null && mGridView.getFooterViewCount() > 0) {
            mGridView.removeFooterView(mFooterView);
        }
    }

    public void removeFooterViewWhenEnd() {
        if (isRemoveFooterWhenEnd && isHasFooterView()) {
            if (mListView != null)
                mListView.removeFooterView(mFooterView);
            if (mGridView != null)
                mGridView.removeFooterView(mFooterView);
        } else {
            if (mAdapter.getCount() >= mMinSizeWhenShowText) {
                mFooterView.setVisibility(View.GONE);
                updateFooterTextAndhideLoad("没有更多了");
            }
        }
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return getIsRefresh(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        onRefresh(ptrFrameLayout);
    }

    public boolean getIsRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int state) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisible) {
            mFooterView.setVisibility(View.GONE);
            if (!checkIsEnd()) {
                updateFooterTipText(BaseApp.getGlobleContext().getString(R.string.txt_loading));
                onLoadMore();
            } else {
                removeFooterViewWhenEnd();
            }
        } else if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mFirstItemVisible) {

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 2);
        mFirstItemVisible = (totalItemCount > 0) && (firstVisibleItem <= 1);
    }

    public void setPageSize(int mSize) {
        this.mPageSize = mSize;
    }

    public abstract View initView(LayoutInflater inflater);

    public abstract void afterView(View view);

    public abstract void onRefresh(PtrFrameLayout ptrFrameLayout);

    public abstract void onLoadMore();


}
