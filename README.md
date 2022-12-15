# Volley_Upload_Photo, PHp + Mysql+ Android+ Java+ Volley

## upload.php
```
<?php

require("conexion.php");

$conn = retornarConexion();

if (!empty($_POST['image'])){
	$path='images/'.date("d-m-y").'-'.time().'-'.rand(10000,100000).'.jpg';
	if (file_put_contents($path, base64_decode($_POST['image']))){
		$sql= "insert into images (path) values(".$path.") ";
		if(mysqli_query($conn,$sql)){
			echo 'success';	
		}else{
			echo 'Failed to insert to database';
		}
	}else echo 'Failed to upload';
}else echo 'No image found';
?>
```

## conexion.php
```
<?php

function retornarConexion(){
	$servername = "  ";
	$username = " ";
	$password = " ";
	$dbname = " ";

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	// Check connection
	if ($conn->connect_error) {
	  die("Connection failed: " . $conn->connect_error);
	}
	return $conn;
}

?>

```

## images.sql

```
CREATE TABLE `images` (
  `id` int(11) NOT NULL,
  `path` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
## Source & Bib
* How to Upload Image to Server in Android Studio
* https://www.youtube.com/watch?v=YMUGyIjV8Yw


