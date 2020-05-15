package com.Qaabel.org.model.Utilities;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


/**
 * Created by Fred on 8/09/19.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
//            final View foregroundView = ((NotificationsAdapter.ChatViewHolder) viewHolder).itemView;
//
//            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
//        final View foregroundView = ((NotificationsAdapter.ChatViewHolder) viewHolder).itemView;
//        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
//                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        final View foregroundView = ((NotificationsAdapter.ChatViewHolder) viewHolder).itemView;
//        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
//        final View foregroundView = ((NotificationsAdapter.ChatViewHolder) viewHolder).itemView;
//
//        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
//                actionState, isCurrentlyActive);
    }

//    @Override
//    public void onSwiped(RecyclerView.ChatViewHolder viewHolder, int direction) {
//        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
//    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
       // void onSwiped(RecyclerView.ChatViewHolder viewHolder, int direction, int position);
    }
}
