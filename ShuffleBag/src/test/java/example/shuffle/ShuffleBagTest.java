package example.shuffle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ShuffleBagTest {
	
	@Test
	public void shuffleBagUserExample() {
		ShuffleBag<User> bag = new ShuffleBag<User>();
		
		// Readers - 60%
		bag.add(new User(2, 0, 0));
		bag.add(new User(2, 0, 0));
		bag.add(new User(3, 0, 0));
		bag.add(new User(3, 0, 0));
		bag.add(new User(4, 0, 0));
		bag.add(new User(5, 0, 0));
		
		// Askers - 30%
		bag.add(new User(2, 1, 0));
		bag.add(new User(3, 1, 0));
		bag.add(new User(4, 1, 0));
		
		// Answerers - 10%
		bag.add(new User(5, 0, 1));
		
		// Simulate 1000 users
		int totalUserCount = 1000;
		
		double readers = 0;
		double askers = 0;
		double answerers = 0;
		for(int i = 0; i < totalUserCount; i++) {
			User user = bag.next();
			if(user.getAnswers() > 0) {
				answerers++;
			} else if(user.getAsks() > 0) {
				askers++;
			} else {
				readers++;
			}
		}
		
		double readerPercent = readers/totalUserCount;
		double askerPercent = askers/totalUserCount;
		double answerPercent = answerers/totalUserCount;
		
		// 60% readers
		Assert.assertEquals(.6, readerPercent, .01);
		
		// 30% askers
		Assert.assertEquals(.3, askerPercent, .01);
		
		// 10% answerers
		Assert.assertEquals(.1, answerPercent, .01);
	}

	@Test
	public void testShuffleBagOneEach() {
		ShuffleBag<Integer> bag = new ShuffleBag<Integer>();
		bag.add(1);
		bag.add(2);
		bag.add(3);
		
		List<Integer> fetchedItems = new ArrayList<Integer>();
		fetchedItems.add(bag.next());
		fetchedItems.add(bag.next());
		fetchedItems.add(bag.next());
		
		// Although the order will change with each run, the
		// values will always be the same
		Assert.assertTrue("1", fetchedItems.contains(1));
		Assert.assertTrue("2", fetchedItems.contains(2));
		Assert.assertTrue("3", fetchedItems.contains(3));
	}
	
	@Test
	public void testShuffleBagMultiples() {
		// Add items to the bag in a ratio of 1 to 2 to 3.
		ShuffleBag<Integer> bag = new ShuffleBag<Integer>();
		bag.add(1);
		
		bag.add(2);
		bag.add(2);
		
		bag.add(3);
		bag.add(3);
		bag.add(3);
		
		// Fetch the next item from the bag 36 total times
		List<Integer> fetchedItems = new ArrayList<Integer>();
		for(int i = 0; i < 36; i++) {
			fetchedItems.add(bag.next());	
		}
		
		// Although the order will differ with each run,
		// the ratio of items will be 1 to 2 to 3
		Assert.assertEquals(new Integer(6), count(1, fetchedItems));
		Assert.assertEquals(new Integer(12), count(2, fetchedItems));
		Assert.assertEquals(new Integer(18), count(3, fetchedItems));
		
	}
	
	@Test
	public void testSwapItems() {
		ShuffleBag<Integer> bag = new ShuffleBag<Integer>();
		bag.add(1);
		bag.add(2);
		
		// Assert that the list is as we expect
		List<Integer> items = bag.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(new Integer(1), items.get(0));
		Assert.assertEquals(new Integer(2), items.get(1));
		
		// Swap
		bag.swapItems(0, 1);
		
		// Assert that the items have been correctly swapped.
		items = bag.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(new Integer(2), items.get(0));
		Assert.assertEquals(new Integer(1), items.get(1));
	}	
	
	// return the number of instances of an item in the list of items
	private Integer count(Integer item, List<Integer> items) {
		int count = 0;
		
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).equals(item)) {
				count++;
			}
		}
		
		return count;
	}
}
