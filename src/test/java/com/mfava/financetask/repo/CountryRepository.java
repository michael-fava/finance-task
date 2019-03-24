package com.mfava.financetask.repo;

import com.mfava.financetask.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
