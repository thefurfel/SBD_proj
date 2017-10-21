package pg.ppabis.bazy1;

import java.io.*;
import java.nio.*;

public class Tasma {

	public static final int RECORDS_PER_PAGE=5;
	
	private File file;
	private FloatBuffer floatBuffer;
	private ByteBuffer buffer;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public Tasma(File f) {
		file = f;
		
	}
	
	public void openAsInput() {
		flush();
		if(outputStream!=null) try {
			outputStream.close();
		} catch(Exception e) {}
		outputStream = null;
		try {
			inputStream = new FileInputStream(file);
			readPage();
		} catch(Exception e) {}
	}
	
	public Rekord readNext() {
		if(eof() && !buffer.hasRemaining()) return null;
		if(!buffer.hasRemaining()) readPage();
		return new Rekord(buffer.getFloat(),buffer.getFloat(),buffer.getFloat());
	}
	
	public void writeNext(Rekord r) {
		if(!buffer.hasRemaining()) flush();
		r.putIntoBuffer(buffer);
	}
	
	private void readPage() {
		try	{ 
			byte[] buf = new byte[0];
			if(inputStream.available()>=RECORDS_PER_PAGE*12) {
				buf = new byte[RECORDS_PER_PAGE*12];
				inputStream.read(buf);
			} else {
				buf = new byte[inputStream.available()];
				inputStream.read(buf);
			}
			buffer = ByteBuffer.wrap(buf);
			System.out.println(">>Wczytano kolejna strone");
		} catch(Exception e){}
	}
	
	private boolean eof() {
		try {
			if(inputStream.available()<=0) return true;
			return false;
		} catch(Exception e){}
		return true;
	}
	
	public void openAsOutput() {
		if(inputStream!=null) try {
			inputStream.close();
		} catch(Exception e) {}
		inputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			buffer = ByteBuffer.allocate(RECORDS_PER_PAGE*12);
		} catch(Exception e) {
		}
	}
	
	public void flush() {
		if(outputStream!=null) try {
			outputStream.write(buffer.array());
			buffer.clear();
			buffer = ByteBuffer.allocate(RECORDS_PER_PAGE*12);
			outputStream.flush();
			System.out.println(">>Flush strony");
		} catch(Exception e) {}
	}
	
}
