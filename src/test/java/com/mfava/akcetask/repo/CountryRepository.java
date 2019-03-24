package com.mfava.akcetask.repo;

import com.mfava.akcetask.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
