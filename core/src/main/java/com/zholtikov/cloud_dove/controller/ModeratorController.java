package com.zholtikov.cloud_dove.controller;

import com.zholtikov.cloud_dove.enums.SortState;
import com.zholtikov.cloud_dove.enums.UserStatus;
import com.zholtikov.cloud_dove.model.PictureMeta;
import com.zholtikov.cloud_dove.service.ModeratorService;
import com.zholtikov.cloud_dove.service.PictureService;
import com.zholtikov.cloud_dove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "Moderators", description = "Requests for moderators")
@Validated
@RequestMapping("/moderators")
@Slf4j
public class ModeratorController {

    private final ModeratorService moderatorService;
    private final PictureService pictureService;


    public ModeratorController(ModeratorService moderatorService, PictureService pictureService, UserService userService) {
        this.moderatorService = moderatorService;
        this.pictureService = pictureService;
    }

    @GetMapping("/list")
    @SecurityRequirement(name = "BasicAuth")
    @Operation(summary = "Get a list of picture of matching filters and sorting. Requires moderator authentication")
    public List<PictureMeta> getPicturesList(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateFrom,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateTo,
                                             @RequestParam(required = false) Long sizeFrom,
                                             @RequestParam(required = false) Long sizeTo,
                                             @RequestParam(required = false) Long picIdFrom,
                                             @RequestParam(required = false) Long picIdTo,
                                             @RequestParam(required = false) SortState sortState) {
        log.info("Get request to endpoint GET \"/moderators/list\" dateFrom = " + dateFrom + ", dateTo = " + dateTo +
                ", sizeFrom = " + sizeFrom + ", sizeTo = " + sizeTo + ", picIdFrom = " + picIdFrom + ", picIdTo = "
                + picIdTo + ", sortState = " + sortState);
        return pictureService.getPicturesListForModerator(dateFrom, dateTo, sizeFrom, sizeTo, picIdFrom, picIdTo, sortState);
    }

    @PatchMapping("/block/{username}/{status}")
    @SecurityRequirement(name = "BasicAuth")
    @Operation(summary = "Block a user. Requires moderator authentication")
    public void blockUser(@PathVariable("username") @NotNull String username,
                          @PathVariable("status") @NotNull UserStatus status) {
        log.info("Get request to endpoint PATCH \"/moderators/block\" username = " + username + " status = " + status);
        moderatorService.changeStatus(username, status);
    }


}
