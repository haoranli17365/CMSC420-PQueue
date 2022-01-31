package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******


/* *****************************************************************************************
 * THE FOLLOWING IMPORTS WILL BE NEEDED BY YOUR CODE, BECAUSE WE REQUIRE THAT YOU USE
 * ANY ONE OF YOUR EXISTING MINHEAP IMPLEMENTATIONS TO IMPLEMENT THIS CLASS. TO ACCESS
 * YOUR MINHEAP'S METHODS YOU NEED THEIR SIGNATURES, WHICH ARE DECLARED IN THE MINHEAP
 * INTERFACE. ALSO, SINCE THE PRIORITYQUEUE INTERFACE THAT YOU EXTEND IS ITERABLE, THE IMPORT OF ITERATOR
 * IS NEEDED IN ORDER TO MAKE YOUR CODE COMPILABLE. THE IMPLEMENTATIONS OF CHECKED EXCEPTIONS
 * ARE ALSO MADE VISIBLE BY VIRTUE OF THESE IMPORTS.
 ** ********************************************************************************* */

import pqueue.exceptions.*;
import pqueue.heaps.ArrayMinHeap;
import pqueue.heaps.EmptyHeapException;
import pqueue.heaps.MinHeap;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
/**
 * <p>{@link MinHeapPriorityQueue} is a {@link PriorityQueue} implemented using a {@link MinHeap}.</p>
 *
 * <p>You  <b>must</b> implement the methods of this class! To receive <b>any credit</b> for the unit tests
 * related to this class, your implementation <b>must</b> use <b>whichever</b> {@link MinHeap} implementation
 * among the two that you should have implemented you choose!</p>
 *
 * @author  ---- Haoran Li ----
 *
 * @param <T> The Type held by the container.
 *
 * @see LinearPriorityQueue
 * @see MinHeap
 * @see PriorityQueue
 */
public class MinHeapPriorityQueue<T> implements PriorityQueue<T>{

	/* ***********************************************************************************
	 * Write any private data elements or private methods for MinHeapPriorityQueue here...*
	 * ***********************************************************************************/
	private ArrayMinHeap<Qnode> heapq;
	private int modify_counter = 0;
	
	private class Qnode implements Comparable<Qnode>{
		protected T data;
		protected int priority;

		public Qnode(T data, int priority){
			this.data = data;
			this.priority = priority;
		}

		@Override
		public int compareTo(Qnode other){
			return (this.priority - other.priority);
		}
		// protected void setData(T data){
		// 	this.data = data;
		// }
		// protected void setPriority(int priority){
		// 	this.priority = priority;
		// }
	}


	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/
		/**
	 * Simple default constructor.
	 */
	public MinHeapPriorityQueue(){
		this.heapq = new ArrayMinHeap<>();
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (priority <= 0){
			throw new InvalidPriorityException("Invalid Priority Number");
		}else{
			modify_counter++;
			heapq.insert(new Qnode(element,priority));
		}
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException {		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (heapq.size() == 0){
			throw new EmptyPriorityQueueException("Empty Queue for dequeue");
		}else{
			modify_counter++;
			T ret_val;
			try{
				ret_val = heapq.getMin().data;
				heapq.deleteMin();
			}catch(EmptyHeapException e){
				throw new EmptyPriorityQueueException("Empty Queue for dequeue");
			}
			return ret_val;
		}
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		T ret_val;
		try{
			ret_val = this.heapq.getMin().data;
		}catch(EmptyHeapException e){
			throw new EmptyPriorityQueueException("Empty Queue for getFirst");
		}
		return ret_val;
	}

	@Override
	public int size() {
		return this.heapq.size();
	}

	@Override
	public boolean isEmpty() {
		return (this.heapq.size() == 0);
	}


	@Override
	public Iterator<T> iterator() {
		return new HeapPQInterator();
	}
	private class HeapPQInterator implements Iterator<T>{
		ArrayMinHeap<Qnode> currArr = heapq;
		Iterator<Qnode> iter = currArr.iterator();
		int curr_mod = modify_counter;

		@Override
		public T next(){
			if (curr_mod != modify_counter){
				throw new ConcurrentModificationException();
			}else{
				return iter.next().data;
			}
		}

		@Override
		public boolean hasNext(){
			if (curr_mod != modify_counter){
				throw new ConcurrentModificationException();
			}else{
				return iter.hasNext();
			}
				
		}
	}

}
