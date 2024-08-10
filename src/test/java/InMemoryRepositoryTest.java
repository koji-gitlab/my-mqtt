import model.Item;
import org.junit.Before;
import org.junit.Test;
import repository.InMemoryRepository;

import static org.junit.Assert.*;

public class InMemoryRepositoryTest {

    private InMemoryRepository repository;

    @Before
    public void setUp() {
        repository = new InMemoryRepository();
    }

    @Test
    public void testCreateAndRead() {
        Item item = new Item();
        item.setId(1);
        item.setName("Sensor1");
        item.setValue(45.67);

        repository.create(item);
        Item result = repository.read(1);

        assertNotNull(result);
        assertEquals("Sensor1", result.getName());
        assertEquals(45.67, result.getValue(), 0);
    }

    @Test
    public void testUpdate() {
        Item item = new Item();
        item.setId(1);
        item.setName("Sensor1");
        item.setValue(45.67);

        repository.create(item);

        Item updatedItem = new Item();
        updatedItem.setId(1);
        updatedItem.setName("SensorUpdated");
        updatedItem.setValue(46.00);

        repository.update(updatedItem);
        Item result = repository.read(1);

        assertNotNull(result);
        assertEquals("SensorUpdated", result.getName());
        assertEquals(46.00, result.getValue(), 0);
    }

    @Test
    public void testDelete() {
        Item item = new Item();
        item.setId(1);
        item.setName("Sensor1");
        item.setValue(45.67);

        repository.create(item);
        repository.delete(1);
        Item result = repository.read(1);

        assertNull(result);
    }

}
