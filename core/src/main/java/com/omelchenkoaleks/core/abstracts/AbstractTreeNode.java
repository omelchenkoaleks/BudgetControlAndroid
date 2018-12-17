package com.omelchenkoaleks.core.abstracts;

import java.util.ArrayList;
import java.util.List;

import com.omelchenkoaleks.core.interfaces.TreeNode;

public abstract class AbstractTreeNode implements TreeNode {

    private long id = -1;// начальное значение id для нового создаваемого объекта, нужно чтобы можно было откатывать изменение в коллекции
    private List<TreeNode> childs = new ArrayList<>();
    private TreeNode parent;
    private String name;
    private long parentId;

    public AbstractTreeNode() {
    }

    public AbstractTreeNode(String name) {
        this.name = name;
    }

    public AbstractTreeNode(List<TreeNode> childs) {
        this.childs = childs;
    }

    public AbstractTreeNode(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public AbstractTreeNode(long id, List<TreeNode> childs, TreeNode parent, String name) {
        this.id = id;
        this.childs = childs;
        this.parent = parent;
        this.name = name;
    }

    @Override
    public void add(TreeNode child) {
        child.setParent(this);
        childs.add(child);
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void remove(TreeNode child) {
        childs.remove(child);
    }

    @Override
    public List<TreeNode> getChilds() {
        return childs;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public TreeNode getChild(long id) {

        for (TreeNode child: childs) {
            if (child.getId() == id){
                return child;
            }
        }

        return null;
    }


    @Override
    public boolean hasChilds(){
        return !childs.isEmpty();// если есть дочерние элементы - вернуть true
    }

    @Override
    public boolean hasParent() {
        return parent!=null;// если есть родитель - вернет true
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTreeNode that = (AbstractTreeNode) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
