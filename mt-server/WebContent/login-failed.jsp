<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>MOBIL-TAXI</title>
		<style>
			body {
	  			font-family: Arial;
    			padding-top: 20px;
			}

			.container {
			    width: 406px;
			    max-width: 406px;
			    margin: 0 auto;
			}

			#signup {
			    padding: 0px 25px 25px;
			    background: #fff;
			    box-shadow: 
			        0px 0px 0px 5px rgba( 255,255,255,0.4 ), 
			        0px 4px 20px rgba( 0,0,0,0.33 );
			    border-radius: 5px;
			    display: table;
			    position: static;
			}
			
			#signup .header {
			    margin-bottom: 20px;
			}
			
			#signup .header p {
			    color: #8f8f8f;
			    font-size: 14px;
			    font-weight: 300;
			    text-align:center;
			}

			#signup .inputs {
			    margin-top: 25px;
			}

			#signup .inputs #submit {
			    width: 100%;
			    margin-top: 20px;
			    padding: 15px 0;
			    color: #fff;
			    font-size: 14px;
			    font-weight: 500;
			    letter-spacing: 1px;
			    text-align: center;
			    text-decoration: none;
			    background: -moz-linear-gradient(
			        top,
			        #b9c5dd 0%,
			        #a4b0cb);
			    background: -webkit-gradient(
			        linear, left top, left bottom, 
			        from(#b9c5dd),
			        to(#a4b0cb));
			    border-radius: 5px;
			    border: 1px solid #737b8d;
			    box-shadow:
			        0px 5px 5px rgba(000,000,000,0.1),
			        inset 0px 1px 0px rgba(255,255,255,0.5);
			    text-shadow:
			        0px 1px 3px rgba(000,000,000,0.3),
			        0px 0px 0px rgba(255,255,255,0);
			    display: table;
			    position: static;
			    clear: both;
			}

			#signup .inputs #submit:hover {
			    background: -moz-linear-gradient(
			        top,
			        #a4b0cb 0%,
			        #b9c5dd);
			    background: -webkit-gradient(
			        linear, left top, left bottom, 
			        from(#a4b0cb),
			        to(#b9c5dd));
			}
		</style>
	
	</head>
	<body>
		<div class="container" id="signup">
			<div class="header">
				<p>Niepoprawny login lub hasło.</p>
			</div>
			<div class="inputs">
				<a href="/mt-server" id="submit">Zaloguj ponownie</a>
			</div>
		</div>
	</body>
</html>