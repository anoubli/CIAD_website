<?php
function labmanager_encrypt($key, $iv, $data) {
        if (strlen($key) < 16) {
            $key = str_pad("$key", 16, "0"); //0 pad to len 16
        } else if (strlen($key) > 16) {
            $key = substr("$key", 0, 16); //truncate to 16 bytes
        }
        $encodedEncryptedData = base64_encode(openssl_encrypt($data, 'aes-128-cbc', $key, OPENSSL_RAW_DATA, $iv));
        $encodedIV = base64_encode($iv);
        $encryptedPayload = $encodedEncryptedData.":".$encodedIV;
        return $encryptedPayload;
}

$iv = '4f76476692be7171'; #Same as in JAVA
$key = '76c3299759c8512c'; #Same as in JAVA

$c = labmanager_encrypt($key, $iv, 'mylogin');

print $c;
?>

