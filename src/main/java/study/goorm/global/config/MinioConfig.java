package study.goorm.global.config;

import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MinioConfig {

//    @Value("${minio.url}")
//    private String url;
//
//    @Value("${minio.url}")
//    private String accessKey;
//
//    @Value("${minio.url}")
//    private String secretKey;
//
//    @Bean
//    public MinioClient minioClient(){
//        return MinioClient.builder()
//                .endpoint(url)
//                .credentials(accessKey,secretKey)
//                .build();
//    }

}
