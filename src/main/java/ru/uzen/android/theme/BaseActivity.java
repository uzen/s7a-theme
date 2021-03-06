package ru.uzen.android.theme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.util.ArrayList;

public abstract class BaseActivity extends Activity {
	public static final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
	public static final int MATCH_PARENT = LayoutParams.MATCH_PARENT;
    
    /* ID */
    public static final int CONTENT_LAYOUT_ID = 0x00000000;
    public static final int FOOTER_LAYOUT_ID = 0x00000001;
    public static final int HEADER_LAYOUT_ID = 0x00000002;
    public static final int BUTTONS_LAYOUT_ID = 0x10000000;
    
    public static int BUTTONS_MAX_NUM = 2;  
    public static float density = 1;
    
    private ArrayList<Button> mButtons = new ArrayList<Button>(BUTTONS_MAX_NUM);

    private RelativeLayout mGlobalLayout;
    
    private LinearLayout mContentLayout;
    private LinearLayout mFooterLayout;
    private LinearLayout mHeaderLayout;
    
    private TextView mHeaderViewText;
    
    private View mContent;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        for (int i = 0; i < BUTTONS_MAX_NUM; i++) {
        	mButtons.add(new Button(this));
        }
        
        density = getResources().getDisplayMetrics().density;	
		
		initHeader();
		initFooter();
		initContent();
		initActivity();
		
		setContentView(mGlobalLayout);
		
		initData();
    }
    
    private void initContent() {
    	mContent = new View(this);
    	mContentLayout = new LinearLayout(this);
    	mContentLayout.setId(CONTENT_LAYOUT_ID);
    	mContentLayout.addView(mContent, createRelative(MATCH_PARENT, WRAP_CONTENT));	
    }
    
    private void initFooter() {
    	mFooterLayout = new LinearLayout(this);
    	mFooterLayout.setId(FOOTER_LAYOUT_ID);
    	mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
    	mFooterLayout.setLayoutParams(createLinear(MATCH_PARENT, WRAP_CONTENT));
    	mFooterLayout.setBackgroundColor(0xFF000000);
    	    	
    	int footer_num_buttons = BUTTONS_MAX_NUM;
    	
    	for (int i = 0; i < footer_num_buttons; i++) {
    		/*button properties*/
    		mButtons.get(i).setClickable(true);
			
			mFooterLayout.addView(mButtons.get(i), createLinear(WRAP_CONTENT, WRAP_CONTENT, 1, Gravity.CENTER_HORIZONTAL));
		}    
    } 
    
    private void initHeader() {
    	mHeaderLayout = new LinearLayout(this);
    	mHeaderLayout.setId(HEADER_LAYOUT_ID);
    	mHeaderViewText = new TextView(this);
    	mHeaderViewText.setTextSize(dp(18));
    	mHeaderViewText.setTextColor(0xFFD3D3D3);
    	
		mHeaderLayout.addView(mHeaderViewText, createLinear(MATCH_PARENT, WRAP_CONTENT, 1, Gravity.LEFT | Gravity.TOP, 16, 16, 16, 16));
    }
    
    private void initActivity() {
    	mGlobalLayout = new RelativeLayout(this);
        mGlobalLayout.setBackgroundColor(0xFFFAFAFA); 
        mGlobalLayout.setLayoutParams(createLinear(MATCH_PARENT, MATCH_PARENT));
        
        /* add Header Layout */
        mGlobalLayout.addView(mHeaderLayout, createRelative(MATCH_PARENT, WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_TOP, -1, -1));
		
		/* add Footer Layout */
		mGlobalLayout.addView(mFooterLayout, createRelative(MATCH_PARENT, WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM, -1, -1));

		/* add Content Layout */
		mGlobalLayout.addView(mContentLayout, createRelative(MATCH_PARENT, WRAP_CONTENT, -1, RelativeLayout.ABOVE, FOOTER_LAYOUT_ID));
    }

    /* LinearLayout params */
    public static LinearLayout.LayoutParams createLinear(int width, int height) {
        return new LinearLayout.LayoutParams(getSize(width), getSize(height));
    }
    
    public static LinearLayout.LayoutParams createLinear(int width, int height, float weight, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(width), getSize(height), weight);
        layoutParams.gravity = gravity;
        return layoutParams;
    }
    
    public static LinearLayout.LayoutParams createLinear(int width, int height, float weight, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(width), getSize(height), weight);
        layoutParams.setMargins(dp(leftMargin), dp(topMargin), dp(rightMargin), dp(bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
	}
    
    /* RelativeLayout params */
    public static RelativeLayout.LayoutParams createRelative(int width, int height) {
        return createRelative(width, height, 0, 0, 0, 0, -1, -1, -1);
    }
    
    public static RelativeLayout.LayoutParams createRelative(int width, int height, int alignParent, int alignRelative, int anchorRelative) {
        return createRelative(width, height, 0, 0, 0, 0, alignParent, alignRelative, anchorRelative);
    }
    
    public static RelativeLayout.LayoutParams createRelative(float width, float height, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignParent, int alignRelative, int anchorRelative) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getSize(width), getSize(height));
        if (alignParent >= 0) {
            layoutParams.addRule(alignParent);
        }
        if (alignRelative >= 0 && anchorRelative >= 0) {
            layoutParams.addRule(alignRelative, anchorRelative);
        }
        layoutParams.leftMargin = dp(leftMargin);
        layoutParams.topMargin = dp(topMargin);
        layoutParams.rightMargin = dp(rightMargin);
        layoutParams.bottomMargin = dp(bottomMargin);
        return layoutParams;
    }
    
    public static int getSize(float size) {
        return (int) (size < 0 ? size : dp(size));
    }
     
    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    } 
    
    public abstract void initData();
    
	public Button getFooterButtonId(int index) {
		if (index < 0 || index > BUTTONS_MAX_NUM - 1) {
			System.err.println("Footer button index is out of bound.");
		} else {
			return mButtons.get(index);
		}
		return null;
	}
	
	public void setContent(View contentView) {
		if (mContentLayout != null && !contentView.equals(mContent)) {
			mContentLayout.removeView(mContent);
			mContentLayout.addView(contentView, createRelative(MATCH_PARENT, WRAP_CONTENT));
		}
		mContent = contentView;
	}
	
	public void setHeaderViewText(String text) {
		setHeaderViewText(text, -1);
	}

	public void setHeaderViewText(String text, int color) {
		setHeaderViewText(text, color, -1);
	}

	public void setHeaderViewText(String text, int color, float size) {
		if (!mHeaderViewText.equals(text)) {
			mHeaderViewText.setText(text);
		}
		if (color >= 0) {
			mHeaderViewText.setTextColor(color);
		}
		if (size >= 0) {
			mHeaderViewText.setTextSize(size);
		}
	}
}