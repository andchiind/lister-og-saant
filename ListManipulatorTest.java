package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import common.InvalidIndexException;
import common.ListNode;
import interfaces.IListManipulator;
import interfaces.IOperator;
import interfaces.ITransformation;

/**
 * Abstract test class for testing IListManipulator implementations.
 *
 */
public abstract class ListManipulatorTest {

    private final int element1 = 3;
    private final int element2 = 75;
    private final int element3 = -31;
    private final int element4 = 7;

    private IListManipulator manipulator;

    private ListNode list1;
    private ListNode list2;
    private ListNode list3;
    private ListNode list4;

    /**
     * The factory method that must be implemented by any concrete subclass of ListManipulatorTest in order to instantiate a particular implementation of IListManipulator.
     * @return the desired concrete implementation of IListManipulator
     */
    public abstract IListManipulator makeListManipulator();

    /**
     * Method used to set up common test objects prior to every test.
     */
    @Before
    public void setup() {
        manipulator = makeListManipulator();

        list1 = new ListNode(element1);
        list2 = new ListNode(element2, list1);
        list3 = new ListNode(element1, list2);
        list4 = new ListNode(element3, list3);
    }

    /**
     * Tests the size method in the IListManipulator implementation.
     */
    @Test
    public void size() {

        assertEquals(0, manipulator.size(null));
        assertEquals(3, manipulator.size(list3));
    }

    /**
     * Tests the count method in the IListManipulator implementation.
     */
    @Test
    public void count() {

        assertEquals(0, manipulator.count(list3, 1));
        assertEquals(1, manipulator.count(list3, element2));
        assertEquals(2, manipulator.count(list3, element1));
        assertEquals(2, manipulator.count(list3, new Integer(element1)));
    }

    /**
     * Tests the convertToString method in the IListManipulator implementation.
     */
    @Test
    public void convertToString() {

        assertEquals("", manipulator.convertToString(null));
        assertEquals(String.valueOf(element1), manipulator.convertToString(list1));
        assertEquals(element1 + "," + element2 + "," + element1, manipulator.convertToString(list3));
    }

    /**
     * Tests the getFromFront method in the IListManipulator implementation during normal operation.
     * @throws InvalidIndexException not expected to be thrown during this test.
     */
    @Test
    public void getCountingForwards() throws InvalidIndexException {

        assertEquals(element3, manipulator.getFromFront(list4, 0));
        assertEquals(element1, manipulator.getFromFront(list4, 1));
        assertEquals(element2, manipulator.getFromFront(list4, 2));
        assertEquals(element1, manipulator.getFromFront(list4, 3));
    }

    /**
     * Tests the getFromFront method in the IListManipulator implementation with an index that is too large.
     * @throws InvalidIndexException is expected to be thrown during this test.
     */
    @Test(expected = InvalidIndexException.class)
    public void getCountingForwardsIndexTooLarge() throws InvalidIndexException {

        manipulator.getFromFront(list3, 4);
    }

    /**
     * Tests the getFromFront method in the IListManipulator implementation with an empty (null) list and index 0.
     * @throws InvalidIndexException is expected to be thrown during this test.
     */
    @Test(expected = InvalidIndexException.class)
    public void getCountingForwardsEmptyList() throws InvalidIndexException {

        manipulator.getFromFront(null, 0);
    }

    /**
     * Tests the getFromBack method in the IListManipulator implementation during normal operation.
     * @throws InvalidIndexException not expected to be thrown during this test.
     */
    @Test
    public void getCountingBackwards() throws InvalidIndexException {

        assertEquals(element1, manipulator.getFromBack(list4, 0));
        assertEquals(element2, manipulator.getFromBack(list4, 1));
        assertEquals(element1, manipulator.getFromBack(list4, 2));
        assertEquals(element3, manipulator.getFromBack(list4, 3));
    }

    /**
     * Tests the getFromBack method in the IListManipulator implementation with an index that is too large.
     * @throws InvalidIndexException is expected to be thrown during this test.
     */
    @Test(expected = InvalidIndexException.class)
    public void getCountingBackwardsIndexTooLarge() throws InvalidIndexException {

        manipulator.getFromBack(list4, 4);
    }

    /**
     * Tests the getFromBack method in the IListManipulator implementation with an empty (null) list and index 0.
     * @throws InvalidIndexException is expected to be thrown during this test.
     */
    @Test(expected = InvalidIndexException.class)
    public void getCountingBackwardsEmptyList() throws InvalidIndexException {

        manipulator.getFromBack(null, 0);
    }

    /**
     * Tests the deepEquals method in the IListManipulator implementation.
     */
    @Test
    public void deepEquals() {

        assertTrue(manipulator.deepEquals(null, null));
        assertFalse(manipulator.deepEquals(list1, null));
        assertFalse(manipulator.deepEquals(null, list1));
        assertTrue(manipulator.deepEquals(list1, list1));
        assertTrue(manipulator.deepEquals(list1, new ListNode(element1)));
        assertTrue(manipulator.deepEquals(list2, new ListNode(element2, new ListNode(element1))));
        assertFalse(manipulator.deepEquals(list1, list2));
    }

    /**
     * Tests the deepCopy method in the IListManipulator implementation.
     */
    @Test
    public void deepCopy() {

        assertNull(manipulator.deepCopy(null));

        ListNode copy1 = manipulator.deepCopy(list1);
        assertNotSame(list1, copy1);
        assertTrue(manipulator.deepEquals(list1, copy1));

        ListNode copy2 = manipulator.deepCopy(list4);
        assertNotSame(list4, copy2);
        assertNotSame(list4.next, copy2.next);
        assertNotSame(list4.next.next, copy2.next.next);
        assertTrue(manipulator.deepEquals(list4, copy2));
    }

    /**
     * Tests the append method in the IListManipulator implementation.
     */
    @Test
    public void append() {

        assertNull(manipulator.append(null, null));
        assertTrue(manipulator.deepEquals(list1, manipulator.append(list1, null)));
        assertTrue(manipulator.deepEquals(list1, manipulator.append(null, list1)));

        ListNode first = new ListNode(element1, new ListNode(element2));
        ListNode second = new ListNode(element3, new ListNode(element4));
        ListNode expected = new ListNode(element1, new ListNode(element2, new ListNode(element3, new ListNode(element4))));

        assertTrue(manipulator.deepEquals(expected, manipulator.append(first, second)));
    }

    /**
     * Tests the map method in the IListManipulator implementation.
     */
    @Test
    public void map() {

        assertNull(manipulator.map(null, addOne));

        ListNode expected = new ListNode(element3 + 1, new ListNode(element1 + 1, new ListNode(element2 + 1, new ListNode(element1 + 1))));

        assertTrue(manipulator.deepEquals(expected, manipulator.map(list4, addOne)));
    }

    /**
     * Tests the reduce method in the IListManipulator implementation.
     */
    @Test
    public void reduce() {

        assertEquals(0, manipulator.reduce(null, add, 0));
        assertEquals(element3 + element1 + element2 + element1, manipulator.reduce(list4, add, 0));
    }

    /**
     * Tests the flatten method in the IListManipulator implementation.
     */
    @Test
    public void flatten() {

        assertNull(manipulator.flatten(null));
        ListNode testList = manipulator.flatten(new ListNode(list4));
        assertTrue(manipulator.deepEquals(list4, testList));

        ListNode first = new ListNode(element1, new ListNode(element2));
        ListNode second = new ListNode(element3, new ListNode(element4));
        ListNode input = new ListNode(first, new ListNode(second, null));
        ListNode expected = new ListNode(element1, new ListNode(element2, new ListNode(element3, new ListNode(element4))));

        assertTrue(manipulator.deepEquals(expected, manipulator.flatten(input)));
    }

    /**
     * Tests the contains method in the IListManipulator implementation.
     */
    @Test
    public void contains() {

        assertFalse(manipulator.contains(null, element1));
        assertTrue(manipulator.contains(list1, element1));
        assertFalse(manipulator.contains(list1, element2));
        assertFalse(manipulator.contains(list3, element3));
        assertTrue(manipulator.contains(list4, element3));
    }

    /**
     * Tests the containsDuplicates method in the IListManipulator implementation.
     */
    @Test
    public void containsDuplicates() {

        assertFalse(manipulator.containsDuplicates(null));
        assertFalse(manipulator.containsDuplicates(list1));
        assertFalse(manipulator.containsDuplicates(list2));
        assertTrue(manipulator.containsDuplicates(list3));
        assertTrue(manipulator.containsDuplicates(list4));
    }

    /**
     * Tests the isCircular method in the IListManipulator implementation.
     */
    @Test
    public void isCircular() {

        assertFalse(manipulator.isCircular(null));
        assertFalse(manipulator.isCircular(list1));
        assertFalse(manipulator.isCircular(list4));

        ListNode cycle1 = new ListNode(element1);
        cycle1.next = cycle1;

        ListNode last2 = new ListNode(element3);
        ListNode cycle2 = new ListNode(element1, last2);
        last2.next = cycle2;

        ListNode last3 = new ListNode(element3);
        ListNode cycle3 = new ListNode(element1, new ListNode(element2, last3));
        last3.next = cycle3;

        ListNode cyclic = new ListNode(element1, cycle3);

        assertTrue(manipulator.isCircular(cycle1));
        assertTrue(manipulator.isCircular(cycle2));
        assertTrue(manipulator.isCircular(cycle3));
        assertFalse(manipulator.isCircular(cyclic));
    }

    /**
     * Tests the containsCycles method in the IListManipulator implementation.
     */
    @Test
    public void containsCycles() {

        assertFalse(manipulator.containsCycles(null));
        assertFalse(manipulator.containsCycles(list1));
        assertFalse(manipulator.containsCycles(list4));

        ListNode cycle1 = new ListNode(element1);
        cycle1.next = cycle1;

        ListNode last2 = new ListNode(element3);
        ListNode cycle2 = new ListNode(element1, new ListNode(element2, last2));
        last2.next = cycle2;

        ListNode cyclic = new ListNode(element1, cycle2);

        assertTrue(manipulator.containsCycles(cycle1));
        assertTrue(manipulator.containsCycles(cycle2));
        assertTrue(manipulator.containsCycles(cyclic));
    }

    /**
     * Tests the sort method in the IListManipulator implementation.
     */
    @Test
    public void sort() {

        assertNull(manipulator.sort(null, int_comparator));

        ListNode list_a = new ListNode(3);
        ListNode list_b = new ListNode(3, new ListNode(-2));
        ListNode list_c = new ListNode(3, new ListNode(-2, new ListNode(-14)));

        ListNode list_d = new ListNode(5, new ListNode(-2, new ListNode(17, new ListNode(0, new ListNode(-3)))));
        ListNode list_e = new ListNode(5, new ListNode(-2, new ListNode(17, new ListNode(0, new ListNode(-3, new ListNode(0, new ListNode(0, new ListNode(0, new ListNode(-17)))))))));

        assertTrue(manipulator.deepEquals(list_a, manipulator.sort(list_a, int_comparator)));
        assertTrue(manipulator.deepEquals(new ListNode(-2, new ListNode(3)), manipulator.sort(list_b, int_comparator)));
        assertTrue(manipulator.deepEquals(new ListNode(-14, new ListNode(-2, new ListNode(3))), manipulator.sort(list_c, int_comparator)));

        //I added more tests with longer lists to sort, since I was not sure that three nodes was sufficient to prove that the sorter could sort any list
        assertTrue(manipulator.deepEquals(new ListNode(-3, new ListNode(-2, new ListNode(0, new ListNode(5, new ListNode(17))))), manipulator.sort(list_d, int_comparator)));
        assertTrue(manipulator.deepEquals(new ListNode(-17, new ListNode(-3, new ListNode(-2, new ListNode(0, new ListNode(0, new ListNode(0, new ListNode(0, new ListNode(5, new ListNode(17))))))))), manipulator.sort(list_e, int_comparator)));
    }

    /**
     * This ITransformation is to permit the map method to add a 1 to each element in the list.
     */
    private final ITransformation addOne = new ITransformation() {

        @Override
        public Object transform(Object element) {

            return (Integer) element + 1;
        }
    };

    /**
     * This IOperator is to permit the reduce method to perform addition when combining 2 elements.
     */
    private final IOperator add = new IOperator() {

        @Override
        public Object operate(Object element1, Object element2) {

            return (Integer) element1 + (Integer) element2;
        }
    };

    /**
     * This Comparator (raw as we haven't formally covered Polymorphism yet) compares 2 list nodes containing Integer elements.
     */
    private final Comparator int_comparator = new Comparator() {

        @Override
        public int compare(Object object1, Object object2) {

            return (Integer) object1 - (Integer) object2;
        }
    };

}
