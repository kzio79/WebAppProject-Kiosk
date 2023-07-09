/*
    @ id - list

    // 상품관련
    item_code: 상품 ID
    item_name: 상품 이름
    item_cate: 상품 카테고리
    item_price: 상품 가격
    item_desc: 상품 설명
    itemTable: 상품 목록 테이블

    // 버튼관련
    logoutOk: 로그아웃 모달 안의 버튼
    deleteConfirm: 삭제 모달 안의 버튼
    edit: 수정하기 버튼
    (
        editBtnImg: 수정버튼 이미지
        editText: 수정 버튼 텍스트
        dropdownMenuLink: 사진 드롭다운 버튼
    )
    goBack: 뒤로가기 버튼



    @ 변수
    isEditing: 화면 전환을 위한 변수 설정값(기본값 false)
    original_data: 데이터베이스에 저장된 값(보통 초기값)
    new_data: 수정모드 진입 후 수정완료 버튼 클릭 시 영역 별 값들 담는 데이터
    codeValue: 테이블에서 아이템 선택 시 선택한 아이템 code값
    

*/

// 새로운 입력 값 받기 함수
function getNewData(){
    return {
        code: $('#item_code').val().trim(),
        name: $('#item_name').val().trim(),
        price: parseInt($('#item_price').val()) || 0,
        content: $('#item_desc').val().trim()
    };
}


// 수정 화면 전환 함수
// 보기/수정 모드 UI 설정 함수
function updateUI(isEditing) {

    if(isEditing){

        console.log("수정하기 모드")
        // 수정 시작

        // 수정버튼 색상 변경
        $('#edit').removeClass('btn btn-warning');
        $('#edit').addClass('btn btn-success');

        // 수정버튼 이미지 변경
        $('#editBtnImg').removeClass('fas fa-wrench');
        $('#editBtnImg').addClass('fas fa-check');

        // 이름 값 수정모드로 전환
        $('#item_name').removeClass('bg-white');
        $('#editText').text("수정완료");
        $('#item_name').addClass('bg-light');
        $('#item_name').prop('disabled',false);

        // 카테고리 수정모드로 전환
        $('#item_status').prop('disabled',false);

        // 가격 수정모드로 전환
        $('#item_price').removeClass('bg-white');
        $('#item_price').addClass('bg-light');
        $('#item_price').prop('disabled',false);

        // 상품 설명 수정모드로 전환
        $('#item_desc').removeClass('bg-white');
        $('#item_desc').addClass('bg-light');
        $('#item_desc').prop('disabled',false);

        //사진 드롭다운메뉴 수정모드로 전환
        $('#dropdownMenuLink').css('display','inline-block');

        } else {

        // 수정 완료 
        console.log("아이템 보기 모드")

        // 수정버튼 색상 변경
        $('#edit').removeClass('btn btn-success');
        $('#edit').addClass('btn btn-warning');

        // 수정버튼 이미지 변경
        $('#editBtnImg').removeClass('fas fa-check');
        $('#editBtnImg').addClass('fas fa-wrench');

        // 수정버튼 텍스트 변경
        $('#editText').text("수정하기");

        // 이름 값 수정모드에서 전환
        $('#item_name').removeClass('bg-light');
        $('#item_name').addClass('bg-white');
        $('#item_name').prop('disabled', true);

        // 카테고리 수정모드에서 전환
        $('#item_status').prop('disabled',true);

        
        // 가격 수정모드에서 전환
        $('#item_price').removeClass('bg-light');
        $('#item_price').addClass('bg-white');
        $('#item_price').prop('disabled', true);

        // 상품 설명 수정모드에서 전환
        $('#item_desc').removeClass('bg-light');
        $('#item_desc').addClass('bg-white');
        $('#item_desc').prop('disabled', true);

        //사진 드롭다운메뉴 수정모드에서 전환
        $('#dropdownMenuLink').css('display','none');
        
        }
};

// 삭제하기 버튼 클릭 시 나오는 Modal 안의 삭제버튼 제어
function deleteItem(codeValue){
    $('#deleteConfirm').click(function(event){
        event.preventDefault();

        var jwtToken = localStorage.getItem("jwtToken");

        $.ajax({
            url: "http://localhost:8080/item/deleteitem",
            method: "DELETE",
            data: {
                code: codeValue
            },
            beforeSend: function(jqXHR) {
                jqXHR.setRequestHeader('Authorization', 'Bearer ' + jwtToken);
            },
            success: function(response) {
                console.log("삭제 성공?", response);

                if(localStorage.getItem("isEditing")){
                    localStorage.removeItem("isEditing");
                }

                window.location.href = "item_tables.html";
            },
            error: function(jqXHR, status, errorThrown) {
                console.log("삭제 실패", xhr, status, errorThrown);
            }

        });

    });
}

// 영역별 value값 채우기 함수
function setItemValue(codeValue, original_data){

    var jwtToken = localStorage.getItem("jwtToken");

    $.ajax({
            url:"http://localhost:8080/item/getcontent", 
            method: "GET",
            data: {
                code: codeValue 
            },
            contentType: "application/json",
            beforeSend: function(jqXHR) {
                jqXHR.setRequestHeader('Authorization', 'Bearer ' + jwtToken);
            },
            success: function(response) {

                console.log("2.처음 데이터베이스 값 가져와서 띄워주기 성공");
                console.log("codeValue: "+codeValue);
                    
                    
                // 영역 별로 값 띄우기
                $('#item_code').val(response.code);
                $('#item_name').val(response.name);    
                $('#item_price').val(response.price);
                $('#item_desc').val(response.content);
                
                // 데이터베이스에 저장되어있는 값을 저장 //기본값 설정
                original_data.code = response.code;
                original_data.name = response.name;
                original_data.price = response.price;
                original_data.content = response.content;
                // 콘솔 확인
                console.log("2. 첫 로딩 시 데이터베이스 값 저장한 값 : "+ JSON.stringify(original_data));
                
                },

                error: function(jqXHR, textStatus, errorThrown) {
                    console.log("2. 첫 로딩 시 데이터베이스 값 띄워주기 실패", textStatus, errorThrown);
                }
            });
}

// 업데이트 보내기 함수
function sendUpdatedData(original_data) {

    return new Promise((resolve, reject) => {
        var jwtToken = localStorage.getItem("jwtToken");
    
        $.ajax({
            url: "http://localhost:8080/item/updateitem",
            method: "PUT",
            data: JSON.stringify(getNewData()),
            contentType: "application/json",
            beforeSend: function(jqXHR) {
                jqXHR.setRequestHeader('Authorization', 'Bearer ' + jwtToken);
            },
            success: function (response) {
                console.log("데이터 전송 성공", response);
                
                // 기존 값 변경값으로 덮어씌우기 
                original_data = getNewData();
    
                console.log("sendUpdatedData() 업데이트 변경 후 값: ",JSON.stringify(original_data));
    
                resolve(original_data);
            },
            
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("데이터 전송 실패", textStatus, errorThrown);
                const message = jqXHR.responseJSON && jqXHR.responseJSON.message ? jqXHR.responseJSON.message : "에러 발생";
                reject(new Error(message));
            }
            
        });
    });
};

// 수정하기 버튼 함수
async function editItem(isEditing, original_data) {
    $('#edit').click(async function(event) {

        // 페이지 새로고침 방지
        event.preventDefault();

        console.log("수정 버튼 클릭");

        if(isEditing){
            var new_data = getNewData();

            if(new_data.code === ""){
                alert("상품 코드를 입력하세요.");
            }else if(new_data.name === ""){
                alert("상품 이름 입력하세요.");
            }else if(new_data.price === 0) {
                alert("상품 가격을 입력하세요.");
            }else if(new_data.content === "") {
                alert("상품 설명을 입력하세요");
            }else {
                console.log("검증 전 org "+JSON.stringify(original_data));
                console.log("검증 전 new "+JSON.stringify(new_data));
                console.log("검증 전 new "+new_data.price);

                if(original_data.name !== new_data.name ||
                original_data.price !== new_data.price ||
                original_data.content !== new_data.content
                ){
                
                    console.log("버튼 클릭시 하나라도 값 변경 시 업데이트");

                    sendUpdatedData(original_data)
                    .then(function(updated_data){
                        console.log("원래 값: ", JSON.stringify(original_data));
                        console.log("새로운 값: ", JSON.stringify(new_data));
                        
                        // 새로 변경된 값으로 덮어씌우기
                        original_data = updated_data;

                        console.log("업데이트 후 org: ", JSON.stringify(original_data));

                        isEditing = changeEditingState(isEditing);
                    }).catch(function(error){
                        console.log("에러 발생", error.message);
                    });
                
                }else {
                    isEditing = changeEditingState(isEditing);
                }
            }

        }else {
            isEditing = changeEditingState(isEditing);
        }

        
    });
}

function changeEditingState(isEditing) {
    isEditing = !isEditing;
    // 수정 모드 값을 localStorage에 저장하여 새로고침 시 모드 고정
    localStorage.setItem("isEditing", JSON.stringify(isEditing));
    updateUI(isEditing);
    return isEditing;
}

// 상품 추가하기 함수
function addItem() {
    $(document).ready(function(){
        $('#item_add').click(function(event) {
            event.preventDefault();

            var jwtToken = localStorage.getItem("jwtToken");

            var new_data = getNewData();
            console.log(JSON.stringify(new_data));

            if(new_data.code === ""){
                alert("상품 코드를 입력하세요");
            }else if(new_data.name === ""){
                alert("상품 이름을 입력하세요");
            }else if(new_data.price === 0){
                console.log("가격 "+new_data.price);
                alert("상품 가격을 입력하세요");
            }else if($('#item_desc').val() === ""){
                alert("상품 설명을 입력하세요");
            }else {
                $.ajax({
                    url: "http://localhost:8080/item/additem",
                    method: "POST",
                    data: JSON.stringify(new_data),
                    contentType: "application/json",
                    beforeSend: function(jqXHR) {
                        jqXHR.setRequestHeader('Authorization', 'Bearer ' + jwtToken);
                    },
                    success: function(response){

                        if(response){

                            alert("상품 등록 성공");
                            console.log("등록 요청 상품 정보 "+JSON.stringify(new_data));
                            console.log("등록 성공 여부 "+response);

                            // 페이지 새로고침
                            location.reload();
                            
                        }else{

                            alert("상품 등록 실패\n"+"(이미 존재하는 상품 ID입니다.)");
                            console.log("등록 요청 상품 정보 "+JSON.stringify(new_data));
                            console.log("등록 성공 여부 "+response);
                        }

                        
                    },
                    error:function(jqXHR, status, errorThrown){
                        alert("에러 발생");
                        console.log("error: "+ status + "-" + errorThrown);
                    }
                });
              }
        });
    });
}

function setItemTable() {
    $(document).ready(function() {

        var jwtToken = localStorage.getItem("jwtToken");

        $('#itemTable').DataTable({
            destroy: true,
            "ajax": {
                "url": "http://localhost:8080/item/getlist",
                "dataSrc":"",
                "method":"GET",
                "beforeSend": function(jqXHR){
                    jqXHR.setRequestHeader('Authorization','Bearer ' + jwtToken);
                }
            },

            "columns": [
                {"data": "code"},
                {"data": "name",
                "render": function(data, type, full) {
                    return `<a href="item.html?code=${full.code}">${data}</a>`;
                }
                },
                {"data": "price"}
            ],

            "language": {
                "paginate": {
                    "previous": "이전",
                    "next": "다음"
                },
                "search": "검색:"
            }

        });
    });
}

// 사이드바 a태그 이동 함수
function aTagTo(isEditing, original_data) {

    // 수정 값 입력 존재 시 a링크 이동시 처리
    $('a.for-modal').click(function(event){
        event.preventDefault();

        // a태그 href 설정 값 담기
        var aTagLink = $(this).attr('href');

        var new_data = getNewData();

        if( !isEditing && 
            (original_data.code !== new_data.code ||
            original_data.name !== new_data.name ||
            original_data.price !== new_data.price ||
            original_data.content !== new_data.content)
        ){  

            console.log("org "+ JSON.stringify(original_data));
            console.log("new "+ JSON.stringify(new_data));

            // 모달 띄우기
            $('#editingMoveModal').modal('show');

            // 나가기 버튼
            $('#editingMoveModal').on('click','#exit', function(event) {
                event.preventDefault();

                localStorage.removeItem("isEditing");
                window.location.href = aTagLink;
            
            });

        }else {
            localStorage.removeItem("isEditing");
            window.location.href = aTagLink;
        }

    });
}

function goBack(isEditing, original_data) {
        // 뒤로가기 버튼
        $('#goBack').click(function(event){
            event.preventDefault();

            var new_data = getNewData();
  
            if( !isEditing && 
                (original_data.code !== new_data.code ||
                original_data.name !== new_data.name ||
                original_data.price !== new_data.price ||
                original_data.content !== new_data.content)
            ){
                console.log("org "+ JSON.stringify(original_data));
                console.log("new "+ JSON.stringify(new_data));

                // 모달 띄우기
                $('#editingMoveModal').modal('show');
    
                // 나가기 버튼
                $('#editingMoveModal').on('click','#exit', function(event) {
                    event.preventDefault();

                    localStorage.removeItem("isEditing");
                    window.history.back();
                });
    
            }else {
                localStorage.removeItem("isEditing");
                window.history.back();
            }
    
        });
}

function setOrderTable() {
    $(document).ready(function() {

        var jwtToken = localStorage.getItem("jwtToken");

        $('#orderTable').DataTable({
            destroy: true,
            "ajax": {
                "url": "http://localhost:8080/order/getlist",
                "dataSrc":"",
                "method":"GET",
                "beforeSend": function(jqXHR){
                    jqXHR.setRequestHeader('Authorization','Bearer ' + jwtToken);
                }
            },

            "columns": [
                {"data": "orderid"},
                {"data": "ordername"},
                {"data": "totalprice"},
                {"data": "orderdate"}
            ],

            "language": {
                "paginate": {
                    "previous": "이전",
                    "next": "다음"
                },
                "search": "검색:"
            }

        });
    });
}