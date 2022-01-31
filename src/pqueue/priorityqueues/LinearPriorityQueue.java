package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORTS ARE HERE ONLY TO MAKE THE JAVADOC AND iterator() METHOD SIGNATURE
 * "SEE" THE RELEVANT CLASSES. SOME OF THOSE IMPORTS MIGHT *NOT* BE NEEDED BY YOUR OWN
 * IMPLEMENTATION, AND IT IS COMPLETELY FINE TO ERASE THEM. THE CHOICE IS YOURS.
 * ********************************************************************************** */

import demos.GenericArrays;
import pqueue.exceptions.*;
import pqueue.fifoqueues.FIFOQueue;
import pqueue.heaps.ArrayMinHeap;

import java.util.*;
/**
 * <p>{@link LinearPriorityQueue} is a {@link PriorityQueue} implemented as a linear {@link java.util.Collection}
 * of common {@link FIFOQueue}s, where the {@link FIFOQueue}s themselves hold objects
 * with the same priority (in the order they were inserted).</p>
 *
 * <p>You  <b>must</b> implement the methods in this file! To receive <b>any credit</b> for the unit tests related to
 * this class, your implementation <b>must</b>  use <b>whichever</b> linear {@link Collection} you want (e.g
 * {@link ArrayList}, {@link LinkedList}, {@link java.util.Queue}), or even the various {@link List} and {@link FIFOQueue}
 * implementations that we provide for you. You can also use <b>raw</b> arrays, but take a look at {@link GenericArrays}
 * if you intend to do so. Note that, unlike {@link ArrayMinHeap}, we do not insist that you use a contiguous storage
 * {@link Collection}, but any one available (including {@link LinkedList}) </p>
 *
 * @param <T> The type held by the container.
 *
 * @author  ---- Haoran Li ----
 *
 * @see MinHeapPriorityQueue
 * @see PriorityQueue
 * @see GenericArrays
 */
public class LinearPriorityQueue<T> implements PriorityQueue<T> {

	/* ***********************************************************************************
	 * Write any private data elements or private methods for LinearPriorityQueue here...*
	 * ***********************************************************************************/
	private ArrayList<Qnode> queue;
	private int modify_counter = 0;
	private int capacity;

	private class Qnode{
		T data;
		int priority;
		public Qnode(T data, int priority){
			this.data = data;
			this.priority = priority;
		}
	}



	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/

	/**
	 * Default constructor initializes the element structure with
	 * a default capacity. This default capacity will be the default capacity of the
	 * underlying element structure that you will choose to use to implement this class.
	 */
	public LinearPriorityQueue(){
		this.queue = new ArrayList<>();
		this.capacity = 20;
	}

	/**
	 * Non-default constructor initializes the element structure with
	 * the provided capacity. This provided capacity will need to be passed to the default capacity
	 * of the underlying element structure that you will choose to use to implement this class.
	 * @see #LinearPriorityQueue()
	 * @param capacity The initial capacity to endow your inner implementation with.
	 * @throws InvalidCapacityException if the capacity provided is less than 1.
	 */
	public LinearPriorityQueue(int capacity) throws InvalidCapacityException{	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (capacity < 1){
			throw new InvalidCapacityException("Invalid Capacity");
		}else{
			queue = new ArrayList<>();
			this.capacity = capacity;
		}
		
		
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException{	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (priority <= 0){
			throw new InvalidPriorityException("Invalid Priority Number");
		}else{
			modify_counter ++;
			boolean added_flag = false;
			if (queue.size() == 0){
				queue.add(new Qnode(element, priority));
			}else{
				for(int i = 0;i < this.queue.size(); i++){
					Qnode currNode = this.queue.get(i);
					if (currNode.priority > priority){
						queue.add(i, new Qnode(element, priority));
						added_flag = true;
						break;
					}
				}
				if (added_flag == false){
					queue.add(new Qnode(element, priority)); // if not added any element, add to the end of the queue 
				}
				
			}	
		}
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException { 	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (this.queue.size() == 0){
			throw new EmptyPriorityQueueException("Empty Queue");
		}else{
			modify_counter ++;
			T ret_val = queue.get(0).data;
			queue.remove(0);
			return ret_val;
		}
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (this.queue.size() == 0){
			throw new EmptyPriorityQueueException("Empty Queue");
		}else{
			return queue.get(0).data;
		}
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return (queue.size() == 0);
	}


	@Override
	public Iterator<T> iterator() {
		return new LinearPQInterator();
	}

	private class LinearPQInterator implements Iterator<T>{
		private ArrayList<Qnode> currQueue = queue;
		private int i = 0;
		private	int curr_mod = modify_counter;
		@Override
		public T next() throws ConcurrentModificationException{
			if (curr_mod != modify_counter){
				throw new ConcurrentModificationException();
			}
			T ret_val = currQueue.get(i).data;
			i++;
			return ret_val;
		}

		@Override
		public boolean hasNext(){
			return (i < currQueue.size());
		}
	}
}