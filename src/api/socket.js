import { over } from "stompjs";
import SockJS from "sockjs-client";

let stompClient = null;

export const connectSocket = (onMessageReceived) => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = over(socket);

  stompClient.connect({}, () => {
    console.log("✅ WebSocket connected");
    stompClient.subscribe("/topic/jobs", (msg) => {
      const body = JSON.parse(msg.body);
      onMessageReceived(body);
    });
  });
};

export const disconnectSocket = () => {
  if (stompClient) {
    stompClient.disconnect(() => console.log("❌ WebSocket disconnected"));
  }
};
