package org.example.web_service_v2.domain.chat.room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.room.dto.ChatRoomListItemResponse;
import org.example.web_service_v2.domain.chat.room.dto.ChatRoomResponse;
import org.example.web_service_v2.domain.chat.room.dto.CreateChatRoomRequest;
import org.example.web_service_v2.domain.chat.room.service.ChatRoomCommandService;
import org.example.web_service_v2.domain.chat.room.service.ChatRoomQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat - Room", description = "채팅방 생성/조회 및 목록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/chat/rooms")
public class ChatRoomController {
    private final ChatRoomCommandService chatRoomCommandService;
    private final ChatRoomQueryService chatRoomQueryService;

    @Operation(summary = "채팅방 생성 또는 조회", description = "1:1 채팅방을 pairKey 기준으로 생성하거나 기존 방을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "프로필 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "동시 생성 충돌",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(
            @Parameter(description = "내 프로필 ID", example = "3", required = true)
            @RequestParam Long myProfileId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateChatRoomRequest.class),
                            examples = @ExampleObject(
                                    name = "createRoom",
                                    value = "{\"otherProfileId\":10}"
                            )
                    )
            )
            @RequestBody CreateChatRoomRequest request
            ){
        return ResponseEntity.ok(
                chatRoomCommandService.createOrGet(myProfileId,
                        request.getOtherProfileId()
                )
        );
    }

    @Operation(summary = "내 채팅방 목록", description = "좌측 채팅방 리스트용. lastMessage/unreadCount 포함")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatRoomListItemResponse.class)))),
            @ApiResponse(responseCode = "404", description = "프로필 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ChatRoomListItemResponse>> myRooms(
            @Parameter(description = "내 프로필 ID", example = "3", required = true)
            @RequestParam Long myProfileId
    ){
        return ResponseEntity.ok(chatRoomQueryService.getMyRooms(myProfileId));
    }
}
