import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class ParkingOut extends ParkingParent {
	
	private int ticketID;
	
	ParkingOut(Semaphore semIn, ConcurrentHashMap <Integer, 
			Parking.Place> cars, ArrayList<Ticket> tickets, 
			int ticketID) {
		super(semIn, cars, tickets);
		this.ticketID = ticketID;
	}

	@Override
	public void run() {
		try
        {
			//Вход в очередь на выезд
			semIn.acquire(); 
			
			//Блокируем доступ к билетам
			synchronized (tickets) {
				/*
				 * Считаем, что место освобождается при выезде мгновенно
				 */
				tickets.add(new Ticket(cars.get(ticketID).getTicketID()));
				cars.remove(ticketID);
			}
			
			//Выход из очереди на выезд
			semIn.release();
			
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
