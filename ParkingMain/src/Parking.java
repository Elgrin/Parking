import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Parking extends Thread{
	
	//Размер парковки
	private int space;
	
	//Флаг для въезда по 2 авто, с пропуском в порядке очереди
	private Semaphore semIn = new Semaphore(2, true);
	
	//Флаг для выезда по 2 авто, с выпуском в порядке очереди
	private Semaphore semOut = new Semaphore(2, true);
	
	//Таблица для хранения авто
	private ConcurrentHashMap<Integer, Place> cars =
			new ConcurrentHashMap <Integer, Place>();
	
	//Список билетов
	private ArrayList<Ticket> tickets; 
	
	//Внутренний класс для парковочного места
	public class Place {
		protected String carID;
		protected int ticketID;
		
		Place() {}
		
		Place(String carID, int ticketID) {
			this.carID = carID;
			this.ticketID = ticketID;
		}
		
		//Вывод номера авто и номера билета
		@Override
		public String toString() {
			return "carID: " + carID + "; " 
					+ "ticketID: " + ticketID;
		}
		
		public int getTicketID() {
			return ticketID;
		}
	}	
	/*
	 * Инициализируем конструкторе размер парковки 
	 * и список билетов для неё
	 */
	Parking(int space) {
		this.space = space;
		tickets = new ArrayList<Ticket>(space);
		for(int i = 1; i <= space; i++) {
			tickets.add(new Ticket(i));
		}
	}
	
	//Размер парковки
	//public int getSpace() {return space;}
	
	/*
	 * Попытка добавление автомобиля на парковку
	 * если там есть место
	 */
	public void addCar(Car car) {
		if(getTicketsCount() >= 1) {
			new Thread(new ParkingThread(new Place(), car.getNumber(),
					semIn, this.cars, tickets)).start();
		}
	}
	
	//Количество свободных билетов
	public int getTicketsCount() {
		return tickets.size();
	}
	
	//Вывод списка авто на парковке
	public void getCarsList() {
		if(cars.size() == 0) {
			System.out.println("На парковке нет автомобилей.");
			return;
		}
		int i = 1;
		for (Entry<Integer, Place> entry : cars.entrySet()) {
			  System.out.println(i + ") " + entry.getValue().toString());
			  i++;
			}	
	}
	/*
	 * Удаление авто с парковки по номеру билета
	 * если место, соотвествующее билету занято и 
	 * существует
	 */
	public void removeByTicket(int[] mass) {

		for(int i = 0; i < mass.length; i++) {
			if(cars.get(mass[i]) != null) {				
				new Thread(new ParkingOut(semOut, this.cars, tickets, mass[i])).start();
			}
			else {
				if(mass[i] > space || mass[i] == 0) {
					System.out.println("Такого места не существует: " + mass[i]);
				} else {
					System.out.println("Место под номером " + mass[i] + " не было занято");
				}
			}
		}
	}
	
}
