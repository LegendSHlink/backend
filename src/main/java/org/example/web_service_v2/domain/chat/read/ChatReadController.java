package org.example.web_service_v2.domain.chat.read;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.read.dto.MarkReadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat - Read", description = "읽음 처리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/chat/rooms/{roomId}/read")
public class ChatReadController {

    private final ChatReadCommandService chatReadCommandService;

    @Operation(
            summary = "읽음 처리",
            description = "사용자가 실제로 확인한 마지막 메시지 ID(lastReadMessageId)를 저장합니다. unreadCount 계산 기준입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 lastReadMessageId(다른 방 메시지 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "참여자 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "채팅방/프로필 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> markRead(
            @Parameter(description = "채팅방 ID", example = "1", required = true)
            @PathVariable Long roomId,

            @Parameter(description = "내 프로필 ID", example = "3", required = true)
            @RequestParam Long myProfileId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MarkReadRequest.class),
                            examples = @ExampleObject(
                                    name = "markRead",
                                    value = "{\"lastReadMessageId\":12346}"
                            )
                    )
            )
            @RequestBody MarkReadRequest request
            ){
        chatReadCommandService.markAsRead(roomId, myProfileId, request.getLastReadMessageId());
        return ResponseEntity.ok().build();
    }
}
