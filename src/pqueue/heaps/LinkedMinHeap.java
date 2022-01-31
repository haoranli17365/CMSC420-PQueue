package pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******



/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;

import pqueue.exceptions.UnimplementedMethodException;

/**
 * <p>A {@link LinkedMinHeap} is a tree (specifically, a <b>complete</b> binary tree) where every node is
 * smaller than or equal to its descendants (as defined by the {@link Comparable#compareTo(Object)} overridings of the type T).
 * Percolation is employed when the root is deleted, and insertions guarantee maintenance of the heap property in logarithmic time. </p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a &quot;linked&quot;, <b>non-contiguous storage</b> implementation based on a
 * binary tree of nodes and references. Use the skeleton code we have provided to your advantage, but always remember
 * that the only functionality our tests can test is {@code public} functionality.</p>
 * 
 * @author ---- Haoran Li ----
 *
 * @param <T> The {@link Comparable} type of object held by {@code this}.
 *
 * @see MinHeap
 * @see ArrayMinHeap
 */
public class LinkedMinHeap<T extends Comparable<T>> implements MinHeap<T> {

	/* ***********************************************************************
	 * An inner class representing a minheap's node. YOU *SHOULD* BUILD YOUR *
	 * IMPLEMENTATION ON TOP OF THIS CLASS!                                  *
	  * ********************************************************************* */
	private class MinHeapNode {
		private T data;
		private MinHeapNode lChild, rChild;
		
        /* *******************************************************************
         * Write any further data elements or methods for MinHeapNode here...*
         ********************************************************************* */
		private int index;
	}

	/* *********************************
	  * Root of your tree: DO NOT ERASE!
	  * *********************************
	 */
	private MinHeapNode root;



    /* *********************************************************************************** *
     * Write any further private data elements or private methods for LinkedMinHeap here...*
     * *************************************************************************************/
	private int size;
	private int modify_counter = 0;

	// copy all the nodes connection to the currnt heap.
	protected MinHeapNode copyConstructorHelper(MinHeapNode currNode, MinHeapNode corresNode, int index){
		if (corresNode == null){
			return null;
		}else{
			currNode = new MinHeapNode();
			currNode.data = corresNode.data;
			currNode.index = index;
			currNode.lChild = copyConstructorHelper(currNode.lChild, corresNode.lChild, 2*index+1);
			currNode.rChild = copyConstructorHelper(currNode.rChild, corresNode.rChild, 2*index+2);
			return currNode;
		}
	}

	/*
	*	covert the current heap as arraylist based on the index of each node
	*/
	protected void covertToArray(MinHeapNode currNode, ArrayList<MinHeapNode> arr){
		if (currNode == null){
			return;
		}else{
			arr.add(currNode);
			covertToArray(currNode.lChild, arr);
			covertToArray(currNode.rChild, arr);
		}
	}
	/*
	*	covert the current Arraylist to the minheap based on the index
	*/
	protected MinHeapNode covertToHeap(MinHeapNode currNode, ArrayList<MinHeapNode> arr, int index){
		if (index >= arr.size()){
			return null;
		}else{
			currNode = arr.get(index);
			// currNode.index = index;
			currNode.lChild = covertToHeap(currNode.lChild, arr, 2*index+1);
			currNode.rChild = covertToHeap(currNode.rChild, arr, 2*index+2);
			return currNode;
		}
	}

	protected int siftDownOptionIdx(ArrayList<MinHeapNode> currArr, int currIdx){
		// if right child index of current index is excessed the max array size or left child is lesser than right child, return left child index, vise versa.
		return ((currIdx*2+2 > currArr.size()-1)||(currArr.get(currIdx*2+1).data).compareTo(currArr.get(currIdx*2+2).data) <= 0) ? currIdx*2+1 : currIdx*2+2;
	}

    /* *********************************************************************************************************
     * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
     ***********************************************************************************************************/

	/**
	 * Default constructor.
	 */
	public LinkedMinHeap() {
		this.root = null;
		this.size = 0;
	}

	/**
	 * Second constructor initializes {@code this} with the provided element.
	 *
	 * @param rootElement the data to create the root with.
	 */
	public LinkedMinHeap(T rootElement) {
		this.root = new MinHeapNode();
		this.root.data = rootElement;
		this.root.lChild = null;
		this.root.rChild = null;
		this.root.index = 0;
		this.size = 1;

		
	}

	/**
	 * Copy constructor initializes {@code this} as a carbon
	 * copy of the parameter, which is of the general type {@link MinHeap}!
	 * Since {@link MinHeap} is an {@link Iterable} type, we can access all
	 * of its elements in proper order and insert them into {@code this}.
	 *
	 * @param other The {@link MinHeap} to copy the elements from.
	 */
	public LinkedMinHeap(MinHeap<T> other) {
		this.size = ((LinkedMinHeap<T>) other).size;
		this.root = copyConstructorHelper(this.root, ((LinkedMinHeap<T>) other).root, 0);
	}
	

    /**
     * Standard {@code equals} method. We provide this for you. DO NOT EDIT!
     * You should notice how the existence of an {@link Iterator} for {@link MinHeap}
     * allows us to access the elements of the argument reference. This should give you ideas
     * for {@link #LinkedMinHeap(MinHeap)}.
     * @return {@code true} If the parameter {@code Object} and the current MinHeap
     * are identical Objects.
     *
     * @see Object#equals(Object)
     * @see #LinkedMinHeap(MinHeap)
     */
	/**
	 * Standard equals() method.
	 *
	 * @return {@code true} If the parameter Object and the current MinHeap
	 * are identical Objects.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while (itThis.hasNext())
			if (!itThis.next().equals(itOther.next()))
				return false;
		return !itOther.hasNext();
	}

	@Override
	public boolean isEmpty() {
		return (this.root == null);
	}

	@Override
	public int size() {
		return this.size;
	}


	@Override
	public void insert(T element) {
		modify_counter++;
		// added node
		MinHeapNode added = new MinHeapNode();
		added.data = element;

		if (this.root == null){ // empty heap
			added.index = 0; // update index
			this.root = added;
			this.size++;
		}else{
			ArrayList<MinHeapNode> heapArr = new ArrayList<>();
			covertToArray(this.root, heapArr);
			// sort based on index
			for (int i = heapArr.size()-1; i >= 1;i--){
				for(int j = 1;j<i;j++){
					if (heapArr.get(j).index < heapArr.get(j-1).index){
						Collections.swap(heapArr, i, j);
					}
				}
			}
			heapArr.add(added);
			added.index = heapArr.size() - 1; // update index
			int idx_ptr = heapArr.size() - 1;
			while(idx_ptr >= 0){
				if (((T)heapArr.get(idx_ptr).data).compareTo(heapArr.get((idx_ptr-1)/2).data) < 0){
					Collections.swap(heapArr, idx_ptr, (idx_ptr-1)/2);
					// swap index for both element
					int temp = heapArr.get(idx_ptr).index; 
					heapArr.get(idx_ptr).index = heapArr.get((idx_ptr-1)/2).index;
					heapArr.get((idx_ptr-1)/2).index = temp;
					idx_ptr = (idx_ptr-1)/2;
				}else{
					break;
				}
			}
			this.size++;
			this.root = covertToHeap(this.root, heapArr, 0);

		}	
		
		
	}
	

	@Override
	public T getMin() throws EmptyHeapException {		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (isEmpty()){
			throw new EmptyHeapException("Empty Heap for getMin");
		}else{
			return this.root.data; 
		}
		
	}

	@Override
	public T deleteMin() throws EmptyHeapException {    // DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (this.size == 0){
			throw new EmptyHeapException("Empty Linked Heap");
		}else{
			modify_counter++;
			
			T ret_val = this.root.data;
			if (this.size == 1){
				this.root = null;
				this.size--;
				return ret_val;
			}else{
				ArrayList<MinHeapNode> heapArr = new ArrayList<>();
				covertToArray(this.root, heapArr);
				// sort based on index
				for (int i = heapArr.size()-1; i >= 1;i--){
					for(int j = 1;j<i;j++){
						if (heapArr.get(j).index < heapArr.get(j-1).index){
							Collections.swap(heapArr, i, j);
						}
					}
				}
				Collections.swap(heapArr, 0, heapArr.size()-1); // swap the first and the last one
				heapArr.get(0).index = 0; // reset the root index
				heapArr.remove(heapArr.size()-1); // remove the last one
				
				int idx_ptr = 0;
				while(idx_ptr*2+1 < heapArr.size()){
					int childIndex = siftDownOptionIdx(heapArr, idx_ptr);
					if ((heapArr.get(idx_ptr).data.compareTo(heapArr.get(childIndex).data)) > 0){
						Collections.swap(heapArr, idx_ptr, childIndex);
						// swap index for both elements
						int temp = heapArr.get(idx_ptr).index;
						heapArr.get(idx_ptr).index = heapArr.get(childIndex).index;
						heapArr.get(childIndex).index = temp;
						idx_ptr = childIndex;
					}else{
						break;
					}
				}
				this.size--;
				this.root = covertToHeap(this.root, heapArr, 0);
				return ret_val;
			}
			
			
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedHeapIterator(this.root);
	}

	private class LinkedHeapIterator implements Iterator<T>{
		private MinHeapNode root = null;
		private int size = 0;
		private int curr_mod = modify_counter;
		private int getSize(MinHeapNode currNode){
			if (currNode == null){
				return 0;
			}else{
				return 1 + getSize(currNode.lChild) + getSize(currNode.rChild);
			}
		}

		protected MinHeapNode copyNode(MinHeapNode currNode, MinHeapNode corresNode, int index){
			if (corresNode == null){
				return null;
			}else{
				currNode = new MinHeapNode();
				currNode.data = corresNode.data;
				currNode.index = index;
				currNode.lChild = copyNode(currNode.lChild, corresNode.lChild, 2*index+1);
				currNode.rChild = copyNode(currNode.rChild, corresNode.rChild, 2*index+2);
				return currNode;
			}
		}

		public LinkedHeapIterator(MinHeapNode otherRoot){
			this.size = this.getSize(otherRoot);
			this.root = copyNode(this.root, otherRoot, 0);
			
		}
		
		@Override
		public T next() throws ConcurrentModificationException{
			if (curr_mod != modify_counter) {
				throw new ConcurrentModificationException();
			}
			T ret_val = this.root.data;
			if (this.size == 1){
				this.root = null;
				this.size--;
				return ret_val;
			}else{
				ArrayList<MinHeapNode> heapArr = new ArrayList<>();
				covertToArray(this.root, heapArr);
				// sort based on index
				for (int i = heapArr.size()-1; i >= 1;i--){
					for(int j = 1;j<i;j++){
						if (heapArr.get(j).index < heapArr.get(j-1).index){
							Collections.swap(heapArr, i, j);
						}
					}
				}
				Collections.swap(heapArr, 0, heapArr.size()-1); // swap the first and the last one
				heapArr.get(0).index = 0; // reset the root index
				heapArr.remove(heapArr.size()-1); // remove the last one
				int idx_ptr = 0;
				while(idx_ptr*2+1 < heapArr.size()){
					int childIndex = siftDownOptionIdx(heapArr, idx_ptr);
					if ((heapArr.get(idx_ptr).data.compareTo(heapArr.get(childIndex).data)) > 0){
						Collections.swap(heapArr, idx_ptr, childIndex);
						// swap index for both elements
						int temp = heapArr.get(idx_ptr).index;
						heapArr.get(idx_ptr).index = heapArr.get(childIndex).index;
						heapArr.get(childIndex).index = temp;
						idx_ptr = childIndex;
					}else{
						break;
					}
				}
				this.size--;
				this.root = covertToHeap(this.root, heapArr, 0);
				return ret_val;
			}
		}

		@Override
		public boolean hasNext(){
			return (this.size > 0);
		}
	}

}
