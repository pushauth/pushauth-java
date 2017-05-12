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
<!--
# vim: set tw=79:
-->
