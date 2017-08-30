# Push Authentication Library in JAVA

Push authorization via client device mobile application.

## Requirements

Java 1.6 or later.

## Installation

### IntelliJ IDEA

You'll need to manually install the following JAR: 

* Download [JAR](https://github.com/Vla2yslav/PushAuth/releases/tag/1.0)

* Open IDE

* File - Project Structure - Libraries - "+" - Choose PushAuth.jar.

## Documentation

Please see the [PushAuth API docs](https://pushauth.io/api/index.html) for the most up-to-date documentation.

## Dependencies or Used libraries
-
 
 
## Usage

PushAuthExample.java

```java

public class PushAuthExample {
    public static void main(String[] args){
        //Setting your Public & Private keys
        PushAuth push = new PushAuth("public key", "private key");
        //Setting other params for request
        push.setAddress("client@yourfirm.com");
        push.setModeType("code");
        push.setCode("213");
        push.setFlashResponse(false);


        try{
            //Send Push request to your client device and print
            System.out.println("Request_hash:" + push.send());
            // will return Request Hash
            
            //show your client answer
            System.out.println(push.isAccept());
            // will return true/false or null if answer not received

        }catch(Exception e){
            System.out.println(e.toString());
        }

        try{
                //Show push request information
               System.out.println(push.requestStatus("PushRequestCode"));
               //return array

        }catch(Exception e){
            System.out.println(e.toString());
        }

    }
}

```

### Setting keys

```java
PushAuth push = new PushAuth("public key", "private key");
```


### Sending Push Request

And waiting responce from client:

```java
push.setAddress("client@yourfirm.com");
push.setModeType("push");
push.setFlashResponse(false);
```

Or without client response:

```java
push.setAddress("client@yourfirm.com");
push.setModeType("push");
push.setFlashResponse(true);
```

### Sending Push security code

And waiting responce from client:

```java
push.setAddress("client@yourfirm.com");
push.setModeType("code");
push.setCode("213");
```

### Show push request status

```java
 try{
            //Send Push request to your client device and print
            System.out.println("Request_hash:" + push.send());
            // will return Request Hash
            
            //show your client answer
            System.out.println(push.isAccept());
            // will return true/false or null if answer not received

        }catch(Exception e){
            System.out.println(e.toString());
        }
```

## Documentation

Please see: https://dashboard.pushauth.io/api/index.html

## Support

Please see: https://dashboard.pushauth.io/support/request/create
