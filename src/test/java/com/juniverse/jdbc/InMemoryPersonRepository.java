package com.juniverse.jdbc;

import com.juniverse.jdbc.dao.PersonRepository;
import com.juniverse.jdbc.dto.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** Repositorio en memoria para probar el menú sin base de datos. */
class InMemoryPersonRepository implements PersonRepository {

    private final Map<Integer, Person> people = new TreeMap<>();
    private int nextId = 1;

    @Override
    public int save(List<Person> list) {
        list.forEach(this::save);
        return list.size();
    }

    @Override
    public boolean save(Person person) {
        person.setId(nextId++);
        people.put(person.getId(), person);
        return true;
    }

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(people.values());
    }

    @Override
    public Optional<Person> findById(int id) {
        return Optional.ofNullable(people.get(id));
    }

    @Override
    public boolean update(Person person) {
        return people.replace(person.getId(), person) != null;
    }

    @Override
    public boolean deleteById(int id) {
        return people.remove(id) != null;
    }
}
