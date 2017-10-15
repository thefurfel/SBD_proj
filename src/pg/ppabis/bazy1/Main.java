package pg.ppabis.bazy1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
 * 2017 (c) Piotr Pabis 149528 Grupa 6B
 * */

public class Main {

	public static void main(String[] args) {
		if(args.length<1) {
			System.out.println("Usage: Main <COMMAND> [OPTIONS]");
		} else {
			if(args[0].equalsIgnoreCase("create")) {
				if(args.length>2){
					File f = new File(args[1]);
					int number = Integer.parseInt(args[2]);
					try {
						DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
						for(int i=0;i<number;++i) {
							Rekord.losowy().wpiszDoPliku(dos);
						}
						dos.close();
					} catch(IOException e) {
						System.err.println(e.getMessage());
					}
				} else {
					System.err.println("use create file.bin number");
				}
			} else if(args[0].equalsIgnoreCase("view")) {
				if(args.length>1) {
					try {
						DataInputStream dis = new DataInputStream(new FileInputStream(args[1]));
						while(dis.available()>=12) {
							float a = dis.readFloat();
							float b = dis.readFloat();
							float h = dis.readFloat();
							System.out.println(new Rekord(a, b, h));
						}
						dis.close();
					} catch(IOException e) {System.err.println(e.getMessage());}
				} else {System.err.println("use view file.bin");}
			}
		}
	}

}
