import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Cache {

	private int associativity;
	private int cacheSize;
	private int blockSize;
	private int runningMode;
	private String  addressStreamFile;
	private String detailedLog;

	//For Part II
	private int historyResetFrequency;
	private int processCounter = 0;
	//

	private int tagBit;
	private int indexBit;
	private int blockOffsetBit;

	private int blockAmount;

	private int totalBitSize = 32;

	private ArrayList<Block[]> setAssociativeArray;

	private CacheLogger cacheLogger;

	public Cache(int associativity, int cacheSize, int blockSize,  String addressStreamFile, 
			String detailedLog, int historyResetFrequency, int runningMode) {

		this.associativity = associativity;
		this.cacheSize = cacheSize;
		this.blockSize = blockSize;
		this.addressStreamFile = addressStreamFile;
		this.detailedLog = detailedLog;
		this.historyResetFrequency = historyResetFrequency;
		this.runningMode = runningMode;

		this.blockAmount = (cacheSize/associativity)/blockSize;

		try {
			cacheLogger = new CacheLogger(detailedLog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setAssociativeArray = new ArrayList<Block[]>(this.associativity);

		for(int n = 0; n < this.associativity; n++){
			Block[] blocks = new Block[blockAmount];
			for(int i =0; i<blocks.length;i++)
				blocks[i] = new Block();
			setAssociativeArray.add(blocks);
		}
	}

	public void calculateAddressFields(){

		int indexPower = (int) (Math.log(blockAmount)/Math.log(2));
		setIndexBit(indexPower);

		int offsetPower = (int) (Math.log(blockSize)/Math.log(2));
		setBlockOffsetBit(offsetPower);

		setTagBit(totalBitSize - (indexBit + blockOffsetBit));
	}

	public void addAddress(Address address,boolean isWrite) {

		int setNumber = searchEmptyBlock(address.getIndex());
		Block block = null;

		if(runningMode == 0) { 
			//For Part I
			if(setNumber == -1)
				setNumber = (new Random()).nextInt(associativity);

			Block[] blocks = setAssociativeArray.get(setNumber);
			block = blocks[address.getIndex()];
		} else {
			//For Part II

			if(setNumber == -1)
				block = getBlockWithMinimumCounter(address);
			else{
				Block[] blocks = setAssociativeArray.get(setNumber);
				block = blocks[address.getIndex()];
			}
		}

		int tempOldDirtyBit = block.getDirtyBit();
		
		if(isWrite)
			block.setDirtyBit(1);
		else
			block.setDirtyBit(0);
			
		block.setTag(address.getTag());
		block.setValidBit(1);
		block.setHitCounter(0);

		try{
			if(isWrite)
				cacheLogger.writeMiss(setNumber,address,tempOldDirtyBit);
			else 
				cacheLogger.readMiss(setNumber,address,tempOldDirtyBit);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int searchEmptyBlock(int index) {

		for(int n = 0; n < associativity; n++){
			Block[] blocks = setAssociativeArray.get(n);
			if(blocks[index].getValidBit() == 0)
				return n;
		}
		return -1;
	}

	public void modifyBlock(Address address, int setNumber, boolean isWrite) {

		Block[] block = setAssociativeArray.get(setNumber);

		if(isWrite)
			block[address.getIndex()].setDirtyBit(1);

		block[address.getIndex()].setValidBit(1);
		block[address.getIndex()].incrementCounter();

		try {
			if(isWrite)
				cacheLogger.writeHit(setNumber,address);
			else
				cacheLogger.readHit(setNumber,address);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Block getBlockWithMinimumCounter(Address address){

		ArrayList<Block> tempBlockArray = new ArrayList<Block>();

		for(int n = 0; n < associativity; n++){
			Block[] blocks = setAssociativeArray.get(n);
			if(blocks[address.getIndex()].getHitCounter() == 0)
				tempBlockArray.add(blocks[address.getIndex()]);
		}

		if(tempBlockArray.isEmpty()){
			for(int n = 0; n < associativity; n++){
				Block[] blocks = setAssociativeArray.get(n);
				if(blocks[address.getIndex()].getHitCounter() == 1)
					tempBlockArray.add(blocks[address.getIndex()]);
			}
		}

		if(tempBlockArray.isEmpty()){
			for(int n = 0; n < associativity; n++){
				Block[] blocks = setAssociativeArray.get(n);
				if(blocks[address.getIndex()].getHitCounter() == 2)
					tempBlockArray.add(blocks[address.getIndex()]);
			}
		}

		return tempBlockArray.get((new Random()).nextInt(tempBlockArray.size()));
	}

	//Get & Set

	public int getAssociativity() {
		return associativity;
	}

	public int getTagBit() {
		return tagBit;
	}
	public void setTagBit(int tagBit) {
		this.tagBit = tagBit;
	}
	public int getIndexBit() {
		return indexBit;
	}
	public void setIndexBit(int indexBit) {
		this.indexBit = indexBit;
	}
	public int getBlockOffsetBit() {
		return blockOffsetBit;
	}
	public void setBlockOffsetBit(int blockOffsetBit) {
		this.blockOffsetBit = blockOffsetBit;
	}
	public void setAssociativity(int associativity) {
		this.associativity = associativity;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int chacheSize) {
		this.cacheSize = chacheSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getHistoryResetFrequency() {
		return historyResetFrequency;
	}

	public void setHistoryResetFrequency(int historyResetFrequency) {
		this.historyResetFrequency = historyResetFrequency;
	}

	public int getBlockAmount() {
		return blockAmount;
	}

	public void setBlockAmount(int blockAmount) {
		this.blockAmount = blockAmount;
	}

	public ArrayList<Block[]> getSetAssociativeArray() {
		return setAssociativeArray;
	}

	public void setSetAssociativeArray(ArrayList<Block[]> setAssociativeArray) {
		this.setAssociativeArray = setAssociativeArray;
	}

	public CacheLogger getCacheLogger() {
		return cacheLogger;
	}

	public void setCacheLogger(CacheLogger cacheLogger) {
		this.cacheLogger = cacheLogger;
	}

	public void incrementProcessCounter() {
		// TODO Auto-generated method stub
		setProcessCounter(getProcessCounter() + 1);
	}

	public int getProcessCounter() {
		return processCounter;
	}

	public void setProcessCounter(int processCounter) {
		this.processCounter = processCounter;
	}

	public void resetBlockCounters() {
		// TODO Auto-generated method stub
		for(Block[] blocks : setAssociativeArray){
			for(Block block : blocks){
				block.decrementCounter();
			}
		}
	}

	public int getRunningMode() {
		return runningMode;
	}

	public void setRunningMode(int runningMode) {
		this.runningMode = runningMode;
	}

	public String getAddressStreamFile() {
		return addressStreamFile;
	}

	public void setAddressStreamFile(String addressStreamFile) {
		this.addressStreamFile = addressStreamFile;
	}

	public String getDetailedLog() {
		return detailedLog;
	}

	public void setDetailedLog(String detailedLog) {
		this.detailedLog = detailedLog;
	}
}
