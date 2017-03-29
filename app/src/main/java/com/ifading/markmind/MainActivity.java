package com.ifading.markmind;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzhoujay.markdown.MarkDown;
import com.zzhoujay.richtext.RichText;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.parentLayout)
    protected RelativeLayout mParentLayout;
    @BindView(R.id.markDown_layout)
    protected LinearLayout mMarkDownLayout;
    @BindView(R.id.button)
    protected Button mButton;
    @BindView(R.id.textView)
    protected EditText mEd;
    @BindView(R.id.markdown_tv)
    protected TextView mMDTv;
    //软件键盘是否显示
    private boolean mSoftKeyboardDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        registerKeyBoardSizeChange();
    }

    private void registerKeyBoardSizeChange() {
        final Context context = getApplicationContext();

        mParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int statusBarHeight = 0;
                Rect r = new Rect();
                // r will be populated with the coordinates of your view that area still visible.
                mParentLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = mMarkDownLayout.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                if (heightDiff > 100)
                    // if more than 100 pixels, its probably a keyboard
                    // get status bar height
                    statusBarHeight = 0;
                try {
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = context.getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int realKeyboardHeight = heightDiff - statusBarHeight;
                Log.i(TAG, "keyboard height = " + realKeyboardHeight);


                if (realKeyboardHeight>100) {
                    if (!mSoftKeyboardDisplay){
                        onSoftKeyboardDisplay(realKeyboardHeight);
                    }
                    mSoftKeyboardDisplay = true;
                }else{
                    if (mSoftKeyboardDisplay){
                        onSoftKeyboardHidden();
                    }
                    mSoftKeyboardDisplay = false;
                }
                /*Log.d(TAG,"markdown位置:l:"+mMarkDownLayout.getLeft()+" t:"+mMarkDownLayout.getTop()
                        +" r:"+mMarkDownLayout.getRight()+" b:"+mMarkDownLayout.getBottom()+" screenHeight:"+screenHeight);*/
            }

        });
    }

    /**
     * 软键盘隐藏了
     */
    private void onSoftKeyboardHidden() {
        mMarkDownLayout.setVisibility(View.GONE);
    }

    /**
     * 键盘显示了
     */
    private void onSoftKeyboardDisplay(int keyboardHeight) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mMarkDownLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, keyboardHeight);
        mMarkDownLayout.setLayoutParams(layoutParams);
        mMarkDownLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button)
    protected void onClick(){
        showToast("点击按钮了");
        // 设置为Markdown
        RichText.fromMarkdown(mEd.getEditableText().toString()).into(mMDTv);
    }

    private void showToast(String content) {
        Toast.makeText(this.getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
}
