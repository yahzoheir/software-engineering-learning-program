import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    void constructorStoresCustomerDetails() {
        Customer customer = new Customer(1, "Yahia");

        assertEquals(1, customer.getId());
        assertEquals("Yahia", customer.getName());
    }

    @Test 
    void constructorRejectsNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
         new Customer(1, null) ;
        });
    }

    @Test 
    void constructorRejectsWhiteSpaceName() {
        assertThrows(IllegalArgumentException.class, () -> {
         new Customer(1, "  ") ;
        });
    }

    @Test 
    void constructorRejectsEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
         new Customer(1, "") ;
        });
    }

    @Test 
    void newlyCreatedCustomerHasNoActiveRentals() {
        Customer customer = new Customer(1, "Yahia");

        assertTrue(customer.getActiveRentals().isEmpty());
    }

    @Test 
    void getActiveRentalsPreventsExternelAddition() {
        Customer customer = new Customer(1, "Yahia");
        assertThrows(UnsupportedOperationException.class, () -> {
            customer.getActiveRentals().add(new Rental());
        });

        assertTrue(customer.getActiveRentals().isEmpty());
    }

    @Test 
    void addActiveRentalStoresRentalInCustomerList() {
        Customer customer = new Customer(0, "Yahia");
        Rental rental = new Rental();
        customer.addActiveRental(rental);

        assertEquals(1, customer.getActiveRentals().size());
    }

    @Test 
    void removeActiveRentalRemovesRentalFromCustomerList() {
        Customer customer = new Customer(0, "Yahia");
        Rental rental = new Rental();
        customer.addActiveRental(rental);
        assertEquals(1, customer.getActiveRentals().size());
        customer.removeActiveRental(rental);
        assertEquals(0, customer.getActiveRentals().size());
    }

    @Test 
    void removeActiveRentalPreservesOtherRentals() {
        Customer customer = new Customer(0, "Yahia");
        Rental rental1 = new Rental();
        Rental rental2 = new Rental();
        customer.addActiveRental(rental1);
        customer.addActiveRental(rental2);
        customer.removeActiveRental(rental2);
        assertEquals(1, customer.getActiveRentals().size());
        assertSame(rental1, customer.getActiveRentals().get(0));

    }

    @Test 
    void removeActiveRentalLeavesListUnchangedWhenRentalIsAbsent() {
        Customer customer = new Customer(0, "Yahia");
        Rental rental1 = new Rental();
        Rental rental2 = new Rental();
        customer.addActiveRental(rental1);
        customer.removeActiveRental(rental2);
        assertEquals(1, customer.getActiveRentals().size());
        assertSame(rental1, customer.getActiveRentals().get(0));
    }



}