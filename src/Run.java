import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Run {

	public static void main(String[] args) {

		int associativity;
		int cacheSize;
		int blockSize;
		String addressStreamFile;
		String detailedLog;
		int historyResetFrequency;
		int runningMode;

		if(args.length != 0){
			associativity = Integer.parseInt(args[0]);
			cacheSize = Integer.parseInt(args[1]);
			blockSize = Integer.parseInt(args[2]);
			addressStreamFile = args[3];
			detailedLog = args[4];
			historyResetFrequency = Integer.parseInt(args[5]);
			runningMode = Integer.parseInt(args[6]);
		} else {
			associativity = 1;
			cacheSize = 512;
			blockSize = 4;
			addressStreamFile = "addresses.txt";
			detailedLog = "CacheLogs.txt";
			historyResetFrequency = 100;
			runningMode = 0;
		}

		Cache cache = new Cache(associativity, cacheSize, blockSize, addressStreamFile, detailedLog,
				historyResetFrequency, runningMode);
		
		cache.calculateAddressFields();

		ArrayList<Address> addresses = readAddresses(cache);

		CacheSimulator simulator = new CacheSimulator(cache);

		simulator.setAddressList(addresses);

		simulator.run();
	}

	private static ArrayList<Address> readAddresses(Cache cache){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(cache.getAddressStreamFile()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Address> addresses = new ArrayList<>();
		String line;
		try {
			while((line = in.readLine()) != null)
			{
				addresses.add(new Address(fixAddressLength(Integer.toBinaryString(Integer.parseInt(line))),
						cache.getTagBit(),
						cache.getIndexBit(),
						cache.getBlockOffsetBit()));

			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return addresses;
	}

	public static String fixAddressLength(String address){
		int l = 32 - address.length();

		for(int n = 0; n < l; n++){
			address = "0" + address;
		}
		return address;
	}
}
