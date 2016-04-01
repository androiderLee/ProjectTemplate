
public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isDestroy;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        isDestroy = false;
        if (getLayoutResID() > 0) {
            setContentView(getLayoutResID());
            InjectUtility.initInjectedView(this);//加入依赖注入框架
        }
    }

    /**
     * 获取LayoutoutId
     *
     * @return 返回值必须大于0
     */
    protected abstract int getLayoutResID();

    @Override
    protected void onDestroy() {
        isDestroy = true;
        AppManager.getAppManager().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 重新加载本页面
     */
    public final void reload() {
        reload(getIntent());
    }

    /**
     * 重新加载
     *
     * @param intent
     */
    public final void reload(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void showLoading(Boolean flag) {
        showLoading(flag, getString(R.string.tx_loading));
    }

    public void showLoading(Boolean flag, String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(msg);
        }
        if (flag) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    public final boolean isDestroy() {
        return isDestroy;
    }

    public final void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected final void showToast(int strId) {
        Toast.makeText(this, getString(strId), Toast.LENGTH_SHORT).show();
    }
}