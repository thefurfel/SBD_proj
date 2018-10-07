package pg.ppabis.bazy1;

import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.nio.*;

public class Tasma {

	public static final int RECORDS_PER_PAGE=5;
	private Rekord[] strona = new Rekord[RECORDS_PER_PAGE];
	private int wskStrona = 0;

	private File file;
	private BufferedReader fileReader;
	private FileWriter fileWriter;
	
	public Tasma(File f) {
		file = f;
		
	}
	
	public Tasma openAsInput() {
		flush();
		if(fileWriter!=null) try {
			fileWriter.close();
		} catch(Exception e) {e.printStackTrace();}
		fileWriter = null;
		try {
			fileReader = new BufferedReader(new FileReader(file));
			readPage();
		} catch(Exception e) {e.printStackTrace();}
		return this;
	}
	
	public Rekord readNext() {
		if(wskStrona>= RECORDS_PER_PAGE) readPage();
		return strona[wskStrona++];
	}
	
	public void writeNext(Rekord r) {
		if(wskStrona>=RECORDS_PER_PAGE) flush();
		strona[wskStrona++]=r;
	}
	
	private void readPage() {
		try	{ 
			strona = new Rekord[RECORDS_PER_PAGE];
			wskStrona = 0;
			String s = null;
			int i=0;
			while( i<RECORDS_PER_PAGE && (s = fileReader.readLine()) != null) {
				strona[i] = new Rekord(s);
				++i;
			}
			System.out.println(">["+file.getName()+"]>Wczytano kolejna strone ("+i+" rekordow)");
		} catch(Exception e){e.printStackTrace();}
	}
	
	public Tasma openAsOutput() {
		if(fileReader!=null) try {
			fileReader.close();
		} catch(Exception e) {e.printStackTrace();}
		fileReader = null;
		try {
			fileWriter = new FileWriter(file);
			strona = new Rekord[RECORDS_PER_PAGE];
		} catch(Exception e) {e.printStackTrace();
		}
		return this;
	}
	
	public void flush() {
		if(fileWriter!=null) try {
			for(int i=0;i<RECORDS_PER_PAGE;++i)
				if(strona[i]!=null)
					fileWriter.write(strona[i].toString()+"\r\n");
			fileWriter.flush();
			wskStrona = 0;
			strona = new Rekord[RECORDS_PER_PAGE];
			System.out.println(">["+file.getName()+"]>Flush strony");
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public void close() {
		if(fileWriter!=null) try {
			fileWriter.close();
		} catch(Exception e) {e.printStackTrace();}
		else if(fileReader!=null) try {
			fileReader.close();
		} catch(Exception e) {e.printStackTrace();}
	}
	
}
