// package com.gamyeon.report.application.service;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.gamyeon.report.application.port.in.GetReportDetailUseCase;
// import com.gamyeon.report.application.port.out.LoadReportPort;
// import com.gamyeon.report.domain.Report;
// import com.gamyeon.report.infrastructure.web.dto.ReportDetailResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// @Service
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// public class GetReportDetailService implements GetReportDetailUseCase {
//
//  private final LoadReportPort loadReportPort;
//  private final ObjectMapper objectMapper;
//
//  @Override
//  public ReportDetailResponse getDetail(Long intvId, Long userId) {
//    // 1. 리포트 조회 및 본인 확인
//    Report report =
//        loadReportPort
//            .findByIntvId(intvId)
//            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리포트입니다."));
//
//    if (!report.getUserId().equals(userId)) {
//      throw new IllegalStateException("해당 리포트에 대한 접근 권한이 없습니다.");
//    }
//
//    // 2. [핵심] JSONB(Map) 데이터를 DTO(ReportDetail)로 변환
//    ReportDetailResponse.ReportDetail detail = null;
//    if (report.getReportData() != null) {
//      detail =
//          objectMapper.convertValue(
//              report.getReportData(), ReportDetailResponse.ReportDetail.class);
//    }
//
//    // 3. 최종 응답 조립
//    return ReportDetailResponse.builder()
//        .intvId(report.getIntvId())
//        .intvStatus("COMPLETED") // 면접 도메인 상태값 (필요 시 주입)
//        .reportStatus(report.getStatus().name())
//        .report(detail)
//        .build();
//  }
// }
