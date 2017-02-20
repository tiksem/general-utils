package com.utils.framework.collections;

/**
 * User: Tikhonenko.S
 * Date: 09.07.14
 * Time: 22:03
 */
public class LinkedStack<T> implements Stack<T> {
    private static class Node<T> {
        Node<T> next;
        T data;
    }

    private Node<T> head;

    @Override
    public void push(T value) {
        if (head == null) {
            head = new Node<>();
            head.data = value;
        } else {
            Node<T> newNode = new Node<>();
            newNode.data = value;
            newNode.next = head;
            head = newNode;
        }
    }

    @Override
    public T pop() {
        throwIfEmpty();
        T result = head.data;
        head = head.next;
        return result;
    }

    @Override
    public T top() {
        throwIfEmpty();
        return head.data;
    }

    private void throwIfEmpty() {
        if (head == null) {
            throw new EmptyStackException();
        }
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void clear() {
        head = null;
    }

    @Override
    public void replaceTop(T value) {
        throwIfEmpty();
        head.data = value;
    }
}
