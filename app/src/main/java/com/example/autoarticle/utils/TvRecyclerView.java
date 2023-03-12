package com.example.autoarticle.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/6.
 */

public class TvRecyclerView extends RecyclerView {
    public static final String TAG = TvRecyclerView.class.getSimpleName();

    private static final int DEFAULT_DIRECTION = -1;
    /**
     * 焦点position
     */
    private int mSelectedPosition;
    /**
     * 焦点position
     */
    protected View mSelectedItem;

    /**
     * 滚动方向标识
     */
    private int mOrientation;
    /**
     * 按键方向（滑动值设置关键参数）
     */
    private int mDirection;

    /**
     * 是否允许itme调整
     */
    private boolean mIsSetItemSelected = false;

    /**
     * 滑动方式：
     * <p>
     * SCROLL_ALIGN 翻页滑动（默认）
     * SCROLL_FOLLOW 向左置顶滑动
     * SCROLL_NORMAL 默认滑动
     * SCROLL_PLACEDTOP 向上置顶滑动
     * SCROLL_MIDDLE 居中滑动
     */
    private static final int SCROLL_ALIGN = 0;
    private static final int SCROLL_FOLLOW = 1;
    private static final int SCROLL_NORMAL = 2;
    private static final int SCROLL_PLACEDTOP = 3;
    private static final int SCROLL_MIDDLE = 4;

    /**
     * 滑动方式选择（默认、翻页滑动）
     */
    private int mScrollMode = SCROLL_ALIGN;

    /**
     * 是否可以纵向移出
     */
    private boolean mCanFocusOutVertical = true;
    /**
     * 是否可以横向移出
     */
    private boolean mCanFocusOutHorizontal = true;

    /**
     * 默认第一次选中第一个位置(数据刷新后如果需要从首item开始获得焦点，别忘了设置默认值为0)
     */
    private int mCurrentFocusPosition = 0;

    /**
     * 获得焦点item
     */
    private View mFocusGainChildItem;

    /**
     * 按键keycode值
     */
    private int keycode = 10000;
    /**
     * 是否拦截
     */
    private boolean isIntercept = false;

    /**
     * 焦点移出recyclerview的事件监听
     */
    private FocusLostListener mFocusLostListener;
    /**
     * 焦点移入recyclerview的事件监听
     */
    private FocusGainListener mFocusGainListener;
    /**
     * 按确认键回调item position值回调
     */
    private OnItemStateListener mListener;

    public TvRecyclerView(Context context) {
        this(context, null);
    }

    public TvRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        setAttributeSet(attrs);
        setFocusable(false);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mSelectedPosition = 0;
        mOrientation = HORIZONTAL;
    }

    /**
     * 设置属性
     */
    private void setAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.TvRecyclerView);
            mScrollMode = typeArray.getInteger(R.styleable.TvRecyclerView_scrollMode, SCROLL_ALIGN);

            setChildrenDrawingOrderEnabled(true);
            typeArray.recycle();
        }
    }


    /**
     * 设置布局管理器
     */
    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            mOrientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        L.i(TAG, "setLayoutManager: orientation==" + mOrientation);
        super.setLayoutManager(layoutManager);
    }


    /**
     * 搜索并返回下一个焦点控件（可以是recyclerview之外的控件）
     */
    @Override
    public View focusSearch(View focused, int direction) {
        L.i(TAG, "focusSearch " + focused + ",direction= " + direction);
        mDirection = direction;
        View view = super.focusSearch(focused, direction);
        if (focused == null) {
            return view;
        }
        if (view != null) {
            int id = view.getId();
            //该方法返回焦点view所在的父view,如果是在recyclerview之外，就会是null.所以根据是否是null,来判断是否是移出了recyclerview
            View nextFocusItemView = findContainingItemView(view);
            if (nextFocusItemView == null) {
                if (!mCanFocusOutVertical && (direction == View.FOCUS_DOWN || direction == View.FOCUS_UP)) {
                    //屏蔽焦点纵向移出recyclerview
                    return focused;
                }
                if (!mCanFocusOutHorizontal && (direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT)) {
                    //屏蔽焦点横向移出recyclerview
                    return focused;
                }
                //调用移出的监听
                if (mFocusLostListener != null) {
                    mFocusLostListener.onFocusLost(focused, direction);
                }
                return view;
            }
        }
        return view;
    }

    /**
     * 设置item焦点
     *
     * @param子视图
     * @param聚焦焦点视图
     */
    @Override
    public void requestChildFocus(View child, View focused) {
        if (focused != null) {
            this.mFocusGainChildItem = focused;
        }
        if (mSelectedPosition < 0) {
            mSelectedPosition = getChildAdapterPosition(focused);
        }

        L.i(TAG, "nextchild= " + child + ",focused = " + focused);
        if (!hasFocus()) {
            //recyclerview 子view 重新获取焦点，调用移入焦点的事件监听
            if (mFocusGainListener != null) {
                mFocusGainListener.onFocusGain(child, focused);
            }
        }
        super.requestChildFocus(child, focused);
        mCurrentFocusPosition = getChildViewHolder(child).getAdapterPosition();
        L.i(TAG, "focusPos = " + mCurrentFocusPosition);
        int position = getChildAdapterPosition(focused);
        if (mSelectedPosition != position) {
            mSelectedPosition = position;
            if (focused != null && null != mListener) {
                mListener.onItemFocus(mFocusGainChildItem, mSelectedPosition);
            }
            L.d(TAG, "requestChildFocus: SelectPos=" + mSelectedPosition);
            mSelectedItem = focused;
            int distance = getNeedScrollDistance(focused);
            if (distance != 0) {
                if (mScrollMode == SCROLL_MIDDLE) {
                    smoothToCenter(mSelectedPosition);
                    return;
                }
                L.d(TAG, "requestChildFocus: scroll distance=" + distance);
                smoothScrollView(distance);
            }
        }
    }

    @Override
    public void setOnFlingListener(@Nullable OnFlingListener onFlingListener) {
        super.setOnFlingListener(new OnFlingListener() {
            @Override
            public boolean onFling(int i, int i1) {
                return false;
            }
        });
    }

    /**
     * 焦点记忆
     */
    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        View view;
        if (this.hasFocus() || mCurrentFocusPosition < 0 || (view = getLayoutManager().findViewByPosition(mCurrentFocusPosition)) == null) {
            super.addFocusables(views, direction, focusableMode);
        } else if (view.isFocusable()) {
            views.add(view);
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
    }


    /**
     * 平滑滚动视图
     */
    public void smoothScrollView(int scrollDistance) {
        if (mOrientation == HORIZONTAL) {
            smoothScrollBy(scrollDistance, 0);
        } else {
            smoothScrollBy(0, scrollDistance);
        }
    }


    @Override
    public boolean isInTouchMode() {
        boolean result = super.isInTouchMode();
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !result);
        } else {
            return result;
        }
    }


    /**
     * 焦点选则
     */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        L.d(TAG, "onFocusChanged: gainFocus==" + gainFocus);
        if (mSelectedItem != null) {
            if (gainFocus) {
                mSelectedItem.setSelected(true);
            } else {
                mSelectedItem.setSelected(false);
            }
        }
    }


    /**
     * 获取子图顺序
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int focusIndex = indexOfChild(mSelectedItem);
        if (focusIndex < 0) {
            return i;
        }
        if (i < focusIndex) {
            return i;
        } else if (i < childCount - 1) {
            return focusIndex + childCount - 1 - i;
        } else {
            return focusIndex;
        }
    }


    /**
     * 测量
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mIsSetItemSelected) {
            adjustSelectMode();
            mIsSetItemSelected = false;
        }
        // 修复问题:当启动anim时，FocusView定位错误在自动处理焦点模式
        Adapter adapter = getAdapter();
        if (adapter != null && mSelectedPosition >= adapter.getItemCount()) {
            mSelectedPosition = adapter.getItemCount() - 1;
        }
        int selectPos = mSelectedPosition - getFirstVisiblePosition();
        selectPos = selectPos < 0 ? 0 : selectPos;
        mSelectedItem = getChildAt(selectPos);
        L.d(TAG, "onLayout: selectPos=" + selectPos + "=SelectedItem=" + mSelectedItem);
    }


    /**
     * 恢复实例状态
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("super_data");
        super.onRestoreInstanceState(superData);
        setItemSelected(bundle.getInt("select_pos", 0));
    }

    /**
     * 保存实例状态
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("super_data", superData);
        bundle.putInt("select_pos", mSelectedPosition);
        return bundle;
    }

    long mOldTime = 0;
    long mTimeStamp = 300;

    /**
     * 设置按键滚动的时间间隔.
     * 在小于time的间隔内消除掉.
     */
    public void setKeyScrollTime(long time) {
        this.mTimeStamp = time;
    }

    /**
     * 调度按键事件
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isIntercept) {
            if (keycode == -1) {
                return true;
            }
        }
        /**
         *  用于优化按键快速滚动卡顿的问题.
         */
        if (event.getRepeatCount() >= 2 && event.getAction() == KeyEvent.ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mOldTime <= mTimeStamp) {
                return true;
            }
            mOldTime = currentTime;
        }

        if (mListener != null) {
            mListener.onItemStateChange(this.mFocusGainChildItem, event, this.getSelectedPosition());
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    if (mListener != null) {
                        mListener.onItemClick(this.mFocusGainChildItem, this.getSelectedPosition());
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:

                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:

                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:

                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    break;
                case KeyEvent.KEYCODE_ENTER:
                    if (mListener != null) {
                        mListener.onItemClick(this.mFocusGainChildItem, this.getSelectedPosition());
                    }
                    break;
            }
            if (mSelectedItem == null) {
                mSelectedItem = getChildAt(mSelectedPosition);
            }
        }
        return super.dispatchKeyEvent(event);

    }

    /**
     * 获得滚动方式
     */
    private int getNeedScrollDistance(View focusView) {
        switch (mScrollMode) {
            case SCROLL_ALIGN:
            default:
                return getAlignDistance(focusView);
            case SCROLL_FOLLOW:
                return getFollowDistance(focusView);
            case SCROLL_NORMAL:
                return getNormalDistance(focusView);
            case SCROLL_PLACEDTOP:
                return getPlacedtopDistance(focusView);
            case SCROLL_MIDDLE:
                return getScrollMiddle(focusView);
        }
    }

    /**
     * 获得计算后的距离
     */
    private int getAlignDistance(View view) {
        int scrollDistance = 0;
        boolean isVisible = isVisibleChild(view);
        boolean isHalfVisible = isHalfVisibleChild(view);
        if (isHalfVisible || !isVisible) {
            scrollDistance = getScrollPrimary(view);
        }
        return scrollDistance;
    }

    /**
     * 获得向左置顶距离
     */
    private int getFollowDistance(View view) {
        return view.getLeft();
    }

    /**
     * 获得正常滑动的距离
     */
    private int getNormalDistance(View view) {
        return 0;
    }

    /**
     * 获得向上置顶距离
     */
    private int getPlacedtopDistance(View view) {
        return view.getTop();
    }

    /**
     * 获得居中滚动距离
     */
    private int getScrollMiddle(View view) {
        int distance;
        if (mOrientation == HORIZONTAL) {
            if (mDirection != DEFAULT_DIRECTION) {
                if (mDirection == View.FOCUS_UP || mDirection == View.FOCUS_DOWN) {
                    return 0;
                }
            }
            distance = view.getLeft() + (view.getWidth() / 2) - (getWidth() / 2);
        } else {
            if (mDirection != DEFAULT_DIRECTION) {
                if (mDirection == View.FOCUS_LEFT || mDirection == View.FOCUS_RIGHT) {
                    return 0;
                }
            }
            distance = view.getTop() + (view.getHeight() / 2) - (getHeight() / 2);
        }
        L.d(TAG, "distance: " + distance);
        return distance;
    }

    /**
     * 是可见的item
     */
    private boolean isVisibleChild(View child) {
        if (child != null) {
            Rect ret = new Rect();
            return child.getLocalVisibleRect(ret);
        }
        return false;
    }

    /**
     * 是半可见的item
     */
    private boolean isHalfVisibleChild(View child) {
        if (child != null) {
            Rect ret = new Rect();
            boolean isVisible = child.getLocalVisibleRect(ret);
            if (mOrientation == HORIZONTAL) {
                return isVisible && (ret.width() < child.getWidth());
            } else {
                return isVisible && (ret.height() < child.getHeight());
            }
        }
        return false;
    }

    /**
     * 获取滚动值
     */
    private int getScrollPrimary(View view) {
        int distance;
        if (mOrientation == HORIZONTAL) {
            if (mDirection != DEFAULT_DIRECTION) {
                if (mDirection == View.FOCUS_UP || mDirection == View.FOCUS_DOWN) {
                    return 0;
                }
            }
            if (mDirection == View.FOCUS_LEFT) {
                distance = view.getRight() - getClientSize();
            } else {
                distance = view.getLeft();
            }
        } else {
            if (mDirection != DEFAULT_DIRECTION) {
                if (mDirection == View.FOCUS_LEFT || mDirection == View.FOCUS_RIGHT) {
                    return 0;
                }
            }
            if (mDirection == View.FOCUS_DOWN) {
                distance = view.getTop();
            } else {
                distance = view.getBottom() - getClientSize();
            }
        }
        L.e(TAG, "distance: " + distance);
        return distance;
    }

    /**
     * 超过半屏
     */
    private boolean isOverHalfScreen(View child) {
        Rect ret = new Rect();
        child.getGlobalVisibleRect(ret);
        int size = getClientSize();
        if (mOrientation == HORIZONTAL) {
            if (ret.right > size / 2 || ret.left < size / 2) {
                return true;
            }
        } else {
            if (ret.top < size / 2 || ret.bottom > size / 2) {
                return true;
            }
        }
        return false;
    }


    /**
     * 得到控件实际占屏幕宽高
     */
    private int getClientSize() {
        if (mOrientation == HORIZONTAL) {
            return getWidth() - getPaddingLeft() - getPaddingRight();
        } else {
            return getHeight() - getPaddingTop() - getPaddingBottom();
        }
    }

    /**
     * 获得第一可见位置
     */
    public int getFirstVisiblePosition() {
        int firstVisiblePos = -1;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisiblePos = ((LinearLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            }
        }
        return firstVisiblePos;
    }


    /**
     * 调整选择模式
     * 所选项目，有两种情况:
     * 1。项目显示在屏幕上
     * 2。项目没有显示在屏幕上
     */
    private void adjustSelectMode() {
        int childCount = getChildCount();
        if (mSelectedPosition < childCount) {
            mSelectedItem = getChildAt(mSelectedPosition);
            adjustSelectOffset(mSelectedItem);
        } else {
            scrollToPosition(mSelectedPosition);
        }
    }

    /**
     * 调整选择抵消：
     * 调整选定的项目位置到半屏幕位置
     */
    private void adjustSelectOffset(View selectView) {
        scrollOffset(selectView);
        selectView.requestFocus();
    }

    /**
     * 滚动抵消
     */
    private void scrollOffset(View selectView) {
        int dx;
        if (mOrientation == HORIZONTAL) {
            dx = getRight() - getClientSize();
            scrollBy(dx, 0);
        } else {
            dx = selectView.getTop();
            scrollBy(0, dx);
        }
    }

    /**
     * 设置item选中
     * 当调用此方法时，必须确保视图的位置已经显示
     *
     * @param position selected item position
     */
    public void setItemSelected(int position) {
        if (mSelectedPosition == position) {
            return;
        }

        mIsSetItemSelected = true;
        if (position >= getAdapter().getItemCount()) {
            position = getAdapter().getItemCount() - 1;
        }
        mSelectedPosition = position;
        requestLayout();
    }


    /**
     * 设置是否可以纵向移出
     */
    public void setCanFocusOutVertical(boolean mCanFocusOutVertical) {
        this.mCanFocusOutVertical = mCanFocusOutVertical;
    }

    /**
     * 是否可以横向移出
     */
    public void setCanFocusOutHorizontal(boolean mCanFocusOutHorizontal) {
        this.mCanFocusOutHorizontal = mCanFocusOutHorizontal;
    }


    /**
     * 返回选中的item对象
     */
    public View getSelectedView() {
        return mSelectedItem;
    }

    /**
     * 动态设置滚动方式
     */
    public void setScrollMode(int scrollMode) {
        mScrollMode = scrollMode;
    }

    /***
     * 更新内容后设置默认选中的位置
     * */
    public void setCurrentFocusPosition(int currentFocusPosition) {
        mCurrentFocusPosition = currentFocusPosition;
    }

    /**
     * 设置按键屏蔽
     *
     * @param keycode     传-1将忽略该参数
     * @param isIntercept
     */
    public void setKeysIntercept(int keycode, boolean isIntercept) {
        this.keycode = keycode;
        this.isIntercept = isIntercept;
    }

    /**
     * 获取选中position值
     * <p>
     * 当确定tvrecycling clerview宽度时，返回的位置是正确的
     *
     * @return selected view position
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }


    /**
     * 焦点移出列表监听
     */
    public void setFocusLostListener(FocusLostListener focusLostListener) {
        this.mFocusLostListener = focusLostListener;
    }

    public interface FocusLostListener {
        void onFocusLost(View lastFocusChild, int direction);
    }


    /**
     * 焦点移入监听
     */
    public void setGainFocusListener(FocusGainListener focusListener) {
        this.mFocusGainListener = focusListener;
    }

    public interface FocusGainListener {
        void onFocusGain(View child, View focued);
    }

    /**
     * 按确认键返回item position值
     */
    public void setOnItemStateListener(OnItemStateListener listener) {
        mListener = listener;
    }

    public interface OnItemStateListener {
        /**
         * item点击事件
         *
         * @param view
         * @param position
         */
        void onItemClick(View view, int position);

        /**
         * item其他按钮事件
         *
         * @param view
         * @param event
         * @param position
         */
        void onItemStateChange(View view, KeyEvent event, int position);

        /**
         * item焦点事件
         *
         * @param view
         * @param position
         */
        void onItemFocus(View view, int position);
    }

    public Scroller mScroller;
    public int mLasty = 0;
    private int mLastx = 0;


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller != null && mScroller.computeScrollOffset() && mOrientation == HORIZONTAL) {
            scrollBy(mLastx - mScroller.getCurrX(), 0);
            mLastx = mScroller.getCurrX();
            postInvalidate();
        } else if (mScroller != null && mScroller.computeScrollOffset() && mOrientation == VERTICAL) {
            scrollBy(0, mLasty - mScroller.getCurrY());
            mLasty = mScroller.getCurrY();
            postInvalidate();
        }
    }

    /**
     * 将指定item平滑移动到整个view的中间位置
     *
     * @param position
     */
    public void smoothToCenter(int position) {
        if (mOrientation == HORIZONTAL) {
            int parentwidth = getWidth();
            int firstvisiableposition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            View targetChild;
            if ((position - firstvisiableposition) >= 0) {
                targetChild = getChildAt(position - firstvisiableposition);
            } else {
                targetChild = getChildAt(0);
            }
            int childwidth = targetChild.getWidth();
            int childPx = targetChild.getLeft() + childwidth / 2;
            int center = parentwidth / 2;
            mLastx = childPx;
            mScroller.startScroll(childPx, 0, center - childPx, 0, 800);
            postInvalidate();
        } else {
            int parentheight = getHeight();
            int firstvisiableposition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            View targetChild;
            if ((position - firstvisiableposition) >= 0) {
                targetChild = getChildAt(position - firstvisiableposition);
            } else {
                targetChild = getChildAt(0);
            }
            int childheight = targetChild.getHeight();
            int childPy = targetChild.getTop() + childheight / 2;
            int center = parentheight / 2;
            mLasty = childPy;
            mScroller.startScroll(0, childPy, 0, center - childPy, 800);
            postInvalidate();
        }
    }


}
