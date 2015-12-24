package com.smartdatainc.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.smartdatainc.adapters.SwipeListAdapter;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.SwipeRefresh;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.SwipeRefreshManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag Sethi on 26-06-2015.
 */
public class SwipeRefreshActivity extends AppActivity implements SwipeRefreshLayout.OnRefreshListener, ServiceRedirection{

    ListView listViewObj;
    SwipeRefreshLayout swipeRefreshLayoutObj;
    SwipeListAdapter swipeListAdapterObj;
    List<SwipeRefresh> movieListObj;
    SwipeRefreshManager swipeRefreshManagerObj;
    SwipeRefresh swipeRefreshObj;

    //initially offset will 0, later will be updating while parsing the json
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_refresh);

        initData();
        bindControls();

    }


    public void initData() {

        swipeRefreshLayoutObj = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        listViewObj = (ListView) findViewById(R.id.activity_main_listview);
        movieListObj = new ArrayList<>();

        swipeRefreshManagerObj = new SwipeRefreshManager(this, this);
        swipeRefreshObj = new SwipeRefresh();
    }

    public void bindControls() {
        swipeListAdapterObj = new SwipeListAdapter(this, movieListObj);
        listViewObj.setAdapter(swipeListAdapterObj);
        swipeRefreshLayoutObj.setOnRefreshListener(this);
        swipeRefreshLayoutObj.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        /**
         * Showing Swipe Refresh animation on activity create
         * An animation won't start on onCreate, post runnable is used
         */

        swipeRefreshLayoutObj.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutObj.setRefreshing(true);
                fetchMovies();
            }
        });

    }

    @Override
    public void onRefresh() {
        fetchMovies();
    }

    /**
     * Fetching movies from the provided URL
     */
    private void fetchMovies() {

        //showing refresh animation before making http call
        swipeRefreshLayoutObj.setRefreshing(true);

        swipeRefreshObj.offset = offset;

        swipeRefreshManagerObj.fetchMovies(swipeRefreshObj);
    }

    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */
    @Override
    public void onSuccessRedirection(int taskID) {
        if(taskID == Constants.TaskID.SWIPE_REFRESH_TASK_ID) {
            int arrayListCount = AppInstance.swipeRefreshListObj.size();

            for(int i = 0; i < arrayListCount; i++) {
                movieListObj.add(i, AppInstance.swipeRefreshListObj.get(i));
            }

            swipeListAdapterObj.notifyDataSetChanged();
            swipeRefreshLayoutObj.setRefreshing(false);
        }
    }

    /**
     * The interface method implemented in the java files
     *
     * @param errorMessage the error message to be displayed
     * @return none
     */
    @Override
    public void onFailureRedirection(String errorMessage) {

    }
    public void onSuccessRedirection(int taskID,String jsonMesseage) {

    }
}
