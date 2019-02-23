import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Parking extends Thread{
	
	//������ ��������
	private int space;
	
	//���� ��� ������ �� 2 ����, � ��������� � ������� �������
	private Semaphore semIn = new Semaphore(2, true);
	
	//���� ��� ������ �� 2 ����, � �������� � ������� �������
	private Semaphore semOut = new Semaphore(2, true);
	
	//������� ��� �������� ����
	private ConcurrentHashMap<Integer, Place> cars =
			new ConcurrentHashMap <Integer, Place>();
	
	//������ �������
	private ArrayList<Ticket> tickets; 
	
	//���������� ����� ��� ������������ �����
	public class Place {
		protected String carID;
		protected int ticketID;
		
		Place() {}
		
		Place(String carID, int ticketID) {
			this.carID = carID;
			this.ticketID = ticketID;
		}
		
		//����� ������ ���� � ������ ������
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
	 * �������������� ������������ ������ �������� 
	 * � ������ ������� ��� ��
	 */
	Parking(int space) {
		this.space = space;
		tickets = new ArrayList<Ticket>(space);
		for(int i = 1; i <= space; i++) {
			tickets.add(new Ticket(i));
		}
	}
	
	//������ ��������
	//public int getSpace() {return space;}
	
	/*
	 * ������� ���������� ���������� �� ��������
	 * ���� ��� ���� �����
	 */
	public void addCar(Car car) {
		if(getTicketsCount() >= 1) {
			new Thread(new ParkingThread(new Place(), car.getNumber(),
					semIn, this.cars, tickets)).start();
		}
	}
	
	//���������� ��������� �������
	public int getTicketsCount() {
		return tickets.size();
	}
	
	//����� ������ ���� �� ��������
	public void getCarsList() {
		if(cars.size() == 0) {
			System.out.println("�� �������� ��� �����������.");
			return;
		}
		int i = 1;
		for (Entry<Integer, Place> entry : cars.entrySet()) {
			  System.out.println(i + ") " + entry.getValue().toString());
			  i++;
			}	
	}
	
	/*
	 * �������� ���� � �������� �� ������ ������
	 * ���� �����, �������������� ������ ������ � 
	 * ����������
	 */
	public void removeByTicket(int[] mass) {

		for(int i = 0; i < mass.length; i++) {
			if(cars.get(mass[i]) != null) {				
				new Thread(new ParkingOut(semOut, this.cars, tickets, mass[i])).start();
			}
			else {
				if(mass[i] > space || mass[i] == 0) {
					System.out.println("������ ����� �� ����������: " + mass[i]);
				} else {
					System.out.println("����� ��� ������� " + mass[i] + " �� ���� ������");
				}
			}
		}
	}
	
}
