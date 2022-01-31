package pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */

import pqueue.exceptions.UnimplementedMethodException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;



/**
 * <p>{@link ArrayMinHeap} is a {@link MinHeap} implemented using an internal array. Since heaps are <b>complete</b>
 * binary trees, using contiguous storage to store them is an excellent idea, since with such storage we avoid
 * wasting bytes per {@code null} pointer in a linked implementation.</p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a <b>contiguous storage</b> implementation based on a linear {@link java.util.Collection}
 * like an {@link java.util.ArrayList} or a {@link java.util.Vector} (but *not* a {@link java.util.LinkedList} because it's *not*
 * contiguous storage!). or a raw Java array. We provide an array for you to start with, but if you prefer, you can switch it to a
 * {@link java.util.Collection} as mentioned above. </p>
 *
 * @author ---- Haoran Li ----
 *
 * @see MinHeap
 * @see LinkedMinHeap
 * @see demos.GenericArrays
 */

public class ArrayMinHeap<T extends Comparable<T>> implements MinHeap<T> {

	/* *****************************************************************************************************************
	 * This array will store your data. You may replace it with a linear Collection if you wish, but
	 * consult this class' 	 * JavaDocs before you do so. We allow you this option because if you aren't
	 * careful, you can end up having ClassCastExceptions thrown at you if you work with a raw array of Objects.
	 * See, the type T that this class contains needs to be Comparable with other types T, but Objects are at the top
	 * of the class hierarchy; they can't be Comparable, Iterable, Clonable, Serializable, etc. See GenericArrays.java
	 * under the package demos* for more information.
	 * *****************************************************************************************************************/
	private Object[] data;
	
	/* *********************************************************************************** *
	 * Write any further private data elements or private methods for LinkedMinHeap here...*
	 * *************************************************************************************/
	int modify_counter = 0;
	/*
	* Private method for choosing the correct child for sift down operation , return index of the child.
	*/
	private int siftDownOptionIdx(Object[] currArr, int currIdx){
		// if right child index of current index is excessed the max array size or left child is lesser than right child, return left child index, vise versa.
		return ((currIdx*2+2 > currArr.length-1)||((T)currArr[currIdx*2+1]).compareTo((T)currArr[currIdx*2+2]) <= 0) ? currIdx*2+1 : currIdx*2+2;
	}

	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/

	/**
	 * Default constructor initializes the data structure with some default
	 * capacity you can choose.
	 */
	public ArrayMinHeap(){
		this.data = new Object[0];
	}

	/**
	 *  Second, non-default constructor which provides the element with which to initialize the heap's root.
	 *  @param rootElement the element to create the root with.
	 */
	public ArrayMinHeap(T rootElement){
		this.data = new Object[0];
		this.data[0] = rootElement;

	}

	/**
	 * Copy constructor initializes {@code this} as a carbon copy of the {@link MinHeap} parameter.
	 *
	 * @param other The MinHeap object to base construction of the current object on.
	 */
	public ArrayMinHeap(MinHeap<T> other){
		ArrayMinHeap<T> newHeap = (ArrayMinHeap<T>) other;
		Object[] copyData = newHeap.data;
		this.data = new Object[copyData.length - 1];
		for(int i=0; i<=copyData.length-1; i++){
			this.data[i] = copyData[i];
		}
	}

	/**
	 * Standard {@code equals()} method. We provide it for you: DO NOT ERASE! Consider its implementation when implementing
	 * {@link #ArrayMinHeap(MinHeap)}.
	 * @return {@code true} if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 * @see #ArrayMinHeap(MinHeap)
	 */
	@Override
	public boolean equals(Object other){
		if(other == null || !(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while(itThis.hasNext())
			if(!itThis.next().equals(itOther.next()))
				return false;
		return !itOther.hasNext();
	}


	@Override
	public void insert(T element) {
		modify_counter ++;
		Object[] updateArr = new Object[this.data.length+1]; 
		int i; // later use for index pointer
		for(i = 0;i<this.data.length;i++){
			updateArr[i] = data[i];
		}
		updateArr[i] = element;
		this.data = updateArr;
		int idx_ptr = i;
		while (idx_ptr>=0){
			// if current is lesser than its parent, then sift up.
			if (((T) this.data[idx_ptr]).compareTo((T)this.data[(idx_ptr-1)/2]) < 0){
				T temp = (T) this.data[idx_ptr];
				this.data[idx_ptr] = this.data[(idx_ptr-1)/2];
				this.data[(idx_ptr-1)/2] = temp;
			}else{ // it's in the proper location,break out the loop
				break;
			}
			idx_ptr = (idx_ptr-1)/2; // set index pointer to its parent
		}
	}

	@Override
	public T deleteMin() throws EmptyHeapException { // DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (this.data.length > 0){
			modify_counter ++;
			T ret_val = (T)this.data[0];
			if (this.data.length == 1){ // only contain 1 elemnt: retrieve element and delete.
				this.data = new Object[0];
				return ret_val;
			}else{
				Object[] updateArr = new Object[this.data.length-1];
				updateArr[0] = this.data[this.data.length-1];
				int idx_ptr = 1;
				while(idx_ptr < updateArr.length){
					updateArr[idx_ptr] = this.data[idx_ptr];
					idx_ptr++;
				}
				idx_ptr = 0; // initailly points to the root
				while ((2*idx_ptr+1) < updateArr.length){
					// find proper child index, see if the child is lesser than current node: if YES, sift down
					int child_idx = siftDownOptionIdx(updateArr, idx_ptr);
					if(((T)updateArr[child_idx]).compareTo((T)updateArr[idx_ptr]) < 0){
						T temp = (T)updateArr[idx_ptr];
						updateArr[idx_ptr] = updateArr[child_idx];
						updateArr[child_idx] = temp;
						idx_ptr = child_idx; // set index pointer points to the child_index
					}else{ // the node is in the proper location, break out the loop.
						break;
					}
				}
				this.data = updateArr;
				return ret_val;
			}	
		}else{
			throw new EmptyHeapException("Empty heap for deleting min");
		}
	}

	@Override
	public T getMin() throws EmptyHeapException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (this.data.length > 0){
			return (T)this.data[0];
		}else{
			throw new EmptyHeapException("Empty Heap for getting min.");
		}
	}

	@Override
	public int size() {
		return this.data.length;
	}

	@Override
	public boolean isEmpty() {
		return (this.data.length == 0);
	}

	/**
	 * Standard equals() method.
	 * @return {@code true} if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 */


	@Override
	public Iterator<T> iterator() {
		return new ArrayHeapIterator<T>(this.data);
	}

	private class ArrayHeapIterator<T extends Comparable<T>> implements Iterator<T>{
		private ArrayList<Object> currArr;
		int curr_mod = modify_counter;
		public ArrayHeapIterator(Object[] data){
			currArr = new ArrayList<>();
			for(int i = 0; i< data.length; i++){
				currArr.add(i, data[i]);
			}
		}
		private int siftDownOptionIdx(ArrayList<Object> currArr, int currIdx){
			// if right child index of current index is excessed the max array size or left child is lesser than right child, return left child index, vise versa.
			return ((currIdx*2+2 > currArr.size()-1)||((T)currArr.get(currIdx*2+1)).compareTo((T)currArr.get(currIdx*2+2)) <= 0) ? currIdx*2+1 : currIdx*2+2;
		}
		@Override
		public T next(){
			if (curr_mod != modify_counter){
				throw new ConcurrentModificationException();
			}else{
				T ret_val = (T)currArr.get(0);
				if (currArr.size() == 1){
					currArr = new ArrayList<>();
					return ret_val;
				}else{
					Collections.swap(currArr, 0, currArr.size()-1);
					currArr.remove(currArr.size()-1);
					int idx_ptr = 0;
					while(2*idx_ptr+1 < currArr.size()){
						int child_idx = siftDownOptionIdx(this.currArr, idx_ptr);
						if(((T)currArr.get(child_idx)).compareTo((T)currArr.get(idx_ptr)) < 0){
							// exchange child node and current node
							Collections.swap(currArr, child_idx, idx_ptr);
							idx_ptr = child_idx; // update idx_ptr
						}else{ // the node is in the proper location, break out the loop.
							break;
						}
					}
					return ret_val;
				}
			}
		}

		@Override
		public boolean hasNext(){
			if (curr_mod != modify_counter){
				throw new ConcurrentModificationException();
			}else{
				return (this.currArr.size() > 0);
			}
		}


	}  
	// private class ArrayHeapIterator<T extends Comparable<T>> implements Iterator<T>{
	// 	private Object[] currArr;
		
	// 	public ArrayHeapIterator(Object[] data){ //constructor
	// 		modify_counter = 0;
	// 		currArr = new Object[data.length];
	// 		for(int i = 0;i<data.length;i++){ 
	// 			currArr[i] = data[i];
	// 		}
	// 	}

	// 	@Override
	// 	public T next() throws ConcurrentModificationException {
	// 		if (modify_counter != 0){
	// 			throw new ConcurrentModificationException();
	// 		}else{
	// 			T ret_val = (T)this.currArr[0];
	// 			if (currArr.length == 1){
	// 				currArr = new Object[0];
	// 				return ret_val;
	// 			}else{
	// 				// create updateArray
	// 				Object[] updateArr = new Object[this.currArr.length-1];
	// 				updateArr[0] = this.currArr[this.currArr.length-1];
	// 				int idx_ptr = 1;
	// 				while(idx_ptr<currArr.length){
	// 					updateArr[idx_ptr] = currArr[idx_ptr];
	// 				}
	// 				idx_ptr = 0; // set index pointer to the root, prepare for sift down operation
	// 				while(2*idx_ptr+1 < updateArr.length){
	// 					int child_idx = siftDownOptionIdx(this.currArr, idx_ptr);
	// 					if(((T)updateArr[child_idx]).compareTo((T)updateArr[idx_ptr]) < 0){
	// 						// exchange child node and current node
	// 						T temp = (T)updateArr[idx_ptr];
	// 						updateArr[idx_ptr] = updateArr[child_idx];
	// 						updateArr[child_idx] = temp;
	// 						idx_ptr = child_idx; // update idx_ptr
	// 					}else{ // the node is in the proper location, break out the loop.
	// 						break;
	// 					}
	// 				}
	// 				this.currArr = updateArr;
	// 				return ret_val;
	// 			}
				
	// 		}
	// 	}

	// 	@Override 
	// 	public boolean hasNext(){
	// 		if (modify_counter != 0){
	// 			throw new ConcurrentModificationException();
	// 		}else{
	// 			return (this.currArr.length > 0);
	// 		}
			
	// 	}
	// }
}
