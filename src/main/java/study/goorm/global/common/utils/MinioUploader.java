package study.goorm.global.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MinioUploader {

//    private final MinioClient minioClient;
//
//    @Value("${minio.bucket}")
//    private String bucket;
//
//    public String uploadImage(MultipartFile file) {
//        String objectName= UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//        try{
//            minioClient.putObject(
//                    putObjectArgs.builder()
//                            .bucket(bucket)
//                            .object(objectName)
//                            .stream(file.getInputStream(),file.getSize(),-1)
//                            .contentType(file.getContentType)
//                            .build()
//            );
//            return minioClient.getPresignedObjectUrl(
//                    GetPresignObjectUrlArgs.builder()
//                            .method(Method.GET)
//                            .bucket(bucket)
//                            .object(objectName)
//                            .expire(60*30)
//                            .build()
//            );
//
//
//
//        }catch(Exception e)
//        {
//            throw new RuntimeException("MiniO 이미지 업로드 실패",e);
//
//        }
//
//    }
}
