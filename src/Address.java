
public class Address {

	private String binaryAddress;

	private String tag;
	private int index;
	private String blockOffset;
	
	public Address(String binaryAddress, int tagLength, int indexLength, int blockOffsetLength) {
		this.binaryAddress = binaryAddress;
		this.tag = binaryAddress.substring(0,tagLength);
		this.index = Integer.parseInt(binaryAddress.substring(tagLength,tagLength+indexLength),2);
		this.blockOffset = binaryAddress.substring(tagLength+indexLength);
	}

	//Get & Set

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getBlockOffset() {
		return blockOffset;
	}
	public void setBlockOffset(String blockOffset) {
		this.blockOffset = blockOffset;
	}

	public String getBinaryAddress() {
		return binaryAddress;
	}

	public void setBinaryAddress(String binaryAddress) {
		this.binaryAddress = binaryAddress;
	}
}
