package com.zholtikov.cloud_dove.controller;


import com.zholtikov.cloud_dove.authentication.IAuthenticationFacade;
import com.zholtikov.cloud_dove.dto.UserInputDto;
import com.zholtikov.cloud_dove.enums.SortState;
import com.zholtikov.cloud_dove.exception.ValidationErrorResponseCustom;
import com.zholtikov.cloud_dove.model.PictureMeta;
import com.zholtikov.cloud_dove.model.User;
import com.zholtikov.cloud_dove.service.PictureService;
import com.zholtikov.cloud_dove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
@Slf4j
@Tag(name = "Users", description = "Requests for users")
public class UserController {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final UserService userService;
    private final PictureService pictureService;


    public UserController(UserService userService, PictureService pictureService) {
        this.userService = userService;
        this.pictureService = pictureService;
    }


    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration of a new user. Doesn't require authentication")
    public User createUser(@Valid @RequestBody UserInputDto userInputDto) {
        log.info("Get request to endpoint POST \"/users/registration\" ");
        return userService.createUser(userInputDto);
    }

    @PostMapping(path = "/upload")
    @SecurityRequirement(name = "BasicAuth")
    @Operation(summary = "Upload list of pictures (*.jpg or *.png) to cloud. Requires user authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation = ValidationErrorResponseCustom.class), mediaType = "application/json")})
    })
    public ResponseEntity<String> uploadPictures(@RequestParam("file") List<MultipartFile> files) throws Exception {
        Authentication authentication = authenticationFacade.getAuthentication();
        Long ownerId = userService.getUsersIdIfExist(authentication.getName());
        log.info("Get request to endpoint POST \"/users/upload\" ownerId = " + ownerId );
        return ResponseEntity.ok("Uploaded pictures: \n" + pictureService.uploadPictures(ownerId, files));
    }

    @GetMapping("/list")
    @SecurityRequirement(name = "BasicAuth")
    @Operation(summary = "Get a list of picture matching filters and sorting for pictures' owner. Requires user authentication")
    public List<PictureMeta> getPicturesList(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateFrom,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateTo,
                                             @RequestParam(required = false) Long sizeFrom,
                                             @RequestParam(required = false) Long sizeTo,
                                             @RequestParam(required = false) Long picIdFrom,
                                             @RequestParam(required = false) Long picIdTo,
                                             @RequestParam(required = false) SortState sortState) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Long ownerId = userService.getUsersIdIfExist(authentication.getName());
        log.info("Get request to endpoint GET \"/users/list\" dateFrom = " + dateFrom + ", dateTo = " + dateTo +
                ", sizeFrom = " + sizeFrom + ", sizeTo = " + sizeTo + ", picIdFrom = " + picIdFrom + ", picIdTo = "
                + picIdTo + ", sortState = " + sortState);
        return pictureService.getPicturesListForOwner(ownerId, dateFrom, dateTo, sizeFrom, sizeTo, picIdFrom, picIdTo, sortState);
    }

    @GetMapping("/download/{filename}")
    @SecurityRequirement(name = "BasicAuth")
    @Operation(summary = "Download a picture from cloud by picture's owner. Requires user authentication")
    public ResponseEntity<byte[]> downloadPictures(@PathVariable String filename) throws Exception {
        Authentication authentication = authenticationFacade.getAuthentication();
        Long ownerId = userService.getUsersIdIfExist(authentication.getName());
        log.info("Get request to endpoint GET \"/users/upload\" ownerId = " + ownerId );
        return pictureService.downloadPictures(ownerId, filename);
    }

}


