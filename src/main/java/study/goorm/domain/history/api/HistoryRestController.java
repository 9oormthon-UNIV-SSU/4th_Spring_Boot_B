package study.goorm.domain.history.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.goorm.domain.history.application.HistoryService;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.global.common.response.BaseResponse;
import study.goorm.global.error.code.status.SuccessStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histories")
@Validated
public class HistoryRestController {

    private final HistoryService historyService;

    @GetMapping("/monthly")
    @Operation(summary = "월별 기록 조회 API", description = "Query Parameter로 clokeyId와 month(YYYY-MM)를 전달해주세요. clokeyId를 생략하면 본인 기록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "기록이 성공적으로 조회되었습니다."),
    })
    public BaseResponse<HistoryResponseDTO.MonthlyHistoryViewResult> getMonthlyHistories(
            @RequestParam(required = false) Long clokeyId,
            @RequestParam String month
    ) {
        HistoryResponseDTO.MonthlyHistoryViewResult result = historyService.getMonthlyHistories(clokeyId, month);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_VIEW_SUCCESS, result);
    }
}