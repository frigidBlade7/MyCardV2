package com.spaceandjonin.mycrd.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;

import androidx.annotation.BoolRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.internal.ThemeEnforcement;
import com.spaceandjonin.mycrd.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

public class MyCardToggleGroup extends LinearLayout {


    /**
     * Interface definition for a callback to be invoked when a {@link MaterialButton} is checked or
     * unchecked in this group.
     */
    public interface OnButtonCheckedListener {
        /**
         * Called when a {@link MaterialButton} in this group is checked or unchecked.
         *
         * @param group The group in which the MaterialButton's checked state was changed
         * @param checkedId The unique identifier of the MaterialButton whose check state changed
         * @param isChecked Whether the MaterialButton is currently checked
         */
        void onButtonChecked(MyCardToggleGroup group, @IdRes int checkedId, boolean isChecked);
    }

    private static final String LOG_TAG = MaterialButtonToggleGroup.class.getSimpleName();
    private static final int DEF_STYLE_RES =
            R.style.Widget_MaterialComponents_MaterialButtonToggleGroup;


    private final MyCardToggleGroup.CheckedStateTracker checkedStateTracker = new MyCardToggleGroup.CheckedStateTracker();
    //private final MyCardToggleGroup.PressedStateTracker pressedStateTracker = new MyCardToggleGroup.PressedStateTracker();
    private final LinkedHashSet<MyCardToggleGroup.OnButtonCheckedListener> onButtonCheckedListeners =
            new LinkedHashSet<>();
    private final Comparator<MaterialButton> childOrderComparator =
            new Comparator<MaterialButton>() {
                @Override
                public int compare(MaterialButton v1, MaterialButton v2) {
                    int checked = Boolean.valueOf(v1.isChecked()).compareTo(v2.isChecked());
                    if (checked != 0) {
                        return checked;
                    }

                    int stateful = Boolean.valueOf(v1.isPressed()).compareTo(v2.isPressed());
                    if (stateful != 0) {
                        return stateful;
                    }

                    // don't return 0s
                    return Integer.valueOf(indexOfChild(v1)).compareTo(indexOfChild(v2));
                }
            };

    private Integer[] childOrder;
    private boolean skipCheckedStateTracker = false;
    private boolean singleSelection;
    private boolean selectionRequired;

    @IdRes private int checkedId;

    public MyCardToggleGroup(@NonNull Context context) {
        this(context, null);
    }

    public MyCardToggleGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.materialButtonToggleGroupStyle);
    }

    public MyCardToggleGroup(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        context = getContext();
        @SuppressLint("RestrictedApi") TypedArray attributes =
                ThemeEnforcement.obtainStyledAttributes(
                        context, attrs, R.styleable.MaterialButtonToggleGroup, defStyleAttr, DEF_STYLE_RES);

        setSingleSelection(
                attributes.getBoolean(R.styleable.MaterialButtonToggleGroup_singleSelection, false));
        checkedId =
                attributes.getResourceId(R.styleable.MaterialButtonToggleGroup_checkedButton, View.NO_ID);
        selectionRequired =
                attributes.getBoolean(R.styleable.MaterialButtonToggleGroup_selectionRequired, false);
        setChildrenDrawingOrderEnabled(true);
        attributes.recycle();

        ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Checks the appropriate button as requested via XML
        if (checkedId != View.NO_ID) {
            checkForced(checkedId);
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        updateChildOrder();
        super.dispatchDraw(canvas);
    }

    /**
     * This override prohibits Views other than {@link MaterialButton} to be added. It also makes
     * updates to the add button shape and margins.
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!(child instanceof MaterialButton)) {
            Log.e(LOG_TAG, "Child views must be of type MaterialButton.");
            return;
        }

        super.addView(child, index, params);
        MaterialButton buttonChild = (MaterialButton) child;
        setGeneratedIdIfNeeded(buttonChild);
        // Sets sensible default values and an internal checked change listener for this child
        setupButtonChild(buttonChild);

        // Reorders children if a checked child was added to this layout
        if (buttonChild.isChecked()) {
            updateCheckedStates(buttonChild.getId(), true);
            setCheckedId(buttonChild.getId());
        }

        ViewCompat.setAccessibilityDelegate(
                buttonChild,
                new AccessibilityDelegateCompat() {
                    @Override
                    public void onInitializeAccessibilityNodeInfo(
                            View host, @NonNull AccessibilityNodeInfoCompat info) {
                        super.onInitializeAccessibilityNodeInfo(host, info);
                        info.setCollectionItemInfo(
                                AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(
                                        /* rowIndex= */ 0,
                                        /* rowSpan= */ 1,
                                        /* columnIndex= */ getIndexWithinVisibleButtons(host),
                                        /* columnSpan= */ 1,
                                        /* heading= */ false,
                                        /* selected= */ ((MaterialButton) host).isChecked()));
                    }
                });
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);

        if (child instanceof MaterialButton) {
            ((MaterialButton) child).removeOnCheckedChangeListener(checkedStateTracker);
            //((MaterialButton) child).setOnPressedChangeListenerInternal(null);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @NonNull
    @Override
    public CharSequence getAccessibilityClassName() {
        return MaterialButtonToggleGroup.class.getName();
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        AccessibilityNodeInfoCompat infoCompat = AccessibilityNodeInfoCompat.wrap(info);
        infoCompat.setCollectionInfo(
                AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(
                        /* rowCount= */ 1,
                        /* columnCount= */ getVisibleButtonCount(),
                        /* hierarchical= */ false,
                        /* selectionMode = */ isSingleSelection()
                                ? AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_SINGLE
                                : AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_MULTIPLE));
    }

    /**
     * Sets the {@link MaterialButton} whose id is passed in to the checked state. If this
     * MaterialButtonToggleGroup is in {@link #isSingleSelection() single selection mode}, then all
     * other MaterialButtons in this group will be unchecked. Otherwise, other MaterialButtons will
     * retain their checked state.
     *
     * @param id View ID of {@link MaterialButton} to set checked
     * @see #uncheck(int)
     * @see #clearChecked()
     * @see #getCheckedButtonIds()
     * @see #getCheckedButtonId()
     */
    public void check(@IdRes int id) {
        if (id == checkedId) {
            return;
        }

        checkForced(id);
    }

    /**
     * Sets the {@link MaterialButton} whose id is passed in to the unchecked state.
     *
     * @param id View ID of {@link MaterialButton} to set unchecked
     * @see #check(int)
     * @see #clearChecked()
     * @see #getCheckedButtonIds()
     * @see #getCheckedButtonId()
     */
    public void uncheck(@IdRes int id) {
        setCheckedStateForView(id, false);
        updateCheckedStates(id, false);
        checkedId = View.NO_ID;
        dispatchOnButtonChecked(id, false);
    }

    /**
     * Clears the selections. When the selections are cleared, no {@link MaterialButton} in this group
     * is checked and {@link #getCheckedButtonIds()} returns an empty list.
     *
     * @see #check(int)
     * @see #uncheck(int)
     * @see #getCheckedButtonIds()
     * @see #getCheckedButtonId()
     */
    public void clearChecked() {
        skipCheckedStateTracker = true;
        for (int i = 0; i < getChildCount(); i++) {
            MaterialButton child = getChildButton(i);
            child.setChecked(false);

            dispatchOnButtonChecked(child.getId(), false);
        }
        skipCheckedStateTracker = false;

        setCheckedId(View.NO_ID);
    }

    /**
     * When in {@link #isSingleSelection() single selection mode}, returns the identifier of the
     * selected button in this group. Upon empty selection, the returned value is {@link View#NO_ID}.
     * If not in single selection mode, the return value is {@link View#NO_ID}.
     *
     * @return The unique id of the selected {@link MaterialButton} in this group in {@link
     *     #isSingleSelection() single selection mode}. When not in {@link #isSingleSelection() single
     *     selection mode}, returns {@link View#NO_ID}.
     * @see #check(int)
     * @see #uncheck(int)
     * @see #clearChecked()
     * @see #getCheckedButtonIds()
     * @attr ref R.styleable#MaterialButtonToggleGroup_checkedButton
     */
    @IdRes
    public int getCheckedButtonId() {
        return singleSelection ? checkedId : View.NO_ID;
    }

    /**
     * Returns the identifiers of the selected {@link MaterialButton}s in this group. Upon empty
     * selection, the returned value is an empty list.
     *
     * @return The unique IDs of the selected {@link MaterialButton}s in this group. When in {@link
     *     #isSingleSelection() single selection mode}, returns a list with a single ID. When no
     *     {@link MaterialButton}s are selected, returns an empty list.
     * @see #check(int)
     * @see #uncheck(int)
     * @see #clearChecked()
     * @see #getCheckedButtonId()
     */
    @NonNull
    public List<Integer> getCheckedButtonIds() {
        List<Integer> checkedIds = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            MaterialButton child = getChildButton(i);
            if (child.isChecked()) {
                checkedIds.add(child.getId());
            }
        }

        return checkedIds;
    }

    /**
     * Add a listener that will be invoked when the check state of a {@link MaterialButton} in this
     * group changes. See {@link MaterialButtonToggleGroup.OnButtonCheckedListener}.
     *
     * <p>Components that add a listener should take care to remove it when finished via {@link
     * #removeOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener)}.
     *
     * @param listener listener to add
     */
    public void addOnButtonCheckedListener(@NonNull MyCardToggleGroup.OnButtonCheckedListener listener) {
        onButtonCheckedListeners.add(listener);
    }

    /**
     * Remove a listener that was previously added via {@link
     * #addOnButtonCheckedListener(MyCardToggleGroup.OnButtonCheckedListener)}.
     *
     * @param listener listener to remove
     */
    public void removeOnButtonCheckedListener(@NonNull MaterialButtonToggleGroup.OnButtonCheckedListener listener) {
        onButtonCheckedListeners.remove(listener);
    }

    /** Remove all previously added {@link MaterialButtonToggleGroup.OnButtonCheckedListener}s. */
    public void clearOnButtonCheckedListeners() {
        onButtonCheckedListeners.clear();
    }

    /**
     * Returns whether this group only allows a single button to be checked.
     *
     * @return whether this group only allows a single button to be checked
     * @attr ref R.styleable#MaterialButtonToggleGroup_singleSelection
     */
    public boolean isSingleSelection() {
        return singleSelection;
    }

    /**
     * Sets whether this group only allows a single button to be checked.
     *
     * <p>Calling this method results in all the buttons in this group to become unchecked.
     *
     * @param singleSelection whether this group only allows a single button to be checked
     * @attr ref R.styleable#MaterialButtonToggleGroup_singleSelection
     */
    public void setSingleSelection(boolean singleSelection) {
        if (this.singleSelection != singleSelection) {
            this.singleSelection = singleSelection;
            clearChecked();
        }
    }

    /**
     * Sets whether we prevent all child buttons from being deselected.
     *
     * @attr ref R.styleable#MaterialButtonToggleGroup_selectionRequired
     */
    public void setSelectionRequired(boolean selectionRequired) {
        this.selectionRequired = selectionRequired;
    }

    /**
     * Returns whether we prevent all child buttons from being deselected.
     *
     * @attr ref R.styleable#MaterialButtonToggleGroup_selectionRequired
     */
    public boolean isSelectionRequired() {
        return selectionRequired;
    }

    /**
     * Sets whether this group only allows a single button to be checked.
     *
     * <p>Calling this method results in all the buttons in this group to become unchecked.
     *
     * @param id boolean resource ID of whether this group only allows a single button to be checked
     * @attr ref R.styleable#MaterialButtonToggleGroup_singleSelection
     */
    public void setSingleSelection(@BoolRes int id) {
        setSingleSelection(getResources().getBoolean(id));
    }

    private void setCheckedStateForView(@IdRes int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView instanceof MaterialButton) {
            skipCheckedStateTracker = true;
            ((MaterialButton) checkedView).setChecked(checked);
            skipCheckedStateTracker = false;
        }
    }

    private void setCheckedId(int checkedId) {
        this.checkedId = checkedId;

        dispatchOnButtonChecked(checkedId, true);
    }

    /**
     * Sets a negative marginStart on all but the first child, if two adjacent children both have a
     * stroke width greater than 0. This prevents a double-width stroke from being drawn for two
     * adjacent stroked children, and instead draws the adjacent strokes directly on top of each
     * other.
     *
     * <p>The negative margin adjustment amount will be equal to the smaller of the two adjacent
     * stroke widths.
     *
     * <p>Also rearranges children such that they are shown in the correct visual order.
     */
    private void adjustChildMarginsAndUpdateLayout() {
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        if (firstVisibleChildIndex == -1) {
            return;
        }

        for (int i = firstVisibleChildIndex + 1; i < getChildCount(); i++) {
            // Only adjusts margins if both adjacent children are MaterialButtons
            MaterialButton currentButton = getChildButton(i);
            MaterialButton previousButton = getChildButton(i - 1);

            // Calculates the margin adjustment to be the smaller of the two adjacent stroke widths
            int smallestStrokeWidth =
                    Math.min(currentButton.getStrokeWidth(), previousButton.getStrokeWidth());

            LayoutParams params = buildLayoutParams(currentButton);
            if (getOrientation() == HORIZONTAL) {
                MarginLayoutParamsCompat.setMarginEnd(params, 0);
                MarginLayoutParamsCompat.setMarginStart(params, -smallestStrokeWidth);
                params.topMargin = 0;
            } else {
                params.bottomMargin = 0;
                params.topMargin = -smallestStrokeWidth;
                MarginLayoutParamsCompat.setMarginStart(params, 0);
            }

            currentButton.setLayoutParams(params);
        }

        resetChildMargins(firstVisibleChildIndex);
    }

    private MaterialButton getChildButton(int index) {
        return (MaterialButton) getChildAt(index);
    }

    private void resetChildMargins(int childIndex) {
        if (getChildCount() == 0 || childIndex == -1) {
            return;
        }

        MaterialButton currentButton = getChildButton(childIndex);
        LayoutParams params = (LayoutParams) currentButton.getLayoutParams();
        if (getOrientation() == VERTICAL) {
            params.topMargin = 0;
            params.bottomMargin = 0;
            return;
        }

        MarginLayoutParamsCompat.setMarginEnd(params, 0);
        MarginLayoutParamsCompat.setMarginStart(params, 0);
        params.leftMargin = 0;
        params.rightMargin = 0;
    }



    private int getFirstVisibleChildIndex() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isChildVisible(i)) {
                return i;
            }
        }

        return -1;
    }

    private int getLastVisibleChildIndex() {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            if (isChildVisible(i)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isChildVisible(int i) {
        View child = getChildAt(i);
        return child.getVisibility() != View.GONE;
    }

    private int getVisibleButtonCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (this.getChildAt(i) instanceof MaterialButton && isChildVisible(i)) {
                count++;
            }
        }
        return count;
    }

    private int getIndexWithinVisibleButtons(@Nullable View child) {
        if (!(child instanceof MaterialButton)) {
            return -1;
        }
        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (this.getChildAt(i) == child) {
                return index;
            }
            if (this.getChildAt(i) instanceof MaterialButton && isChildVisible(i)) {
                index++;
            }
        }
        return -1;
    }



    /**
     * When a checked child is added, or a child is clicked, updates checked state and draw order of
     * children to draw all checked children on top of all unchecked children.
     *
     * <p>If {@code singleSelection} is true, this will unselect any other children as well.
     *
     * <p>If {@code selectionRequired} is true, and the last child is unchecked it will undo the
     * deselection.
     *
     * @param childId ID of child whose checked state may have changed
     * @param childIsChecked Whether the child is checked
     * @return Whether the checked state for childId has changed.
     */
    private boolean updateCheckedStates(int childId, boolean childIsChecked) {
        List<Integer> checkedButtonIds = getCheckedButtonIds();
        if (selectionRequired && checkedButtonIds.isEmpty()) {
            // undo deselection
            setCheckedStateForView(childId, true);
            checkedId = childId;
            return false;
        }

        // un select previous selection
        if (childIsChecked && singleSelection) {
            checkedButtonIds.remove((Integer) childId);
            for (int buttonId : checkedButtonIds) {
                setCheckedStateForView(buttonId, false);
                dispatchOnButtonChecked(buttonId, false);
            }
        }
        return true;
    }

    private void dispatchOnButtonChecked(@IdRes int buttonId, boolean checked) {
        for (MyCardToggleGroup.OnButtonCheckedListener listener : onButtonCheckedListeners) {
            listener.onButtonChecked(this, buttonId, checked);
        }
    }

    private void checkForced(int checkedId) {
        setCheckedStateForView(checkedId, true);
        updateCheckedStates(checkedId, true);
        setCheckedId(checkedId);
    }

    private void setGeneratedIdIfNeeded(@NonNull MaterialButton materialButton) {
        // Generates an ID if none is set, for relative positioning purposes
        if (materialButton.getId() == View.NO_ID) {
            materialButton.setId(ViewCompat.generateViewId());
        }
    }

    /**
     * Sets sensible default values for {@link MaterialButton} child of this group, set child to
     * {@code checkable}, and set internal checked change listener for this child.
     *
     * @param buttonChild {@link MaterialButton} child to set up to be added to this {@link
     *     MaterialButtonToggleGroup}
     */
    private void setupButtonChild(@NonNull MaterialButton buttonChild) {
        buttonChild.setMaxLines(1);
        buttonChild.setEllipsize(TextUtils.TruncateAt.END);
        buttonChild.setCheckable(true);

        buttonChild.addOnCheckedChangeListener(checkedStateTracker);
        //buttonChild.setOnPressedChangeListenerInternal(pressedStateTracker);

        // Enables surface layer drawing for semi-opaque strokes
        //buttonChild.setShouldDrawSurfaceColorStroke(true);
    }

    @NonNull
    private LinearLayout.LayoutParams buildLayoutParams(@NonNull View child) {
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            return (LayoutParams) layoutParams;
        }

        return new LayoutParams(layoutParams.width, layoutParams.height);
    }

    /**
     * We keep track of which views are pressed and checked to draw them last. This prevents visual
     * issues with overlapping strokes.
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (childOrder == null || i >= childOrder.length) {
            Log.w(LOG_TAG, "Child order wasn't updated");
            return i;
        }

        return childOrder[i];
    }

    private void updateChildOrder() {
        final SortedMap<MaterialButton, Integer> viewToIndexMap = new TreeMap<>(childOrderComparator);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            viewToIndexMap.put(getChildButton(i), i);
        }

        childOrder = viewToIndexMap.values().toArray(new Integer[0]);
    }

    private class CheckedStateTracker implements MaterialButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(@NonNull MaterialButton button, boolean isChecked) {
            // Prevents infinite recursion
            if (skipCheckedStateTracker) {
                return;
            }

            if (singleSelection) {
                checkedId = isChecked ? button.getId() : View.NO_ID;
            }

            boolean buttonCheckedStateChanged = updateCheckedStates(button.getId(), isChecked);
            if (buttonCheckedStateChanged) {
                // Dispatch button.isChecked instead of isChecked in case its checked state was updated
                // internally.
                dispatchOnButtonChecked(button.getId(), button.isChecked());
            }
            invalidate();
        }
    }

/*    private class PressedStateTracker implements MaterialButton.OnPressedChangeListener {

        @Override
        public void onPressedChanged(@NonNull MaterialButton button, boolean isPressed) {
            invalidate();
        }
    }*/
}
