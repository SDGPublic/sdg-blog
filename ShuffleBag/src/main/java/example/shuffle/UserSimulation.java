package example.shuffle;

public class UserSimulation {
	
	private ShuffleBag<User> bag = new ShuffleBag<User>(); 
	
	private double readers = 0;
	private double askers = 0;
	private double answerers = 0;
	
	public void setup() {
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
	}

	public void execute(int userCount) {
		for(int i = 0; i < userCount; i++) {
			User user = bag.next();
			if(user.getAnswers() > 0) {
				answerers++;
			} else if(user.getAsks() > 0) {
				askers++;
			} else {
				readers++;
			}
		}
		
		System.out.println("Test complete.");
		System.out.println("Reader count: " + readers + " - " + readers/userCount*100);
		System.out.println("Asker count: " + askers + " - " + askers/userCount*100);
		System.out.println("Answerer count: " + answerers + " - " + answerers/userCount*100);
	}
	
	
	public static void main(String[] args) {
		UserSimulation simulation = new UserSimulation();
		simulation.setup();
		simulation.execute(7508);
	}
}
