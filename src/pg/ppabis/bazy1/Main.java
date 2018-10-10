package pg.ppabis.bazy1;

import java.io.File;

/*
 * 2017 (c) Piotr Pabis 149528 Grupa 6B
 * */

public class Main {

	public static void main(String[] args) {
		if(args.length<1) {
			System.out.println("Usage: Main <COMMAND> [OPTIONS]");
			System.out.println("\nCommands:\n\tcreate <FILE> <NUM> - tworzy NUM losowych rekordow do pliku  <FILE>\n\tview <FILE> - wyswietla zawartosc <FILE>\n\t"
					+ "array <FILE> [rekord1] [rekord2] [...] - utworz z linii komend\n"
					+ "sort <FILE> - posortuj");
		} else {
			if(args[0].equalsIgnoreCase("create")) {
				create(args);
			} else if(args[0].equalsIgnoreCase("view")) {
				view(args);
			} else if(args[0].equalsIgnoreCase("array")) {
				fromarray(args);
			} else if(args[0].equalsIgnoreCase("sort")) {
				sort(args);
			}
		}
	}
	
	public static void fromarray(String[] args) {
		if(args.length>2) {
			Tasma tasma = null;
			File f = new File(args[1]);
			File f2 = new File(args[1]+".new");
			boolean isnew = false;
			if(f.exists()) {
				Tasma tasmain = new Tasma(f).openAsInput();
				System.out.println("!Uwaga! Plik istnieje, przenoszenie do nowego pliku!");
				tasma = new Tasma(f2).openAsOutput();
				Rekord r = tasmain.readNext(); int rekordy = 0;
				while(r!=null) {
					tasma.writeNext(r);
					r = tasmain.readNext();
					++rekordy;
				}
				System.out.println("Skopiowano "+rekordy+" starych rekordow.");
				tasmain.close();
				f.delete();
				isnew = true;
			} else {
				tasma = new Tasma(new File(args[1])).openAsOutput();
				isnew = false;
			}
			int i=2;
			while(i<args.length) {
				tasma.writeNext(new Rekord(args[i]));
				++i;
			}
			tasma.flush();
			tasma.close();
			if(isnew) f2.renameTo(f);
		} else {
			System.err.println("use array file.txt records...");
		}
	}
	
	public static void create(String[] args) {
		if(args.length>2){
			Tasma tasma = new Tasma(new File(args[1]));
			tasma.openAsOutput();
			int number = Integer.parseInt(args[2]);
			for(int i=0;i<number;++i) {
				tasma.writeNext(Rekord.losowy());
			}
			tasma.flush();
		} else {
			System.err.println("use create file.txt number");
		}
	}
	
	public static void view(String[] args) {
		int rekordy = 0;
		if(args.length>1) {
			Tasma tasma = new Tasma(new File(args[1]));
			tasma.openAsInput();
			Rekord r = tasma.readNext();
			while(r!=null) {
				rekordy++;
				System.out.println(r);
				r = tasma.readNext();
			}
		} else {System.err.println("use view file.txt");}
		System.out.println("Rekordow w tym pliku: "+rekordy);
	}
	
	public static void sort(String[] args) {
		if(args.length>1) {
			Tasma tasma3 = new Tasma(new File(args[1])); //Źródłowa taśma
			Tasma tasma1 = new Tasma(new File(args[1].split("\\.")[0]+".1.txt"));
			Tasma tasma2 = new Tasma(new File(args[1].split("\\.")[0]+".2.txt"));
			
			Tasma out = new Tasma(new File(args[1].split("\\.")[0]+".sorted.txt"));
			Tasma dest = tasma1;
			
			Rekord r,r2;
			boolean sorted = false;
			int fazy = 1;

			while(!sorted) {
				System.out.println("[FAZA #"+fazy+"]");
				//Ustawienie kierunku tasm
				tasma3.openAsInput();
				tasma2.openAsOutput();
				tasma1.openAsOutput();
				
				r = tasma3.readNext();
				r2 = tasma3.readNext();
				
				int zmianTasmPrzyDystrybucji = 0;
				
				//Dopoki zrodlo ma rekordy
				while(r2!=null) {
					dest.writeNext(r);
					//Zmien kierunek zapisu jezeli konczymy serie
					if(r2.compareTo(r)<0) {
						if(dest==tasma1) dest=tasma2; else dest=tasma1;
						zmianTasmPrzyDystrybucji++;
					}
					r=r2;
					r2 = tasma3.readNext();
				}
				dest.writeNext(r);
				tasma2.flush();
				tasma1.flush();
				if(tasma3!=out) {
					tasma3 = out;
				}
				
				//Ustawienie kierunkow
				tasma3.openAsOutput();
				tasma2.openAsInput();
				tasma1.openAsInput();
				
				r = tasma1.readNext();
				r2 = tasma2.readNext();

				//Czy tasmy maja rekordy
                if(r!=null && r2!=null) {
                    //Dopoki sa jakies rekordy...
                    while(r!=null || r2!=null) {
                        //Sa na obu tasmach
                        if(r!=null && r2!=null) {
                            //Scalamy, bo nigdzie seria sie nie skoczyla
                            while(!tasma1.endSeries && !tasma2.endSeries){
                                if(r.compareTo(r2)<0) {
                                    tasma3.writeNext(r);
                                    r = tasma1.readNext();
                                } else {
                                    tasma3.writeNext(r2);
                                    r2 = tasma2.readNext();
                                }
                            }
                            //Dopisywanie do scalonej serii az do konca tej serii
                            while(!tasma1.endSeries) {
                                tasma3.writeNext(r);
                                r=tasma1.readNext();
                            }
                            while(!tasma2.endSeries) {
                                tasma3.writeNext(r2);
                                r2=tasma2.readNext();
                            }
                            tasma1.endSeries=false;
                            tasma2.endSeries=false;
                        } else if(r!=null) { //Sa na tasmie 1 -> przepisujemy az do konca
                            tasma3.writeNext(r);
                            r = tasma1.readNext();
                        } else if(r2!=null) { //Sa na tasmie 2
                            tasma3.writeNext(r2);
                            r2 = tasma2.readNext();
                        }
                    }
                    tasma1.endSeries=false;
                    tasma2.endSeries=false;
                    tasma3.flush();
                } else {
                    sorted=true;
                }
				fazy++;
				
				System.out.println("Zmian tasm przy dystrybucji = "+zmianTasmPrzyDystrybucji);
				if(zmianTasmPrzyDystrybucji < 2) sorted=true;
			}
			System.out.println("Posortowano w "+(fazy-1)+" fazach");
            printStats(tasma1, tasma2, tasma3);
		}
	}

	public static void printStats(Tasma... tasm) {
	    int ready = 0;
	    int writy = 0;
	    for(Tasma t: tasm) {
            ready += t.getOdczyt();
            writy += t.getZapis();
	    }
	    System.out.println("W sumie: "+ready+" odczytow, "+writy+" zapisow");
    }

}
