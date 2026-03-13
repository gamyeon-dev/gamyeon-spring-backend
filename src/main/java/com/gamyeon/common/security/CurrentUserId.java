package com.gamyeon.common.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * 로그인된 사용자의 userId를 컨트롤러 파라미터에 주입하는 어노테이션.
 *
 * <p>사용 예:
 *
 * <pre>
 *   public ResponseEntity<?> create(@CurrentUserId Long userId, ...) { ... }
 * </pre>
 *
 * JWT 인증 필터에서 SecurityContext에 저장된 principal(Long userId)을 추출합니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentUserId {}
