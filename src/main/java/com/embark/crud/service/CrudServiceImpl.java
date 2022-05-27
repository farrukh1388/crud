package com.embark.crud.service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import com.embark.crud.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrudServiceImpl<T> implements CrudService<T> {

    private final CrudRepository<T> crudRepository;

    @Override
    public T createAccount(T t) {
        return crudRepository.createAccount(t);
    }

    @Override
    public T readAccount(String id) {
        return crudRepository.readAccount(id);
    }

    @Override
    public T updateAccount(T t) {
        return crudRepository.updateAccount(t);
    }

    @Override
    public T deleteAccount(String id) {
        return crudRepository.deleteAccount(id);
    }

    @Override
    public List<T> readAccountList(int page, int size) {
        return crudRepository.readAccountList(page, size);
    }
}
