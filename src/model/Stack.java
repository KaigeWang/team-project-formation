package model;

import java.util.LinkedList;
import java.util.List;

public class Stack<T> {
	private List<T> dataCollection;

	public Stack() {
		dataCollection = new LinkedList<T>();
	}

	public void push(T item) {
		dataCollection.add(dataCollection.size(), item);
	}

	public T pop() {
		if (dataCollection.size() > 0)
			return dataCollection.remove(dataCollection.size() - 1);
		else
			return null;
	}

	public int size() {
		return dataCollection.size();
	}

	public void clear() {
		dataCollection.clear();
	}

	public List<T> getHistory() {
		List<T> dataCollectionreverse = new LinkedList<>();
		for (int i = 0; i < dataCollection.size(); i++) {
			dataCollectionreverse.add(dataCollection.get(dataCollection.size() - i - 1));

		}

		return dataCollectionreverse;

	}

}
