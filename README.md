# Push Authentication Library

Library help you here [pushauth.zenlix.com](http://pushauth.zenlix.com/).

## Requirements

Java 1.6 or later.

## Installation

### IntelliJ IDEA

You'll need to manually install the following JAR: 

* Downolad [JAR](https://github.com/Vla2yslav/PushAuth/releases/tag/1.0)

* Open IDE

* File - Project Structure - Libraries - "+" - Choose PushAuth.jar.

## Documentation

Please see the [PushAuth API docs](http://pushauth.zenlix.com/docs/app/) for the most up-to-date documentation.

## Usage

PushAuthExample.java

```java

public class PushAuthExample {
    public static void main(String[] args){
        PushAuth push = new PushAuth("public key", "private key");
        push.setAddress("client@yourfirm.com");
        push.setModeType("code");
        push.setCode("213");
        push.setFlashResponse(false);


        try{

            System.out.println("Request_hash:" + push.send());
            System.out.println(push.isAccept());

        }catch(Exception e){
            System.out.println(e.toString());
        }

        try{

            System.out.println(push.requestStatus("PushRequestCode"));

        }catch(Exception e){
            System.out.println(e.toString());
        }

    }
}

```

See the project's [functional tests]() for more examples.

<!--
# vim: set tw=79:
-->
