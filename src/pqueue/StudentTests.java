package pqueue;

import org.junit.Test;
import pqueue.exceptions.InvalidCapacityException;
import pqueue.exceptions.InvalidPriorityException;
import pqueue.heaps.ArrayMinHeap;
import pqueue.heaps.MinHeap;
import pqueue.priorityqueues.EmptyPriorityQueueException;
import pqueue.priorityqueues.LinearPriorityQueue;
import pqueue.priorityqueues.MinHeapPriorityQueue;
import pqueue.priorityqueues.PriorityQueue;
import pqueue.heaps.EmptyHeapException;
import pqueue.heaps.LinkedMinHeap;

import static org.junit.Assert.*;

/**
 * {@link StudentTests} is a {@code jUnit} testing library which you should extend with your own tests.
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a> and Haoran Li
 */
public class StudentTests {

    private static String throwableInfo(Throwable thrown){
        return "Caught a " + thrown.getClass().getSimpleName() +
                " with message: " + thrown.getMessage();
    }

    private MinHeap<String> myHeap;
    private PriorityQueue<String> myQueue;

    @Test
    public void initAndAddOneElement() throws InvalidPriorityException {
        try {
            myHeap = new ArrayMinHeap<>();
            myQueue = new MinHeapPriorityQueue<>();
        } catch(Throwable t){
            fail(throwableInfo(t));
        }
        assertTrue("After initialization, all MinHeap and PriorityQueue implementations should report that they are empty.",
                myHeap.isEmpty() && myQueue.isEmpty());
        assertTrue("After initialization, all MinHeap and PriorityQueue implementations should report a size of 0.",
                (myHeap.size() == 0) && (myQueue.size() == 0));
        myHeap.insert("Mary");
        assertEquals("After inserting an element, ArrayMinHeap instances should report a size of 1.", 1, myHeap.size());

        // MinHeap::enqueue() declares that it checks InvalidPriorityException if priority <= 0 (from the docs of MinHeap).
        // In this case, we know for sure that InvalidPriorityException should *not* be thrown, since priority = 2 >= 0.
        // To avoid cluttering a code with "dummy" try-catch blocks, we declare InvalidPriorityException as checked from
        // this test as well. This is why we have the throws declaration after the name of the test.
        myQueue.enqueue("Jason", 2);
        assertEquals("After inserting an element, MinHeapPriorityQueue instances should report a size of 1.", 1, myQueue.size());
    }

    // Here is one simple way to write tests that expect an Exception to be thrown. Another, more powerful method is to
    // use the class org.junit.rules.ExpectedException: https://junit.org/junit4/javadoc/4.12/org/junit/rules/ExpectedException.html
    @Test(expected = InvalidCapacityException.class)
    public void ensureInvalidCapacityExceptionThrown() throws InvalidCapacityException{
         myQueue = new LinearPriorityQueue<>(-2);
    }

    @Test(expected = InvalidPriorityException.class)
    public void ensureInvalidPriorityExceptionThrown() throws InvalidPriorityException, InvalidCapacityException{
        myQueue = new LinearPriorityQueue<>(4);
        myQueue.enqueue("Billy", -1);
    }

    @Test
    public void testEnqueingOrder() throws InvalidPriorityException, EmptyPriorityQueueException {
        myQueue = new MinHeapPriorityQueue<>();
        myQueue.enqueue("Ashish", 8);
        myQueue.enqueue("Diana", 2);        // Lower priority, so should be up front.
        myQueue.enqueue("Adam", 2);        // Same priority, but should be second because of FIFO.
        assertEquals("We were expecting Diana up front.", "Diana", myQueue.getFirst());
    }

    @Test
    public void testDequeuingOrder() throws InvalidPriorityException, EmptyPriorityQueueException {
        testEnqueingOrder();    // To populate myQueue with the same elements.
        myQueue.dequeue();      // Now Adam should be up front.
        assertEquals("We were expecting Adam up front.", "Adam", myQueue.getFirst());
    }

    /* ******************************************************************************************************** */
    /* ********************** YOU SHOULD ADD TO THESE UNIT TESTS BELOW. *************************************** */
    /* ******************************************************************************************************** */
    @Test
    public void testArrayHeapInit(){
        myHeap = new ArrayMinHeap<>();
        
        assertTrue("After initialization, all MinHeap and PriorityQueue implementations should report that they are empty.",
                myHeap.isEmpty());
        
    }
    @Test
    public void testArrayHeapInsertDelete(){
        myHeap = new ArrayMinHeap<>();
        myHeap.insert("t");
        try{
            String a = myHeap.getMin();
            assertEquals("t",a);
            myHeap.insert("b");
            myHeap.insert("e");
            myHeap.insert("z");
            myHeap.insert("w");
            myHeap.insert("a");
            a = myHeap.deleteMin();
            assertEquals("a", a);
            a = myHeap.deleteMin();
            assertEquals("b", a);
            // a = myHeap.deleteMin();
            // assertEquals("t", a);
            // a = myHeap.deleteMin();
            // assertEquals("w", a);
            // a = myHeap.deleteMin();
            // assertEquals("z", a);
        }catch(EmptyHeapException e){
            System.out.println("Empty Heap");
        }
    }
    @Test
    public void testLinearPQ() throws InvalidPriorityException, EmptyPriorityQueueException{
        LinearPriorityQueue queue = new LinearPriorityQueue<>();
        queue.enqueue(10, 12);
        assertEquals(1, queue.size());
        queue.enqueue(7,5);
        assertEquals(2, queue.size());
        queue.enqueue(15, 20);
        assertEquals(3, queue.size());
        queue.enqueue(17, 12);
        assertEquals(4, queue.size());
        assertEquals(7, queue.getFirst());
        queue.dequeue();
        assertEquals(10, queue.getFirst());
        queue.dequeue();
        assertEquals(17, queue.getFirst());
        
    }
    @Test
    public void testMinHeapPQ() throws InvalidPriorityException, EmptyPriorityQueueException{
        MinHeapPriorityQueue queue = new MinHeapPriorityQueue<>();
        queue.enqueue(10, 12);
        queue.enqueue(7, 5);
        assertEquals(2, queue.size());
        assertEquals(7, queue.getFirst());
        queue.enqueue(15, 20);
        queue.enqueue(17, 13);
        assertEquals(4, queue.size());
        assertEquals(7, queue.getFirst());
        Object ret = queue.dequeue();
        assertEquals(7, ret);
        assertEquals(3, queue.size());
        ret = queue.dequeue();
        assertEquals(10, ret);
        assertEquals(2, queue.size());
        ret = queue.dequeue();
        assertEquals(17, ret);
        assertEquals(1, queue.size());
    }

    @Test
    public void testLinkedMinHeap() throws EmptyHeapException{
        LinkedMinHeap heap = new LinkedMinHeap<>();
        heap.insert(12);
        heap.insert(5);
        heap.insert(13);
        heap.insert(4);
        heap.insert(2);
        heap.insert(10);
        heap.insert(25);
        assertEquals(7, heap.size());
        int min = (int)heap.deleteMin();
        assertEquals(2, min);
        min = (int) heap.deleteMin();
        assertEquals(4, min);
        min = (int) heap.deleteMin();
        assertEquals(5, min);
        min = (int) heap.deleteMin();
        assertEquals(10, min);
        min = (int) heap.deleteMin();
        assertEquals(12, min);
        min = (int) heap.deleteMin();
        assertEquals(13, min);
        min = (int) heap.deleteMin();
        assertEquals(25, min);
        assertEquals(0, heap.size());
    }
}
