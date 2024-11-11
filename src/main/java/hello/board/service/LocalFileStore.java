package hello.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("local")
public class LocalFileStore implements FileStore {

    @Value("${file.dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileName = createFileName(originalFileName);
        String filePath = Paths.get(uploadDir, fileName).toString();

        // 디렉토리가 없으면 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일 저장
        file.transferTo(new File(filePath));

        return fileName;
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        String fullPath = Paths.get(uploadDir, filePath).toString();
        File file = new File(fullPath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("파일 삭제에 실패했습니다: " + fullPath);
            }
        }
    }

    @Override
    public String generateFileUrl(String fileName) {
        return "/upload-images/" + fileName;
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
