
public class Block {

	private String tag;
	private int dirtyBit;
	private int validBit;

	//For Part II
	private int useCounter;

	public Block() {
		this.dirtyBit = 0;
		this.validBit = 0;
	}

	public void incrementCounter(){
		if(useCounter!=2)
			useCounter++;
	}

	public void decrementCounter(){
		if(useCounter!=0)
			useCounter--;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getDirtyBit() {
		return dirtyBit;
	}
	public void setDirtyBit(int dirtyBit) {
		this.dirtyBit = dirtyBit;
	}
	public int getValidBit() {
		return validBit;
	}
	public void setValidBit(int validBit) {
		this.validBit = validBit;
	}

	public int getHitCounter() {
		return useCounter;
	}

	public void setHitCounter(int counter) {
		this.useCounter = counter;
	}
}
