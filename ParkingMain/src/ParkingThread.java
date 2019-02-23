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
			//���� � ������� �� �����
			semIn.acquire(); 
			
			//��������� ������ � ������ �������
			synchronized (tickets) {
				
				if(tickets.size() >= 1 ) {
					//������ ����� �� ��������� �������
					final Random random = new Random();
					int r = (random.nextInt(tickets.size()));
					int ticketNumber = tickets.get(r).getNumber();
					tickets.remove(r);
					
					/*
					 * ��������� ������ � ������ ���� 
					 * � ����� ������ � ���-�������
					 */
					place.carID = carID;
					place.ticketID = ticketNumber;
					cars.putIfAbsent(ticketNumber, place);
					
					/*
					 * �������� ������� ������ �� 1 �� 5 ������
					 */
					final Random randomTime = new Random();
					Thread.sleep(randomTime.nextInt(5000) + 1000);
				}
			}
			
			//����������� ������� �� �����
			semIn.release();
			
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
