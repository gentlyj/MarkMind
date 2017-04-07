package com.ifading.markmind;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
    private static final byte SHORTCUT_BOLD = 1;
    private static final byte SHORTCUT_TITLE = 2;
    private static final byte SHORTCUT_LIST = 3;
    private static final byte SHORTCUT_CENTER = 4;
    private static final byte SHORTCUT_QUOTE = 5;
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


    @BindView(R.id.edit_page_short_cut_title)
    protected TextView mShortCutTitle;
    @BindView(R.id.edit_page_short_cut_center)
    protected TextView mShortCutCenter;
    @BindView(R.id.edit_page_short_cut_list)
    protected TextView mShortCutList;
    @BindView(R.id.edit_page_short_cut_bold)
    protected TextView mShortCutBold;
    @BindView(R.id.edit_page_short_cut_quote)
    protected TextView mShortCutQuote;

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


                if (realKeyboardHeight > 100) {
                    if (!mSoftKeyboardDisplay) {
                        onSoftKeyboardDisplay(realKeyboardHeight);
                    }
                    mSoftKeyboardDisplay = true;
                } else {
                    if (mSoftKeyboardDisplay) {
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

    @OnClick({R.id.button, R.id.edit_page_short_cut_bold
            , R.id.edit_page_short_cut_center
            , R.id.edit_page_short_cut_quote
            , R.id.edit_page_short_cut_list
            , R.id.edit_page_short_cut_title})
    protected void onClick(View v) {
        if (v == mButton) {
            showToast("点击按钮了");
            // 设置为Markdown
            RichText.fromMarkdown(mEd.getEditableText().toString()).into(mMDTv);
        } else if (v == mShortCutBold) {
            showToast("点击Bold了");
            clickShotrCut(SHORTCUT_BOLD);
        } else if (v == mShortCutTitle) {
            //showToast("点击Title了");
            clickShotrCut(SHORTCUT_TITLE);
            /*int currentCursorLine = getCurrentCursorLine(mEd);
            String multiLines = mEd.getText().toString();
            String[] streets;
            String delimiter = "\n";

            streets = multiLines.split(delimiter);
            String currentLine = streets[currentCursorLine];
            showToast(currentLine);

            int startPos = mEd.getLayout().getLineStart(currentCursorLine);
            int endPos = mEd.getLayout().getLineEnd(currentCursorLine);
            Log.d(TAG, "当前行,startPos:" + startPos + " endPos:" + endPos + " currentLine:" + currentLine);

            String substringFirst = mEd.getText().toString().substring(0, startPos);
            String substringEnd = mEd.getText().toString().substring(endPos);

            Log.d(TAG, "当前行,substringFirst:" + substringFirst + " substringEnd:" + substringEnd);
            String add;
            if (currentLine.startsWith("#")) {
                add = "#" + currentLine;
            } else {
                add = "# " + currentLine;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(substringFirst).append(add).append(substringEnd);

            mEd.setText(sb.toString());*/


        } else if (v == mShortCutList) {
            showToast("点击List了");
            clickShotrCut(SHORTCUT_LIST);
        } else if (v == mShortCutCenter) {
            showToast("点击Center了");
            clickShotrCut(SHORTCUT_CENTER);
        } else if (v == mShortCutQuote) {
            showToast("点击Quote了");
            clickShotrCut(SHORTCUT_QUOTE);
        }
    }


    private void clickShotrCut(byte type) {

        int currentCursorLine = getCurrentCursorLine(mEd);
        String multiLines = mEd.getText().toString();
        String[] streets;
        String delimiter = "\n";

        streets = multiLines.split(delimiter);
        String currentLine = streets[currentCursorLine];
        Log.d(TAG, "当前行, currentLine:" + currentLine);
        int startPos = mEd.getLayout().getLineStart(currentCursorLine);
        int endPos = mEd.getLayout().getLineEnd(currentCursorLine);
        String substringFirst = mEd.getText().toString().substring(0, startPos);
        String substringEnd = mEd.getText().toString().substring(endPos);
        String add = currentLine;
        switch (type) {
            case SHORTCUT_TITLE:

                if (currentLine.startsWith("# ")||currentLine.startsWith("## ")) {
                    add = "#" + currentLine;
                } else if (currentLine.startsWith("### ")){
                    add = currentLine.replace("###","#");
                }else {
                    add = "# " + currentLine;
                }
                break;

            case SHORTCUT_CENTER:
                add = "[" + currentLine + "]";
                break;

            case SHORTCUT_LIST:
                if (currentLine.startsWith("-")){
                    break;
                }
                add = "- " + currentLine;
                break;

            case SHORTCUT_BOLD:

                break;

            case SHORTCUT_QUOTE:
                if (currentLine.startsWith(">")){
                    break;
                }
                add = "> " + currentLine;
                break;

            default:
                add = currentLine;
                break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(substringFirst).append(add).append(substringEnd);

        mEd.setText(sb.toString());
        // TODO: 17/4/7 有bug,会吃掉换行
        mEd.setSelection(startPos+add.length());
    }

    public int getCurrentCursorLine(EditText editText) {
        int selectionStart = Selection.getSelectionStart(editText.getText());
        Layout layout = editText.getLayout();

        if (selectionStart != -1) {
            return layout.getLineForOffset(selectionStart);
        }

        return -1;
    }

    private void showToast(String content) {
        Toast.makeText(this.getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }
}
