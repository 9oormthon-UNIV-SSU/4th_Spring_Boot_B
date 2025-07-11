package study.goorm.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import study.goorm.global.error.code.BaseErrorCode;
import study.goorm.global.error.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //Cloth
    NO_SUCH_CLOTH(HttpStatus.BAD_REQUEST,"CLOTH_4001","옷이 존재하지 않습니다"),

    //bug data
    NO_ClOTH_IMAGE(HttpStatus.BAD_REQUEST,"CLOTH_4002","옷의 사진이 존재하지 않습니다."),

    //Member
    NO_SUCH_MEMBER(HttpStatus.BAD_REQUEST,"MEMBER_4001","멤버가 존재하지 않습니다."),

    //Page
    PAGE_UNDER_ONE(HttpStatus.BAD_REQUEST,"PAGE_4001","페이지는 1이상으로 입력해야 합니다."),
    PAGE_SIZE_UNDER_ONE(HttpStatus.BAD_REQUEST,"PAGE_4002","페이지 사이즈는 1이상으로 입력해야합니다."),

    NO_SUCH_CATEGORY(HttpStatus.BAD_REQUEST,"CLOTH_4003","카테고리가 존재하지 않습니다."),

    LOWER_TEMP_BIGGER_THAN_UPPER_TEMP(HttpStatus.BAD_REQUEST,"CLOTH_4004","옷의 하한 온도가 상한 온도 보다 높습니다."),


    //History
    NO_SUCH_HISTORY(HttpStatus.NOT_FOUND, "HISTORY_4002","존재하지 않는 기록ID입니다"),
    //hyowon
    INVALID_HISTORY_DATE_FORTMAT(HttpStatus.BAD_REQUEST,"HISTORY_4001","잘못된 날짜 형식입니다."),
    EMPTY_HISTORY_IMAGE(HttpStatus.BAD_REQUEST,"HISTORY_4004","기록의 이미지는 필수입니다."),
    EMPTY_CLOTH(HttpStatus.BAD_REQUEST,"CLOTH_4005","등록할 옷이 비어있습니다."),
    DUPLICATE_CLOTH(HttpStatus.BAD_REQUEST,"CLOTH_4006","중복되는 옷이 있습니다."),
    INVALID_CLOTH(HttpStatus.BAD_REQUEST,"CLOTH_4007","존재하지 않거나 소유하지 않은 옷id입니다."),

    NO_AUTHORITY_HISTORY(HttpStatus.FORBIDDEN,"HISTORY_4006","기록에 접근 권한이 없습니다."),
    NO_SUCH_HISTORY_MEMBER(HttpStatus.BAD_REQUEST,"MEMBER_4001","존재하지 않는 ID멤버입니다."),
    CONTENT_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST,"HISTORY_4007","내용은 200자 초과 불과합니다"),
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST,"HISTORY_4008","이미지는 최대 10장까지만 업로드할 수 있습니다."),


    //Like
    LIKE_STATE_MISMATCH(HttpStatus.BAD_REQUEST,"LIKE_4001","유효하지 않은 좋아요상태입니다"),
    LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST,"LIKE_4002","Like를 찾지 못했습니다."),

    //HASHTAG
    EMPTY_HASHTAGS(HttpStatus.BAD_REQUEST,"HASHTAG_4001","해쉬태그가 없습니다."),
    DUPLICATE_HASHTAGS(HttpStatus.BAD_REQUEST,"HASHTAG_4002","중복되는 해시태그가 있습니다."),


    COMMENT_CONTENT_EMPTY(HttpStatus.BAD_REQUEST,"COMMENT_4001","댓글이 비어있습니다."),
    COMMENT_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST,"COMMENT_4002","댓글 글자수 초과입니다."),
    ;





    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}

