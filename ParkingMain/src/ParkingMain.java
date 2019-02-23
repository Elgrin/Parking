import java.util.Random;
import java.util.Scanner;

public class ParkingMain {
	
	
	private static Parking parking;
	
	//Список букв, разрешенных в гос. номерах авто
	private static final String Alphabet = "АВЕКМНОРСТУХ";
	
	public static void main(String[] args) {
		
		System.out.println("> Введите размер парковки: ");
		
		int space = 0;
		boolean lock = true;
		Scanner in = new Scanner(System.in);
		
		/*
		 * Ввод размера парковки
		 */
		while(lock) {
			in = new Scanner(System.in);
			if(in.hasNextInt()) {
				
		        space = in.nextInt();
		        if(space < 1) {
		        	System.out.println("> Размер не может быть больше 1.");
		        }
		        else 
		        	lock = false;
				}
				else {
					System.out.println("> Ошибка ввода. Пожалуйста, введите число.");
				}
		}
		
		//Инициализируем парковку с указанным размером
		parking = new Parking(space);
		
		/*
		 * Консоль команд
		 */
		
		lock = true;
		while(lock) {
			System.out.println("> Введите команду:");
			in = new Scanner(System.in);
			
			if(in.hasNext("l")) {
				//вывод списка авто на парковке
				getCarsList();
				continue;	
			} else if(in.hasNext("c")) {
				//Вывод свободного места на парковке
				System.out.println("Осталось " + getFreeSpace() + " мест.");
				continue;
			} else if(in.hasNext("e")) {
				//выход из приложения
				lock = false;
				System.out.println("> Выполнение завершено.");
				continue;	
			} if(in.hasNext("p:[1-9]\\d*")) {
				//добавление определенного числа авто
				String parts[] = in.nextLine().split(":");
				onNewCar(Integer.parseInt(parts[1]));
				continue;	
			}
			else if(in.hasNext("u:[1-9]\\d*"))  {
				//удаление авто с парковочного места 
				String command = in.nextLine();
				int[] arrMass = new int[1];
				arrMass[0] = Integer.parseInt(command.replaceAll("u:",""));
				onOutCars(arrMass);
				continue;	
			} else if(in.hasNext("(u:\\[[1-9]+((,[0-9]+)*)\\])"))  {
				//удаление авто с парковочных мест из списка
				String command = in.nextLine();
				onOutCars(convertStringToInt(command
						.replaceAll("(u:|\\[|\\])","").split(",")));
				continue;
			} else {
				//Если команда не найдена
				System.out.println("> Команда не найдена. Повторите запрос");
				continue;	
			}
		}
		in.close();
	}
	
	
	//Добавление авто
	private static void onNewCar(int count) {
		
		//Отдельный поток, чтобы остальные команды не блокировались
		Thread addCar = new Thread()
		{
		     public void run()
		     {
		 		if(parking.getTicketsCount() == 0) {
					System.out.println("На парковке нет мест. Если никто не покинет парковку в ближайщее время,"
							+ "то недождавшиеся машины будут уезжать.\n"
							+ "Если места появятся, то въезд будет осуществляться в порядке очереди.");
				} else if(count > parking.getTicketsCount()) {
					System.out.println("На парковке осталось места меньше, чем количество автомобилей, "
							+ "которые туда хотят заехать. \nНа данный момент заехать сможет только: " 
							+ Integer.toString(getFreeSpace()) + " машин(ы) из " + count + ".\n"
							+ "Если никто не покинет парковку в ближайщее время, "
							+ "то недождавшиеся машины будут уезжать.\n"
							+ "Если места появятся, то въезд будет осуществляться в порядке очереди.");
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
					//Отдельный поток для генерации авто, чтобы не блокировать другие потоки
					Thread carCreate = new Thread()
					{
					     public void run()
					     {
					    	 //Генерация номера авто и добавление его на парковку
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
	
	//Преобразование String array to ing array
	private static int[] convertStringToInt(String[] mass) {
		int[] intMass = new int[mass.length];
		
		for(int i = 0; i < mass.length; i++) {
			intMass[i] = Integer.parseInt(mass[i]);
		}
		return intMass;
	}
	
	//Удаление авто с парковки в отдельном потоке
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
	
	//Список авто на парковке
	private static void getCarsList() {
		parking.getCarsList();
	}
	
	//Сколько осталось места на парковке
	private static int getFreeSpace() {
		return parking.getTicketsCount();
	}
}
