import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class MovieTest {

    @Test
    void constructorStoresMovieDetails() {
        Movie movie = new Movie(1, "Inception", 2);

        assertEquals(1, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(2, movie.getAvailableCopies());
    }

    @Test 
    void constructorRejectsNegativeAvailableCopies() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Movie(1, "Inception", -1);
    });
    }

    @Test 
    void constructorAcceptsZeroAvailableCopies() {
        Movie movie = new Movie(1, "Inception", 0);
        assertEquals(0, movie.getAvailableCopies());
    }

    @Test 
    void constructorRejectsNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Movie(1, null , 2);
        }); 
    }

    @Test 
    void constructorRejectsWhiteSpaceOnlyTitle() {
        assertThrows( IllegalArgumentException.class, () -> {
            new Movie(1, "   ", 1);
        });
    }

    @Test 
    void constructorRejectsEmptyTitle() {
        assertThrows( IllegalArgumentException.class, () -> {
            new Movie(1, "", 1);
        });
    }

    //Testing that renting one copy reduces availablity by one

    @Test 
    void rentCopyDecreasesAvailableCopiesByOne() {
        Movie movie = new Movie(1, "Inception", 2);

        movie.rentCopy();

        assertEquals(1, movie.getAvailableCopies());
    }

    @Test 
    void rentingCopyWith0AvailableCopiesIsRejected() {
        Movie movie = new Movie(1, "Inception", 0);

        assertThrows(IllegalStateException.class, () -> {
            movie.rentCopy();
        });
    }

    @Test
    void rentCopyAllowsRentingLastAvailableCopy() {
    Movie movie = new Movie(1, "Inception", 1);

    movie.rentCopy();

    assertEquals(0, movie.getAvailableCopies());
    }

    @Test
    void returnCopyIncreasesAvailableCopiesByOne() {
    Movie movie = new Movie(1, "Inception", 0);

    movie.returnCopy();

    assertEquals(1, movie.getAvailableCopies());
    }

    

}