package study.goorm.domain.member.exception;

import study.goorm.global.error.code.BaseErrorCode;
import study.goorm.global.exception.GeneralException;

public class MemberException extends GeneralException {

    public MemberException(BaseErrorCode code) {
        super(code);
    }
}
