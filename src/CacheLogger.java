import java.io.IOException;
import java.io.PrintWriter;


public class CacheLogger {

	private static PrintWriter bw;

	public CacheLogger(String logs) throws IOException {
		bw = new PrintWriter(logs);
	}

	public void closeWriter() throws IOException{
		bw.close();
	}

	public void writeHit(int setNumber, Address adr) throws IOException {

		bw.println("Data in the set " + setNumber + " is modified.");
		bw.println("Tag: " + Integer.parseInt(adr.getTag(),2) + "    Index: " + adr.getIndex() +   "    Block Offset: "  +  Integer.parseInt(adr.getBlockOffset(), 2) + "       " + "(Write) hit");	
		bw.println("");
	}

	public void readHit(int setNumber, Address adr) throws IOException {

		bw.println("Data in the set " + setNumber + " is accessed.");
		bw.println("Tag: " + Integer.parseInt(adr.getTag(),2) + "    Index: " + adr.getIndex() +   "    Block Offset: "  +  Integer.parseInt(adr.getBlockOffset(), 2) + "       " + "(Read) hit");		
		bw.println("");
	}

	public void writeMiss(int setNumber, Address adr, int oldDirtyBit) throws IOException {

		bw.println("Data in the set " + setNumber + " is copied to the cache from memory.");
		bw.println("Tag: " + Integer.parseInt(adr.getTag(),2) + "    Index: " + adr.getIndex() +   "    Block Offset: "  +  Integer.parseInt(adr.getBlockOffset(), 2) + "       " + "(Write) miss");		
		if(oldDirtyBit == 1)
			bw.println("Cache performed a write back. Older block values are written to the memory!");
		bw.println("");
	}

	public void readMiss(int setNumber, Address adr, int oldDirtyBit) throws IOException {

		bw.println("Data in the set " + setNumber + " is copied to the cache from memory.");
		bw.println("Tag: " + Integer.parseInt(adr.getTag(),2) + "    Index: " + adr.getIndex() +   "    Block Offset: "  +  Integer.parseInt(adr.getBlockOffset(), 2) + "       " + "(Read) miss");		
		if(oldDirtyBit == 1)
			bw.println("Cache performed a write back. Older block values are written to the memory!");
		bw.println("");
	}

	public void writeRates(int hitCounter, int totalCounter) {
		int missCount = totalCounter - hitCounter;
		double hitRate = (double)hitCounter/(double)totalCounter;
		double missRate = 1 - hitRate;
		System.out.println(hitCounter);
		System.out.println(totalCounter);
		bw.println("--Results--");
		bw.println();
		bw.println("Hit count: " + hitCounter);
		bw.println("Miss count: " + missCount);
		bw.println("Total count: " + totalCounter);
		bw.println("");
		bw.println("Hit rate is: " + hitRate);
		bw.println("Miss rate is: " + missRate);
	}


}
