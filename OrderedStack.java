import java.util.*;

public interface OrderedStack<T extends Comparable<T>>{
	class Node<T>{
		private T data;
		private Node<T> higher;
		private Node<T> lower;
		private Node<T> smaller;
		private Node<T> larger;

		public Node(T newdata){
			setData(newdata);
		}

		public void setData(T data) {
			this.data = data;
		}

		public T getData() {
			return data;
		}

		public void setHigher(Node<T> top) {
			this.higher = top;
		}

		public Node<T> getHigher() {
			return higher;
		}

		public void setLower(Node<T> bottom) {
			this.lower = bottom;
		}

		public Node<T> getLower() {
			return lower;
		}
		public void setSmaller(Node<T> smaller) {
			this.smaller = smaller;
		}

		public Node<T> getSmaller() {
			return smaller;
		}

		public void setLarger(Node<T> larger) {
			this.larger = larger;
		}

		public Node<T> getLarger() {
			return larger;
		}
	}
	public void push(T elem);  
	public T pop();
	public T removeSmallest();
	public T removeLargest();

	public Iterator<T> stackIterator();
	public Iterator<T> orderedIterator(); 
}