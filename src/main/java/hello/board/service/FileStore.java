package hello.board.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStore {

    String uploadFile(MultipartFile file) throws IOException;

    void deleteFile(String filePath) throws IOException;

    String generateFileUrl(String fileName);
}
