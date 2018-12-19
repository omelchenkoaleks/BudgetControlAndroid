package com.omelchenkoaleks.budgetcontrol.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omelchenkoaleks.budgetcontrol.adapters.abstracts.TreeNodeListAdapter;
import com.omelchenkoaleks.budgetcontrol.listeners.TreeNodeActionListener;
import com.omelchenkoaleks.core.interfaces.TreeNode;

public class TreeNodeListFragment<T extends TreeNode> extends BaseNodeListFragment<T, TreeNodeListAdapter, TreeNodeActionListener> {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); // потом уже вызывать родительский метод
        return view;
    }

    public void insertChildNode(TreeNode node) {
        adapter.insertChildNode(node);
    }


}
