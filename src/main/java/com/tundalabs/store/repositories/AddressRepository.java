package com.tundalabs.store.repositories;

import com.tundalabs.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}