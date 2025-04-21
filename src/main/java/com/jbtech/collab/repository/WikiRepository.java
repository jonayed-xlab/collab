package com.jbtech.collab.repository;

import com.jbtech.collab.model.Wiki;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiRepository extends JpaRepository<Wiki, Long> {
}