package example.shuffle;

public class User {
	
	private int reads;
	private int asks;
	private int answers;
	
	public User(int reads, int asks, int answers) {
		this.reads = reads;
		this.asks = asks;
		this.answers = answers;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}

	public int getAsks() {
		return asks;
	}

	public void setAsks(int asks) {
		this.asks = asks;
	}

	public int getAnswers() {
		return answers;
	}

	public void setAnswers(int answers) {
		this.answers = answers;
	}
	
	public String toString() {
		String s = "I read " + reads + " question(s).\n";
		s+= "I asked " + asks + " question(s)\n";
		s+= "I answered " + answers + " question(s)\n";
		
		return s;
	}

}
