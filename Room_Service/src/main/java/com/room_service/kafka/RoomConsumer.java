package com.room_service.kafka;
//
//import com.room_service.service.RoomService;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RoomConsumer {
//
//    private final RoomService roomService;
//
//    public RoomConsumer(RoomService roomService) {
//        this.roomService = roomService;
//    }
//
//    @KafkaListener(topics = "room-booking-topic", groupId = "room-service-group")
//    public void consume(String message) {
//        System.out.println("📥 Received message: " + message);
//
//        try {
//            if (message.startsWith("RoomBooked:")) {
//                String[] parts = message.split(",");
//                Long roomId = Long.parseLong(parts[0].split(":")[1]);
//
//                Long userId = null;
//                if (parts.length > 1 && parts[1].startsWith("User:")) {
//                    userId = Long.parseLong(parts[1].split(":")[1]);
//                }
//
//                roomService.updateRoomStatus(roomId, "BOOKED", userId != null ? "UserID:" + userId : "SYSTEM");
//
//                System.out.println("✅ Room " + roomId + (userId != null ? " booked by User " + userId : ""));
//            }
//        } catch (Exception e) {
//            System.err.println("⚠️ Failed to process Kafka message: " + message);
//            e.printStackTrace();
//        }
//    }
//}


import com.room_service.service.RoomService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RoomConsumer {

    private final RoomService roomService;

    public RoomConsumer(RoomService roomService) {
        this.roomService = roomService;
    }


    @KafkaListener(topics = "room-booking-topic", groupId = "room-service-group")
    public void consume(String message) {
        System.out.println("📥 Received Kafka message: " + message);

        try {
            if (message.startsWith("RoomBooked:")) {
                String[] parts = message.split(",");
                Long roomId = Long.parseLong(parts[0].split(":")[1]);

                Long userId = null;
                if (parts.length > 1 && parts[1].startsWith("User:")) {
                    userId = Long.parseLong(parts[1].split(":")[1]);
                }

                roomService.updateRoomStatus(
                        roomId,
                        "BOOKED",
                        userId != null ? "UserID:" + userId : "SYSTEM"
                );

                System.out.println("✅ Room " + roomId +
                        (userId != null ? " booked by User " + userId : " booked by SYSTEM"));
            }

            else if (message.startsWith("RoomReleased:")) {
                Long roomId = Long.parseLong(message.split(":")[1]);

                roomService.updateRoomStatus(
                        roomId,
                        "AVAILABLE",
                        "SYSTEM"
                );

                System.out.println("♻️ Room " + roomId + " set to AVAILABLE via Kafka");
            }

        } catch (Exception e) {
            System.err.println("⚠️ Failed to process Kafka message: " + message);
            e.printStackTrace();
        }
    }

}

