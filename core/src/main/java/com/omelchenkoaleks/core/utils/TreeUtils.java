package com.omelchenkoaleks.core.utils;

import com.omelchenkoaleks.core.interfaces.TreeNode;

import java.util.List;

// построитель дерева
public class TreeUtils<T extends TreeNode> {

    public void addToTree(long parentId, T newNode, List<T> storageList) {
        if (parentId != 0) {
            for (T currentNode : storageList) {
                if (currentNode.getId() == parentId) {
                    currentNode.add(newNode);
                    return;
                } else {
                    TreeNode node = recursiveSearch(parentId, currentNode);
                    if (node != null) {
                        node.add(newNode);
                        return;
                    }
                }
            }
        }
        storageList.add(newNode);
    }

    private TreeNode recursiveSearch(long parentId, TreeNode child) {
        for (TreeNode node : child.getChilds()) {
            if (node.getId() == parentId) {
                return node;
            } else if (node.hasChilds()) {
                recursiveSearch(parentId, node);
            }
        }
        return null;
    }

}
