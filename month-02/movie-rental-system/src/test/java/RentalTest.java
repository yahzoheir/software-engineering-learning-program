import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RentalTest {

    @Test 
    void constructorStoresRentalDetails() {
        Customer customer = new Customer(1, "Yahia");
        Movie movie = new Movie(2, "Inception", 2);
        Rental rental = new Rental(3, customer, movie);

        assertEquals(3, rental.getId());
        assertSame(movie, rental.getMovie());
        assertSame(customer, rental.getCustomer());
    }

    @Test 
    void newRentalIsNotReturned() {
        Customer customer = new Customer(1, "Yahia");
        Movie movie = new Movie(2, "Inception", 2);
        Rental rental = new Rental(3, customer, movie);

        assertFalse(rental.isReturned());

    }
    
    @Test 
    void markReturnedChangesRentalToReturned() {
        Customer customer = new Customer(1, "Yahia");
        Movie movie = new Movie(2, "Inception", 2);
        Rental rental = new Rental(3, customer, movie);

        rental.markReturned();

        assertTrue(rental.isReturned());

    }

    @Test
    void markReturnedRejectsAlreadyReturnedRental() {
        Customer customer = new Customer(1, "Yahia");
        Movie movie = new Movie(2, "Inception", 2);
        Rental rental = new Rental(3, customer, movie);
        rental.markReturned();

        assertThrows(IllegalStateException.class, () -> {
            rental.markReturned();
        });

        assertTrue(rental.isReturned());
    }
}
