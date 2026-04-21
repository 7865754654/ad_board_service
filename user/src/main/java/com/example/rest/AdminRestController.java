package com.example.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.dto.response.AdminResponseDto;
import com.example.dto.response.MessageResponseDto;
import com.example.service.admin.AdminService;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/admin/")
@Tag(name = "Админ панель объявлений", description = "API для управления объявлениями " +
        "только для пользователей с ролью администратора")
public class AdminRestController {
    private AdminService adminService;

    @GetMapping("/user/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователя по логину")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь был найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким логином не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public  ResponseEntity<AdminResponseDto> getByUsername(@PathVariable("username") String username) {
        AdminResponseDto user = adminService.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Был найден список пользователей"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<AdminResponseDto>> getAllUsers() {
        List<AdminResponseDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь был найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AdminResponseDto>  getUserById(@PathVariable("id") Long id) {
        AdminResponseDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Блокировать пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь был найден и заблокирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MessageResponseDto> blockUser(@PathVariable("id") Long id) {
        adminService.blockUser(id);
        return ResponseEntity.ok(new MessageResponseDto(
                true,
                "User successfully blocked",
                id,
                LocalDateTime.now()
        ));
    }

    @PutMapping("/user/unblock/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Разблокировать пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь был найден и разблокирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MessageResponseDto> unblockUser(@PathVariable("id") Long id) {
        adminService.unblockUser(id);
return ResponseEntity.ok(new MessageResponseDto(
        true,
        "User successfully unblocked",
        id,
        LocalDateTime.now()
));
    }

    @PutMapping("/user/{id}/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Добавить роль пользователю по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль пользователю была добавлена"),
            @ApiResponse(responseCode = "404", description = "Не найден пользователь или роль"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MessageResponseDto> addRoleToUser(@PathVariable("id") Long id,
                                                            @PathVariable ("roleName")String roleName) {
        adminService.addRoleToUser(id, roleName);
        return ResponseEntity.ok(new MessageResponseDto(
                true,
                "Role " + roleName + " successfully added to user",
                id,
                LocalDateTime.now()
        ));
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MessageResponseDto> deleteUser(@PathVariable("id") Long id) {
        adminService.delete(id);
        return ResponseEntity.ok(new MessageResponseDto(
                true,
                "User with" + id + " successfully deleted",
                id,
                LocalDateTime.now()
        ));
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить все объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все объявления успешно удалены"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MessageResponseDto> deleteAllAdvertisements() {
        adminService.deleteAllAdvertisement();

        MessageResponseDto response = new MessageResponseDto(
                true,
                "All advertisements successfully deleted",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
