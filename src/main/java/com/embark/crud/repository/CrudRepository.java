package com.embark.crud.repository;

import java.util.List;

public interface CrudRepository<T> {

    T createAccount(T t);

    T readAccount(String id);

    T updateAccount(T t);

    T deleteAccount(String id);

    List<T> readAccountList(int page, int size);
}
