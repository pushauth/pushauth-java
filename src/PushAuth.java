import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class PushAuth {
    private String SERVER_ADDRESS = "http://api.pushauth.zenlix.com";
    private String publicKey;
    private String privateKey;
    private String address;
    private String modeType;
    private boolean response;
    private boolean flashResponse;
    private String code;

    public PushAuth(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public void setFlashResponse(boolean response) {this.flashResponse = response;}

    public void setAddress(String address) {this.address = address;}

    public void setModeType(String modeType) {this.modeType = modeType;}

    public void setCode(String code) {this.code = code;}

    public boolean isAccept(){
        return this.response;
    }

    private String base64_encode(String str){
        String asB64 = Base64.getEncoder().encodeToString(str.getBytes());
        return asB64;
    }

    private byte[] base64_decode(String str){
        byte[] asBytes = Base64.getDecoder().decode(str);
        return asBytes;
    }

    private String hash_hmac(String str, String secret) throws Exception{
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        String hash = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(str.getBytes()));
        return hash;
    }

    private String encrypt(String str)throws Exception{
        return base64_encode(str) + "." + hash_hmac(base64_encode(str), this.privateKey);
    }

    private boolean checkSignature(String data, String clientSign)throws Exception{
        String serverSign = hash_hmac(data, this.privateKey);
        return serverSign.equalsIgnoreCase(clientSign);
    }

    private JSONObject decrypt(String str) throws Exception{
        String[] data = str.split("\\.");
        String body = data[0];
        String sign = data[1];
        if (!checkSignature(body, sign)){
            throw new Exception("Error signature");
        }
        String dataNorm = new String (base64_decode(body), "utf-8");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(dataNorm);
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj;
    }

    private String pushUrl(String to){
        switch (to){
            case "send":
                return "/push/send";
            case "status":
                return "/push/status";
        }
        return null;
    }

    private String request(String data, String to)throws Exception{
        OutputStreamWriter wr;
        BufferedReader rd;
        StringBuilder sb = null;
        String line, message;
        int code;
        String pushUrl = pushUrl(to);
        HttpURLConnection con = (HttpURLConnection) new URL(this.SERVER_ADDRESS+pushUrl).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content_Type", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setConnectTimeout(100000);
        con.connect();
        wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(data);
        wr.flush();
        code = con.getResponseCode();
        message = con.getResponseMessage();
        if (code != 200){
            System.out.println(message);
            throw new Exception();
        }
        else {
            System.out.println("Code: "+code);
            System.out.println("Message: "+message);
        }
        rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer result = new StringBuffer();
        while((line = rd.readLine()) != null){
            result.append(line);
        }
        wr.close();
        rd.close();
        return result.toString();
    }

    private JSONObject parseResult(String data)throws Exception{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(data);
        JSONObject jsonObj = (JSONObject) obj;
        return decrypt((String) jsonObj.get("data"));
    }

    private String showResultStatus(String request)throws Exception{
        JSONObject data = parseResult(request);
        return data.toString();
    }

    private String showResult(String data)throws Exception{
        JSONObject jsonObject = parseResult(data);
        try{
        response = (Boolean) jsonObject.get("answer");}
        catch(NullPointerException e){
            System.out.println("Answer is null");
        }
        return jsonObject.get("req_hash").toString();
    }

    public String send()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("addr_to", this.address);
        jsonObject.put("mode", this.modeType);
        jsonObject.put("code", this.code);
        jsonObject.put("flash_response", this.flashResponse);
        String hashStr = encrypt(jsonObject.toJSONString());
        JSONObject jsobj = new JSONObject();
        jsobj.put("pk", this.publicKey);
        jsobj.put("data", hashStr);
        String request = request(jsobj.toJSONString(), "send");
        return showResult(request);
    }

    public String requestStatus(String req_hash) throws Exception{
        JSONObject data = new JSONObject();
        data.put("req_hash", req_hash);
        String hashStr = encrypt(data.toJSONString());
        JSONObject pk = new JSONObject();
        pk.put("pk", this.publicKey);
        pk.put("data", hashStr);
        String request = request(pk.toJSONString(), "status");
        return showResultStatus(request);
    }
}
