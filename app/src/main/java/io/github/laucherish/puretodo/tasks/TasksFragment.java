package io.github.laucherish.puretodo.tasks;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.laucherish.puretodo.R;
import io.github.laucherish.puretodo.data.Task;

/**
 * @author laucherish
 * @date 16/4/14
 */
public class TasksFragment extends Fragment implements TasksContract.View{

    private TasksContract.Presenter mPresenter;

    private TasksAdapter mTasksAdapter;

    private RecyclerView mRcvTasks;

    private View mViewNoTasks;

    private ImageView mIvNoTasks;

    private TextView mTvNoTasks;

    private TextView mTvNoTasksAdd;

    private LinearLayout mLlTasksView;

    private TextView mTvFilteringLabel;

    public TasksFragment(){}

    public static TasksFragment newInstance(){
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(new ArrayList<Task>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        mRcvTasks = (RecyclerView) root.findViewById(R.id.rcv_tasks);
        mRcvTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvTasks.setHasFixedSize(true);
        mRcvTasks.setAdapter(mTasksAdapter);

        mTvFilteringLabel = (TextView) root.findViewById(R.id.tv_filtering_label);
        mLlTasksView = (LinearLayout) root.findViewById(R.id.ll_tasks);

        mViewNoTasks = root.findViewById(R.id.ll_notasks);
        mIvNoTasks = (ImageView) root.findViewById(R.id.iv_notasks);
        mTvNoTasks = (TextView) root.findViewById(R.id.tv_notasks);
        mTvNoTasksAdd = (TextView) root.findViewById(R.id.tv_notasks_add);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasks,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedTask();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTasksAdapter.setList(tasks);

        mLlTasksView.setVisibility(View.VISIBLE);
        mViewNoTasks.setVisibility(View.GONE);
    }

    @Override
    public void showAddTask() {

    }

    @Override
    public void showTaskDetailsUi(String taskId) {

    }

    @Override
    public void showTaskMarkedCompleted() {

    }

    @Override
    public void showTaskMarkedActive() {

    }

    @Override
    public void showCompletedTasksCleared() {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showNoTasks() {

    }

    @Override
    public void showNoCompletedTasks() {

    }

    @Override
    public void showNoActiveTasks() {

    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mPresenter.setFiltering(TaskFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mPresenter.setFiltering(TaskFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mPresenter.setFiltering(TaskFilterType.ALL_TASKS);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void showAllFilterLabel() {
        mTvFilteringLabel.setText(R.string.label_all);
    }

    @Override
    public void showCompletedFilterLabel() {
        mTvFilteringLabel.setText(R.string.label_completed);
    }

    @Override
    public void showActiveFilterLabel() {
        mTvFilteringLabel.setText(R.string.label_active);
    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    TasksAdapter.TaskItemListener mItemListener = new TasksAdapter.TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetail(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };
}
