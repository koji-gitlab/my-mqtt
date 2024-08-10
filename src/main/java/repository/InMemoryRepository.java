package repository;

import model.Item;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository implements Repository<Item> {
    private final Map<Integer, Item> storage = new HashMap<>();

    @Override
    public void create(Item item) {
        storage.put(item.getId(), item);
    }

    @Override
    public Item read(int id) {
        return storage.get(id);
    }

    @Override
    public void update(Item item) {
        storage.put(item.getId(), item);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
