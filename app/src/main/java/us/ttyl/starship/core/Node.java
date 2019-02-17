package us.ttyl.starship.core;

public class Node<T> {
    private T t;
    Node next;
    Node previous;

    public Node(T t) {
        this.t = t;
    }
    public T getValue() {
        return t;
    }

    public Node<T> next() {
        return next;
    }

    public Node<T> previous() {
        return previous;
    }
}
