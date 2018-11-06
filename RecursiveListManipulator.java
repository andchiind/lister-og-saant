package impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import common.InvalidIndexException;
import common.ListNode;
import interfaces.IListManipulator;
import interfaces.IOperator;
import interfaces.ITransformation;

/**
 * This class represents the recursive implementation of the IListManipulator interface.
 *
 */
public class RecursiveListManipulator implements IListManipulator {

    @Override
    public int size(ListNode head) {

        if (head == null) {
            return 0;
        }
        if (head.next == null) {
            return 1;
        } else {
            return size(head.next) + 1;
        }
    }

    @Override
    public boolean contains(ListNode head, Object element) {

        if (head == null) {
            return false;
        } else if (head.element.equals(element)) {
            return true;
        }

        return contains(head.next, element);
    }

    @Override
    public int count(ListNode head, Object element) {

        if (head == null) {
            return 0;
        } else if (head.element.equals(element)) {
            return count(head.next, element) + 1;
        }

        return count(head.next, element);
    }

    @Override
    public String convertToString(ListNode head) {

        if (head == null) {
            return "";
        }
        if (head.next == null) {
            return head.element.toString();
        } else {
            return head.element.toString() + "," + convertToString(head.next);
        }
    }

    @Override
    public Object getFromFront(ListNode head, int n) throws InvalidIndexException {

        if (head == null) {
            throw new InvalidIndexException();
        }
        if (n == 0) {
            return head.element;
        } else {
            return getFromFront(head.next, n - 1);
        }
    }

    @Override
    public Object getFromBack(ListNode head, int n) throws InvalidIndexException {

        if (head == null) {
            throw new InvalidIndexException();
        }
        if (size(head) - 1 == n) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   HIGH COMPLEXITY
            return head.element;
        } else {
            return getFromFront(head.next, n);
        }
    }

    @Override
    public boolean deepEquals(ListNode head1, ListNode head2) {

        if (head1 == null && head2 == null) {
            return true;
        } else if (head1 == null || head2 == null) {
            return false;
        } else if (!head1.element.equals(head2.element)) {
            return false;
        } else {
            return deepEquals(head1.next, head2.next);
        }
    }

    @Override
    public ListNode deepCopy(ListNode head) {

        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return new ListNode(head.element);
        } else {
            return new ListNode(head.element, deepCopy(head.next));
        }
    }

    private boolean checkDuplicates(ListNode head, HashSet<Object> elementSet) {

        if (head == null) {
            return false;
        } else {
            if (elementSet.contains(head.element)) {
                return true;
            } else {
                elementSet.add(head.element);
                return checkDuplicates(head.next, elementSet);
            }
        }
    }

    @Override
    public boolean containsDuplicates(ListNode head) {

        if (head == null) {
            return false;
        }

        return checkDuplicates(head, new HashSet<>());
    }

    private ListNode appendList(ListNode head1, ListNode head2) { //This method reduces the number of checks per method call

        if (head1.next == null) {
            head1.next = head2;
            return head1;
        } else {
            head1.next = append(head1.next, head2);
            return head1;
        }
    }

    @Override
    public ListNode append(ListNode head1, ListNode head2) {

        if (head2 == null) {
            return head1;
        }
        if (head1 == null) {
            return head2;
        }
        return appendList(head1, head2);
    }

    @Override
    public ListNode flatten(ListNode head) {
        if (head == null) {
            return null;
        }
        if (head.element instanceof ListNode) {

            ListNode next = head.next;

            head.next = ((ListNode) head.element).next;
            head.element = ((ListNode) head.element).element;

            head = append(head, next); // High Complexity, but easier to understand and debug
        }

        head.next = flatten(head.next);
        return head;
    }

    private boolean checkCircular(ListNode head, HashMap<ListNode, ListNode> map) {

        if (head.next == null) {

            return false;
        } else {

            if (map.containsKey(head.next)) {

                return map.get(head.next) == head;

            } else {

                map.put(head.next, head);
                return checkCircular(head.next, map);
            }
        }
    }

    @Override
    public boolean isCircular(ListNode head) {

        if (head == null) {
            return false;
        }

        return checkCircular(head, new HashMap<>());
    }

    private boolean checkCycles(ListNode head, HashSet<ListNode> set) {

        if (head.next == null) {

            return false;

        } else {

            if (set.contains(head.next)) {
                return true;
            } else {
                set.add(head);
                return checkCycles(head.next, set);
            }
        }

    }

    @Override
    public boolean containsCycles(ListNode head) {
        if (head == null) {
            return false;
        }
        return checkCycles(head, new HashSet<>());
    }

    /**
     * This method is a recursive form of bubble sort, except that it does not need to return to the initial head if a swap is made.
     * @param head the head of the list
     * @param comparator the ordering to be used
     * @return the head of the sorted list
     */

    @Override
    public ListNode sort(ListNode head, Comparator comparator) {

        if (head == null) {

            return null;

        } else if (head.next == null) {

            return head;

        } else if (comparator.compare(head.element, head.next.element) > 0) {

            //Here, the elements of the current and the next node are swapped

            Object swapElement = head.element;
            head.element = head.next.element;
            head.next.element = swapElement;

            sort(head, comparator);
            return head; //If a swap is made, the current head is returned to the previous call

            //This checks whether a swap is made on the next element, in which case current element will sort itself again.
            //If a swap is made in this case, the value will be returned to the previous call, and the cycle will continue.
        } else if (sort(head.next, comparator) == head.next && head.next.next != null) {

            return sort(head, comparator);

        } else {

            //If no swap needs to be made on this node or the next, the recursion continues
            return sort(head.next, comparator);
        }

    }

    @Override
    public ListNode map(ListNode head, ITransformation transformation) {

        if (head == null) {

            return null;

        } else {

            head.element = transformation.transform(head.element);
            head.next = map(head.next, transformation);
            return head;
        }
    }

    @Override
    public Object reduce(ListNode head, IOperator operator, Object initial) {

        if (head == null) {

            return initial;

        } else {

            return operator.operate(head.element, reduce(head.next, operator, initial));
        }
    }

}
