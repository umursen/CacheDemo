import java.io.IOException;
import java.util.ArrayList;

public class CacheSimulator {

	private ArrayList<Address> addressList;

	private Cache cache;

	public CacheSimulator(Cache cache) {
		this.cache = cache;
	}

	public void run(){

		int hitCounter = 0;
		
		for(Address address : addressList){
			
			cache.incrementProcessCounter();
			
			if(cache.getProcessCounter()%cache.getHistoryResetFrequency()==0)
				cache.resetBlockCounters();
				
			boolean isWrite = (cache.getProcessCounter()%5==0);

			int setNumber = 0;

			if(isHit(address,setNumber)){
				hitCounter++;
				cache.modifyBlock(address,setNumber,isWrite);
			}else{
				cache.addAddress(address,isWrite);
			}
		}

		cache.getCacheLogger().writeRates(hitCounter, cache.getProcessCounter());
		
		try {
			cache.getCacheLogger().closeWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private boolean isHit(Address address,int setNumber) {

		ArrayList<Block[]> sets = cache.getSetAssociativeArray();

		for(int n = 0; n < sets.size(); n++){
			Block[] block = sets.get(n);
			if(address.getTag().equals(block[address.getIndex()].getTag())){
				setNumber = n;
				return true;
			}
		}
		return false;
	}

	//Get & Set

	public ArrayList<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(ArrayList<Address> addressList) {
		this.addressList = addressList;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}