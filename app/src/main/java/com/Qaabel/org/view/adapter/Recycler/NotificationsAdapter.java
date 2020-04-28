package com.Qaabel.org.view.adapter.Recycler;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.NotificationModel;
import com.Qaabel.org.view.fragment.MainActivity.notifications.NotificationsFragment;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>
{

    List<FriendModel> users = new ArrayList<>();
    NotificationsFragment mView;
    FriendProfileViewModel profileViewModel;
    String token;
    NotificationsFragment fragment;
    List<NotificationModel> notifications;
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profileImg;
        ImageView action_img;
        TextView name_et, age_et, action_et;
        LinearLayout notifyLayout;

        public ViewHolder(View view)
        {
            super(view);
            name_et = view.findViewById(R.id.name);
            age_et = view.findViewById(R.id.age);
            action_et = view.findViewById(R.id.action);
            profileImg = view.findViewById(R.id.friend_img);
            action_img = view.findViewById(R.id.action_img);
            notifyLayout = view.findViewById(R.id.notify_layout);

        }
    }

    public NotificationsAdapter(NotificationsFragment fragment, List<NotificationModel> notifications)
    {
        this.mView = fragment;
        this.fragment = fragment;
        this.notifications = notifications;
        this.token = new SharedPref(mView.getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        this.profileViewModel = ViewModelProviders.of(mView.getActivity()).get(FriendProfileViewModel.class);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        holder.name_et.setText(notifications.get(position).getFriend().getName());
        Picasso.get().load(notifications.get(position).getFriend().getImage()).into(holder.profileImg);

        if (notifications.get(position).getType().equals("flashes"))
        {
            holder.action_et.setText(notifications.get(position).getType()+ " you");
            holder.action_et.setTextColor(Color.WHITE);
            holder.name_et.setTextColor(Color.WHITE);
            holder.age_et.setTextColor(Color.WHITE);
            holder.action_img.setImageResource(R.drawable.ic_flash_white);
            holder.notifyLayout.setBackgroundResource(R.drawable.ic_notify_flash);
        } else
        {
            holder.action_et.setText("Flashed You Back");
            holder.action_et.setTextColor(Color.BLACK);
            holder.name_et.setTextColor(Color.BLACK);
            holder.age_et.setTextColor(Color.BLACK);
            holder.action_img.setImageResource(R.drawable.ic_flash_icon);
            holder.notifyLayout.setBackgroundResource(R.drawable.ic_notify_flash_back);
         }
        holder.name_et.setText(notifications.get(position).getFriend().getName());

            }


    public void setFriendsList(ArrayList<FriendModel> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        if (notifications == null)
        {
            return 0;
        }

        return notifications.size();
    }

}

