# Peer-to-Peer-file-Transferring
a peer to peer file transferring project
Server sends requested file via UDP protocol, Client can request to broadcast a file
Client sends the name of requested file to server. Since UDP pachet size is limited, for transferring big files we need to segment the file.size of each segment is 128 byte and first byte is considered as offset and indicates counter of that packet.
