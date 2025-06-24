package study.goorm.domain.history.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.goorm.domain.history.application.HistoryService;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.global.common.response.BaseResponse;
import study.goorm.global.error.code.status.SuccessStatus;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histories")
@Validated
public class HistoryRestController {
    private final HistoryService historyService;

    @GetMapping("/monthly")
    @Operation(summary ="월별 기록을 조회하는 API",description = "Query Parameter로 clokeyId와 month(YYYY-MM)를 전달해주세요. clokeyId를 생략하면 본인기록을 조회합니다.")
    @ApiResponses({ @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "OK, 성공적으로 조회되었습니다.")})
    public BaseResponse<HistoryResponseDTO.HistoryMonthlyViewResult> getMonthlyHistoryView(
            @RequestParam(required = false) String clokeyId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ){
        HistoryResponseDTO.HistoryMonthlyViewResult result=historyService.getMonthlyHistoryView(clokeyId,month);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_MONTHLY_VIEW_SUCCESS, result);
    }

    @GetMapping("/{history-id}")
    @Operation(summary ="일별 기록을 조회하는 API",description = "Path Variable로 historyId를 던져주세요.")
    @ApiResponses({ @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "OK, 성공적으로 조회되었습니다.")})
    public BaseResponse<HistoryResponseDTO.HistoryDailyViewResult> getDailyHistoryView(
            @PathVariable(name="history-id") Long historyId
    ){
        HistoryResponseDTO.HistoryDailyViewResult result=historyService.getDailyHistoryView(historyId);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_DAILY_VIEW_SUCCESS, result);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "기록을 추가하는 API",description = "request body에 HistoryCreateRequest 형식의 데이터를 전달해주세요.")
    @ApiResponses({ @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_201", description = "CREATED, 기록이 성공적으로 추가되었습니다."),})
    public BaseResponse<HistoryResponseDTO.HistoryCreateResult> createHistory(
            @RequestPart("historyCreateRequest")HistoryRequestDTO.HistoryCreateRequest historyCreateRequest

    ){
        HistoryResponseDTO.HistoryCreateResult result=historyService.createHistory(historyCreateRequest);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_CREATED,result);
    }



    @PatchMapping(value = "/{history-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "기록을 수정하는 API",description = "request body에 HistoryUpdateRequest 형식의 데이터를 전달해주세요.")
    @ApiResponses({ @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "UPDATED, 기록이 성공적으로 수정되었습니다."),})
    public BaseResponse<HistoryResponseDTO.HistoryUpdateResult> updateHistory(
            @RequestPart("historyUpdateRequest")HistoryRequestDTO.HistoryUpdateRequest historyUpdateRequest

            ){
        HistoryResponseDTO.HistoryUpdateResult result=historyService.updateHistory(historyUpdateRequest);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_UPDATED,result);
    }

    @DeleteMapping("{history-id}")
    @Operation(summary="특정 기록을 삭제하는 API",description = "path variable로 history_id를 넘겨주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "기록이 성공적으로 삭제되었습니다."),
    })
    @Parameters({
            @Parameter(name = "history-id", description = "기록의 id, path variable 입니다.")
    })
    public BaseResponse<Void> deleteHistory(
            @PathVariable(value = "history-id") Long historyId
    ) {

        historyService.deleteHistory(historyId);

        return BaseResponse.onSuccess(SuccessStatus.HISTORY_DELETED, null);
    }


}
