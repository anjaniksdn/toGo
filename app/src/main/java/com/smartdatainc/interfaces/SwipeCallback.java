package com.smartdatainc.interfaces;

import android.widget.ListView;

/**
 * Created by Anurag Sethi on 09-07-2015.
 */
public interface SwipeCallback {

    public void performSecondAction(int position);

    public void performFirstAction(int position);

    public void OnClickListView(int position);

    public void onDismiss(ListView listView, int reverseSortedPositions);
}
