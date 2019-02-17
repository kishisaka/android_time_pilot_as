package us.ttyl.starship.core;

public class LinkedListHolder<T> {
    private Node<T> head = null;
    private Node<T> tail = null;

    private int count = 0;

    public Node<T> getHead() {
        return head;
    }

    public void remove(Node<T> node) {
        if (node == head) {
            // removing start
            Node<T> temp = head.next;
            if (temp != null) {
                temp.previous = null;
                head = temp;
                count = count -1;
            }
        } else if (node == tail) {
            // removing end
            Node<T> temp = tail.previous;
            if (temp != null) {
                temp.next = null;
                tail = temp;
                count = count -1;
            }
        } else {
            //removing middle
            Node<T> previousNode = node.previous;
            Node<T> nextNode = node.next;
            if (previousNode != null) {
                previousNode.next = nextNode;
            }
            if (nextNode != null) {
                nextNode.previous = previousNode;
            }
            count = count -1;
        }
    }

    public void add(Node<T> node) {
        if (head == null) {
            // start new list
            head = node;
            tail = node;
        } else {
            // add to end of list
            tail.next = node;
            node.previous = tail;
            tail = node;
        }
        count = count + 1;
    }

    public void clear() {
        head = null;
        tail = null;
        count = 0;
    }
}
