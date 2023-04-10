![socketapi_header](https://user-images.githubusercontent.com/30220428/230950356-b75bcc70-3c49-4913-ab40-8beaf349be37.png)

# SocketAPI

SocketAPI is a simple library for building client-server communication applications using Java sockets. 
It allows clients to connect to a server and register channels to receive messages on, 
as well as send messages to specific channels.

# Getting Started

## Create the Server

The Server class is used to create a socket server and manage client connections. 
Here's an example of how to create a server that listens for incoming connections:

```ruby
Server server = new Server(8080);
```

## Closing the Server

Finally, when you're done using the server, you should close it:

```ruby
server.close();
```

## Create the Clients

To use SocketAPI, you need to first create a server and a client. 
Here's an example of how to create a client and connect it to a server:

```ruby
Client client = new Client("localhost", 8080);
```

### Registering Channels

Once you've connected to the server, you can register channels to receive messages on. 
Here's an example of how to register a channel:

```ruby
client.registerChannel("test", message -> System.out.println("Received message: " + message));
```

### Sending Messages

To send a message to a channel, you can use the sendMessage method. 
Here's an example of how to send a message to the "test" channel:

```ruby
client.sendMessage("test", "Hello, world!");
```

### Closing the Client

Finally, when you're done using the client, you should close the connection to the server:

```ruby
client.close();
```

## Example

Here's an example of how to use SocketAPI to build a simple chat application:

```ruby
public class ChatServer {
    public static void main(String[] args) {
        Server server = new Server(8080);

        server.start();
    }
}
```

```ruby
public class ChatClient {
    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);

        client.registerChannel("chat", message -> System.out.println(message));

        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            client.sendMessage("chat", message);
        }

        client.close();
    }
}
```
