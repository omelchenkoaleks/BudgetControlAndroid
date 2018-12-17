package com.omelchenkoaleks.core.interfaces;

import java.util.List;

/**
 * Позволяет создать древовидную структуру из любого набора объектов, которые реализуеют
 * интерфейс TreeNode (паттерн "Компоновщик")
 */
public interface TreeNode {

    String getName();

    long getParentId();

    void setId(long id); // установить id

    long getId(); // каждый элемент дерева должен иметь свой уникальный идентификатор

    void add(TreeNode child); // добавить один дочерний элемент

    void remove(TreeNode child); // удалить один дочерний элемент

    List<TreeNode> getChilds(); // дочерних элементов может быть любое количество

    TreeNode getChild(long id); // получение дочернего элемента по id

    TreeNode getParent(); // получение родительского элемента - пригодится в разных ситуациях,
    // например  для отчетности по всем узлам деревьев

    void setParent(TreeNode parent); // установка родительского элемента

    boolean hasChilds(); // проверяет, если дочерние элементы

    boolean hasParent(); // проверяет, есть ли родитель
}
