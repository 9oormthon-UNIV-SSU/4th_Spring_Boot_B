package study.goorm.domain.history.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.history.application.HistoryService;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.global.common.response.BaseResponse;
import study.goorm.global.error.code.status.SuccessStatus;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histories")
@Validated
public class HistoryRestController {

    private final HistoryService historyService;

    @GetMapping("/monthly")
    @Operation(
            summary = "월별 기록 조회 API",
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

    @GetMapping("/daily/{historyId}")
    @Operation(summary = "일별 기록 상세 조회 API", description = "PathVariable로 historyId를 입력하면 해당 기록의 상세 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 historyId 또는 접근 권한 없음.")
    })
    public BaseResponse<HistoryResponseDTO.DailyHistoryDTO> getDailyHistory(
            @Parameter(description = "기록 ID") @PathVariable Long historyId
    ) {
        HistoryResponseDTO.DailyHistoryDTO result = historyService.getDailyHistory(historyId);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_DAILY_VIEW_SUCCESS, result);
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "날짜별 옷 기록 추가 API",
            description = "multipart/form-data 형식으로 기록 정보와 이미지 파일들을 함께 전송하여, 특정 날짜의 옷 착용 기록을 생성합니다. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "기록이 성공적으로 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "요청 형식이 잘못되었거나 필수 값이 누락되었습니다."),
            @ApiResponse(responseCode = "404", description = "요청한 멤버 또는 옷이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
    })
    public BaseResponse<HistoryResponseDTO.CreateHistoryResultDTO> createHistory(
            @RequestPart("metadata") @Valid HistoryRequestDTO.CreateHistoryDTO requestDTO,
            @RequestPart("imageFile") List<MultipartFile> imageFiles
    ) {
        HistoryResponseDTO.CreateHistoryResultDTO result = historyService.createHistory(requestDTO, imageFiles);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_CREATED, result);
    }

    @PatchMapping(value = "/{historyId}", consumes = "multipart/form-data")
    @Operation(
            summary = "날짜별 옷 기록 수정 API",
            description = "특정 날짜의 기록을 수정합니다. 이미지와 metadata를 multipart/form-data 형식으로 받습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기록이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청입니다."),
            @ApiResponse(responseCode = "403", description = "타인의 기록을 수정할 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 기록 또는 옷을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
    })
    public BaseResponse<Void> updateHistory(
            @Parameter(description = "수정할 기록 ID") @PathVariable Long historyId,
            @RequestPart("metadata") @Valid HistoryRequestDTO.UpdateHistoryDTO requestDTO,
            @RequestPart("imageFile") List<MultipartFile> imageFiles
    ) {
        historyService.updateHistory(historyId, requestDTO, imageFiles);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_UPDATED, null);
    }

    @DeleteMapping("/{historyId}")
    @Operation(summary = "날짜별 옷 기록 삭제 API", description = "기록과 관련된 모든 연관 데이터를 함께 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기록이 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 기록 ID입니다."),
            @ApiResponse(responseCode = "403", description = "삭제 권한이 없습니다.")
    })
    public BaseResponse<Void> deleteHistory(@PathVariable Long historyId) {
        historyService.deleteHistory(historyId);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_DELETED, null);
    }

    @PostMapping("/like")
    @Operation(summary = "좋아요 상태 토글 API", description = "기존 liked 상태를 기반으로 좋아요/취소를 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 상태가 성공적으로 변경되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public BaseResponse<HistoryResponseDTO.LikeResponseDTO> toggleLike(
            @Valid @RequestBody HistoryRequestDTO.LikeRequestDTO request
    ) {
        HistoryResponseDTO.LikeResponseDTO result = historyService.toggleLike(request);
        return BaseResponse.onSuccess(SuccessStatus.HISTORY_LIKE_UPDATED, result);
    }


}
