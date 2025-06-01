package study.goorm.domain.history.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.history.application.HistoryService;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.global.common.response.BaseResponse;
import study.goorm.global.error.code.status.SuccessStatus;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histories")
@Validated
public class HistoryRestController {

    private final HistoryService historyService;

    @PatchMapping(value = "/{history-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "기록 수정", description = "특정 날짜의 기록을 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "기록이 성공적으로 수정되었습니다."),
    })
    @Parameters({
            @Parameter(name = "history-id", description = "수정하고자 하는 기록의 Id, path variable 입니다.")
    })
    public BaseResponse<HistoryResponseDTO.HistoryUpdateResult> updateHistory(
            @PathVariable(name = "history-id") Long historyId,
            @RequestPart("metadata") @Valid HistoryRequestDTO.HistoryUpdateRequest historyUpdateRequest,
            @RequestPart("images") List<MultipartFile> images,
            @RequestParam("memberId") Long memberId
    ) {

        HistoryResponseDTO.HistoryUpdateResult result = historyService.updateHistory(historyId, historyUpdateRequest, images, memberId);

        return BaseResponse.onSuccess(SuccessStatus.HISTORY_UPDATE_SUCCESS, result);
    }
    @DeleteMapping("/{history-id}")
    @Operation(summary = "기록 삭제", description = "특정 기록을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HISTORY_200", description = "기록이 성공적으로 삭제되었습니다."),
    })
    @Parameters({
            @Parameter(name = "history-id", description = "삭제하고자 하는 기록의 Id, path variable 입니다.")
    })
    public BaseResponse<Void> deleteHistory(
            @PathVariable(name = "history-id") Long historyId,
            @RequestParam("memberId") Long memberId // 임시로 RequestParam 사용
    ) {

        historyService.deleteHistory(historyId, memberId);

        return BaseResponse.onSuccess(SuccessStatus.HISTORY_DELETE_SUCCESS, null);
    }
}