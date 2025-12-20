package org.example.web_service_v2.domain.chat.message;

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
import org.example.web_service_v2.domain.chat.message.dto.ChatMessageResponse;
import org.example.web_service_v2.domain.chat.message.dto.MessageUpdateResponse;
import org.example.web_service_v2.domain.chat.message.dto.SendMessageRequest;
import org.example.web_service_v2.domain.chat.message.service.ChatMessageCommandService;
import org.example.web_service_v2.domain.chat.message.service.ChatMessageQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat - Message", description = "채팅 메시지 조회/폴링/전송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/chat/rooms/{roomId}/messages")
public class ChatMessageController {
    private final ChatMessageQueryService chatMessageQueryService;
    private final ChatMessageCommandService chatMessageCommandService;


    @Operation(
            summary = "메시지 목록 조회(커서 페이징)",
            description = "beforeMessageId가 없으면 최신 size개, 있으면 해당 id보다 이전 메시지 size개를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessageResponse.class)))),
            @ApiResponse(responseCode = "404", description = "채팅방 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @Parameter(description = "채팅방 ID", example = "1", required = true)
            @PathVariable Long roomId,

            @Parameter(description = "이 메시지 ID보다 이전 메시지 조회(옵션)", example = "12345")
            @RequestParam(required = false) Long beforeMessageId,

            @Parameter(description = "가져올 개수(기본 30, 최대 50)", example = "30")
            @RequestParam(defaultValue = "30") int size
    ){
        return ResponseEntity.ok(
                chatMessageQueryService.getMessages(roomId, beforeMessageId, size)
        );
    }

    @Operation(
            summary = "새 메시지 폴링",
            description = "afterMessageId 이후의 새 메시지만 반환합니다. (폴링 전용)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = MessageUpdateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 커서(afterMessageId 누락 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "참여자 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "채팅방 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/updates")
    public ResponseEntity<MessageUpdateResponse> poll(
            @Parameter(description = "채팅방 ID", example = "1", required = true)
            @PathVariable Long roomId,

            @Parameter(description = "내 프로필 ID", example = "3", required = true)
            @RequestParam Long myProfileId,

            @Parameter(description = "이 메시지 ID 이후의 메시지부터 조회", example = "12345", required = true)
            @RequestParam Long afterMessageId
    ){
        return ResponseEntity.ok(
                chatMessageQueryService.getUpdates(roomId, myProfileId, afterMessageId)
        );
    }

    @Operation(
            summary = "메시지 전송",
            description = "참여자만 메시지를 전송할 수 있습니다. 전송 시 채팅방 lastMessage 캐시도 갱신됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ChatMessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "빈 메시지",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "참여자 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "채팅방/프로필 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ChatMessageResponse> send(

            @Parameter(description = "채팅방 ID", example = "1", required = true)
            @PathVariable Long roomId,

            @Parameter(description = "송신자 프로필 ID", example = "3", required = true)
            @RequestParam Long senderProfileId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SendMessageRequest.class),
                            examples = @ExampleObject(
                                    name = "sendMessage",
                                    value = "{\"message\":\"안녕하세요\"}"
                            )
                    )
            )
            @RequestBody SendMessageRequest request
            ){
        return ResponseEntity.ok(
                chatMessageCommandService.send(roomId, senderProfileId, request.getMessage())
        );
    }
}
