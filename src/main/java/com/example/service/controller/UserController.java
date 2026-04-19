package com.example.service.controller;

//import ch.qos.logback.core.model.Model;
import com.example.service.constant.UrlConstant;
import com.example.service.dto.WatchRequest;
import com.example.service.dto.request.ForgotPasswordRequestDto;
import com.example.service.dto.request.ResetPasswordRequestDto;
import com.example.service.dto.request.SaveSongRequestDto;
import com.example.service.dto.request.UserCreatRequestDto;
import com.example.service.dto.response.*;
import com.example.service.entity.PasswordResetToken;
import com.example.service.entity.Song;
import com.example.service.entity.SongDocument;
import com.example.service.entity.User;
import com.example.service.repository.PasswordResetTokenRepository;
import com.example.service.repository.SongRepository;
import com.example.service.repository.SongSearchRepository;
import com.example.service.repository.UserRepository;
import com.example.service.service.*;
//import com.example.service.service.impl.EmailService;
import com.example.service.service.impl.JwtServiceImpl;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.core.io.Resource;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongService songService;


    @Autowired
    private RedisTemplate<String, Object> redis;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

//    @Autowired
//    private MusicService musicService;

    @Autowired
    private AuthService authService;

    @Autowired
    JwtServiceImpl jwtService;


    @Autowired
    private EmailService mailService;

    @Autowired
    private final MinioClient minioClient;

    public UserController(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

//    @Autowired
//    private JamendoService jamendoService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongSearchRepository songSearchRepository;


    @Autowired
    EmailService emailService;

    @PostMapping(UrlConstant.API_REGISTER)
    public Object createUser( @RequestBody UserCreatRequestDto request) {
        return userService.createUser(request);
    }


    @GetMapping(UrlConstant.UPLOAD_MINIO_URL)
    public ResponseEntity<UploadUrlResponseDto> getUploadUrl(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String fileName,
            @RequestParam(defaultValue = "false") boolean isThumbnail) throws Exception {

        return ResponseEntity.ok(
                songService.getUploadUrl(authHeader, fileName, isThumbnail)
        );
    }


    @PostMapping(UrlConstant.UPLOAD_MINIO_SAVE)
    public ResponseEntity<SaveSongResponseDto> save(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SaveSongRequestDto request) {

        return ResponseEntity.ok(
                songService.saveSong(authHeader, request)
        );
    }

    @PutMapping(UrlConstant.UPDATE_DB_HLS)
    public ResponseEntity<?> updateHls(@RequestParam Long songId,
                                       @RequestParam String hlsUrl,
                                       HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        System.out.println("CALLBACK FROM IP: " + ip);

        if (!isAllowedIp(ip)) {
            return ResponseEntity.status(403).body("Forbidden IP: " + ip);
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        song.setHlsUrl(hlsUrl);
        song.setStatus("READY");

        songRepository.save(song);

        return ResponseEntity.ok("updated");
    }


    private boolean isAllowedIp(String ip) {

        return ip.equals("127.0.0.1")   // localhost
                || ip.equals("::1")         // IPv6 localhost
                || ip.equals("61.106.154.128") // IP server HLS
                || ip.equals("61.106.148.251"); //IP server HLS
    }
    @CrossOrigin("*")
    @GetMapping(UrlConstant.GET_FILE_LIST)
    public ResponseEntity<List<SongResponseDto>> getAll() {
        return ResponseEntity.ok(songService.listSongs());
    }

    @CrossOrigin("*")
    @GetMapping(UrlConstant.API_GET_SONGS_ID)
    public ResponseEntity<List<SongResponseDto>> getSongsByUserId(@PathVariable Long userId) {

        return ResponseEntity.ok(songService.getSongsByUserId(userId));
    }

    @DeleteMapping(UrlConstant.API_DELETE_ID)
    public ResponseEntity<?> deleteSong(@PathVariable Long id,
                                        HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("No token");
        }

        songService.deleteSong(authHeader, id);

        return ResponseEntity.ok("Deleted");
    }


    @GetMapping(UrlConstant.PLAY_ID)
    public ResponseEntity<?> playHls(@PathVariable Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy song"));

        if (song.getHlsUrl() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Video đang xử lý, chưa sẵn sàng"));
        }

        return ResponseEntity.ok(Map.of(
                "id", song.getId(),
                "title", song.getTitle(),
                "hlsUrl", song.getHlsUrl(),
                "status", song.getStatus()
        ));
    }

    @GetMapping(UrlConstant.API_PROFILE)
    public ResponseEntity<?> getProfile(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("No token");
        }

        String token = authHeader.substring(7);

        Long userId = jwtService.getUserIdFromToken(token);

        // lấy từ DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //trả DTO
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("avatarUrl", user.getAvatarUrl());

        return ResponseEntity.ok(result);
    }


    @GetMapping(UrlConstant.API_UPLOAD_AVATAR)
    public ResponseEntity<?> getUploadAvatarUrl(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String fileName) throws Exception {

        UploadUrlResponseDto res = songService.getUploadAvatarUrl(authHeader, fileName);
        return ResponseEntity.ok(res);
    }

    @PostMapping(UrlConstant.API_SAVE_AVATAR)
    public ResponseEntity<?> saveAvatar(HttpServletRequest request,
                                        @RequestBody Map<String, String> body) {

        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.getUserIdFromToken(token);

        String objectName = body.get("objectName");

        String fileUrl = "https://minio01.beatmusic.click/bucket-jun09dev/" + objectName;

        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setAvatarUrl(fileUrl);
        userRepository.save(user);

        return ResponseEntity.ok("Saved avatar");
    }

//    @GetMapping(UrlConstant.SEARCH)
//    public List<SongDocument> search(@RequestParam String keyword) {
//        return songSearchRepository.findByTitleContaining(keyword);
//    }

    @GetMapping(UrlConstant.SEARCH)
    public List<SongResponseDto> search(@RequestParam String keyword) {

        List<SongDocument> docs = songSearchRepository.searchFuzzy(keyword);

        List<Long> ids = docs.stream()
                .map(SongDocument::getId)
                .toList();

        List<Song> songs = songRepository.findAllById(ids);

        return songs.stream().map(song -> {
            SongResponseDto dto = new SongResponseDto();

            dto.setId(song.getId());
            dto.setTitle(song.getTitle());
            dto.setThumbnailUrl(song.getThumbnailUrl());

            if (song.getUser() != null) {
                dto.setUsername(song.getUser().getUsername());
                dto.setAvatarUrl(song.getUser().getAvatarUrl());
            }

            return dto;
        }).toList();
    }

    @GetMapping(UrlConstant.GET_USER_PROFILE)
    public ResponseEntity<UserProfileResponseDto> getProfile(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        // 🔥 check JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Unauthorized");
        }

        jwtService.validateToken(authHeader.substring(7));

        return ResponseEntity.ok(songService.getUserProfile(userId));
    }



    @PostMapping(UrlConstant.FORGOT_PASSWORD)
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(
            @RequestBody ForgotPasswordRequestDto request) {

        return ResponseEntity.ok(authService.forgotPassword(request));
    }



    @PostMapping(UrlConstant.RESET_PASSWORD)
    public ResponseEntity<ResetPasswordResponseDto> resetPassword(
            @RequestBody ResetPasswordRequestDto request) {

        return ResponseEntity.ok(authService.resetPassword(request));
    }

}
