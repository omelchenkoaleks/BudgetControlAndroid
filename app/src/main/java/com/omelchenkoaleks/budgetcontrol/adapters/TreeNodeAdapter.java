package com.omelchenkoaleks.budgetcontrol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.fragments.SprListFragment.OnListFragmentInteractionListener;
import com.omelchenkoaleks.core.interfaces.TreeNode;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TreeNode} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TreeNodeAdapter extends RecyclerView.Adapter<TreeNodeAdapter.ViewHolder> {

    private final List<? extends TreeNode> list;
    private final OnListFragmentInteractionListener mListener;

    public TreeNodeAdapter(List<? extends TreeNode> items, OnListFragmentInteractionListener listener) {
        list = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spr_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tvSprName.setText(list.get(position).getName());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvSprName;

        public ViewHolder(View view) {
            super(view);
            tvSprName = (TextView) view.findViewById(R.id.spr_name);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
