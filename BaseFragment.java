
public abstract class BaseFragment extends Fragment {

    protected boolean isViewPrepared;
    protected BaseActivity mActivity;
    protected Context mContext;

    /**
     * Called when a fragment is first attached to its context
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.mActivity = (BaseActivity) activity;
        }
        mContext = getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initRootView(inflater);
        InjectUtility.initInjectedView(this, view);
        isViewPrepared = true;
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterView(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInvisible();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void afterView(View view, Bundle savedInstanceState) {
    }

    protected abstract View initRootView(LayoutInflater inflater);

    protected void onInvisible() {
    }

    protected void onVisible() {
    }
}
