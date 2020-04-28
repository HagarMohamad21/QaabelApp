package com.Qaabel.org.view.fragment.MainActivity.chat;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Qaabel.org.R;
import com.Qaabel.org.interfaces.DialogItemClicked;
import com.Qaabel.org.interfaces.OnBlockClicked;
import com.Qaabel.org.interfaces.OnMessageLongClick;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.ChatModel;
import com.Qaabel.org.model.entities.LastChatModel;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.ItemDecoration;
import com.Qaabel.org.view.MessageMenuDialog;
import com.Qaabel.org.view.activity.MainActivity;
import com.Qaabel.org.view.adapter.Recycler.ChatsAdapter;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MessagesFragment extends Fragment implements OnMessageLongClick, OnBlockClicked , DialogItemClicked {
    RecyclerView recyclerView;
    ChatsAdapter chatsAdapter;
    UserModel  currentUser;
    Toolbar toolbar;
    private FriendProfileViewModel profileViewModel;
   ImageView menuIcon,deleteIcon;
   TextView messageNumTxt;
   LinearLayout noMessagesLayout;
    private int selectedMessagesCount=0;
    public boolean isInActionMode=false,isSelectAll=false;
    String token;
   public List<LastChatModel> deletedList=new ArrayList<>();


    private DialogInterface.OnDismissListener onDismissListener;


  @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        init(view);
        action();
        return view;
    }



    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void action()
    {
    menuIcon.setOnClickListener(v ->
    {
        //show menu
        MessageMenuDialog dialog=new MessageMenuDialog(getContext());
        dialog.setDialogItemClicked(this);
        dialog.show();
    });

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearActionMenu();
            deletedList.clear();
        }
    });
    deleteIcon.setOnClickListener(v -> {
        Utilities utils=new Utilities();
        utils.setOnBlockClicked(this);
        utils.blockDialog(getActivity(),"",false,true);

    });
    }

    private void clearActionMenu() {
        isInActionMode=false;
        isSelectAll=false;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        messageNumTxt.setVisibility(View.GONE);
        deleteIcon.setVisibility(View.GONE);
        selectedMessagesCount=0;
        chatsAdapter.notifyDataSetChanged();
    }


    private void getChats(String token)
    {
        profileViewModel.getChats(token).observe(this, apiChatResponse -> {
            if (apiChatResponse != null)
            {
                if (apiChatResponse.getChats().size() > 0){
                   populateList(apiChatResponse.getChats());
                }
                else{
                    recyclerView.setVisibility(View.GONE);
                    noMessagesLayout.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void populateList(List<LastChatModel> chats) {
        List<LastChatModel> chatsWithoutFlashesMessages=new ArrayList<>();
        recyclerView.setVisibility(View.VISIBLE);
        noMessagesLayout.setVisibility(View.GONE);
        for (LastChatModel chat :chats){
            if(chat.getChat().getLast()!=null)
            chatsWithoutFlashesMessages.add(chat);
        }
        chatsAdapter.setChatList((ArrayList<LastChatModel>) chatsWithoutFlashesMessages);
        if(chatsWithoutFlashesMessages.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            noMessagesLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init(View view)
    {
        currentUser=new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER);
        recyclerView = view.findViewById(R.id.message_recycler);
        noMessagesLayout=view.findViewById(R.id.noMessagesLayout);
        chatsAdapter=new ChatsAdapter(MessagesFragment.this,currentUser,this);
        toolbar=view.findViewById(R.id.toolbar);
         menuIcon=toolbar.findViewById(R.id.menuIcon);
         messageNumTxt=toolbar.findViewById(R.id.messageNumTxt);
         deleteIcon=toolbar.findViewById(R.id.deleteIcon);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        profileViewModel = ViewModelProviders.of(this.getActivity()).get(FriendProfileViewModel.class);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(chatsAdapter);
        recyclerView.addItemDecoration(new ItemDecoration(getContext()));
        getChats(token);
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

    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos=viewHolder.getAdapterPosition();
          switch(direction){
              case ItemTouchHelper.LEFT:{
                  if(chatsAdapter!=null){
                      deleteChat(chatsAdapter.getChats().get(pos).getChat().getId(),pos);

                  }

                  break;
              }
              case ItemTouchHelper.RIGHT:{
                  break;
              }
          }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
                                boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.detetRed))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
//                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue))
//                    .addSwipeRightActionIcon(R.drawable.ic_unread)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void deleteChat(String id,int pos) {
        profileViewModel.deleteMessage(token,id).observe(this,activeResponse ->{
            if(activeResponse!=null){
                if(activeResponse.getMessage().equals("chat deleted")){
                    chatsAdapter.getChats().remove(pos);
                    chatsAdapter.notifyItemRemoved(pos);
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedMessagesCount=0;

    }

    @Override
    public void onResume() {
        super.onResume();
       selectedMessagesCount=0;
    }


    @Override
    public void onLongClick(boolean add) {
        if(add)
        selectedMessagesCount++;
        else  selectedMessagesCount--;
        isInActionMode=true;
        chatsAdapter.notifyDataSetChanged();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messageNumTxt.setVisibility(View.VISIBLE);
        deleteIcon.setVisibility(View.VISIBLE);
        messageNumTxt.setText(String.valueOf(selectedMessagesCount));
    }

    @Override
    public void onBlockClicked() {
       clearActionMenu();
       deleteSelectedList();
    }

    private void deleteSelectedList() {
       for(LastChatModel chat:deletedList){
           chatsAdapter.getChats().remove(chat);
       }
       chatsAdapter.notifyDataSetChanged();
        deletedList.clear();

    }

    @Override
    public void onUnBlockClicked() {

    }

    @Override
    public void ItemClicked(@NotNull String item) {
        if(item.equals("SELECT_ALL")){
         if(chatsAdapter.getChats().size()>0){
             isSelectAll=true;
             isInActionMode=true;
             chatsAdapter.notifyDataSetChanged();
             ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             messageNumTxt.setVisibility(View.VISIBLE);
             deleteIcon.setVisibility(View.VISIBLE);
             selectedMessagesCount=chatsAdapter.getItemCount();
             messageNumTxt.setText(String.valueOf(selectedMessagesCount));
         }

        }
    }
}
