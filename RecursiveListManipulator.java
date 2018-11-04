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
        if (size(head) - 1 == n) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   HIGH COMPLEXITY
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

    HashSet<Object> elementSet = new HashSet<>();

    @Override
    public boolean containsDuplicates(ListNode head) {

//        // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        if (head == null || head.next == null) {
//            return false;
//        } else if (head.element.equals(head.next.element)) {
//            return true;
//        } else {
//            return containsDuplicates(head.next);
//        }

        if (head == null) {
            System.out.println("false");
            System.out.println();
            elementSet.clear();
            return false;
        } else {
            if (elementSet.contains(head.element)) {
                System.out.println(head.element + " 111");
                System.out.println();
                elementSet.clear();
                return true;
            } else {
                elementSet.add(head.element);
                System.out.println(head.element + " 222");
                return containsDuplicates(head.next);
            }
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
        if (head1.next == null) {
            return new ListNode(head1.element, head2);
        } else {
            return new ListNode(head1.element, append(head1.next, head2));
        }
    }

    @Override
    public ListNode flatten(ListNode head) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            System.out.println("!!!!");
            return new ListNode(head.element, head);
        }
        if (head.element instanceof ListNode) {
            if (((ListNode) head.element).next != null) {
                System.out.println("!");
                return new ListNode(((ListNode) head.element).element, flatten(((ListNode) head.element).next));
            } else {
                System.out.println("!!");
                return new ListNode(((ListNode) head.element).element);
            }
        } else {
            System.out.println("!!!");
            return new ListNode(head.element, flatten(head.next));
        }
    }

    HashMap<ListNode, ListNode> map = new HashMap<>();

    @Override
    public boolean isCircular(ListNode head) {
        if (head == null) {
            map.clear();
            return false;
        } else if (head.next == null) {
            map.clear();
            return false;
        } else {
            if (map.containsKey(head.next)) {
                if (map.get(head.next) == head) {
                    map.clear();
                    return true;
                } else {
                    map.clear();
                    return false;
                }
            } else {
                map.put(head.next, head);
                return isCircular(head.next);
            }
        }
    }

    HashSet<ListNode> set = new HashSet<>();

    @Override
    public boolean containsCycles(ListNode head) {
        if (head == null) {
            set.clear();
            return false;
        } else if (head.next == null) {
            set.clear();
            return false;
        } else {
            if (set.contains(head.next)) {
                set.clear();
                return true;
            } else {
                set.add(head);
                return containsCycles(head.next);
            }
        }
    }

    @Override
    public ListNode sort(ListNode head, Comparator comparator) { //!!!!!!!!!!!!!!!!1

        if (head == null) {

            return null;

        } else if (head.next == null) {

            System.out.println(head.element + "end");
            System.out.println();

            return head;

        } else if (comparator.compare(head.element, head.next.element) > 0) {

            System.out.println(head.element + " 111");

            Object swapElement = head.element;
            head.element = head.next.element;
            head.next.element = swapElement;

            System.out.println(head.element + " 000");

            sort(head, comparator);

            return head; //!!!!!!!!!!!!!111

        } else if (sort(head.next, comparator) == head.next && head.next.next != null) {

            System.out.println(head.element + " 222");

            //head.next = sort(head, comparator);

            return sort(head, comparator);

        } else {

            System.out.println(head.element + " 333");

            return sort(head.next, comparator);
        }

    }

    @Override
    public ListNode map(ListNode head, ITransformation transformation) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return new ListNode(transformation.transform(head.element));
        } else {
            return new ListNode(transformation.transform(head.element), map(head.next, transformation));
        }
    }

    @Override
    public Object reduce(ListNode head, IOperator operator, Object initial) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (head == null) {
            return initial;
        }
        if (head.next == null) {
            return operator.operate(head.element, initial);
        } else {
            return operator.operate(head.element, reduce(head.next, operator, initial));
        }
    }

}
