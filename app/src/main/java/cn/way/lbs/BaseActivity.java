package cn.way.lbs;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.way.lbs.util.VLog;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int layoutId = getContentViewId();
        if (layoutId > 0) {
            setContentView(layoutId);
        } else {
            VLog.w(TAG, "content view id maybe wrong:" + layoutId);
        }

        init(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract int getContentViewId();

    @CallSuper
    protected void init(@Nullable Bundle savedInstanceState) {
        customizeActionBar();
        onCustomizeActionBar(getSupportActionBar());
    }

    protected Toolbar customizeActionBar() {
        final Toolbar toolbar;
        final int toolbarId = getToolbarId();
        if (toolbarId > 0) {
            toolbar = findViewById(toolbarId);
        } else {
            toolbar = null;
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        return toolbar;
    }

    protected int getToolbarId() {
        return 0;
    }

    @CallSuper
    protected void onCustomizeActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
