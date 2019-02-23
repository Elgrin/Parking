import java.util.Random;
import java.util.Scanner;

public class ParkingMain {
	
	
	private static Parking parking;
	
	//������ ����, ����������� � ���. ������� ����
	private static final String Alphabet = "������������";
	
	public static void main(String[] args) {
		
		System.out.println("> ������� ������ ��������: ");
		
		int space = 0;
		boolean lock = true;
		Scanner in = new Scanner(System.in);
		
		/*
		 * ���� ������� ��������
		 */
		while(lock) {
			in = new Scanner(System.in);
			if(in.hasNextInt()) {
				
		        space = in.nextInt();
		        if(space < 1) {
		        	System.out.println("> ������ �� ����� ���� ������ 1.");
		        }
		        else 
		        	lock = false;
				}
				else {
					System.out.println("> ������ �����. ����������, ������� �����.");
				}
		}
		
		//�������������� �������� � ��������� ��������
		parking = new Parking(space);
		
		/*
		 * ������� ������
		 */
		
		lock = true;
		while(lock) {
			System.out.println("> ������� �������:");
			in = new Scanner(System.in);
			
			if(in.hasNext("l")) {
				//����� ������ ���� �� ��������
				getCarsList();
				continue;	
			} else if(in.hasNext("c")) {
				//����� ���������� ����� �� ��������
				System.out.println("�������� " + getFreeSpace() + " ����.");
				continue;
			} else if(in.hasNext("e")) {
				//����� �� ����������
				lock = false;
				System.out.println("> ���������� ���������.");
				continue;	
			} if(in.hasNext("p:[1-9]\\d*")) {
				//���������� ������������� ����� ����
				String parts[] = in.nextLine().split(":");
				onNewCar(Integer.parseInt(parts[1]));
				continue;	
			}
			else if(in.hasNext("u:[1-9]\\d*"))  {
				//�������� ���� � ������������ ����� 
				String command = in.nextLine();
				int[] arrMass = new int[1];
				arrMass[0] = Integer.parseInt(command.replaceAll("u:",""));
				onOutCars(arrMass);
				continue;	
			} else if(in.hasNext("(u:\\[[1-9]+((,[0-9]+)*)\\])"))  {
				//�������� ���� � ����������� ���� �� ������
				String command = in.nextLine();
				onOutCars(convertStringToInt(command
						.replaceAll("(u:|\\[|\\])","").split(",")));
				continue;
			} else {
				//���� ������� �� �������
				System.out.println("> ������� �� �������. ��������� ������");
				continue;	
			}
		}
		in.close();
	}
	
	
	//���������� ����
	private static void onNewCar(int count) {
		
		//��������� �����, ����� ��������� ������� �� �������������
		Thread addCar = new Thread()
		{
		     public void run()
		     {
		 		if(parking.getTicketsCount() == 0) {
					System.out.println("�� �������� ��� ����. ���� ����� �� ������� �������� � ��������� �����,"
							+ "�� ������������� ������ ����� �������.\n"
							+ "���� ����� ��������, �� ����� ����� �������������� � ������� �������.");
				} else if(count > parking.getTicketsCount()) {
					System.out.println("�� �������� �������� ����� ������, ��� ���������� �����������, "
							+ "������� ���� ����� �������. \n�� ������ ������ ������� ������ ������: " 
							+ Integer.toString(getFreeSpace()) + " �����(�) �� " + count + ".\n"
							+ "���� ����� �� ������� �������� � ��������� �����, "
							+ "�� ������������� ������ ����� �������.\n"
							+ "���� ����� ��������, �� ����� ����� �������������� � ������� �������.");
					}
				/*
				Car[] cars = new Car[count];
				
				for(int i = 0; i < cars.length; i++) {
					final Random random = new Random();
					//12
					//Character.toString(Alphabet.charAt(random.nextInt(12) + 1));
					String num = Character.toString(Alphabet.charAt(random.nextInt(12)))
							+ (random.nextInt(9))
							+ (random.nextInt(9))
							+ (random.nextInt(9))
							+ Character.toString(Alphabet.charAt(random.nextInt(12)))
							+ Character.toString(Alphabet.charAt(random.nextInt(12)));
					cars[i] = new Car(num);
				}
				parking.addCars(cars);*/
		 		
				for(int i = 0; i < count; i++) {
					final Random random = new Random();
					//��������� ����� ��� ��������� ����, ����� �� ����������� ������ ������
					Thread carCreate = new Thread()
					{
					     public void run()
					     {
					    	 //��������� ������ ���� � ���������� ��� �� ��������
					    	 String num = Character.toString(Alphabet.charAt(random.nextInt(12)))
										+ (random.nextInt(9))
										+ (random.nextInt(9))
										+ (random.nextInt(9))
										+ Character.toString(Alphabet.charAt(random.nextInt(12)))
										+ Character.toString(Alphabet.charAt(random.nextInt(12)));
								parking.addCar(new Car(num));
					     }
					};
					carCreate.start();
				}
				
		     }
		};
		addCar.start();
	}
	
	//�������������� String array to ing array
	private static int[] convertStringToInt(String[] mass) {
		int[] intMass = new int[mass.length];
		
		for(int i = 0; i < mass.length; i++) {
			intMass[i] = Integer.parseInt(mass[i]);
		}
		return intMass;
	}
	
	//�������� ���� � �������� � ��������� ������
	private static void onOutCars(int mass[]) {
		Thread t = new Thread()
		{
		     public void run()
		     {
		 		parking.removeByTicket(mass);
		     }
		};
		t.start();
	}
	
	//������ ���� �� ��������
	private static void getCarsList() {
		parking.getCarsList();
	}
	
	//������� �������� ����� �� ��������
	private static int getFreeSpace() {
		return parking.getTicketsCount();
	}
}
