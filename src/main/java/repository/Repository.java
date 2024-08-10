package repository;

public interface Repository<T> {
    void create(T item);
    T read(int id);
    void update(T item);
    void delete(int id);
}
