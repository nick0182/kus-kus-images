package com.nikolai.kuskusimages.controller;

import com.nikolai.kuskusimages.service.ImageService;
import com.nikolai.kuskusimages.service.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ImagesController {

    private final Map<Type, ImageService> imageServiceMap;

    public ImagesController(List<ImageService> imageServiceList) {
        this.imageServiceMap = imageServiceList
                .stream()
                .collect(Collectors.toMap(ImageService::type, Function.identity()));
    }

    @GetMapping(value = "/api/v1/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public CompletableFuture<byte[]> fetchImage(@RequestParam String type, @RequestParam String id) {
        return imageServiceMap.get(Type.valueOf(type)).fetchImage(id);
    }
}
