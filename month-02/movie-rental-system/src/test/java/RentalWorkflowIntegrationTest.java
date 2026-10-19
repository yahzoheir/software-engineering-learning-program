import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RentalWorkflowIntegrationTest {

    @Test
    void customersCompeteForLastCopyAndReturnReleasesIt() {
        RentalService service = new RentalService();
        Customer customer1 = service.registerCustomer("Yahia");
        Customer customer2 = service.registerCustomer("Younes");
        Movie movie = service.addMovie("Inception", 1);
        Rental rental1 = service.rentMovie(customer1.getId(), movie.getId());

        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customer2.getId(), movie.getId());
        });

        assertEquals(0, movie.getAvailableCopies());
        assertEquals(1, customer1.getActiveRentals().size());
        assertEquals(0, customer2.getActiveRentals().size());

        service.returnMovie(rental1.getId());
        Rental rental2 = service.rentMovie(customer2.getId(), movie.getId());

        assertEquals(0, movie.getAvailableCopies());
        assertEquals(0, customer1.getActiveRentals().size());
        assertEquals(1, customer2.getActiveRentals().size());
        assertSame(rental2, customer2.getActiveRentals().get(0));
        assertTrue(rental1.isReturned());
        assertFalse(rental2.isReturned());
    }

    @Test
    void customerCanRentSameMovieAgainAfterReturningIt() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 1);
        Rental firstRental = service.rentMovie(customer.getId(), movie.getId());
        service.returnMovie(firstRental.getId());

        Rental secondRental = service.rentMovie(customer.getId(), movie.getId());

        assertNotEquals(firstRental.getId(), secondRental.getId());
        assertSame(firstRental, service.getRentalById(firstRental.getId()));
        assertSame(secondRental, service.getRentalById(secondRental.getId()));
        assertTrue(firstRental.isReturned());
        assertFalse(secondRental.isReturned());
        assertEquals(1, customer.getActiveRentals().size());
        assertSame(secondRental, customer.getActiveRentals().get(0));
        assertEquals(0, movie.getAvailableCopies());
    }

    @Test
    void returningMiddleRentalKeepsOtherActiveRentalsInOrder() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie1 = service.addMovie("Inception", 1);
        Movie movie2 = service.addMovie("Fast and Furious", 1);
        Movie movie3 = service.addMovie("Interstellar", 1);
        Rental rental1 = service.rentMovie(customer.getId(), movie1.getId());
        Rental rental2 = service.rentMovie(customer.getId(), movie2.getId());
        Rental rental3 = service.rentMovie(customer.getId(), movie3.getId());

        service.returnMovie(rental2.getId());

        assertEquals(2, customer.getActiveRentals().size());
        assertSame(rental1, customer.getActiveRentals().get(0));
        assertSame(rental3, customer.getActiveRentals().get(1));
        assertEquals(0, movie1.getAvailableCopies());
        assertEquals(1, movie2.getAvailableCopies());
        assertEquals(0, movie3.getAvailableCopies());
    }

    @Test
    void rejectedOperationsDoNotAffectOtherCustomersOrMovies() {
        RentalService service = new RentalService();
        Customer customerA = service.registerCustomer("Yahia");
        Customer customerB = service.registerCustomer("Younes");
        Movie movie1 = service.addMovie("Inception", 1);
        Movie movie2 = service.addMovie("Fast and Furious", 0);
        Movie movie3 = service.addMovie("Interstellar", 2);
        Rental rental1 = service.rentMovie(customerA.getId(), movie1.getId());
        Rental rental2 = service.rentMovie(customerA.getId(), movie3.getId());

        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customerB.getId(), movie1.getId());
        });
        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customerB.getId(), movie2.getId());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            service.returnMovie(99);
        });

        assertEquals(2, customerA.getActiveRentals().size());
        assertSame(rental1, customerA.getActiveRentals().get(0));
        assertSame(rental2, customerA.getActiveRentals().get(1));
        assertEquals(0, customerB.getActiveRentals().size());
        assertEquals(0, movie1.getAvailableCopies());
        assertEquals(0, movie2.getAvailableCopies());
        assertEquals(1, movie3.getAvailableCopies());
        assertFalse(rental1.isReturned());
        assertFalse(rental2.isReturned());
        assertThrows(IllegalArgumentException.class, () -> {
            service.getRentalById(3);
        });
    }

    @Test
    void rejectedRentalCreatesNoRentalRecord() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie unavailableMovie = service.addMovie("Inception", 0);
        Movie movie1 = service.addMovie("Fast and Furious", 1);
        Movie movie2 = service.addMovie("Interstellar", 1);
        Movie movie3 = service.addMovie("The Matrix", 1);
        Movie movie4 = service.addMovie("Gladiator", 1);

        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customer.getId(), unavailableMovie.getId());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            service.getRentalById(1);
        });

        Rental rental1 = service.rentMovie(customer.getId(), movie1.getId());
        Rental rental2 = service.rentMovie(customer.getId(), movie2.getId());
        Rental rental3 = service.rentMovie(customer.getId(), movie3.getId());
        assertEquals(1, rental1.getId());
        assertEquals(2, rental2.getId());
        assertEquals(3, rental3.getId());

        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customer.getId(), movie4.getId());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            service.getRentalById(4);
        });
        assertEquals(1, movie4.getAvailableCopies());

        service.returnMovie(rental1.getId());
        Rental rental4 = service.rentMovie(customer.getId(), movie4.getId());

        assertEquals(4, rental4.getId());
        assertSame(rental4, service.getRentalById(4));
    }

}
