package ru.hogwarts.school.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarProcessingException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    @Value("${application.avatars-dir-name}")
   // private String avatarsDirName;
     private final Path path;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    /*public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }*/

    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         @Value("${application.avatars-dir-name}") String avatarsDirName) {
        this.avatarRepository = avatarRepository;
        path = Path.of(avatarsDirName);
        this.studentRepository = studentRepository;
    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile multipartFile) {
        logger.info("Was invoked method for \"uploadAvatar\"");
        try {
            /*Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID() + "." + extension);
            Files.createDirectories(avatarPath.getParent());
            Files.deleteIfExists(avatarPath);
                    InputStream is = multipartFile.getInputStream();
                    OutputStream os = Files.newOutputStream(avatarPath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);

                bis.transferTo(bos);

            Avatar avatar =avatarRepository.findByStudent_Id(studentId)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setFilePath(avatarPath.toString());
            avatar.setFileSize(multipartFile.getSize());
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setData(multipartFile.getBytes());
            avatarRepository.save(avatar);*/


           // Path path = Path.of(avatarsDirName);

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() ->{
                        logger.error("There is not student with id = " + studentId);
                        return new StudentNotFoundException(studentId);
                    });
            byte[] data = multipartFile.getBytes();
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID() + "." + extension);
            Files.write(avatarPath, data);
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setFilePath(avatarPath.toString());
            avatar.setFileSize(data.length);
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setData(data);
            avatarRepository.save(avatar);
        } catch (IOException e) {
            logger.error("Blowout exception \"AvatarProcessingException\"");
            throw new AvatarProcessingException();
        }
    }

    public Pair<byte[], String> getAvatarFromDb(long studentId) {
        logger.info("Was invoked method for \"getAvatarFromDb\"");
        Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                .orElseThrow(() ->{
                    logger.error("There is not student with id = " + studentId);
                    return new StudentNotFoundException(studentId);
                });
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> getAvatarFromFs(long studentId) {
        logger.info("Was invoked method for \"getAvatarFromFs\"");
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseThrow(() ->{
                        logger.error("There is not student with id = " + studentId);
                        return new StudentNotFoundException(studentId);
                    });
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            logger.error("Blowout exception \"AvatarProcessingException\"");
            throw new AvatarProcessingException();
        }
    }

    public List<Avatar> getAllAvatarsForPage(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for \"getAllAvatarsForPage\"");
        if(pageNumber==0){
            logger.error("Division by \"0\", in method \"getAllAvatarsForPage\"");
            throw new IllegalArgumentException();
        }
        PageRequest pageRequest = PageRequest.of(pageNumber-1,pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
