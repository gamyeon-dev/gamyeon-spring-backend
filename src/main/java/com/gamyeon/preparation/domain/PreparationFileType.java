package com.gamyeon.preparation.domain;

public enum PreparationFileType {
  RESUME,
  PORTFOLIO,
  COVER_LETTER;

  private static final long MB = 1024L * 1024L;

  public long maxFileSizeBytes() {
    return switch (this) {
      case RESUME -> 5 * MB;
      case PORTFOLIO, COVER_LETTER -> 10 * MB;
    };
  }

  public boolean isRequired() {
    return this == RESUME;
  }
}
