package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import vn.hoidanit.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.annotation.APIMessage;
import vn.hoidanit.jobhunter.util.error.FileUploadException;


@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${venn.upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService ;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @APIMessage("Upload file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(
            @RequestParam(name = "file" , required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, FileUploadException {

        // skip validate
        if ( file == null || file.isEmpty() ) {
            throw new FileUploadException("File trống trơn ... , Vui lòng upload file") ;
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item)) ;

        if ( isValid == false ) {
            throw new FileUploadException("Định dạng file không hợp lệ ... , chỉ chấp nhận file " + allowedExtensions.toString()) ;
        }
        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);

        // store file
        String uploadFile = this.fileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile , Instant.now()) ;



        return ResponseEntity.ok(res);
    }

}
