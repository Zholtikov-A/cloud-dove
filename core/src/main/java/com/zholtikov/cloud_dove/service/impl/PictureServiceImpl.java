package com.zholtikov.cloud_dove.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.zholtikov.cloud_dove.dal.PictureRepository;
import com.zholtikov.cloud_dove.dto.EMailDto;
import com.zholtikov.cloud_dove.enums.SortState;
import com.zholtikov.cloud_dove.kafka.producer.KafkaProducer;
import com.zholtikov.cloud_dove.model.PictureMeta;
import com.zholtikov.cloud_dove.model.User;
import com.zholtikov.cloud_dove.service.PictureService;
import com.zholtikov.cloud_dove.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PictureServiceImpl implements PictureService {

    @Autowired
    PictureRepository pictureRepository;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    UserService userService;
    @Autowired
    S3ServiceImpl s3Service;

    final static Long MAX_PICTURE_SIZE = 10000000L;
    @Value("${file.upload-dir}")
    private String uploadDir;


    @Override
    public String uploadPictures(Long ownerId, List<MultipartFile> files) throws Exception {
        StringBuilder paths = new StringBuilder();
        final Long[] totalSize = {0L};
        files.parallelStream()
                .filter(f -> Objects.equals(f.getContentType(), "image/jpeg") || Objects.equals(f.getContentType(), "image/png"))
                .filter(f -> f.getSize() < MAX_PICTURE_SIZE)
                .forEach(f -> {
                    try {
                        s3Service.putObjectIntoBucket(f);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    paths.append(f.getOriginalFilename()).append("\n");
                    PictureMeta pictureMeta = PictureMeta
                            .builder()
                            .ownerId(ownerId)
                            .fileName(f.getOriginalFilename())
                            .fileSize(f.getSize())
                            .uploaded(LocalDateTime.now())
                            .build();
                    totalSize[0] += f.getSize();
                    pictureRepository.create(pictureMeta);

                });

        User user = userService.findUserById(ownerId);

        EMailDto email = new EMailDto();
        email.setTo(user.getEmail());
        email.setSubject("Pictures uploaded");
        email.setContent("Dear " + user.getName() + ", you have just uploaded pictures on " +
                totalSize[0] + " bytes total value to your cloud.");
        kafkaProducer.sendEMail("mail-topic", email);
        return paths.toString();
    }

    @Override
    public ResponseEntity<byte[]> downloadPictures(Long ownerId, String filename) throws Exception {
        pictureRepository.checkOwnershipByPictureName(ownerId, filename);
        Long fileSize = pictureRepository.getFileSize(ownerId, filename);
        User user = userService.findUserById(ownerId);
        MediaType mediaType;
        S3Object s3Object = s3Service.downloadObjectFromBucket(filename);
        if (s3Object != null) {
            mediaType = MediaType.valueOf(s3Object.getObjectMetadata().getContentType());
        } else {
            log.warn("ERROR! Downloading file filename = " + filename + " failed. No such file in storage!");
            return ResponseEntity.notFound().build();
        }

        byte[] resource = s3Service.convertS3ObjectToByteArray(s3Object);
        EMailDto email = new EMailDto();
        email.setTo(user.getEmail());
        email.setSubject("Picture downloaded");
        email.setContent("Dear " + user.getName() + ", you have just downloaded picture " +
                filename + " on " + fileSize + " bytes total value to your computer.");
        kafkaProducer.sendEMail("mail-topic", email);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    @Override
    public List<PictureMeta> getPicturesListForOwner(Long ownerId, LocalDateTime dateFrom, LocalDateTime dateTo,
                                                     Long sizeFrom, Long sizeTo, Long picIdFrom, Long picIdTo, SortState sortState) {
        return pictureRepository.findPictureMetasForOwner(ownerId, dateFrom, dateTo, sizeFrom, sizeTo, picIdFrom, picIdTo, sortState);
    }

    @Override
    public List<PictureMeta> getPicturesListForModerator(LocalDateTime dateFrom, LocalDateTime dateTo,
                                                         Long sizeFrom, Long sizeTo, Long picIdFrom, Long picIdTo, SortState sortState) {
        return pictureRepository.findPictureMetasForModerator(dateFrom, dateTo, sizeFrom, sizeTo, picIdFrom, picIdTo, sortState);
    }
}
