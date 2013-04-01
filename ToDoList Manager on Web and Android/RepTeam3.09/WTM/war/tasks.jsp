<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
  <head>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script> 
  </head>

  <body>
 
    <script type="text/javascript"> 

    function addTask(element_id, userId) {
        var form = document.getElementById(element_id);
        var name = form.name.value;
        // The other parameters will be obtained by user, for now I am hardcoding them:
        var note = "Some Note";
        var dueTime = "1200";
        var checked = "false";
        var noDueTime = "false";
        var priority = "0";
        var creationDate = "30";

        $.ajax({
                  url: '/api/task',
                  type: 'POST',
                  data: {"name": name, "userId": userId, "note": note, "dueTime": dueTime, "checked": checked, "noDueTime": noDueTime, "priority": priority, "creationDate": creationDate},
                  dataType: "json",
                  async: false,
                  success: function( result) {
                    if (!result.encodedKey) {
                        alert(result);
                    } else {
                        window.location.reload;
                    }
                }
            });    
    }

    function deleteTask() {
        var allTasksDiv = document.getElementById("allTasks");
        var checkboxes = allTasksDiv.getElementsByTagName("input");

        for(var i = checkboxes.length - 1; i >= 0; i--) {
            if (checkboxes[i].checked) {
              var id = checkboxes[i].id;
              $.ajax({
                  url: '/api/task?encodedkey=' + id,
                  type: 'DELETE',
                  async: false,
                  success: function( result) {
                      removeElement("task_" + id);
                  }
              });
            }

        }        
    }

    function deleteUser(key) {
        $.ajax({
            url: '/api/user?encodedKey=' + key,
            type: 'DELETE',
            async: false,
            success: function( result) {
                window.location.href = "/";
            }
        });
    }

    function removeElement(id) {
        var remove = document.getElementById(id);
        remove.parentNode.removeChild(remove);
    }

    </script>

    <div>Tasks for ${user.name}</div>

    <!--Add Task Form-->
    <form id="add_task_form" action="" method="post" onsubmit="addTask('add_task_form','${user.encodedKey}'); return false;">
      <div><input type=text name="name" SIZE=50 VALUE="" >Name</div> 
      <div><input type="submit" value="Add Task" /></div>     
    </form>

    <!-- All Tasks-->
    <div id="allTasks">
      <c:forEach items="${tasks}" var="task">
        <div id="task_${task.encodedKey}"> 
          <input type="checkbox" id="${task.encodedKey}">${task.name}
        </div>
      </c:forEach>  
    </div>

    <button onclick="deleteTask()">Delete</button>
    <button onclick="deleteUser('${user.encodedKey}')">Delete User</button>


  </body>
</html>