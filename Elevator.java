import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MoveElevator{

	static Scanner keyboard = new Scanner(System.in);
	static Elevator elevator;
	static List<Passenger> travelersPassengers;

	public MoveElevator(){}

	static int getInfo(String q) {
		System.out.println(q);
		System.out.print("> ");
		return keyboard.nextInt();
	}

	public static void main(String[] args) {

		int currentFloor = getInfo("Em qual andar o elevador esta?");
		elevator = new Elevator(currentFloor);
		travelersPassengers = new ArrayList<Passenger>();

		int passengerCount = getInfo("Quantos passageiros estao aguardando viagem?");
		for (int i = 0; i < passengerCount; i++) {
			int idPassenger = i + 1;
			int currentPassengerFloor = getInfo("Qual andar estar o passageiro " + idPassenger + "?");
			int intendedFloor = getInfo("Qual andar o passageiro " + idPassenger + " deseja ir?");
			Passenger pass = new Passenger(currentPassengerFloor, intendedFloor, idPassenger);
			elevator.setPassengeres(pass);
			travelersPassengers.add(pass);
		}
		
		System.out.println("### Elevador em Movimento ###");

		while (passengerWaitingTravel()) {
			int indexPassengerNearest = nearestPassenger(currentFloor);

			if (elevator.getCurrentFloor() < travelersPassengers.get(indexPassengerNearest).getCurrentFloor()) {
				elevator.setStatus(Elevator.GOING_UP);
				elevatorGoTo(travelersPassengers.get(indexPassengerNearest).getCurrentFloor());
				travelersPassengers.get(indexPassengerNearest).setOnBoard(true);
				elevator.setStatus(travelersPassengers.get(indexPassengerNearest).getIntendedStatus());
			} else if (elevator.getCurrentFloor() > travelersPassengers.get(indexPassengerNearest).getCurrentFloor()){
				elevator.setStatus(Elevator.GOING_DOWN);
				elevatorGoTo(travelersPassengers.get(indexPassengerNearest).getCurrentFloor());
				travelersPassengers.get(indexPassengerNearest).setOnBoard(true);
				elevator.setStatus(travelersPassengers.get(indexPassengerNearest).getIntendedStatus());
			} else {
				travelersPassengers.get(indexPassengerNearest).setOnBoard(true);
				elevator.setStatus(travelersPassengers.get(indexPassengerNearest).getIntendedStatus());
			}

			if (elevator.getStatus() == Elevator.GOING_UP) {
				for (int i = elevator.getCurrentFloor(); i <= travelersPassengers.get(indexPassengerNearest).getIntendedFloor(); i++) {
					elevator.setCurrentFloor(i);
					checkPassengersOnBoard();
				}
			} else {
				for (int i = elevator.getCurrentFloor(); i >= travelersPassengers.get(indexPassengerNearest).getIntendedFloor(); i--) {
					elevator.setCurrentFloor(i);
					checkPassengersOnBoard();
				}
			}
		}
	}

	static int nearestPassenger(int currentFloor){
		int indexPassenger = 0;
		int floorDistance = Integer.MAX_VALUE;
		for (int i = 0; i < travelersPassengers.size(); i++) {
			int distancePassenger = Math.abs(currentFloor - travelersPassengers.get(i).getCurrentFloor());
			if ((distancePassenger < floorDistance) && !travelersPassengers.get(i).isOnBoard()) {
				floorDistance = distancePassenger;
				indexPassenger = travelersPassengers.indexOf(travelersPassengers.get(i));
			}
		}
		return indexPassenger;
	}

	static void elevatorGoTo(int intended){
		if (elevator.getStatus() == Elevator.GOING_UP) {
			for (int i = elevator.getCurrentFloor(); i <= intended; i++) {
				elevator.setCurrentFloor(i);
			}	
		} else {
			for (int i = elevator.getCurrentFloor(); i >= intended; i--) {
				elevator.setCurrentFloor(i);
			}
		}
	}

	static void checkPassengersOnBoard(){
		travelersPassengers.forEach(pass -> {
			if (!pass.isOnBoard() && pass.getCurrentFloor() == elevator.getCurrentFloor() && pass.getIntendedStatus() == elevator.getStatus() && !pass.isCompletedTravel()) {
				pass.setOnBoard(true);
			}
			if (pass.getIntendedFloor() == elevator.getCurrentFloor() && pass.isOnBoard()) {
				pass.setCompletedTravel(true);
			}
		});
		// Passenger passComplete = travelersPassengers.stream().filter(p -> (p.isCompletedTravel() == true)).findAny().orElse(null);
		// if (passComplete != null) {
		// 	travelersPassengers.remove(passComplete);	
		// }
	}

	static boolean passengerWaitingTravel(){
		Passenger passWaitingTravel = travelersPassengers.stream().filter(pass -> (pass.isCompletedTravel() == false)).findFirst().orElse(null);
		if (passWaitingTravel != null) {
			return true;
		} else{
			return false;
		}
	} 
}

class Passenger {

	private int currentFloor;
	private int intendedFloor;
	private int intendedStatus;
	private boolean completedTravel;
	private boolean onBoard; 
	private int id;

	public Passenger(int currentFloor, int intendedFloor, int id) {
		setId(id);
		setOnBoard(false);
		setCurrentFloor(currentFloor);
		setIntendedFloor(intendedFloor);
		setCompletedTravel(false);
		if (currentFloor < intendedFloor) {
			setIntendedStatus(Elevator.GOING_UP);
		} else {
			setIntendedStatus(Elevator.GOING_DOWN);
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the completedTravel
	 */
	public boolean isCompletedTravel() {
		return completedTravel;
	}

	/**
	 * @param completedTravel the completedTravel to set
	 */
	public void setCompletedTravel(boolean completedTravel) {
		if (completedTravel) {
			System.out.println("Passageiro " + this.id + " completou a viagem");	
		}
		this.completedTravel = completedTravel;
	}

	/**
	 * @param currentFloor the currentFloor to set
	 */
	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}
	/**
	 * @return the currentFloor
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * @param intendedFloor the intendedFloor to set
	 */
	public void setIntendedFloor(int intendedFloor) {
		this.intendedFloor = intendedFloor;
	}
	/**
	 * @return the intendedFloor
	 */
	public int getIntendedFloor() {
		return intendedFloor;
	}

	/**
	 * @param intendedStatus the intendedStatus to set
	 */
	public void setIntendedStatus(int intendedStatus) {
		this.intendedStatus = intendedStatus;
	}
	/**
	 * @return the intendedStatus
	 */
	public int getIntendedStatus() {
		return intendedStatus;
	}

	/**
	 * @param onBoard the onBoard to set
	 */
	public void setOnBoard(boolean onBoard) {
		if (onBoard) {
			System.out.println("Passageiro " + this.id + " entrou no elevador.");	
		}
		this.onBoard = onBoard;
	}
	/**
	 * @return the onBoard
	 */
	public boolean isOnBoard() {
		return onBoard;
	}

}

class Elevator {

    static final int GOING_UP = 1;
    static final int STOPPED = 0;
	static final int GOING_DOWN = -1;

	private int currentFloor;
	private int status;
	private List<Passenger> passengeres;

	public Elevator(int currentFloor) {
		this.setCurrentFloor(currentFloor);
		this.setStatus(STOPPED);
		this.passengeres = new ArrayList<Passenger>();
	}


	/**
	 * @param currentFloor the currentFloor to set
	 */
	public void setCurrentFloor(int currentFloor) {
		System.out.println("O elevador esta no " + currentFloor + " andar.");
		this.currentFloor = currentFloor;
	}
	/**
	 * @return the currentFloor
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		String msg = "";
		if (status == this.GOING_UP) {
			msg = "O elevador esta subindo...";
		} else if (status == this.GOING_DOWN) {
			msg = "O elevador esta descendo...";
		} else {
			msg = "O elevador esta parado.";
		}
		System.out.println(msg);
		this.status = status;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * @param passengeres the passengeres to set
	 */
	public void setPassengeres(Passenger passenger) {
		this.passengeres.add(passenger);
	}
	/**
	 * @return the passengeres
	 */
	public List<Passenger> getPassengeres() {
		return passengeres;
	}

}