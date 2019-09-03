package com.kevinlinxc.guitarautoscroll;

public enum TutorialModelObject {

    ONE(R.string.tutorial1, R.layout.tutorial_1),
    TWO(R.string.tutorial2, R.layout.tutorial_2),
    THREE(R.string.tutorial3, R.layout.tutorial_3);

    private int mTitleResId;
    private int mLayoutResId;

    TutorialModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
