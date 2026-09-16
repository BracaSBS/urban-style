package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}