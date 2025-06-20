import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    boolean isBooked;

    Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    double payment;

    Booking(String customerName, int roomNumber, String category, double payment) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.payment = payment;
    }

    public String toString() {
        return "Name: " + customerName + ", Room: " + roomNumber + " (" + category + "), Paid: INR " + payment;
    }
}

public class HotelReservationSystem {
    static List<Room> rooms = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadRooms();
        loadBookings();

        while (true) {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // flush

            switch (choice) {
                case 1: viewAvailableRooms(); break;
                case 2: bookRoom(); break;
                case 3: cancelReservation(); break;
                case 4: viewBookings(); break;
                case 5: saveBookings(); System.out.println("Exiting...Thank you"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void loadRooms() {
        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Standard"));
        rooms.add(new Room(201, "Deluxe"));
        rooms.add(new Room(202, "Deluxe"));
        rooms.add(new Room(301, "Suite"));
    }

    static void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (!r.isBooked) {
                System.out.println("Room " + r.roomNumber + " - " + r.category);
            }
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        viewAvailableRooms();
        System.out.print("Enter room number to book: ");
        int number = sc.nextInt();
        sc.nextLine();

        Room roomToBook = null;
        for (Room r : rooms) {
            if (r.roomNumber == number && !r.isBooked) {
                roomToBook = r;
                break;
            }
        }

        if (roomToBook != null) {
            double cost = getPriceByCategory(roomToBook.category);
            System.out.println("Room cost: INR " + cost);
            System.out.print("Enter payment amount: INR ");
            double payment = sc.nextDouble();
            sc.nextLine();

            if (payment >= cost) {
                roomToBook.isBooked = true;
                bookings.add(new Booking(name, number, roomToBook.category, payment));
                System.out.println("Room booked successfully.");
            } else {
                System.out.println("Insufficient payment.");
            }
        } else {
            System.out.println("Room not available.");
        }
    }

    static void cancelReservation() {
        System.out.print("Enter your name to cancel booking: ");
        String name = sc.nextLine();
        Iterator<Booking> it = bookings.iterator();
        boolean found = false;

        while (it.hasNext()) {
            Booking b = it.next();
            if (b.customerName.equalsIgnoreCase(name)) {
                it.remove();
                for (Room r : rooms) {
                    if (r.roomNumber == b.roomNumber) {
                        r.isBooked = false;
                        break;
                    }
                }
                System.out.println("Booking canceled.");
                found = true;
                break;
            }
        }
        if (!found) System.out.println("No booking found.");
    }

    static void viewBookings() {
        System.out.println("\nCurrent Bookings:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        }
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    static double getPriceByCategory(String category) {
        switch (category) {
            case "Standard": return 1500;
            case "Deluxe": return 2500;
            case "Suite": return 4000;
            default: return 0;
        }
    }

    static void saveBookings() {
        try (PrintWriter out = new PrintWriter("bookings.txt")) {
            for (Booking b : bookings) {
                out.println(b.customerName + "," + b.roomNumber + "," + b.category + "," + b.payment);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static void loadBookings() {
        File file = new File("bookings.txt");
        if (!file.exists()) return;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    int number = Integer.parseInt(parts[1]);
                    String category = parts[2];
                    double pay = Double.parseDouble(parts[3]);
                    bookings.add(new Booking(name, number, category, pay));
                    for (Room r : rooms) {
                        if (r.roomNumber == number) {
                            r.isBooked = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings.");
        }
    }
}
