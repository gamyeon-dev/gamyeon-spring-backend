package com.gamyeon.intv.infrastructure;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IntvRepositoryImpl implements IntvRepository {

  private final JpaIntvRepository jpaIntvRepository;

  @Override
  public Intv save(Intv intv) {
    return jpaIntvRepository.save(intv);
  }

  @Override
  public Optional<Intv> findById(Long id) {
    return jpaIntvRepository.findById(id);
  }
}
