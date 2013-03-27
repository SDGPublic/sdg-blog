package example.shuffle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShuffleBag<T> {

	private List<T> items;
	private Random random;
	private int currentLocation = 0;
	
	public ShuffleBag() {
		items = new ArrayList<T>();
		random = new Random(System.currentTimeMillis());
	}
	
	public void add(T item) {
		items.add(item);
	}
	
	public T next() {
		// If we've reached the start of the list, then reset our current location to the end.
		if(currentLocation == 0) {
			currentLocation = items.size();
		}

		// Randomly choose an index between 0 and our current location in the list
		int index = random.nextInt(currentLocation);
		
		// Fetch our randomly chosen item
		T chosenItem = items.get(index);
		
		// Swap the chosen item with the item at our current location
		swapItems(index, currentLocation-1);
		
		// Decrement our current location 
		currentLocation--;
		
		return chosenItem;
	}
	
	protected void swapItems(int index1, int index2) {
		T item1 = items.get(index1);
		T item2 = items.get(index2);
		
		items.set(index1, item2);
		items.set(index2, item1);
	}
	
	protected List<T> getItems() {
		return items;
	}
}
