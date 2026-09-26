
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private final int id;
    private final String name;
    private final List<Rental> activeRentals = new ArrayList<>();


    public Customer(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException ("Name cannot be null or blank");
        }
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Rental> getActiveRentals() {
        return Collections.unmodifiableList(activeRentals);
    }
    
    public void addActiveRental(Rental rental) {
        this.activeRentals.add(rental);
    }

    public void removeActiveRental(Rental rental) {
        this.activeRentals.remove(rental);
    }
}
