package impl;

import java.util.Comparator;
import java.util.HashSet;

import common.InvalidIndexException;
import common.ListNode;
import interfaces.IListManipulator;
import interfaces.IOperator;
import interfaces.ITransformation;

/**
 * This class represents the iterative implementation of the IListManipulator interface.
 *
 */
public class IterativeListManipulator implements IListManipulator {

    @Override
    public int size(ListNode head) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        int size = 0;

        ListNode current = head;

        if (head == null) {
            return 0;
        }

        do {
            size++;
            current = current.next;
        } while (current != null);

        return size;
    }

    @Override
    public boolean contains(ListNode head, Object element) {

        if (head == null) {
            return false;
        }

        ListNode current = head;

        do {
            if (current.element.equals(element)) {
                return true;
            }
            current = current.next;
        } while (current != null);

        return false;
    }

    @Override
    public int count(ListNode head, Object element) {

        int count = 0;

        if (head == null) {
            return 0;
        }

        ListNode current = head;

        do {
            if (current.element.equals(element)) {
                count++;
            }
            current = current.next;
        } while (current != null);

        return count;
    }

    @Override
    public String convertToString(ListNode head) {

        if (head == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        ListNode current = head;

        do {

            if (current != head) {
                builder.append(",");
            }

            builder.append(current.element.toString());
            current = current.next;

        } while (current != null);

        return builder.toString();
    }

    @Override
    public Object getFromFront(ListNode head, int n) throws InvalidIndexException {

        ListNode current = head;

        if (head == null) {
            throw new InvalidIndexException();
        }

        for (int i = 0; i < n; i++) {
            current = current.next;
            if (current == null) {
                throw new InvalidIndexException();
            }
        }
        return current.element;
    }

    @Override
    public Object getFromBack(ListNode head, int n) throws InvalidIndexException {

        int index = size(head) - n - 1;

        if (index < 0) {
            throw new InvalidIndexException();
        }

        ListNode current = head;

        if (head == null) {
            throw new InvalidIndexException();
        }

        for (int i = 0; i < index; i++) {
            current = current.next;
            if (current == null) {
                throw new InvalidIndexException();
            }
        }
        return current.element;
    }

    @Override
    public boolean deepEquals(ListNode head1, ListNode head2) {

        if ((head1 == null && head2 != null) || (head1 != null && head2 == null)) {
            return false;
        }

        if (head1 == null && head2 == null) { // head2 == null is redundant, but makes the code easier to debug
            return true;
        }

        ListNode current1 = head1;
        ListNode current2 = head2;

        do {

            if (!current1.element.equals(current2.element)) {
                return false;
            }

            current1 = current1.next;
            current2 = current2.next;

        } while (current1 != null && current2 != null);

        return current1 == null && current2 == null;
    }

    @Override
    public ListNode deepCopy(ListNode head) {

        if (head == null) {
            return null;
        }

        ListNode current = head;
        ListNode copy = new ListNode(head.element);

        ListNode copyHead = copy;

        do { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            if (current.next != null) {
                copy.next = new ListNode(current.next.element);
            }
            copy.element = current.element;

            current = current.next;
            copy = copy.next;

        } while (current != null);

        return copyHead;
    }

    @Override
    public boolean containsDuplicates(ListNode head) {
        HashSet<Object> set = new HashSet<>();

        if (head == null) {
            return false;
        }

        ListNode current = head;

        do {
            if (set.contains(current.element)) {
                return true;
            } else {
                set.add(current.element);
            }
            current = current.next;
        } while (current != null);

        return false;
    }

    @Override
    public ListNode append(ListNode head1, ListNode head2) {

        if (head1 == null) {
            return head2;
        } else if (head2 == null) {
            return head1;
        }

        ListNode current = head1;

        do {
            current = current.next;
        } while (current.next != null);

        current.next = head2;

        return head1;
    }

    @Override
    public ListNode flatten(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode flatHead = head;
        ListNode flat = null;
        ListNode current = head;

        HashSet<ListNode> set = new HashSet<>();

        do {
            set.add(current); // Checks for circular lists

            if (current.element instanceof ListNode) {

                ListNode tempHead = (ListNode) current.element;
                if (flat == null) { //If the first element is a listNode, then this value should not be assigned as the head, but rather its element
                    flat = (ListNode) current.element;
                    flatHead = flat;
                } else {
                    flat.next = (ListNode) current.element;
                }

                while (flat.next != null && flat.next != tempHead) {
                    flat = flat.next;
                }

            } else {
                if (flat == null) {
                    flat = current;
                    flatHead = flat;
                } else {
                    flat.next = current;
                }
            }

            current = current.next;

            if (flat.next != null) {
                flat = flat.next;
            }

        } while (current != null && current != head && !set.contains(current));

        return flatHead;
    }

    @Override
    public boolean isCircular(ListNode head) {
        HashSet<Object> set = new HashSet<>();

        if (head == null) {
            return false;
        }

        ListNode current = head;

        while (current.next != null) {
            if (set.contains(current.next)) {
                return current.next == head;
            } else {
                set.add(current);
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean containsCycles(ListNode head) {

        HashSet<Object> set = new HashSet<>();

        if (head == null) {
            return false;
        }

        ListNode current = head;

        while (current.next != null) {
            if (set.contains(current.next)) {
                return true;
            } else {
                set.add(current);
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public ListNode sort(ListNode head, Comparator comparator) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode current = head;

        boolean done = false;
        boolean hasMadeSwaps = false;

        while (!done) {

            if (current.next == null) {

                if (!hasMadeSwaps) { //If there have been no swaps made in the loop, the list is sorted
                    done = true;
                } else {
                    current = head; //If there have been swaps made, go through the list again
                    hasMadeSwaps = false;
                }

            } else if (comparator.compare(current.element, current.next.element) > 0) {

                Object swapElement = current.element;
                current.element = current.next.element;
                current.next.element = swapElement;
                hasMadeSwaps = true;

            } else {
                current = current.next;
            }
        }

        return head;
    }

    @Override
    public ListNode map(ListNode head, ITransformation transformation) {

        ListNode current = head;

        while (current != null) {

            current.element = transformation.transform(current.element);
            current = current.next;
        }

        return head;
    }

    @Override
    public Object reduce(ListNode head, IOperator operator, Object initial) {
        ListNode current = head;
        Object returnValue = initial;

        while (current != null) {

            returnValue = operator.operate(current.element, returnValue);
            current = current.next;
        }

        return returnValue;
    }
}
