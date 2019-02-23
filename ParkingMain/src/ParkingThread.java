import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class ParkingThread extends ParkingParent{
		
	protected Parking.Place place;
	protected String carID;
	
	ParkingThread(Parking.Place place, String carID,
			Semaphore semIn, ConcurrentHashMap <Integer, 
			Parking.Place> cars, ArrayList<Ticket> tickets) {
		super(semIn, cars, tickets);
		this.place = place;
		this.carID = carID;
	}
	
	@Override
	public void run() {
		try
        {
			//Вход в очередь на въезд
			semIn.acquire(); 
			
			//Блокируем доступ к списку билетов
			synchronized (tickets) {
				
				if(tickets.size() >= 1 ) {
					//Выдаем билет со случайным номером
					final Random random = new Random();
					int r = (random.nextInt(tickets.size()));
					int ticketNumber = tickets.get(r).getNumber();
					tickets.remove(r);
					
					/*
					 * Добавляем данные о номера авто 
					 * и номер билета в хеш-таблицу
					 */
					place.carID = carID;
					place.ticketID = ticketNumber;
					cars.putIfAbsent(ticketNumber, place);
					
					/*
					 * Эмуляция времени въезда от 1 до 5 секунд
					 */
					final Random randomTime = new Random();
					Thread.sleep(randomTime.nextInt(5000) + 1000);
				}
			}
			
			//Освобождаем очередь на въезд
			semIn.release();
			
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
