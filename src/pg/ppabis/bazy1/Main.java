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
		if(args.length<2) {
			System.out.println("Usage: "+args[0]+" <COMMAND> [OPTIONS]");
		} else {
			if(args[1].equalsIgnoreCase("create")) {
				if(args.length>3){
					File f = new File(args[2]);
					int number = Integer.parseInt(args[3]);
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
			} else if(args[1].equalsIgnoreCase("view")) {
				if(args.length>2) {
					try {
						DataInputStream dis = new DataInputStream(new FileInputStream(args[2]));
						while(dis.available()>=12) {
							float a = dis.readFloat();
							float b = dis.readFloat();
							float h = dis.readFloat();
							System.out.println(new Rekord(a, b, h));
						}
					} catch(IOException e) {System.err.println(e.getMessage());}
				} else {System.err.println("use view file.bin");}
			}
		}
	}

}
