package study.goorm.global.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.global.config.MinioConfig;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String uploadImage(MultipartFile file) {
        try {
            // 파일명 생성 (UUID + 원본 파일명)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // MinIO에 파일 업로드
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object("history/" + fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // 업로드된 파일의 URL 반환
            return minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/history/" + fileName;

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }
}