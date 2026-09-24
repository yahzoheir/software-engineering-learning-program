public class Movie {
    
    private final int id;
    private final String title;
    private int availableCopies;

    public Movie(int id, String title, int availableCopies) {
        if (availableCopies < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie name cannot be null or whitespace only");
        }

        this.id = id;
        this.title = title;
        this.availableCopies = availableCopies;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getAvailableCopies() {
        return this.availableCopies;
    }

    public void rentCopy() {
        if (this.availableCopies == 0) {
            throw new IllegalStateException("There are no available copies of this movie");
        }
        this.availableCopies--;
    }

    public void returnCopy() {
        availableCopies++;
    }

}
