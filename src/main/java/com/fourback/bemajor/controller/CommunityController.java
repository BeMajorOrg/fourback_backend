package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.dto.*;
import com.fourback.bemajor.jwt.JWTUtil;
import com.fourback.bemajor.service.BoardService;
import com.fourback.bemajor.service.FavoriteService;
import com.fourback.bemajor.service.ImageService;
import com.fourback.bemajor.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CommunityController {
    private static String UPLOAD_DIR = "uploads/";

    private final PostService postService;
    private final BoardService boardService;
    private final FavoriteService favoriteService;
    private final ImageService imageService;


    @ResponseBody
    @PostMapping("/api/post")
    public String postCreate(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("boardId") Long boardId,
                             Principal principal,
                             @RequestParam(value = "images", required = false) MultipartFile[] images){
        String oauth2Id = principal.getName();
        PostDto postDto = new PostDto();
        postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setBoardId(boardId);
        postService.create(postDto,oauth2Id,images);
        return "ok";
    }


    @GetMapping("/api/post")
    public List<PostListDto> posts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "boardId", defaultValue = "1") Long boardId,
            Principal principal
    ) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,
                "createdDate"));
        String oauth2Id = principal.getName();

        return postService.posts(pageRequest,boardId,oauth2Id);
    }

    @GetMapping("/api/post2")
    public List<PostListDto> posts2(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "boardId", defaultValue = "1") Long boardId,
            Principal principal
    ) {
        Sort sort = null;



        if(boardId == 1) {
            sort = Sort.by(Sort.Direction.DESC, "createdDate");
        } else if(boardId == 2 || boardId == 3) {
            sort = Sort.by(Sort.Direction.DESC, "post.createdDate");
        } else if(boardId == 4) {
            sort = Sort.by(Sort.Direction.DESC, "goodCount");
        }

        PageRequest pageRequest = PageRequest.of(page, pageSize,
                sort);
        String oauth2Id = principal.getName();

        return postService.posts2(pageRequest,boardId,oauth2Id);
    }

    @GetMapping("/api/post/{id}")
    public PostUpdateDto post(@PathVariable("id") Long postId) {

        return postService.updatePostGet(postId);
    }

    @PatchMapping("/api/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") Long postId,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam(value = "images", required = false) MultipartFile[] images) {
        return postService.update(postId,title,content,images);

    }

    @PatchMapping("/api/post/{id}/view")
    public ResponseEntity<String> viewCountUp(@PathVariable("id") Long postId)  {
        return postService.viewCountUp(postId);

    }

    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long postId) {
        try {
            postService.delete(postId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete post");
        }

    }

    @GetMapping("/api/post/search")
    public List<PostListDto> postSearch(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
            @RequestParam(value = "keyword") String keyword,
            Principal principal
    ) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,
                "createdDate"));
        String oauth2Id = principal.getName();
        return postService.postSearch(pageRequest,keyword,oauth2Id);
    }

    @GetMapping("/api/board")
    public List<BoardDto>  boards(Principal principal) {
        String oauth2Id = principal.getName();

        return boardService.boards(oauth2Id);
    }

    @PostMapping("/api/board/favorite")
    public String favoriteBoard(@RequestBody FavoriteDto favoriteDto,Principal principal) {
        String oauth2Id = principal.getName();
        favoriteService.add(favoriteDto,oauth2Id);
        return "ok";
    }

    @PostMapping("/api/post/{id}/favorite")
    public String favoritePost(@PathVariable("id") Long postId,Principal principal) {
        String oauth2Id = principal.getName();
        favoriteService.post(postId,oauth2Id);
        return "ok";
    }




    @GetMapping("/images/{name}")
    public ResponseEntity<Resource> getImage(@PathVariable("name") String filename) {
        try {
            String uploadDir = "uploads/";;

            // 파일 경로 생성 및 정규화
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();;

            // 파일 리소스 생성
            Resource resource = new UrlResource(filePath.toUri());


            if (resource.exists() || resource.isReadable()) {
                // 파일이 존재하고 읽을 수 있을 경우 파일을 응답 본문에 포함
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // 파일이 존재하지 않거나 읽을 수 없을 경우 404 상태 코드 반환
                return ResponseEntity.status(404).body(null);
            }
        } catch (MalformedURLException e) {
            // URL 형식이 잘못된 경우 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/api/images")
    public ResponseEntity<String> deleteImage(@RequestBody List<String> fileNames) {
        return imageService.delete(fileNames);

    }







}
