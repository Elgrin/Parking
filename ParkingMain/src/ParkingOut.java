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
			//���� � ������� �� �����
			semIn.acquire(); 
			
			//��������� ������ � �������
			synchronized (tickets) {
				/*
				 * �������, ��� ����� ������������� ��� ������ ���������
				 */
				tickets.add(new Ticket(cars.get(ticketID).getTicketID()));
				cars.remove(ticketID);
			}
			
			//����� �� ������� �� �����
			semIn.release();
			
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
