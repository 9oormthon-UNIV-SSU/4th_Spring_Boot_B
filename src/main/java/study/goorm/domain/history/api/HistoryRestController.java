package study.goorm.domain.history.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.goorm.domain.history.application.HistoryImageQueryService;
import study.goorm.domain.history.application.HistoryService;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.global.common.response.BaseResponse;
import study.goorm.global.error.code.status.SuccessStatus;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histories-")
@Validated
public class HistoryRestController {

    private final HistoryService historyService;
    private final HistoryImageQueryService historyImageQueryService;

    @GetMapping("/monthly")
    @Operation(
            summary = "특정 회원의 월별 기록 조회 API",
            description = "Query Parameter로 clokeyId와 month를 입력받아 월별 기록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월별 기록이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public BaseResponse<HistoryResponseDTO.MonthlyHistoryDTO> getMonthlyHistories(
            @Parameter(description = "조회할 회원의 Clokey ID, 미입력 시 본인") @RequestParam(name = "clokeyId", required = false) String clokeyId,
            @Parameter(description = "조회할 연-월 (YYYY-MM 형식)") @RequestParam(name = "month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        HistoryResponseDTO.MonthlyHistoryDTO result = historyService.getMonthlyHistories(clokeyId, month);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_MONTHLY_VIEW_SUCCESS, result);
    }

}
