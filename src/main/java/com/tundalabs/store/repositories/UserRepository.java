package com.tundalabs.store.repositories;

import com.tundalabs.store.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
