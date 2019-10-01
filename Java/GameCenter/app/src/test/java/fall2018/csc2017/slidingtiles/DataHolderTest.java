package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataHolderTest {

    @Test
    public void save() {
        DataHolder.getInstance().save("1", 123);
        assertEquals(123, DataHolder.getInstance().retrieve("1"));

        DataHolder.getInstance().save("1", "game");
        assertEquals("game", DataHolder.getInstance().retrieve("1"));

        DataHolder.getInstance().save("2", "Slide");
        assertEquals("Slide", DataHolder.getInstance().retrieve("2"));
    }

}