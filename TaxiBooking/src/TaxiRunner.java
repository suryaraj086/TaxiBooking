import java.util.Scanner;

public class TaxiRunner {

	public static void main(String[] args) {
		TaxiBooking tObj = new TaxiBooking();
		System.out.println("Enter number of taxi's you want to create");
		Scanner scan = new Scanner(System.in);
		int taxi = scan.nextInt();
		for (int i = 0; i < taxi; i++) {
			Taxi taxipojo = new Taxi();
			tObj.createTaxi(taxipojo);
		}
		boolean bool = true;
		while (bool) {
			System.out.println("1.Book taxi\n2.Print taxi details");
			int val = scan.nextInt();
			switch (val) {
			case 1:
				System.out.println("Enter from location");
				char from = scan.next().toUpperCase().charAt(0);
				System.out.println("Enter to location");
				char to = scan.next().toUpperCase().charAt(0);
				System.out.println("Enter time to reach");
				int time = scan.nextInt();
				long time1 = time * 3600000;
				System.out.println(tObj.bookTaxi(time1, from, to, 1));
				break;
			case 2:
				System.out.println(tObj.printTaxiDetails());
				break;

			default:
				bool = false;
				break;
			}
		}
		scan.close();
	}
}
