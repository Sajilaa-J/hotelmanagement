package com.booking_service.client;

//import com.booking_service.security.RoomFeignConfig;
import com.booking_service.security.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name = "room-service", url = "http://localhost:8083/api/rooms")
//public interface RoomClient {
//    @PutMapping("/{roomId}/status")
//    ResponseEntity<String> updateRoomStatus(@PathVariable("roomId") Long roomId,
//                                            @RequestParam("status") String status);
//
//}
//@FeignClient(name = "room-service", url = "http://localhost:8083")
//public interface RoomClient {
//
//    @PutMapping("/api/rooms/{roomId}/status")
//    ResponseEntity<String> updateRoomStatus(@PathVariable("roomId") Long roomId,
//                                            @RequestParam("status") String status,
//                                            @RequestHeader("Authorization") String token);
//}

@FeignClient(
        name = "room-service",
        url = "http://localhost:8083",
       configuration = FeignClientConfig.class
)
public interface RoomClient {

    @PutMapping("/api/internal/rooms/{roomId}/status")
    void updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam String status,
            String system);
}

