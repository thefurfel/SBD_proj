package pg.ppabis.bazy1;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

/*
 * 2017 (c) Piotr Pabis 149528 Grupa 6B
 * */

public class Rekord implements Comparable<Rekord> {
	byte[] values;
	byte[] sortedValues;
	
	public Rekord(String input) {
		values = input.getBytes().clone();
		sortedValues = values.clone();
		Arrays.sort(sortedValues);
	}

	public boolean isZero() {
		for(int i=0; i<values.length; ++i) if(values[i]!='0') return false;
		return true;
	}

	public int length() {return values.length;}
	
	public static final Random RandomGenerator = new Random();
	public static final byte MIN_CHAR = '0';
	public static final byte MAX_CHAR = '9';
	public static final byte MAX_LENGTH = 30;
	public static final byte MIN_LENGTH = 1;
	public static Rekord losowy() {
		int l = MIN_LENGTH + RandomGenerator.nextInt(MAX_LENGTH-MIN_LENGTH);
		String str = "";
		for(int i=0; i<l; ++i)
			str+=(char)(MIN_CHAR + RandomGenerator.nextInt(MAX_CHAR-MIN_CHAR+1));
		return new Rekord(str);
	}

	@Override
	public int compareTo(Rekord o) {
		if(this.isZero() && o.isZero()) return 0;
		if(this.length()==o.length()) {
			for(int i=this.length()-1; i >= 0 ;--i) {
				if(this.sortedValues[i]<o.sortedValues[i]) return -1;
				else if(this.sortedValues[i]>o.sortedValues[i]) return 1;
			}
		} else if(this.length()<o.length()) {
			if(!this.isZero() && o.isZero()) return 1;
			else return -1;
		} else if(this.length()>o.length()) {
			if(this.isZero() && !o.isZero()) return -1;
			else return 1;
		}
		return 0;
	}
	
	public void wpiszDoPliku(FileWriter fw) throws IOException {
		fw.write(new String(values));
	}
	
	@Override
	public String toString() {
		return new String(values);
	}
}
