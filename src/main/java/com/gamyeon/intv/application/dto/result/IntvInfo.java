package com.gamyeon.intv.application.dto.result;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvStatus;

public record IntvInfo(Long intvId, String title, IntvStatus status) {

  public static IntvInfo from(Intv intv) {
    return new IntvInfo(intv.getId(), intv.getTitle(), intv.getStatus());
  }
}
