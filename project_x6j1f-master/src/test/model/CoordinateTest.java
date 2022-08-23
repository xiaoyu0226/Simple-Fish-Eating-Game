package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CoordinateTest {
    private Coordinate coordinate;

    @BeforeEach
    void runBefore() {
        coordinate = new Coordinate(10, 20);
    }

    @Test
    void testCoordinate() {
        assertNotNull(coordinate);
        assertEquals(10, coordinate.getX());
        assertEquals(20, coordinate.getY());
    }
}
