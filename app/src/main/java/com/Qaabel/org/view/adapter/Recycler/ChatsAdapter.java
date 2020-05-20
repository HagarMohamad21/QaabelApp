package com.Qaabel.org.view.adapter.Recycler;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.helpers.TimeUtils;
import com.Qaabel.org.interfaces.OnMessageLongClick;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.LastChatModel;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.fragment.MainActivity.chat.MessagesFragment;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>
{

    ArrayList<LastChatModel> chats = new ArrayList<>();
    MessagesFragment mView;
    FriendProfileViewModel profileViewModel;
    String token;
    UserModel currentUser;
    OnMessageLongClick onMessageLongClick;
    boolean selected=false;

    MessagesFragment fragment;

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {
        public CircleImageView profileImg;
       public TextView name_et, lastMessageTxt, date_et,unreadTxt;
        ConstraintLayout chatLayout;
        ImageView seenImage;

        public ChatViewHolder(View view)
        {
            super(view);
            seenImage=view.findViewById(R.id.seenImage);
            name_et = view.findViewById(R.id.chat_user_name_txt);
            lastMessageTxt = view.findViewById(R.id.lastMessageTxt);
            profileImg = view.findViewById(R.id.chat_img);
            date_et = view.findViewById(R.id.chat_date_txt);
            chatLayout = view.findViewById(R.id.chat_layout);
            unreadTxt=view.findViewById(R.id.unread_count_txt);


        }
    }

    public ChatsAdapter(MessagesFragment fragment,UserModel currentUser,OnMessageLongClick onMessageLongClick)
    {
        this.currentUser=currentUser;
        this.mView = fragment;
        this.fragment = fragment;
        this.onMessageLongClick=onMessageLongClick;
        this.token = new SharedPref(mView.getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        this.profileViewModel = ViewModelProviders.of(mView.getActivity()).get(FriendProfileViewModel.class);

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);

        ChatViewHolder vh = new ChatViewHolder(v);
        return vh;
    }

    private static final String TAG = "ChatsAdapter";
    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder holder, final int position)
    {
       selected=false;
       if(!mView.isInActionMode){
           holder.chatLayout.setTag(null);
           holder.chatLayout.setBackgroundColor(ContextCompat.getColor(mView.getContext(),R.color.white));
       }
       if(mView.isSelectAll){
           Log.d(TAG, "onBindViewHolder: -----------------------IS SELECT ALL------------------------");
           holder.chatLayout.setBackgroundColor(ContextCompat.getColor(holder.chatLayout.getContext(),R.color.selectedChat));
           holder.chatLayout.setTag("SELECTED");
           mView.messagesList.add(chats.get(position));
       }
        holder.name_et.setText(chats.get(position).getUser().getName());
        if(chats.get(position).getChat().getLast()!=null){
            Log.d(TAG, "onBindViewHolder: -----------------------------------"+chats.get(position).getChat().getLast().getTime());
            holder.lastMessageTxt.setText(chats.get(position).getChat().getLast().getMessage());
           holder.date_et.setText(new TimeUtils().getFormattedDate(chats.get(position).getChat().getLast().getTime()));
           if(chats.get(position).getUnread()>0||mView.MessageUnRead){
               holder.unreadTxt.setVisibility(View.VISIBLE);
               holder.unreadTxt.setText(String.valueOf(mView.MessageUnRead?1:chats.get(position).getUnread()));
               holder.date_et.setTextColor(ContextCompat.getColor(mView.getContext(),R.color.blue));
           }

           if(chats.get(position).getChat().getLast().getSender()==currentUser.get_id()){

               if(!chats.get(position).getChat().getLast().getSeenby().get(0).equals(currentUser.get_id())){
                   Log.d(TAG, "onBindViewHolder: ---SEEN SHOULD BE VISIBLE---------------------" +
                           "------------------"+currentUser.get_id());
                   Log.d(TAG, "onBindViewHolder: ---SEEN SHOULD BE VISIBLE--------------------" +
                           "-------------------"+chats.get(position).getChat().getLast().getSeenby());

                   holder.seenImage.setVisibility(View.VISIBLE);
               }
           }

        }
        Picasso.get().load(Uri.parse(chats.get(position).getUser().getImage())).placeholder(R.drawable.ic_default_img).into(holder.profileImg);
        RxView.clicks(holder.chatLayout).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                     if(mView.isInActionMode){
                         mView.isSelectAll=false;
                         notifyDataSetChanged();
                         if(holder.chatLayout.getTag()!=null&&(holder.chatLayout.getTag().equals("SELECTED"))){
                             mView.messagesList.remove(chats.get(position));
                        holder.chatLayout.setTag(null);
                       onMessageLongClick.onLongClick(false);
                       holder.chatLayout.setBackgroundColor(ContextCompat.getColor(holder.chatLayout.getContext(),R.color.white));
                   }

                      else{
                             mView.messagesList.add(chats.get(position));
                             holder.chatLayout.setBackgroundColor(ContextCompat.getColor(holder.chatLayout.getContext(),R.color.selectedChat));
                             onMessageLongClick.onLongClick(true);
                             holder.chatLayout.setTag("SELECTED");
                         }
                   }

                   else{
                       Bundle bundle = new Bundle();
                       bundle.putString("CHAT_ID",chats.get(position).getChat().getId());
                       bundle.putParcelable("FRIEND_USER",chats.get(position).getUser());
                       Navigation.findNavController(mView.getActivity(), R.id.shopping_nav_host_fragment).navigate(R.id.action_messagesFragment_to_navigation_chat, bundle); }
                });
        holder.chatLayout.setOnLongClickListener(v -> {
           if (holder.chatLayout.getTag()==null){
               holder.chatLayout.setBackgroundColor(ContextCompat.getColor(holder.chatLayout.getContext(),R.color.selectedChat));
               onMessageLongClick.onLongClick(true);
               holder.chatLayout.setTag("SELECTED");
               mView.messagesList.add(chats.get(position));
               return true;
           }
           else return false;
        });


//
    }


    public void setChatList(ArrayList<LastChatModel> chats)
    {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public ArrayList<LastChatModel> getChats() {
        return chats;
    }

    @Override
    public int getItemCount()
    {
        return chats.size();
    }

}

