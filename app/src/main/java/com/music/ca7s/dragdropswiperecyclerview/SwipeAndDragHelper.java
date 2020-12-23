package com.music.ca7s.dragdropswiperecyclerview;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Srijith on 22-10-2017.
 */

public class SwipeAndDragHelper extends ItemTouchHelper.Callback {

    private ActionCompletionContract contract;

    public SwipeAndDragHelper(ActionCompletionContract contract) {
        this.contract = contract;
    }

    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        contract.onViewMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        contract.onViewSwiped(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onChildDraw(@NotNull Canvas c,
                            @NotNull RecyclerView recyclerView,
                            @NotNull RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
//            viewHolder.itemView.setAlpha(alpha);
//        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface ActionCompletionContract {
        void onViewMoved(int oldPosition, int newPosition);

        void onViewSwiped(int position);
    }

}
