Messaging and Voice Calling App - Spring Boot Backend
A fully functional Spring Boot backend for a real-time messaging and voice calling application with WebRTC support.

Features
🔐 User Management
Automatic UUID Generation: Unique user ID generation on first installation

Secure Device Storage: UUID stored securely on client devices

User Registration: Backend registration using unique UUIDs

💬 Real-time Messaging
WebSocket-based Communication: Real-time text messaging using Spring WebSocket

Offline Message Storage: Encrypted message storage for offline users

Delivery Acknowledgments: Real-time message delivery status

End-to-End Encryption: Optional AES encryption for message security

📞 Voice Calling
WebRTC Integration: Peer-to-peer voice calls

Signaling Server: WebSocket-based signaling for offer/answer/ICE candidate exchange

TURN Server Support: NAT traversal configuration for difficult network conditions

Secure Calls: WebRTC built-in DTLS-SRTP encryption

👥 User Presence
Online/Offline Status: Real-time user presence tracking

Status Notifications: Notify users when contacts come online/offline

Offline Message Queue: Store messages until recipients come online

Technology Stack
Java 17+

Spring Boot 2.5+

Spring WebSocket - Real-time communication

Spring Data JPA - Database operations

H2/MySQL/PostgreSQL - Message and user storage

WebRTC - Peer-to-peer voice calls

TLS/SSL - Secure WebSocket connections (wss://)
