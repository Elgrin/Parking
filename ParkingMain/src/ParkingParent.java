import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public abstract class ParkingParent  implements Runnable {

	protected ConcurrentHashMap <Integer, Parking.Place> cars = 
			new ConcurrentHashMap <Integer, Parking.Place>();
	protected Semaphore semIn;
	protected ArrayList<Ticket> tickets;
	
	ParkingParent(Semaphore semIn, ConcurrentHashMap <Integer, 
			Parking.Place> cars, ArrayList<Ticket> tickets) {
		this.semIn = semIn;
		this.cars = cars;
		this.tickets = tickets;
	}
	
}
