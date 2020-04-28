package com.Qaabel.org.view.fragment.MainActivity.notifications;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.ApiFreindsResponse;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.RecyclerItemTouchHelper;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.NotificationModel;
import com.Qaabel.org.view.adapter.Recycler.NotificationsAdapter;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{

    private NotificationsViewModel notificationsViewModel;

    RecyclerView recyclerView;

    MutableLiveData<ApiFreindsResponse> mfreinds, mflshes;
    private FriendProfileViewModel profileViewModel;

    String token;

    NotificationsAdapter notificationsAdapter;


    public NotificationsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        init(view);
        action();
        return view;
    }

    private void init(View view)
    {
        recyclerView = view.findViewById(R.id.notify_ecycler);

        profileViewModel = ViewModelProviders.of(this.getActivity()).get(FriendProfileViewModel.class);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        List<NotificationModel> notifications=new SharedPref(getActivity()).getNotifications(AppSharedPrefs.SHARED_PREF_Notification);
        notificationsAdapter = new NotificationsAdapter(NotificationsFragment.this,notifications);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(notificationsAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
            {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
            {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(itemTouchHelperCallback2).attachToRecyclerView(recyclerView);


//        getFlashes(token);
//        getFriends(token);


    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void action()
    {


    }


    @SuppressLint("ResourceAsColor")
    private void FillFriends()
    {
        recyclerView.setAdapter(notificationsAdapter);

        getFriends(token);
    }

    private void getFlashes(String token)
    {

    }

    private void getFriends(String token)
    {
        profileViewModel.getFlashes(token).observe(this, apiFreindsResponse -> {
            if (apiFreindsResponse != null)
            {
                if (apiFreindsResponse.getFlashes().size() > 0)
                    notificationsAdapter.setFriendsList((ArrayList<FriendModel>) apiFreindsResponse.getFlashes());
            }
        });
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
//    {
//        Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();
//    }
}