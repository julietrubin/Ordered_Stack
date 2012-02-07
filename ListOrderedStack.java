import java.util.*;

public class ListOrderedStack<T extends Comparable<T>> implements OrderedStack <T>{
	private Node<T> smallest;
	private Node<T> largest;
	private Node<T> top;
	private int modificationCount;

	private boolean compare(Node<T> n, Node<T> n1){
		return n.getData().compareTo(n1.getData()) <= 0 ? true : false;
	}

	public void push(T elem) {
		modificationCount++;
		Node<T> n = new Node<T>(elem);
		//empty stack
		if(top == null){
			top = n;
			smallest = n;
			largest = n;
			return;
		}
		//set elem in front of stack
		n.setLower(top);
		top.setHigher(n);
		top = n;

		//elem is the smallest value
		if (compare(n, smallest)) {
			n.setLarger(smallest);
			smallest.setSmaller(n);
			smallest = n;
		}
		//elem is the largest value
		else if (compare(largest, n)){
			n.setSmaller(largest);
			largest.setLarger(n);
			largest = n;
		}
		//elem is middle of list
		else{
			Node<T> walker = smallest;
			while(compare(walker, n))
				walker = walker.getLarger();
			n.setLarger(walker);
			n.setSmaller(walker.getSmaller());
			walker.getSmaller().setLarger(n);
			walker.setSmaller(n);
		}
	}

	private  T remove(Node<T> n){
		modificationCount++;
		//zero elements
		if(top == null)
			throw new NoSuchElementException();

		T elem = n.getData();
		//one element
		if(smallest == largest) {
			top = null; 
			smallest = null; 
			largest = null;
			n = null;
			return elem;
		}

		if(n == smallest){
			smallest = smallest.getLarger();
			smallest.setSmaller(null);
		}     
		else if(n == largest){
			largest = largest.getSmaller();
			largest.setLarger(null);
		}    
		else {
			n.getSmaller().setLarger(n.getLarger());
			n.getLarger().setSmaller(n.getSmaller());
		}

		if(n == top){
			top = top.getLower();
			top.setHigher(null);
		}
		else if (n.getLower() == null){
			n.getHigher().setLower(null);
		}
		else {
			n.getHigher().setLower(n.getLower());
			n.getLower().setHigher(n.getHigher());
		}
		n = null;
		return elem;
	}

	public T pop() {
		return remove(top);
	}

	public T removeLargest() {
		return remove(largest);
	}

	public T removeSmallest() {
		return remove(smallest);
	}
	public String toString(){
		Iterator<T> it = stackIterator();
		if(!it.hasNext())
			return "";

		String returnString = "[";
		returnString += it.next();
		while(it.hasNext())
			returnString +=  ", " +it.next();

		returnString += "]";
		return returnString;
	}

	public Iterator<T> stackIterator() {
		return new StackIterator();
	}

	public Iterator<T> orderedIterator() {
		return new OrderedIterator();
	}

	private class StackIterator implements Iterator<T> {
		private Node<T> walker;
		private int modificationCount;

		public StackIterator() {
			modificationCount = ListOrderedStack.this.modificationCount;
			walker = new Node<T>(null);
			walker.setLower(top);
		}

		public boolean hasNext() {
			if (modificationCount != ListOrderedStack.this.modificationCount)
				throw new ConcurrentModificationException();
			return walker.getLower() != null ? true : false;
		}

		public T next() {
			if (modificationCount != ListOrderedStack.this.modificationCount)
				throw new ConcurrentModificationException();
			if(!hasNext())
				throw new NoSuchElementException();
			walker = walker.getLower();
			return walker.getData();
		}

		public void remove() {
			if (modificationCount != ListOrderedStack.this.modificationCount)
				throw new ConcurrentModificationException();
			modificationCount++;
			if (walker == null)
				throw new IllegalStateException();
			ListOrderedStack.this.remove(walker);
		}
	}


	private class OrderedIterator implements Iterator<T> {
		private Node<T> walker;
		private int modificationCount;

		public OrderedIterator() {
			modificationCount = ListOrderedStack.this.modificationCount;
			walker = new Node<T>(null);
			walker.setLarger(smallest);
		}

		public boolean hasNext() {
			if (modificationCount != ListOrderedStack.this.modificationCount)
				throw new ConcurrentModificationException();
			return walker.getLarger() != null ? true : false;
		}

		public T next() {
			if (modificationCount != ListOrderedStack.this.modificationCount)
				throw new ConcurrentModificationException();
			if(!hasNext())
				throw new NoSuchElementException();
			walker = walker.getLarger();
			return walker.getData();
		}

		public void remove() {
			if (modificationCount != ListOrderedStack.this.modificationCount)
				throw new ConcurrentModificationException();
			modificationCount++;
			if (walker == null)
				throw new IllegalStateException();
			ListOrderedStack.this.remove(walker);
		}        
	}



	public static void main(String args[]) {
		OrderedStack<String> os = new ListOrderedStack<String>(); 
		/*
		os.push("A"); 
		os.push("B"); 
		os.push("C"); 
		Iterator<String> it = os.stackIterator(); 
		System.out.println(it.next()); // OK, returns C 
		it.remove(); // OK 
		System.out.println(it.next()); // OK, returns B 
		it.remove(); // OK 
		System.out.println(it.next()); // OK, returns A 
		//it.next(); // BAD: Should throw a NoSuchElementException 
		 */
		/*
		os.push("A"); 
		os.push("B"); 
		os.push("C"); 
		Iterator<String> it1 = os.stackIterator(); 
		Iterator<String> it2 = os.stackIterator(); 
		Iterator<String> it3 = os.orderedIterator(); 
		it1.next(); // OK, returns C 
		it2.next(); // OK, returns C 
		it3.next(); // OK, returns A 
		it2.remove(); // OK 
		it3.next(); // BAD: Should throw a ConcurrentModificationException 
		 */
		/*
		os.push("A"); 
		os.push("B"); 
		os.push("C"); 
		Iterator<String> it1 = os.stackIterator(); 
		os.push("D"); 
		it1.next(); // BAD: Should throw a concurrentModificationException 
		 */
		
		os.push("A"); 
		os.push("C"); 
		os.push("D"); 
		os.push("E"); 
		os.push("B"); 
		System.out.println(os); 
		Iterator<String> it = os.stackIterator(); 
		it.next(); 
		it.next(); 
		it.remove(); 
		it = os.stackIterator(); 
		while (it.hasNext()) 
		{ 
		System.out.print(it.next()+ " "); 
		} 
		System.out.println(); 
		it = os.orderedIterator(); 
		while (it.hasNext()) 
		{ 
		System.out.print(it.next()+ " "); 
		} 

		 

	}
}
