import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RentalServiceTest {

    @Test 
    void registerCustomerCreatesCustomerWithIdAndName() {
        RentalService service = new RentalService();

        Customer customer = service.registerCustomer("Yahia");

        assertEquals(1, customer.getId());
        assertEquals("Yahia", customer.getName());
    }


    @Test 
    void registerCustomerAssignsConsecutiveIds() {
        RentalService service = new RentalService();
        Customer customer1 = service.registerCustomer("Yahia");
        Customer customer2 = service.registerCustomer("Younes");

        assertEquals(1, customer1.getId());
        assertEquals(2, customer2.getId());
    }

    @Test 
    void getCustomerByIdReturnsRegisteredCustomer() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");

        assertSame(customer, service.getCustomerById(1));
    }

    @Test 
    void getCustomerByIdRejectsUnknownId() {
        RentalService service = new RentalService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.getCustomerById(3);
        });
    }

    @Test 
    void registerCustomerRejectsNullOrBlankName() {
        RentalService service = new RentalService();
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.registerCustomer(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.registerCustomer("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.registerCustomer("  ");
        });
    }

    @Test
    void registerCustomerDoesNotConsumeIdWhenNameIsInvalid() {
    RentalService service = new RentalService();

    assertThrows(IllegalArgumentException.class, () -> {
        service.registerCustomer(null);
    });

    Customer customer = service.registerCustomer("Yahia");

    assertEquals(1, customer.getId());
    }

    @Test 
    void addMovieCreatesMovieWithIdTitleAndAvailableCopies() {
        RentalService service = new RentalService(); 

        Movie movie = service.addMovie("Inception", 2);

        assertEquals(1, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(2, movie.getAvailableCopies());
    }

    @Test 
    void addMovieAssignsConsecutiveIds() {
        RentalService service = new RentalService(); 

        Movie movie1 = service.addMovie("Inception", 2);
        Movie movie2 = service.addMovie("Fast and Furious", 4);
       assertEquals(1, movie1.getId());
       assertEquals(2, movie2.getId());
    }

    @Test 
    void getMovieByIdReturnsAddedMovie() {
        RentalService service = new RentalService();

        Movie movie1 = service.addMovie("Inception", 2);
        assertSame(movie1, service.getMovieById(1)); 
    }

    @Test 
    void getMovieByIdRejectsUnknownId() {
        RentalService service = new RentalService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.getMovieById(1);
        });
    }

    @Test
    void addMovieRejectsNullEmptyAndBlankTitle() {
    RentalService service = new RentalService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie(null, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie("", 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie("   ", 2);
        });
    }

    @Test 
    void addMovieRejectsNegativeAvailableCopies() {
        RentalService service = new RentalService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie("Inception", -1);
        });
    }

    @Test
    void addMovieDoesNotConsumeIdWhenAvailableCopiesAreInvalid() {
    RentalService service = new RentalService();

    assertThrows(IllegalArgumentException.class, () -> {
        service.addMovie("Inception", -1);
    });

    Movie movie = service.addMovie("Inception", 2);

    assertEquals(1, movie.getId());
    }

    @Test
    void addMovieAllowsZeroAvailableCopies() {
        RentalService service = new RentalService();

        Movie movie = service.addMovie("Inception", 0);

        assertEquals(0, movie.getAvailableCopies());
    }
    
    @Test 
    void rentMovieCreatesRentalForCustomerAndMovie() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        service.addMovie("Fast and Furious", 1);
        Movie movie = service.addMovie("Inception", 2);

        Rental rental = service.rentMovie(customer.getId(), movie.getId());

        assertEquals(1, rental.getId());
        assertEquals(customer, rental.getCustomer());
        assertEquals(movie, rental.getMovie());
    }

    @Test
    void rentMovieDecreasesAvailableCopiesByOne() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);

        service.rentMovie(customer.getId(), movie.getId());

        assertEquals(1, movie.getAvailableCopies());
    }

    @Test
    void rentMovieAddsRentalToCustomerActiveRentals() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);

        Rental rental = service.rentMovie(customer.getId(), movie.getId());

        assertEquals(1, customer.getActiveRentals().size());
        assertSame(rental, customer.getActiveRentals().get(0));
    }

    @Test
    void rentMovieAssignsConsecutiveRentalIds() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);

        Rental rental1 = service.rentMovie(customer.getId(), movie.getId());
        Rental rental2 = service.rentMovie(customer.getId(), movie.getId());

        assertEquals(1, rental1.getId());
        assertEquals(2, rental2.getId());
    }

    @Test
    void rentMovieRejectsUnknownCustomer() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            service.rentMovie(99, movie.getId());
        });

        assertEquals(2, movie.getAvailableCopies());
        assertEquals(1, service.rentMovie(customer.getId(), movie.getId()).getId());
    }

    @Test
    void rentMovieRejectsUnknownMovie() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            service.rentMovie(customer.getId(), 99);
        });

        assertEquals(0, customer.getActiveRentals().size());
        assertEquals(1, service.rentMovie(customer.getId(), movie.getId()).getId());
    }

    @Test
    void rentMovieRejectsMovieWithNoAvailableCopies() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie unavailableMovie = service.addMovie("Inception", 0);
        Movie availableMovie = service.addMovie("Fast and Furious", 1);

        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customer.getId(), unavailableMovie.getId());
        });

        assertEquals(0, unavailableMovie.getAvailableCopies());
        assertEquals(0, customer.getActiveRentals().size());
        assertEquals(1, service.rentMovie(customer.getId(), availableMovie.getId()).getId());
    }

    @Test
    void rentMovieRejectsFourthActiveRental() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie1 = service.addMovie("Inception", 1);
        Movie movie2 = service.addMovie("Fast and Furious", 1);
        Movie movie3 = service.addMovie("Interstellar", 1);
        Movie movie4 = service.addMovie("The Matrix", 1);
        service.rentMovie(customer.getId(), movie1.getId());
        service.rentMovie(customer.getId(), movie2.getId());
        service.rentMovie(customer.getId(), movie3.getId());

        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customer.getId(), movie4.getId());
        });

        assertEquals(1, movie4.getAvailableCopies());
        assertEquals(3, customer.getActiveRentals().size());
        Customer otherCustomer = service.registerCustomer("Younes");
        assertEquals(4, service.rentMovie(otherCustomer.getId(), movie4.getId()).getId());
    }

    @Test
    void getRentalByIdReturnsRentedRental() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        Rental rental = service.rentMovie(customer.getId(), movie.getId());

        assertSame(rental, service.getRentalById(rental.getId()));
    }

    @Test
    void getRentalByIdRejectsUnknownId() {
        RentalService service = new RentalService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.getRentalById(1);
        });
    }

    @Test
    void returnMovieMarksRentalAsReturned() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        Rental rental = service.rentMovie(customer.getId(), movie.getId());

        service.returnMovie(rental.getId());

        assertTrue(rental.isReturned());
    }

    @Test
    void returnMovieIncreasesAvailableCopiesByOne() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        Rental rental = service.rentMovie(customer.getId(), movie.getId());
        assertEquals(1, movie.getAvailableCopies());

        service.returnMovie(rental.getId());

        assertEquals(2, movie.getAvailableCopies());
    }

    @Test
    void returnMovieRemovesRentalFromCustomerActiveRentals() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        Rental rental = service.rentMovie(customer.getId(), movie.getId());

        service.returnMovie(rental.getId());

        assertEquals(0, customer.getActiveRentals().size());
    }

    @Test
    void returnMovieKeepsReturnedRentalInHistory() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        Rental rental = service.rentMovie(customer.getId(), movie.getId());

        service.returnMovie(rental.getId());

        assertSame(rental, service.getRentalById(rental.getId()));
    }

    @Test
    void returnMovieRejectsUnknownRentalId() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        service.rentMovie(customer.getId(), movie.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            service.returnMovie(99);
        });

        assertEquals(1, movie.getAvailableCopies());
        assertEquals(1, customer.getActiveRentals().size());
    }

    @Test
    void returnMovieRejectsAlreadyReturnedRental() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie = service.addMovie("Inception", 2);
        Rental rental = service.rentMovie(customer.getId(), movie.getId());
        service.returnMovie(rental.getId());

        assertThrows(IllegalStateException.class, () -> {
            service.returnMovie(rental.getId());
        });

        assertEquals(2, movie.getAvailableCopies());
        assertEquals(0, customer.getActiveRentals().size());
        assertTrue(rental.isReturned());
    }

    @Test
    void returnMovieLeavesOtherRentalsUntouched() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie1 = service.addMovie("Inception", 1);
        Movie movie2 = service.addMovie("Fast and Furious", 1);
        Rental rental1 = service.rentMovie(customer.getId(), movie1.getId());
        Rental rental2 = service.rentMovie(customer.getId(), movie2.getId());

        service.returnMovie(rental1.getId());

        assertEquals(1, customer.getActiveRentals().size());
        assertSame(rental2, customer.getActiveRentals().get(0));
        assertFalse(rental2.isReturned());
        assertEquals(0, movie2.getAvailableCopies());
    }

    @Test
    void returnMovieFreesCustomerRentalSlot() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");
        Movie movie1 = service.addMovie("Inception", 1);
        Movie movie2 = service.addMovie("Fast and Furious", 1);
        Movie movie3 = service.addMovie("Interstellar", 1);
        Movie movie4 = service.addMovie("The Matrix", 1);
        Rental rental1 = service.rentMovie(customer.getId(), movie1.getId());
        service.rentMovie(customer.getId(), movie2.getId());
        service.rentMovie(customer.getId(), movie3.getId());
        assertThrows(IllegalStateException.class, () -> {
            service.rentMovie(customer.getId(), movie4.getId());
        });

        service.returnMovie(rental1.getId());
        Rental rental4 = service.rentMovie(customer.getId(), movie4.getId());

        assertEquals(3, customer.getActiveRentals().size());
        assertSame(rental4, customer.getActiveRentals().get(2));
        assertEquals(1, movie1.getAvailableCopies());
        assertEquals(0, movie4.getAvailableCopies());
    }

}
