<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
  <head>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script> 
  </head>

  <body>

    <script type="text/javascript"> 
        function login(element_id) {
            var form = document.getElementById(element_id);
            var name = form.name.value;
            var password = form.password.value;
            //var data = {"name": name, "encryptedPwd": password};

            $.ajax({
                  url: '/api/user',
                  type: 'GET',
                  data: {"name": name, "encryptedPwd": password},
                  async: false,
                  success: function( result) {
                    if (!result.encodedKey) {
                        alert(result);
                    } else {
                        setUserCookie(name);           
                        window.location.href="/tasks?username="+ name;
                    }
                }
            }); 
        }

        function newUser(element_id) {
            var form = document.getElementById(element_id);
            var name = form.name.value; 
            var password = form.password.value;
            //var password = document.getElementById("password").value;
            var data = {"name": name, "encryptedPwd": password};
            $.ajax({
                  url: '/api/user',
                  type: 'POST',
                  data: JSON.stringify(data),
                  dataType: "json",
                  contentType: "application/json",
                  async: false,
                  success: function( result) {
                    if (!result.encodedKey) {
                        alert(result);
                    } else {
                        window.location.href="/api/task?username="+ name;
                    }
                }
            });        
        }

        function setUserCookie (userName) {
            document.cookie = "user=" + userName;
        }

    </script>
    
    <!--Login Form-->
    <form id="user_form" action="" method="post" onsubmit="login('user_form'); return false;">
      <input type=text name="name" SIZE=50 VALUE="" >Name <br />
      <input type=text name="password" SIZE=50 VALUE="" >Password<br />
      <div><input type="submit" value="Login" /></div>     
    </form>

    <br />
    <!--New User Form-->
    <form id="new_user_form" action="" method="post" onsubmit="newUser('new_user_form'); return false;">
      <div><input type=text name="name" SIZE=50 VALUE="" >Name</div> 
      <div><input type=password name="password" SIZE=50 VALUE="" >Password</div> 
      <div><input type="submit" value="New User" /></div>     
    </form>
    
  </body>
</html>