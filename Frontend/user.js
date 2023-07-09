function login() {
    $(document).ready(function() {
        $('#loginBtn').click(function(event) {
            event.preventDefault();

            var id = $('#email').val();
            var pwd = $('#password').val();

            var data = {
                userid: id,
                userpw: pwd
            };

            $.ajax({
                type: 'post',
                url: 'http://localhost:8080/user/signin',
                data: JSON.stringify(data),
                contentType: 'application/JSON',
                success: function(response) {

                    var jwtToken = response.token;

                    localStorage.setItem("jwtToken", jwtToken);

                    console.log("보내진 데이터: "+ JSON.stringify(data));
                    console.log("토큰 값: "+jwtToken);
                    alert("로그인 성공");
                    window.location.href = "index.html";
                },

                error: function(xhr, status, error) {
                    alert("로그인 실패: "+ error ); 
                }

            });
        });
    });
}

function logout() {
    // 로그아웃 버튼
    $('#logoutOk').click(function(event){
        event.preventDefault();

        // jwtToken 값과 isEditing 값이 존재할 수도 있으므로 비워주기
        localStorage.clear();

        window.location.href = "login.html";
    });
}