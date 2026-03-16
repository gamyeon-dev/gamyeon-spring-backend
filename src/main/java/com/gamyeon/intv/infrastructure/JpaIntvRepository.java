package com.gamyeon.intv.infrastructure;

import com.gamyeon.intv.domain.Intv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIntvRepository extends JpaRepository<Intv, Long> {}
