package pg.ppabis.bazy1;

import java.util.Random;

/*
 * 2017 (c) Piotr Pabis 149528 Grupa 6B
 * */

public class Rekord implements Comparable<Rekord> {
	float a,b,h;
	
	public Rekord(float a, float b, float wysokosc) {
		this.a=a;
		this.b=b;
		this.h=wysokosc;
	}
	
	public float objetosc() {
		return a*b*h;
	}
	
	
	public static final Random RandomGenerator = new Random();
	public static final float MIN_PODSTAWA = 2f;
	public static final float MAX_PODSTAWA = 10f;
	public static final float MIN_WYSOKOSC = 10f;
	public static final float MAX_WYSOKOSC = 30f;
	public static Rekord losowy() {
		return new Rekord(
				MIN_PODSTAWA+RandomGenerator.nextFloat()*MAX_PODSTAWA,
				MIN_PODSTAWA+RandomGenerator.nextFloat()*MAX_PODSTAWA,
				MIN_WYSOKOSC+RandomGenerator.nextFloat()*MAX_WYSOKOSC);
	}

	@Override
	public int compareTo(Rekord o) {
		if(this.objetosc()==o.objetosc()) return 0;
		else if(this.objetosc()<o.objetosc()) return -1;
		else if(this.objetosc()>o.objetosc()) return 1;
		return 0;
	}
}
