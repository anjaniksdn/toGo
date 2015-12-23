package com.smartdatainc.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.smartdatainc.adapters.ListAdapter;
import com.smartdatainc.helpers.ListViewSwipeGesture;
import com.smartdatainc.interfaces.SwipeCallback;
import com.smartdatainc.toGo.R;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi on 08-07-2015.
 */
public class SwipeListActivity extends AppActivity implements SwipeCallback {

    private ListView listViewObj;
    private ArrayList<String> listData;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.swipe_list);

        initData();
        bindControls();
    }

    public void initData() {
        listViewObj = (ListView) findViewById(R.id.cmn_list_view);
        listData = new ArrayList();

    }

    public void bindControls() {

        initializeListView();

        final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(listViewObj, SwipeListActivity.this, this);
        touchListener.SwipeType = ListViewSwipeGesture.Dismiss;
        listViewObj.setOnTouchListener(touchListener);
    }


    private void initializeListView() {

        listData.add("one");
        listData.add("two");
        listData.add("three");
        listData.add("four");
        listData.add("five");
        listData.add("six");
        listData.add("seven");
        listData.add("eight");
        listData.add("nine");
        listData.add("ten");
        listAdapter = new ListAdapter(this, R.layout.swipe_list_layout ,listData);
        listViewObj.setAdapter(listAdapter);
    }


    @Override
    public void performSecondAction(int position) {

    }

    @Override
    public void performFirstAction(int position) {

    }

    @Override
    public void OnClickListView(int position) {

    }

    @Override
    public void onDismiss(ListView listView, int position) {
        listData.remove(position);
        listAdapter.notifyDataSetChanged();
    }
}
