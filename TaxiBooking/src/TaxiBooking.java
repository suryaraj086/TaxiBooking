import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaxiBooking {

	List<Taxi> taxi = new ArrayList<>();
	List<Character> points = new ArrayList<>();
	List<Passenger> passenger = new ArrayList<Passenger>();
	Map<Integer, List<TaxiHistory>> history = new HashMap<>();

	public TaxiBooking() {
		points.add('A');
		points.add('B');
		points.add('C');
		points.add('D');
		points.add('E');
		points.add('F');
	}

	int num = 0;

	public void createTaxi(Taxi taxiDetails) {
		taxiDetails.setTaxiNumber(num++);
		taxi.add(taxiDetails);
		history.put(taxiDetails.getTaxiNumber(), new ArrayList());
	}

	int k = 0;

	public String bookTaxi(long pickupTime, char currPoint, char toPoint, int customerid) {
		if (!(currPoint >= 'A') || !(currPoint <= 'F') || !(toPoint >= 'A') || !(toPoint <= 'F')) {
			return "not valid location";
		}
		List<Taxi> temp = new ArrayList<Taxi>();
		for (int i = 0; i < taxi.size(); i++) {
			if (getIdleTaxi(taxi.get(i)) && timeChecker(pickupTime, taxi.get(i), currPoint)) {
				temp.add(taxi.get(i));
			}
		}
		if (temp.size() == 0) {
			return "No Taxi Found";
		}
		Taxi booked = getNearestTaxi(temp, currPoint);
		booked.setStatus(TaxiState.BUSY);
		booked.setPosition(toPoint);
		int amount = paymentCalculator(currPoint, toPoint);
		int earned = booked.getAmountEarned();
		booked.setAmountEarned(earned + amount);
		TaxiHistory temp1 = historySetter(pickupTime, currPoint, toPoint, amount, k++, customerid);
		addToHistory(temp1, booked);
		return "Booked successfully and the amount to pay is " + amount;
	}

	public void addToHistory(TaxiHistory temp1, Taxi booked) {
		List<TaxiHistory> arr = history.get(booked.getTaxiNumber());
		arr.add(temp1);
		history.put(booked.getTaxiNumber(), arr);
	}

	public TaxiHistory historySetter(long pickuptime, char fromLocation, char toLocation, int amount, int bookingid,
			int customerid) {
		TaxiHistory history1 = new TaxiHistory();
		history1.setAmount(amount);
		history1.setBookingid(bookingid);
		history1.setDropLocation(toLocation);
		history1.setPickupLocation(fromLocation);
		history1.setCustomerid(customerid);
		history1.setPickuptime(pickuptime);
		return history1;
	}

	public int paymentCalculator(char currPoint, char toPoint) {
		int pay = 100;
		int fromindex = points.indexOf(currPoint);
		int toindex = points.indexOf(toPoint);
		if (fromindex > toindex) {
			pay += 10 * 10;
			for (int i = fromindex; i > toindex + 1; i--) {
				pay += 10 * 15;
			}
		} else {
			pay += 10 * 10;
			for (int i = toindex; i > fromindex + 1; i--) {
				pay += 10 * 15;
			}
		}
		return pay;
	}

	public Taxi getNearestTaxi(List<Taxi> list, char pickup) {
		if (list.size() == 1) {
			return list.get(0);
		}
		int min = Integer.MAX_VALUE;
		Taxi temp = null;
		List<Taxi> arr = new ArrayList<Taxi>();
		for (int i = 0; i < list.size(); i++) {
			Taxi obj = list.get(i);
			int val = obj.getPosition() - pickup;
			if (val <= min) {
				min = Math.min(val, min);
				temp = obj;
				if (val == min) {
					arr.add(temp);
				} else {
					arr.clear();
				}
			}
		}
		if (arr.size() != 0) {
			temp = lowestEarning(arr);
		}
		return temp;
	}

	public Taxi lowestEarning(List<Taxi> list) {
		int temp = Integer.MAX_VALUE;
		Taxi out = null;
		for (int i = 0; i < list.size(); i++) {
			Taxi obj = list.get(i);
			if (obj.getAmountEarned() < temp) {
				temp = obj.getAmountEarned();
				out = obj;
			}
		}
		return out;
	}

	public boolean getIdleTaxi(Taxi taxiObj) {
		if (taxiObj.getStatus() == TaxiState.IDLE) {
			return true;
		}
		return false;
	}

	public void changeStatus(int taxino) {
		for (int i = 0; i < taxi.size(); i++) {
			Taxi obj = taxi.get(i);
			if (obj.getTaxiNumber() == taxino) {
				obj.setStatus(TaxiState.IDLE);
			}

		}
	}

	public boolean timeChecker(long pickuptime, Taxi taxiObj, char pickupPoint) {
		char temp = taxiObj.getPosition();
		long currtime = 0;
		int fromindex = points.indexOf(temp);
		int toindex = points.indexOf(pickupPoint);
		if (fromindex > toindex) {
			for (int i = fromindex - 1; i >= toindex; i--) {
				currtime += 3600000;
			}
		} else {
			for (int i = toindex - 1; i >= fromindex; i--) {
				currtime += 3600000;
			}
		}
		if (pickuptime < currtime) {
			return false;
		}
		return true;
	}

	public String printTaxiDetails() {
		String out = "";
		Set<Integer> key = history.keySet();
		for (int val : key) {
			out += "Taxi number " + val + " " + history.get(val).toString();
		}
		return out;
	}

}